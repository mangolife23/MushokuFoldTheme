# Mushoku Fold Theme

A Galaxy Z Fold7 adaptive Android theme companion inspired by fantasy/mana aesthetics from *Mushoku Tensei*.

## v0.1 features
- Fold7-friendly adaptive layout (cover and inner display)
- Three generated fantasy wallpaper palettes
- On-device wallpaper generation (no bundled copyrighted anime art)
- One-tap wallpaper application
- GitHub Actions APK build

## Build locally
Requires Android SDK 36, JDK 17, and Gradle 9.1.

```bash
gradle :app:assembleDebug
```

APK output:
`app/build/outputs/apk/debug/app-debug.apk`

## GitHub build
Open **Actions → Build Fold7 Theme APK → Run workflow**.
Download the `MushokuFoldTheme-debug` artifact after the workflow completes.
