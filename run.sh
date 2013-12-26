#!/bin/sh
# For debugging jagged graphics
#java -verbose:gc -XX:+PrintCompilation -client -cp $SCALA_HOME/lib/scala-library.jar:./target/classes:lib/base64-2.3.8.jar $@

export SCALA_JAR=$SCALA_HOME/libexec/lib/scala-library.jar

if [ ! -f $SCALA_JAR ]; then
    echo "Scala jar not found from $SCALA_JAR"
    exit -1
fi

# Try -Xint
java -d64 -client -cp $SCALA_JAR:./target/classes:./lib/* $@
