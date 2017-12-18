#!/bin/bash --login

if [ "$JAVA_HOME" != "" ]; then
    JAVA_HOME=$JAVA_HOME
fi

if [ "$JAVA_HOME" = "" ]; then
    echo "Error: JAVA_HOME is not set."
    exit 1
fi

JAVA=$JAVA_HOME/bin/java

#classpath
#CLASSPATH=$CONF_DIR:$(ls $LIB_DIR/*.jar | tr '\n' :)
CLASSPATH=$CONF_DIR:$LIB_DIR/*

#java base opts
JAVA_BASE_OPTS=" -Djava.net.preferIPv4Stack=true "

#java jmx opts
#JAVA_JMX_OPTS=" -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=$JMX_PORT -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false "

#java jvm opts
JAVA_JVM_OPTS=" -server -Xmx2g -Xms2g -Xmn1024m -Xss256K -XX:PermSize=64M -XX:MaxPermSize=128M "
#JAVA_JVM_OPTS=" -server -Xmx512M -Xms512m -Xmn256m -Xss256K -XX:PermSize=64M -XX:MaxPermSize=128M"
#JAVA_JVM_OPTS=" -server -Xmx4g -Xms4g -Xmn2048m -Xss256K -XX:PermSize=64M -XX:MaxPermSize=128M "

#java opts
JAVA_OPTS="$JAVA_BASE_OPTS $JAVA_JMX_OPTS $JAVA_JVM_OPTS -cp $CLASSPATH"
