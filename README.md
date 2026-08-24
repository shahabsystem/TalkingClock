FarsiTalkingClock v1.8.0

This version intentionally removes Sherpa-ONNX and all bundled Persian TTS components to keep the APK small and reliable.

Speech is English-only. The app uses Android TextToSpeech and lets the user choose any TTS engine installed on the device.

Major changes:
- Removed Sherpa-ONNX, its AAR, Persian model and Kotlin integration.
- Added configurable pre-announcement tone: on/off, volume, duration and frequency, plus optional custom audio.
- Fixed activity time controls by replacing embedded TimePicker widgets with clickable TimePicker dialogs.
- Reduced oversized settings switches and controls.
- Added working themes, accent colors, text colors and interface font scaling.
- Added multiple clock styles including large digital, 7-segment-like monospace, minimal digital, analog and card digital.
- Main screen now rebuilds when appearance/clock settings change.


## v1.8.1
Fixed ClockReceiver.java missing symbol for android.net.Uri.
