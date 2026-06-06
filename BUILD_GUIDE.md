# Native Kotlin APK Build Guide

## Option 1: Build Locally Using Android Studio (Recommended)

### Prerequisites
- Android Studio Flamingo or newer
- Android SDK 24+ (API 24)
- Java 17 or newer
- Gradle 8.0+

### Steps

1. **Open project in Android Studio**
   ```bash
   cd progress_tracker_jee_native_android
   # Open with Android Studio
   ```

2. **Sync Gradle files**
   - Android Studio will prompt to sync
   - Click "Sync Now"

3. **Build Debug APK**
   - Menu: Build → Build Bundle(s) / APK(s) → Build APK(s)
   - Or use: `./gradlew assembleDebug`

4. **Build Release APK**
   - Menu: Build → Generate Signed Bundle / APK
   - Select APK
   - Create or select signing key
   - Choose release build type
   - Click Finish

5. **Locate APK**
   ```
   app/build/outputs/apk/debug/app-debug.apk
   app/build/outputs/apk/release/app-release.apk
   ```

## Option 2: Build Using Command Line

### Debug APK
```bash
cd progress_tracker_jee_native_android
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk
```

### Release APK (Unsigned)
```bash
./gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release-unsigned.apk
```

### Release APK (Signed)

1. **Create signing key** (first time only)
   ```bash
   keytool -genkey -v -keystore jee-tracker-release.keystore \
     -keyalg RSA -keysize 2048 -validity 10000 \
     -alias jee-tracker
   ```

2. **Sign APK**
   ```bash
   jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 \
     -keystore jee-tracker-release.keystore \
     app/build/outputs/apk/release/app-release-unsigned.apk \
     jee-tracker
   ```

3. **Optimize APK**
   ```bash
   ${ANDROID_HOME}/build-tools/34.0.0/zipalign -v 4 \
     app/build/outputs/apk/release/app-release-unsigned.apk \
     app-release.apk
   ```

## Option 3: Build Using GitHub Actions (Automated)

### Setup

1. **Create GitHub repository**
   ```bash
   git init
   git add .
   git commit -m "Initial commit"
   git remote add origin https://github.com/yourusername/jee-tracker-native.git
   git push -u origin main
   ```

2. **Add Signing Secrets**
   - Go to Settings → Secrets and variables → Actions
   - Add these secrets:
     ```
     SIGNING_KEY: (base64 encoded keystore)
     SIGNING_KEY_ALIAS: jee-tracker
     KEY_STORE_PASSWORD: your_password
     KEY_PASSWORD: your_password
     ```

3. **Encode Keystore**
   ```bash
   base64 jee-tracker-release.keystore > keystore.txt
   # Copy content to SIGNING_KEY secret
   ```

4. **Trigger Build**
   - Push to main branch
   - GitHub Actions automatically builds APK
   - Download from Actions tab

## APK Output

### Debug APK
- **Size**: ~50-70 MB
- **Use**: Testing and development
- **Signing**: Automatically signed with debug key
- **Installation**: Direct install on device

### Release APK
- **Size**: ~30-40 MB (optimized)
- **Use**: Production and Play Store
- **Signing**: Manually signed with release key
- **Installation**: Install via Play Store or sideload

## Installation

### On Emulator
```bash
adb install app-debug.apk
```

### On Physical Device
1. Enable USB debugging (Settings → Developer Options)
2. Connect device via USB
3. Run: `adb install app.apk`

### Via APK File
1. Download APK
2. Transfer to device
3. Open file manager
4. Tap APK to install

## Troubleshooting

### Build Fails
```bash
# Clean build
./gradlew clean build

# Update dependencies
./gradlew dependencies --refresh-dependencies

# Check Java version
java -version  # Should be 17+
```

### Gradle Issues
```bash
# Clear Gradle cache
rm -rf ~/.gradle/caches

# Rebuild
./gradlew clean build
```

### APK Won't Install
- Check Android version compatibility (min API 24)
- Uninstall previous version
- Clear app data: `adb shell pm clear com.jeetracker.app`

### Signing Issues
```bash
# Verify keystore
keytool -list -v -keystore jee-tracker-release.keystore

# Check keystore password
keytool -list -keystore jee-tracker-release.keystore
```

## Play Store Submission

### Prepare App
1. Update version code in `build.gradle.kts`
2. Update version name (e.g., "1.0.0")
3. Build release APK
4. Test thoroughly on device

### Create Play Store Listing
1. Go to Google Play Console
2. Create new app
3. Fill app details
4. Add screenshots and description
5. Set content rating
6. Set pricing

### Upload APK
1. Go to Release → Production
2. Upload signed APK
3. Review and publish

## Next Steps

1. **Test on device** - Install and test all features
2. **Prepare for Play Store** - Add app icon, screenshots, description
3. **Upload to Play Store** - Follow Google Play Console guide
4. **Monitor analytics** - Track user engagement

## Support

For issues:
- Check Android documentation: https://developer.android.com
- Visit Stack Overflow: https://stackoverflow.com/questions/tagged/android
- Contact Google Play Support: https://support.google.com/googleplay
