# Snap Sphere

Snap Sphere is a fully functional Instagram clone built using Firebase for backend services and Kotlin for Android development. This app allows users to share their moments through images, interact with posts by liking, commenting, and searching for other users. It also includes essential features such as user authentication, profile management, and a sleek UI designed using Jetpack Compose.

## Features

- **Post Images**: Users can upload, view, like, and delete images.
- **Comment System**: Users can comment on posts and delete their own comments.
- **Like/Dislike Posts**: Users can like or dislike posts.
- **Profile Management**: Users can create accounts, log in, change profile pictures, update usernames, and view posts by other users.
- **Search Functionality**: Users can search for other users or posts using a custom search bar.
- **User Authentication**: Secure signup and login using Firebase Authentication.
- **Firebase Integration**: Data is stored and retrieved from Firebase for seamless performance.

## Tech Stack

- **Kotlin**: Primary programming language.
- **Jetpack Compose**: UI toolkit used for building native UI.
- **MVVM Architecture**: Ensures a clean separation of concerns.
- **Dagger Hilt**: For dependency injection.
- **Firebase**:
    - Firebase Authentication: For user authentication and management.
    - Firebase Firestore: For real-time database management.
    - Firebase Storage: For storing user-uploaded images.
- **Coil**: For image loading and caching.

## Installation

1. Clone the repository:
   ```
   git clone https://github.com/sahilydv18/snap-sphere.git
   ```
2. Open the project in **Android Studio**.
3. Add Firebase configuration:
   - Connect your Firebase project and add the `google-services.json` file under the app directory.
   - Ensure Firebase Authentication, Firestore, and Storage are properly set up in the Firebase console.
4. Build and run the project on an emulator or Android device.

## Usage

1. **Create an account** using the sign-up screen.
2. **Log in** with your credentials.
3. **Post, like, comment**, and **search for users or posts**.
4. **Manage your profile** by updating your profile picture or username.

## App Architecture

Snap Sphere follows the **MVVM** (Model-View-ViewModel) architecture pattern, which ensures a clean separation between the UI and business logic.

- **Model**: Represents data and business logic (Firebase Firestore, Storage).
- **View**: Composable UI components.
- **ViewModel**: Manages UI-related data and interacts with the Model to update the View.

Dependency injection is managed using **Dagger Hilt**, which provides scoped instances for ViewModels and repositories, ensuring better code organization and reusability.

## Contributing

Contributions, issues, and feature requests are welcome! Feel free to open a pull request or issue.

1. Fork the repository.
2. Create a new branch: ```git checkout -b feature-branch```
3. Make changes and commit: ```git commit -m 'Add new feature'```
4. Push to the branch: ```git push origin feature-branch```
5. Open a pull request.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE.md) file for details.

## Contact
For any queries or suggestions, feel free to reach out:
- [Email](mailto:ydvvsahil09@gmail.com)
- [LinkedIn](https://www.linkedin.com/in/ydvsahil18/)
- [X(Twitter)](https://x.com/sahil_yadvv)