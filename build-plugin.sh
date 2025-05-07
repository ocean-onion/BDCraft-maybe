#!/bin/bash

# Set up Java 17 environment for the build
export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which java))))
export PATH="$JAVA_HOME/bin:$PATH"

# Verify Java version
java -version

# Run the Gradle build using the installed Gradle
gradle clean build -Dorg.gradle.java.home=$JAVA_HOME