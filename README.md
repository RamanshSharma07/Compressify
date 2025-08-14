Compressify

Android image compression & cropping app built with Kotlin and Jetpack Compose. Designed with Clean Architecture and MVVM, and powered by DataStore for persistent, user-friendly settings (themes, defaults). Smooth navigation with NavHost/NavController, MediaStore integration, robust permission handling, and a custom animated splash screen.

✨ Features

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

⸻

🧱 Architecture

	•	MVVM: ViewModel exposes immutable UI state + one-shot events
	•	Unidirectional data flow: UI → intent → ViewModel → state → UI
	•	Use cases encapsulate business rules (e.g., CompressImage, CropImage, GetImages)
	•	Repository mediates between domain and platform APIs (MediaStore, file I/O)
 
⸻

🧰 Tech Stack

	•	Language: Kotlin
	•	UI: Jetpack Compose (Material, Material 3 optional)
	•	Navigation: Navigation Compose (NavHost, NavController)
	•	State: ViewModel, Kotlin Flows/StateFlow
	•	Persistence: DataStore (Preferences)
	•	Media: MediaStore, scoped storage
	•	Permissions: AndroidX Activity Result APIs
	•	Animations: Compose animations + custom splash
