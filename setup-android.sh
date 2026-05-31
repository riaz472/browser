#!/usr/bin/env bash
set -e

ANDROID_SDK_DIR="/home/runner/android-sdk"
CMDLINE_TOOLS_URL="https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip"

export ANDROID_HOME="$ANDROID_SDK_DIR"
export PATH="$PATH:$ANDROID_SDK_DIR/cmdline-tools/latest/bin:$ANDROID_SDK_DIR/platform-tools"

# Only download if not already set up
if [ ! -f "$ANDROID_SDK_DIR/cmdline-tools/latest/bin/sdkmanager" ]; then
  echo "Setting up Android SDK..."
  mkdir -p "$ANDROID_SDK_DIR/cmdline-tools"
  curl -s -o /tmp/cmdline-tools.zip "$CMDLINE_TOOLS_URL"
  unzip -q /tmp/cmdline-tools.zip -d /tmp/cmdline-tools-extract
  mkdir -p "$ANDROID_SDK_DIR/cmdline-tools/latest"
  mv /tmp/cmdline-tools-extract/cmdline-tools/* "$ANDROID_SDK_DIR/cmdline-tools/latest/"
  rm -f /tmp/cmdline-tools.zip
  yes | sdkmanager --licenses > /dev/null 2>&1
  sdkmanager "platforms;android-36" "build-tools;35.0.0" "platform-tools" > /dev/null 2>&1
  echo "Android SDK setup complete."
else
  echo "Android SDK already set up."
fi

echo "sdk.dir=$ANDROID_SDK_DIR" > /home/runner/workspace/local.properties
