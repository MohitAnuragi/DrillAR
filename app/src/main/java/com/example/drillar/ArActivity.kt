package com.example.drillar

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.ar.core.*
import com.google.ar.core.Config.LightEstimationMode
import com.google.ar.core.exceptions.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.ux.ArFragment

class ArActivity : AppCompatActivity() {

    private lateinit var arFragment: ArFragment
    private var isArSessionConfigured = false
    private var objectAnchor: Anchor? = null
    private var currentToast: Toast? = null
    private var userRequestedInstall = true

    private lateinit var trackingStatusText: TextView



    companion object {
        private const val TAG = "ArActivity"
        private const val CAMERA_PERMISSION_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)

        arFragment = supportFragmentManager.findFragmentById(R.id.ar_fragment) as ArFragment
        trackingStatusText = findViewById(R.id.tracking_status_text)

        arFragment.setOnViewCreatedListener { arSceneView ->
            arSceneView.planeRenderer.apply {
                material.thenAccept { material ->
                    material.setFloat3(com.google.ar.sceneform.rendering.PlaneRenderer.MATERIAL_COLOR, Color(0.3f, 0.8f, 0.3f, 0.5f)) // Greenish transparent
                }
                isEnabled = true
                isVisible = true
                isShadowReceiver = true
            }


            arSceneView.scene.addOnUpdateListener { frameTime ->
                val frame = arSceneView.arFrame
                if (frame != null) {
                    val cameraTrackingState = frame.camera.trackingState
                    val planes = arSceneView.session?.getAllTrackables(Plane::class.java)
                    val planeDetected = planes?.any { it.trackingState == TrackingState.TRACKING } ?: false

                    updateTrackingStatus(cameraTrackingState, planeDetected)
                }
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
            } else {
                arSceneView.post {
                    maybeEnableArSession()
                }
            }
        }


        arFragment.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane, motionEvent: MotionEvent ->
            if (plane.type != Plane.Type.HORIZONTAL_UPWARD_FACING) {
                showToast("Tap on a flat, horizontal surface (like a floor or table) to place the object.")
                return@setOnTapArPlaneListener
            }

            objectAnchor?.detach()
            objectAnchor = null

            val anchor = hitResult.createAnchor()
            objectAnchor = anchor

            MaterialFactory.makeOpaqueWithColor(this, Color(android.graphics.Color.BLUE))
                .thenAccept { material ->
                    val cubeRenderable = ShapeFactory.makeCube(
                        Vector3(0.1f, 0.1f, 0.1f),
                        Vector3(0.0f, 0.05f, 0.0f),
                        material
                    )

                    val anchorNode = AnchorNode(anchor)
                    anchorNode.setParent(arFragment.arSceneView.scene)

                    val objectNode = Node()
                    objectNode.setParent(anchorNode)
                    objectNode.renderable = cubeRenderable
                    objectNode.localPosition = Vector3(0f, 0.05f, 0f) // Adjust height to sit on the plane

                    showToast("Object placed! Now you can visualize your drill.")
                }
                .exceptionally { throwable ->
                    Log.e(TAG, "Error creating renderable: ${throwable.message}")
                    showToast("Error placing object: ${throwable.message}")
                    null
                }
        }
    }

    override fun onResume() {
        super.onResume()
        if (::arFragment.isInitialized && arFragment.arSceneView != null) {
            arFragment.arSceneView.post {
                if (!isArSessionConfigured) {
                    maybeEnableArSession()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                arFragment.arSceneView.post {
                    maybeEnableArSession()
                }
            } else {
                showToast("Camera permission is required for AR to function.")
                finish()
            }
        }
    }

    private fun maybeEnableArSession() {
        if (isArSessionConfigured || isFinishing || isDestroyed) {
            return
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
            return
        }

        try {
            when (ArCoreApk.getInstance().requestInstall(this, userRequestedInstall)) {
                ArCoreApk.InstallStatus.INSTALLED -> {
                    arFragment.arSceneView.post {
                        configureArSession()
                    }
                }
                ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {
                    userRequestedInstall = false
                    showToast("ARCore installation requested. Please follow the prompts to install/update.")
                }
            }
        } catch (e: UnavailableUserDeclinedInstallationException) {
            showToast("ARCore installation declined. AR features will not be available.")
            finish()
        } catch (e: UnavailableArcoreNotInstalledException) {
            showToast("ARCore is not installed. Please install it from Google Play.")
            finish()
        } catch (e: UnavailableDeviceNotCompatibleException) {
            showToast("This device does not support ARCore. Please use an ARCore-compatible device.")
            finish()
        } catch (e: UnavailableSdkTooOldException) {
            showToast("ARCore SDK is too old. Please update your app to the latest version.")
            finish()
        } catch (e: UnavailableApkTooOldException) {
            showToast("Google Play Services for AR is too old. Please update it from Google Play.")
            finish()
        } catch (e: Exception) {
            Log.e(TAG, "Exception during ARCore availability check: ${e.message}", e)
            showToast("Failed to check ARCore availability: ${e.message}. Please restart the app.")
            finish()
        }
    }

    private fun configureArSession() {
        if (isFinishing || isDestroyed || isArSessionConfigured) {
            Log.w(TAG, "Skipping AR session configuration due to activity state or already configured.")
            return
        }

        val arSceneView = arFragment.arSceneView
        if (arSceneView == null) {
            Log.e(TAG, "AR Scene View is null. Cannot configure session.")
            showToast("Failed to initialize AR view. Please restart app.")
            finish()
            return
        }

        try {
            if (arSceneView.session == null) {
                arSceneView.session = Session(this)
                Log.w(TAG, "AR Session was null in configureArSession, created a new one.")
            }

            val session = arSceneView.session
            val config = Config(session)
            config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL // Only horizontal for now
            config.lightEstimationMode = LightEstimationMode.DISABLED // Keep disabled to avoid NoSuchMethodError

            session?.configure(config)
            isArSessionConfigured = true
            Log.d(TAG, "AR Session configured successfully with light estimation mode: ${config.lightEstimationMode}")
            updateTrackingStatus(arSceneView.arFrame?.camera?.trackingState ?: TrackingState.PAUSED, false)
            userRequestedInstall = true

        } catch (e: Exception) {
            Log.e(TAG, "Failed to configure AR session: ${e.message}", e)
            showToast("Failed to configure AR session: ${e.message}. Please ensure ARCore is supported and updated.")
            finish()
        }
    }

    private fun updateTrackingStatus(cameraTrackingState: TrackingState, planeDetected: Boolean) {
        val statusMessage = when (cameraTrackingState) {
            TrackingState.PAUSED -> "AR is paused. Move your device slowly to resume tracking."
            TrackingState.STOPPED -> "AR tracking stopped. Please restart the app."
            TrackingState.TRACKING -> {
                if (planeDetected) {
                    "Surface detected! Tap on a highlighted area to place the drill."
                } else {
                    "Scanning for flat surfaces... Slowly move your device around."
                }
            }
            else -> "Initializing AR. Please wait..."
        }
        trackingStatusText.text = statusMessage
    }

    private fun showToast(message: String) {
        currentToast?.cancel()
        currentToast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        currentToast?.show()
    }

    override fun onPause() {
        super.onPause()
        currentToast?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        currentToast?.cancel()
    }
}