#!/bin/bash

# Verify Java version
java -version

# Download Gradle 8.14 if not already available
GRADLE_VERSION=8.14
GRADLE_DIR=gradle-${GRADLE_VERSION}
GRADLE_ZIP=${GRADLE_DIR}-bin.zip
GRADLE_URL=https://services.gradle.org/distributions/${GRADLE_ZIP}

if [ ! -d "${GRADLE_DIR}" ]; then
    echo "Downloading Gradle ${GRADLE_VERSION}..."
    curl -L -o ${GRADLE_ZIP} ${GRADLE_URL}
    if [ -f "${GRADLE_ZIP}" ]; then
        unzip ${GRADLE_ZIP}
    else
        echo "Failed to download Gradle ${GRADLE_VERSION}. Using system Gradle instead."
    fi
fi

# Set path to Gradle 8.14
export PATH=${PWD}/${GRADLE_DIR}/bin:$PATH

# Print Gradle version to verify
gradle --version

# Ensure build directory exists
mkdir -p build/libs

# Run the Gradle build using Gradle 8.14
echo "no" | gradle clean build --no-daemon --stacktrace

# Copy the JAR to a known location
echo "Build complete. Copying JAR file..."
find build/libs -name "*.jar" -not -name "*unshaded*" -exec cp {} build/libs/BDCraft-1.1.0.jar \;
echo "Plugin JAR saved to build/libs/BDCraft-1.1.0.jar"