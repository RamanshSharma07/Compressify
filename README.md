## Compressify

Android image compression & cropping app built with Kotlin and Jetpack Compose. Designed with Clean Architecture and MVVM, and powered by DataStore for persistent, user-friendly settings (themes, defaults). Smooth navigation with NavHost/NavController, MediaStore integration, robust permission handling, and a custom animated splash screen.

## ✨ Features

	•	Clean Architecture + MVVM: testable, maintainable layers with clear separation of concerns
	•	Persistent settings with DataStore: theme mode, quality defaults, last used options
	•	Dynamic theming: Light/Dark/System (Material 3 if enabled)
	•	Jetpack Compose UI: declarative screens, state-driven UI, previews
	•	Navigation Compose: type-safe routes with NavHost/NavController
	•	Image compression: quality/size control with preview & output stats
	•	Image cropping: freeform/ratio crop (e.g., 1:1, 4:3, 16:9)
	•	MediaStore access: pick, save, and update images with scoped storage
	•	Permission handling: modern AndroidX APIs with rationale flows
	•	Animated splash screen: polished first-run experience


 ## 📱 Screenshots

<p align="center">
	<img src="https://github.com/user-attachments/assets/f53aa947-5e3a-4040-b048-1526c41f41e1" alt="Splash Screen" width="250" />
	<img src="https://github.com/user-attachments/assets/407cafe6-5dc9-40fc-a34e-86a791f60134" alt="Home Screen" width="250" />
	<img src="https://github.com/user-attachments/assets/0295830f-7588-43cb-af77-3150a836f2fb" alt="Gallery Picker" width="250" />
</p>

<p align="center">
	<img src="https://github.com/user-attachments/assets/68988305-1b98-4485-ab94-1774baf015ca" alt="Edit Screen" width="250" />
  <img src="https://github.com/user-attachments/assets/18d74b8f-01e9-4e7c-a454-a445e491e25d" alt="Crop Screen" width="250" />
  <img src="https://github.com/user-attachments/assets/0f1e1f34-d0e5-46e5-a286-0ef19e730ae9" alt="Show Screen" width="250" />
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/6c55dddb-13bb-443d-a388-7f51d4513fec" alt="Share Image" width="250" />
  <img src="https://github.com/user-attachments/assets/8ba30639-f273-45ec-9f06-99217d324b50" alt="Settings Screen" width="250" />
</p>

⸻

## 🧱 Architecture

	•	MVVM: ViewModel exposes immutable UI state + one-shot events
	•	Unidirectional data flow: UI → intent → ViewModel → state → UI
	•	Use cases encapsulate business rules (e.g., CompressImage, CropImage, GetImages)
	•	Repository mediates between domain and platform APIs (MediaStore, file I/O)
 
⸻

## 🧰 Tech Stack

	•	Language: Kotlin
	•	UI: Jetpack Compose (Material, Material 3 optional)
	•	Navigation: Navigation Compose (NavHost, NavController)
	•	State: ViewModel, Kotlin Flows/StateFlow
	•	Persistence: DataStore (Preferences)
	•	Media: MediaStore, scoped storage
	•	Permissions: AndroidX Activity Result APIs
	•	Animations: Compose animations + custom splash
