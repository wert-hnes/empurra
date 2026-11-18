# EMPURRA — Android motion editor starter

EMPURRA is an early Android editor foundation written in Kotlin and Jetpack Compose.

## Included in this first milestone
- Dark editor shell and preview-monitor placeholder
- Timeline layer list with video, text and audio layer models
- Add-layer and add-audio UI actions
- Keyframe data model and a basic keyframe insertion interaction
- Bezier curve model
- Time-remap point model
- Media3 dependencies reserved for playback integration

## Build
1. Open this folder in Android Studio (a recent version).
2. Allow Gradle sync to finish.
3. Select the `app` run configuration and launch on an Android device/emulator.
4. To create an APK: Build > Build Bundle(s) / APK(s) > Build APK(s).

## Current limitations
This is a project scaffold, not yet a complete editor. The preview is a placeholder;
media import, decoding, compositing, waveform rendering, exact Bezier time inversion,
effect rendering, audio mixing, and export pipeline are planned next milestones.
