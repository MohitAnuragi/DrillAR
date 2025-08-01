# DrillAR - Augmented Reality Training App 🏋️‍♂️💎


DrillAR is an innovative Android application that uses ARCore to provide interactive training drills with real-time surface detection. The app helps users practice various exercises by visualizing drills in augmented reality on suitable surfaces.

## Features ✨

### 🎯 Drill Selection System
- **Comprehensive Drill Library**: Choose from predefined drill types:
  - Basic Wall Drilling
  - Precision Tile Drilling
  - Concrete Surface Drilling
  - Delicate Material Drilling
- **Detailed Drill Information**: Each drill includes:
  - Recommended drill bits
  - Optimal RPM settings
  - Safety precautions
  - Professional tips

### 🌐 Augmented Reality Core
#### Surface Detection & Visualization (Highlighted Feature 🔍)
- **Smart Plane Detection**:
  - ARCore automatically detects flat, horizontal surfaces (floors, walls, tables)
  - Visualized with a subtle green grid overlay
  - *Auto-pause* when optimal surface is detected
- **Real-Time Feedback**:
  - Surface quality indicators (color-coded from red to green)
  - Haptic feedback on valid surface detection
  - Audio cues for important state changes

#### AR Visualization
- **3D Drill Models**: Life-size virtual drill models with:
  - Realistic animations
  - Proper depth scaling
  - Material-specific visual effects
- **Interactive Placement**:
  - Tap on detected surfaces to position the drill
  - Two-finger gesture to rotate/scale models
  - Long-press to lock position

### 📱 User Interface
- **Modern Material Design 3**:
  - Adaptive color scheme (light/dark mode)
  - Animated transitions between screens
  - Intuitive iconography
- **Status System**:
  - Clear AR initialization progress
  - Surface detection quality meter
  - Step-by-step guidance prompts



 ## Technical Stack 🛠️

### Core Platform
| Component               | Technology                          | Version           | Purpose                          |
|-------------------------|-------------------------------------|-------------------|----------------------------------|
| **Language**           | Kotlin                             | 1.9.0+            | Main app development             |
| **SDK**               | Android SDK                        | API 28+           | Base platform functionality      |
| **Build System**      | Gradle                             | 8.0+              | Dependency management & builds   |

### Augmented Reality
| Component               | Technology                          | Details                              |
|-------------------------|-------------------------------------|--------------------------------------|
| **AR Engine**         | ARCore                             | 1.50.0+ (Surface detection & tracking) |
| **3D Rendering**      | Sceneform (Maintained Fork)        | `com.gorisse.thomas.sceneform:1.25.0` |
| **Model Loading**     | GLTF Support                       | For realistic drill models           |

### UI Framework
| Component               | Technology                          | Usage                                |
|-------------------------|-------------------------------------|--------------------------------------|
| **Declarative UI**    | Jetpack Compose                    | 1.7.0+ (All screens including drill selection) |
| **Design System**     | Material Design 3                  | Theming & component library          |
| **Animation**         | Compose Animation APIs             | Smooth transitions between AR states |

### Architecture Components
| Component               | Technology                          | Role                                 |
|-------------------------|-------------------------------------|--------------------------------------|
| **Dependency Injection** | Hilt                              | 2.48+ (Clean architecture setup)     |
| **State Management**   | ViewModel + Flow                   | AR session state handling            |
| **Persistence**       | Room Database                      | 2.6.1+ (Drill configurations storage) |

### Testing
| Type                   | Framework                          | Coverage                            |
|------------------------|------------------------------------|-------------------------------------|
| **Unit Tests**        | JUnit + MockK                     | Business logic & utilities          |
| **UI Tests**         | Compose Test + Espresso           | Screen interactions                 |
| **AR Integration**   | ARCore Mocking                    | Surface detection validation        |


## 📱 How to Use the App

### 🛠️ 1. Select a Drill
- On the initial screen, use the dropdown menu to choose the type of drill you want to visualize (e.g., **"Basic Wall Drilling"**, **"Precision Tile Drilling"**, etc.).

### 🚀 2. Start AR Drill
- Tap the **"Start AR Drill"** button to launch the AR experience.

---

## 📡 ARCore Initialization & Tracking States

| Message | What It Means |
|--------|----------------|
| 🔄 **"Initializing AR..."** | ARCore is setting up the AR session. |
| ⚠️ **"ARCore installation requested. Please follow the prompts to install/update."** | If **Google Play Services for AR** is not installed or outdated, you’ll be prompted to install/update it from the Play Store. |
| 📷 **"Scanning for flat surfaces... Slowly move your device around."** | Move your device steadily in a **sweeping motion** to help ARCore detect flat surfaces. Well-lit and textured areas work best. |
| ⏸️ **"AR is paused. Move your device slowly to resume tracking."** | ARCore temporarily lost tracking. Move the phone slowly to help it **re-localize**. |
| ✅ **"Surface detected! Tap on a highlighted area to place the drill."** | A flat surface was found (visualized by a **green grid**). You're ready to place the virtual drill marker. |

---

### 🎯 3. Place the Drill
- Tap on the **green grid area** (detected surface) to place the **virtual 3D drill model** in your environment.

### 👀 4. Visualize
- Walk around or change angles to see how the drill looks in real space.

> 📌 **Tip**: Make sure you're in a well-lit environment with visible texture (like carpets, tiles, or furniture) for best results.



## 📸 Screenshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/4871a8dd-5a40-4b57-931b-14a1b15c8497" alt="AR Drill Placed - Top View" width="30%" />
  <img src="https://github.com/user-attachments/assets/65e81405-cacc-4c92-96ec-86b1dce7ab19" alt="AR Drill Placed - Side View" width="30%" />
  <img src="https://github.com/user-attachments/assets/9c57c4b0-e3a9-4df4-b991-35d70c141fc5" alt="AR Drill Placed - Side View" width="30%" />

</p>



### Key Dependencies
```gradle
dependencies {
    // AR Core
    implementation "com.google.ar:core:1.50.0"
    
    // Sceneform 
    implementation "com.gorisse.thomas.sceneform:sceneform-maintained:1.24.0"
    
    // Jetpack Compose
    implementation "androidx.compose.material3:material3:1.2.1"
    implementation "androidx.activity:activity-compose:1.8.2"
    
    // Architecture
    implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0"
    implementation "com.google.dagger:hilt-android:2.48"
}
Clone the repository:

git clone https://github.com/MohitAnuragi/DrillAR.git
