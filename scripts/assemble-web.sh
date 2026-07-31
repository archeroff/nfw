#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

DIST="$ROOT/webApp/build/dist/wasmJs/productionExecutable"
OUT="$ROOT/build/web"

if [ ! -d "$DIST" ]; then
    echo "Distribution not found at $DIST."
    echo "Run first: ./gradlew :webApp:wasmJsBrowserDistribution"
    exit 1
fi

rm -rf "$OUT"
mkdir -p "$OUT"
cp -r "$DIST"/. "$OUT"/

VERSION="$(date +%s)"
sed "s/'w-cache-v1'/'w-cache-v${VERSION}'/" "$DIST/sw.js" > "$OUT/sw.js"

echo "Assembled web app into: $OUT"
echo "Service worker cache version: w-cache-v${VERSION}"
