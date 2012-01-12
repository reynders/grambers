#!/bin/sh

# For debugging jagged graphics
#java -verbose:gc -XX:+PrintCompilation -client -cp $SCALA_HOME/lib/scala-library.jar:./target/classes:lib/base64-2.3.8.jar $@

java -client -cp $SCALA_HOME/lib/scala-library.jar:./target/classes:lib/base64-2.3.8.jar $@
