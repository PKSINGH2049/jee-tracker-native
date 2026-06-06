# JEE Progress Tracker - Native Kotlin Android App

A professional Native Android application built with Kotlin, Jetpack Compose, and Material Design 3 for tracking JEE exam preparation with advanced features.

## Features

### Core Features
- ✅ **Topic Tracking** - Track 76+ topics across 3 subjects (Physics, Chemistry, Maths)
- ✅ **Real-time Progress** - Live completion percentage and breakdown
- ✅ **Subject Navigation** - Browse all subjects and categories
- ✅ **One-click Completion** - Toggle topics with instant feedback
- ✅ **Reset Progress** - Reset per subject or globally

### Premium Features
- 📊 **Analytics Dashboard** - Detailed progress insights
- 🤖 **AI Recommendations** - Personalized study suggestions
- 📝 **Notes & Resources** - Create and manage notes
- 📥 **PDF Export** - Generate progress reports
- 💳 **Stripe Payments** - Secure subscriptions

### Subscription Plans
- **₹5** - 2-day trial
- **₹20** - Monthly subscription
- **₹100** - Yearly subscription

## Tech Stack

- **Kotlin** - Modern Android language
- **Jetpack Compose** - Modern UI toolkit
- **Material Design 3** - Latest design system
- **Room** - Local database
- **Retrofit** - HTTP client
- **Coroutines** - Async programming
- **Stripe** - Payment processing

## Project Structure

```
src/main/kotlin/com/jeetracker/app/
├── data/
│   ├── api/
│   │   └── ApiService.kt          # Retrofit API
│   ├── database/
│   │   ├── AppDatabase.kt         # Room database
│   │   ├── Daos.kt                # Data access objects
│   │   └── TypeConverters.kt      # Type converters
│   └── models/
│       └── Models.kt              # Data models
├── ui/
│   ├── screens/
│   │   └── Screens.kt             # Compose screens
│   ├── theme/
│   │   └── Theme.kt               # Material Design theme
│   └── viewmodel/
│       └── JeeTrackerViewModel.kt # State management
└── MainActivity.kt                # Entry point
```

## Setup & Installation

### Prerequisites
- Android Studio Flamingo or newer
- Android SDK 24+ (API 24)
- Kotlin 1.9+
- Gradle 8.0+

### Installation Steps

1. **Clone/Open project in Android Studio**
   ```bash
   cd progress_tracker_jee_native_android
   ```

2. **Configure Backend URL**
   Edit `src/main/kotlin/com/jeetracker/app/data/api/RetrofitClient.kt`:
   ```kotlin
   const val BASE_URL = "https://3000-iz6cwdyxwdzmopz4hh7r4-944c08ac.sg1.manus.computer/api/trpc/"
   ```

3. **Build the project**
   ```bash
   ./gradlew build
   ```

4. **Run on emulator or device**
   ```bash
   ./gradlew installDebug
   ```

## Building for Production

### Generate Release APK

1. **Create signing key**
   ```bash
   keytool -genkey -v -keystore jee-tracker-release.keystore \
     -keyalg RSA -keysize 2048 -validity 10000 \
     -alias jee-tracker
   ```

2. **Build release APK**
   ```bash
   ./gradlew assembleRelease
   ```

3. **APK location**
   ```
   app/build/outputs/apk/release/app-release.apk
   ```

### Upload to Google Play Store

1. Create Google Play Developer account
2. Create new app
3. Upload APK
4. Fill app details and submit for review

## API Integration

Backend URL: `https://3000-iz6cwdyxwdzmopz4hh7r4-944c08ac.sg1.manus.computer/api/trpc`

### Key Endpoints
- `auth.me` - Get current user
- `topics.getSubjects` - Fetch all subjects
- `topics.getBySubject` - Get topics for a subject
- `progress.getStats` - Get progress statistics
- `progress.toggleTopic` - Mark topic as complete/pending
- `payment.*` - Payment and premium features

## Architecture

### MVVM Pattern
- **Model** - Data models and database
- **View** - Jetpack Compose UI
- **ViewModel** - Business logic and state management

### Data Flow
```
UI (Compose) → ViewModel → Repository → API/Database
```

## Database Schema

### Tables
- `users` - User accounts
- `subjects` - JEE subjects
- `categories` - Topic categories
- `topics` - Individual topics
- `user_progress` - Topic completion status
- `subscriptions` - User subscriptions
- `payments` - Payment history
- `notes` - User notes
- `recommendations` - AI recommendations

## Testing

### Unit Tests
```bash
./gradlew test
```

### Instrumentation Tests
```bash
./gradlew connectedAndroidTest
```

### Manual Testing Checklist
- [ ] Login with OAuth
- [ ] View dashboard and progress
- [ ] Toggle topics
- [ ] Reset progress
- [ ] Navigate subjects
- [ ] View analytics (premium)
- [ ] Create/edit notes (premium)
- [ ] Subscribe to plans
- [ ] Logout

## Performance Optimization

- **Lazy Loading** - Load data on demand
- **Caching** - Local database for offline access
- **Coroutines** - Non-blocking operations
- **Compose** - Efficient UI rendering

## Troubleshooting

### Build Issues
```bash
# Clean build
./gradlew clean build

# Update dependencies
./gradlew dependencies --refresh-dependencies
```

### Runtime Issues
- Check logcat for errors: `adb logcat`
- Verify API connectivity
- Check database queries

### Stripe Integration
- Verify Stripe keys in BuildConfig
- Check webhook configuration
- Review Stripe dashboard

## Future Enhancements

- [ ] Offline mode with sync
- [ ] Push notifications
- [ ] Study schedule integration
- [ ] Video tutorials
- [ ] Leaderboard features
- [ ] Dark mode support
- [ ] Multi-language support

## Support

For issues or questions:
1. Check FAQ in app
2. Contact support via app
3. Visit help.manus.im

## License

MIT License - See LICENSE file

## Version

**1.0.0** - Initial Release

---

**Built with Kotlin + Jetpack Compose + Material Design 3 + Stripe**
