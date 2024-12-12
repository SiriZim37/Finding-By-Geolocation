# Finding-By-Geolocation

**Finding-By-Geolocation** is an Android application that demonstrates how to use Google's Geolocation API to display maps and group clusters on the map. This app allows users to interact with the map and visualize geolocation-based data efficiently.

## Development Roadmap

- [x] [Kotlin](https://kotlinlang.org/)
- [x] [Google Maps SDK](https://developers.google.com/maps/documentation/android-sdk/start)
- [x] [Gradle Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html)

## Features

- [x] **Geolocation** – Display and interact with geolocation maps.
- [x] **Group Clusters** – Organize geolocation points into clusters for better visualization.

## Google Maps SDK for Android Setup

### Steps to Setup:

1. **Create a new project** in the [Google Cloud Console](https://console.cloud.google.com/). Once the project is created, switch to that project.
2. **Generate Credentials** (API Key) for your Android Project.
3. **Add dependencies** to your Android project as listed in the "Dependencies" section.
4. **Add your API Key** to the `local.properties` file:

   ```properties
   # local.properties
   GOOGLE_MAPS_API_KEY=<YOUR_KEY>

```
5. Update your `AndroidManifest.xml` to include the API key. Within the `application` tag.
```
<application
   ...
   ... >
   <meta-data
	  android:name="com.google.android.geo.API_KEY"
	  android:value="${GOOGLE_MAPS_API_KEY}"/>
</application>
```
6. Add `ACCESS_FINE_LOCATION` and `ACCESS_COURSE_LOCATION` permissions to manifest. You'll need these if you want to access the users location.
```
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
```

## How to implement GoogleMap in Kotlin?

https://developers.google.com/codelabs/maps-platform/maps-platform-101-android#11


![ezcv logo]( https://github.com/SiriZim37/Finding-By-Geolocation/blob/main/des/finding_art.png)



