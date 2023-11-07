# Finding-By-Geolocation

# Example source code 
The application shows how to use Google's API to display Geolocation maps and how to group Clusters on the map.

## Development Roadmap

- [x] [Kotlin](https://kotlinlang.org/)
- [x] [Google Map](https://developers.google.com/maps/documentation/android-sdk/start)
- [x] [Gradle Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html)


## Features 

- [x] Geolocation
- [x] Group Cluster

# Google Maps SDK for Android Setup

1. Create a new project in cloud console https://console.cloud.google.com/. Once its created switch to that project.
2. Generate Key (Credentials) for Android Project  
3. Add the dependencies to your android project (listed below in "Dependencies" section).
4. Add your API key to `local.properties`.
   **Note:** To reference the API key in `local.properties` you'll need the secrets plugin installed. It's the `com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1` dependency.
```
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



