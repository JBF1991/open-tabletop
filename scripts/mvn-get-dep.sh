#!/usr/bin/env bash
if [ $# -lt 1 ]; then
    echo "USAGE:"
    echo "    mvn-get-dep.sh <groupId:artifactId:version>"
    echo "EXAMPLE:"
    echo "    mvn-get-dep.sh org.bspfsystems:simple-json:1.2.0"
    echo ""
    exit 1
fi
mvn org.apache.maven.plugins:maven-dependency-plugin:3.1.2:copy \
  -DrepoUrl= \
  -Dartifact="$1" -Dmdep.stripVersion -DoutputDirectory=lib/

