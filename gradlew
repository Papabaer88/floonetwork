#!/usr/bin/env sh
set -eu

if command -v gradle >/dev/null 2>&1; then
  exec gradle "$@"
else
  echo "Gradle is not installed or not on PATH. Please install Gradle 8.x or run this via a local Gradle installation." >&2
  exit 1
fi
