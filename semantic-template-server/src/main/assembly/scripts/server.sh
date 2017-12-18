#!/bin/bash --login

#export LD_PRELOAD=/usr/local/lib/libtcmalloc.so
cd `dirname $0`
BIN_DIR=`pwd`
cd ..
BASE_DIR=`pwd`
CONF_DIR="$BASE_DIR/conf"
LIB_DIR="$BASE_DIR/lib"
LOG_DIR="$BASE_DIR/logs"
LOG_FILE="$LOG_DIR/server.log"
PID_DIR="$BASE_DIR/.pid"
PID_FILE="$PID_DIR/.run.PID"
SERVER_USER=`whoami`
MAIN_CLASS="server.IntentServer"

SERVER_NAME=`sed '/^dubbo.application.name/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`
#default dubbo port
SERVER_PORT=`sed '/^dubbo.protocol.dubbo.port/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`
LOG_FILE=`sed '/^dubbo.log.file/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`
JMX_PORT=`sed '/^jmx.port/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`

if [ -z "$SERVER_NAME" ]; then
    SERVER_NAME="server_name"
fi
if [ -z "$LOG_FILE" ]; then
    LOG_DIR="$BASE_DIR/logs"
    LOG_FILE="$LOG_DIR/server.log"
else
    LOG_DIR=`dirname $LOG_FILE`
fi
if [ -z "$JMX_PORT" ]; then
    JMX_PORT=1099
fi

source $BIN_DIR/env.sh

#check server running
function running(){
    if [ -f "$PID_FILE" ]; then
        PID=$(cat "$PID_FILE")
        if [ -z $PID ]; then
            return 1
        fi
        process=`ps aux|grep "$PID" | grep -v grep`;
        if [ "$process" == "" ]; then
            return 1;
        else
            return 0;
        fi
    else
        return 1
    fi
}

#check port used
function port_using(){
    if [ -n "$SERVER_PORT" ]; then
        SERVER_PORT_COUNT=`netstat -tln | grep $SERVER_PORT | wc -l`
        if [ $SERVER_PORT_COUNT -gt 0 ]; then
            return 0
        else
            return 1
        fi
    fi
    return 1
}

#start server
function start_server(){
    if running; then
        echo "server $SERVER_NAME is already running."
        exit 1
    fi

    if [ -z "$SERVER_PORT" ]; then
        echo "server $SERVER_NAME, port not found in dubbo.properties."
        exit 1
    fi

    if port_using; then
        echo "server $SERVER_NAME, port $SERVER_PORT is used."
        exit 1
    fi

    #create dir
    if [ ! -d $LOG_DIR ]; then
        mkdir -p $LOG_DIR
    fi
    if [ ! -d $PID_DIR ]; then
        mkdir -p $PID_DIR
    fi
    touch $LOG_FILE
    chown -R $SERVER_USER $LOG_DIR
    chown -R $SERVER_USER $PID_DIR

    echo "Starting $SERVER_NAME..."
    #echo "$JAVA $JAVA_OPTS $MAIN_CLASS"
    nohup $JAVA $JAVA_OPTS $MAIN_CLASS 2>&1 >> $LOG_FILE &
    echo $! > $PID_FILE
    chmod 755 $PID_FILE

    #check running
    COUNT=0
    while [ $COUNT -lt 1 ]; do
        echo -e ".\c"
        sleep 1
        if [ -n "$SERVER_PORT" ]; then
            #default protocol: dubbo
            #COUNT=`echo status | nc -i 1 127.0.0.1 $SERVER_PORT | grep -c OK`
            COUNT=`netstat -tln|awk '{print $4}'|grep ":$SERVER_PORT\$"|wc -l`
        fi
        if [ $COUNT -gt 0 ]; then
            break
        fi
    done
    echo "[OK]"
    echo "LOG FILE: $LOG_FILE"
}

#stop server
function stop_server(){
    if ! running; then
        echo "Error: $SERVER_NAME is not running."
        exit 1
    fi
    PID=$(cat $PID_FILE)
    echo -e "Stopping $SERVER_NAME...\c"
    kill $PID > /dev/null 2>&1

    count=0
    while [ $count -lt 1 ]; do
        echo -e ".\c"
        sleep 1
        count=1
        PID_EXIST=`ps -f -p $PID | grep java`
        if [ -n "$PID_EXIST" ]; then
            count=0
            break
        fi
    done
    echo "[OK]"
    echo "PID: $PID"
    >$PID_FILE
}

#help
function help(){
    echo "Usage: server.sh {start|stop|restart|help}" >&2
    echo "     start:       start server"
    echo "     stop:        stop server"
    echo "     restart:     restart server"
}

command=$1
shift 1
case $command in
    start)
        start_server $@;
        ;;
    stop)
        stop_server $@;
        ;;
    restart)
        $0 stop $@
        $0 start $@
        ;;
    help)
        help;
        ;;
    *)
        help;
        exit 1;
        ;;
esac
