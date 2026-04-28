# 📱 SkillMap

SkillMap is an Android application designed to help users explore, manage, and track skills efficiently. It provides an interactive platform where users can organize skill-related data, authenticate securely, and store information in real-time using Firebase services.

---

## 🚀 Features

- 🔐 User Authentication (Firebase Authentication)
- ☁️ Real-time Database Integration (Firebase Realtime Database)
- 📂 Cloud Storage Support (Firebase Storage)
- 🖼️ Image Loading with Glide
- 📋 RecyclerView-based Dynamic UI
- 🎨 Material Design UI Components
- 🔑 Google Sign-In Integration

---

## 🛠️ Tech Stack

- **Language:** Java (Android)
- **Framework:** Android SDK
- **Build System:** Gradle (Kotlin DSL)
- **Backend Services:** Firebase
  - Authentication
  - Realtime Database
  - Storage
- **Libraries Used:**
  - AndroidX (AppCompat, ConstraintLayout, RecyclerView)
  - Material Components
  - Glide (Image Loading)
  - Google Play Services Auth

---

## 📂 Project Structure

```
SkillMap/
│── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/mad_project/
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│
│── gradle/
│── build.gradle.kts
│── settings.gradle.kts
│── gradlew
```

---

## ⚙️ Installation & Setup

### Prerequisites
- Android Studio (latest version recommended)
- Java 11+
- Firebase account

### Steps

1. Clone the repository:
```bash
git clone https://github.com/your-username/SkillMap.git
```

2. Open the project in Android Studio

3. Connect Firebase:
   - Go to Firebase Console
   - Create a new project
   - Add Android app
   - Download `google-services.json`
   - Place it inside:
     ```
     app/google-services.json
     ```

4. Sync Gradle:
```
File → Sync Project with Gradle Files
```

5. Run the app:
```
Run → Run 'app'
```

---

## 🔐 Firebase Configuration

Make sure the following services are enabled in Firebase:

- Authentication (Email/Google Sign-In)
- Realtime Database
- Storage

---

## 📈 Future Improvements

- 🔔 Push Notifications
- 🌐 Offline Mode Support
- 📊 Skill Analytics Dashboard
- 🤖 AI-based Skill Recommendations
- 🎯 Progress Tracking System

---

## 🤝 Contributing

Contributions are welcome!

1. Fork the repository  
2. Create a new branch  
3. Make your changes  
4. Submit a pull request  

---

## 📄 License

This project is licensed under the MIT License.

---

## 👨‍💻 Author

Developed by **Dhyan Sirigeri**

---

## ⭐ Support

If you like this project, give it a ⭐ on GitHub!
