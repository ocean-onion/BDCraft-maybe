#!/bin/bash

# Verify Java version
java -version

# Print Gradle version
gradle --version

# Ensure build directory exists
mkdir -p build/libs

# Run the Gradle build using the installed Gradle
gradle clean build --no-daemon --stacktrace

# Copy the JAR to a known location
echo "Build complete. Copying JAR file..."
find build/libs -name "*.jar" -not -name "*unshaded*" -exec cp {} build/libs/BDCraft-1.0.0.jar \;
echo "Plugin JAR saved to build/libs/BDCraft-1.0.0.jar"