#!/bin/sh

echo "Running static code analysis"
./gradlew lintKotlin
./gradlew detekt