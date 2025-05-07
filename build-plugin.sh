#!/bin/bash

# Set up Java 17 environment for the build
export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which java))))
export PATH="$JAVA_HOME/bin:$PATH"

# Verify Java version
java -version

# Download and extract Gradle 8.14 if not available
if [ ! -d "gradle-8.14" ]; then
  echo "Downloading Gradle 8.14..."
  curl -O https://services.gradle.org/distributions/gradle-8.14-bin.zip
  unzip gradle-8.14-bin.zip
  rm gradle-8.14-bin.zip
fi

# Use the local Gradle 8.14 to build the project
./gradle-8.14/bin/gradle clean build -Dorg.gradle.java.home=$JAVA_HOME