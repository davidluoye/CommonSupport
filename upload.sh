#!/bin/sh

### you should input your module name to execute this action.
# such as:
# ./gradlew lib-core:publishMavenJavaPublicationToSonatypeRepository

#./gradlew $1:publishMavenJavaPublicationToMavenRepository

./gradlew $1:publishMavenJavaPublicationToOssrh
