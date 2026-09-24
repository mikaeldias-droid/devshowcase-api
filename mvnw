#!/bin/sh
# ----------------------------------------------------------------------------
# Apache Maven Wrapper startup batch script, targeting POSIX
# ----------------------------------------------------------------------------

set -e

MAVEN_PROJECTBASEDIR="${0%/*}"
WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"

if [ ! -f "$WRAPPER_JAR" ]; then
    echo "Downloading Maven Wrapper JAR..."
    mkdir -p "$MAVEN_PROJECTBASEDIR/.mvn/wrapper"
    curl -sTool -o "$WRAPPER_JAR" https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar || \
    wget -O "$WRAPPER_JAR" https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
fi

exec java -jar "$WRAPPER_JAR" "$@"
