# Landmark Explorer - Android App

An Android application for exploring, managing, and visualizing landmarks in Bangladesh using OpenStreetMap.

## 📱 App Overview

Landmark Explorer allows users to view landmarks on an interactive map and manage them through a simple list interface. The app fetches data from a  REST API and displays landmarks across Bangladesh with detailed information.

## ✨ Features

### Core Features
- **Interactive Map View**: OpenStreetMap integration focused on Bangladesh
- **Landmark List**: Card-based display of all landmarks with images and details
- **Real-time Data Sync**: Automatic synchronization between map and list views
- **Bottom Navigation**: Easy switching between map and list interfaces

### CRUD Operations
- **Create**: Add new landmarks with title and coordinates
- **Read**: Fetch and display all landmarks from API
- **Update**: Edit existing landmark information partially completed
- **Delete**: Remove landmarks partially completed

### Map Features
- Bangladesh-focused map with zoom and pan controls
- Custom markers for each landmark
- Marker click shows detailed bottom sheet
- Automatic zoom to landmark locations

## 🛠️ Technology Stack

- **Language**: Kotlin
- **Minimum SDK**: Android API 24 (Android 7.0)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Networking**: Retrofit 2 with OkHttp
- **Image Loading**: Coil
- **Mapping**: osmdroid (OpenStreetMap for Android)
- **UI Components**: Material Design, RecyclerView, BottomNavigationView
- **Data Persistence**: ViewModel with LiveData

## 📋 Setup Instructions

### Prerequisites
1. Android Studio (latest version recommended)
2. Android SDK with API level 24+
3. Internet connection for API calls and map tiles

### Installation Steps
1. **Clone or download** the project
2. **Open in Android Studio**: File → Open → Select project folder
3. **Sync Gradle**: Click "Sync Now" when prompted
4. **Build the project**: Build → Make Project
5. **Run on emulator or device**: 
   - Select your target device
   - Click Run button (▶️)


## 🗺️ API Integration

The app connects to a REST API with the following endpoints:

- **Base URL**: `https://labs.anontech.info/cse489/t3/`
- **GET `/api.php`**: Retrieve all landmarks
- **POST `/api.php`**: Create new landmark
- **PUT `/api.php`**: Update existing landmark
- **DELETE `/api.php`**: Remove landmark


## ⚠️ Known Limitations

1. **Image Upload**: Create/Update operations don't support image uploads
2. **Edit/Delete UI**: Functions are implemented in API but Not implemented in UI
3. **Location Picker**: Manual coordinate input only (no map tap selection)
4. **Marker Clustering**: No clustering for densely packed landmarks
5. **Offline Support**: Requires internet for map tiles and API calls
6. **Image Caching**: Images reload on each app restart

### Technical Constraints
- OpenStreetMap tiles require internet connection
- API response format must match expected JSON structure
- Coordinate validation limited to Bangladesh range
- No local database persistence (cloud-only)

