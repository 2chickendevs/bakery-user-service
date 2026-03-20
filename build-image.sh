#!/bin/bash

set -e

DOCKER_USER="nntan041299"

echo "Running Maven build..."
mvn clean install -DskipTests

# Extract artifactId and version from pom.xml
ARTIFACT_ID=$(mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout)
VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

if [ -z "$ARTIFACT_ID" ] || [ -z "$VERSION" ]; then
  echo "Failed to read artifactId or version from pom.xml"
  exit 1
fi

IMAGE_NAME="${DOCKER_USER}/${ARTIFACT_ID}:${VERSION}"

echo "Building Docker image: $IMAGE_NAME"

docker build -t "$IMAGE_NAME" .

echo "Docker image built successfully: $IMAGE_NAME"