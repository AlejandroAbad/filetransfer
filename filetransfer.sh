#!/bin/ksh
# SCRIPT DE EJECUCION DE LA APLICACION FILETRANSFER
# Version del script: v1.1
# Modifica las variables a continuación según te convenga !


# DIRECTORIO DE INSTALACION
# Establecer INSTALL_DIR al directorio raiz de filetransfer.
# Este directorio generalmente es el creado por GIT al hacer
# el 'git clone https://github.com/AlejandroAbad/filetransfer'
# inicialmente.
INSTALL_DIR="/usr/local/filetransfer"

# EJECUTABLE JAVA
# Ruta al ejecutable JAVA. Generalmente, usar el java disponible
# en el PATH funciona, siempre y cuando este sea versión 8 o superior
JAVA="java"

# LOGFILE
# Ruta completa al fichero de log de la aplicación.
LOGFILE="/tmp/filetransfer.log"

# MONGODB URI
# Esta es la cadena de conexión a MongoDB donde la aplicación va a
# intentar grabar todos los movimientos de ficheros que realize.
# Es un destino de log complementario al que se hace a fichero.
MONGOURI="mongodb://fileTransfer:password@mdb.hefame.es/fileTransfer"

# DEBUG
# Indica si se pintará la traza completa de las excepciones al fichero
# de log. (No se envían a MongoDB)
DEBUG="true"

########################################################################
# GENERALMENTE NO HAY QUE TOCAR NADA BAJO ESTA LINEA
########################################################################

VMARGS="-Djava.util.logging.config.file=logging.properties"
VMARGS="$VMARGS -Dmongodb=${MONGOURI}"
VMARGS="$VMARGS -Ddebug=${DEBUG}"
VMARGS="$VMARGS -Dlog=${LOGFILE}"


JAR="${INSTALL_DIR}/target/filetransfer.jar"


if [ "$1" == "update" ]
then
    CWD=$(pwd)
    cd $INSTALL_DIR
    
    if [ "$2" == "last" ]
    then
        echo "Moviendinos hasta la ultima version disponible de la aplicacion ..."
        git config --global user.email "dummy@example.com"
        git config --global user.name "Dummy"
        git add -A .
        git stash
        git stash drop
        git pull origin main
    else
        echo "Moviendonos a la version '$2' de la aplicación ..."
        git checkout tags/$2
        if [ $? -ne 0 ]
        then
            echo "No se pudo mover a la version indicada." >&2
            echo "Debe indicar la version '$0 update <version>'." >&2
            echo "Puede elegir entre una de las siguientes:" >&2
            git tag --list >&2
            echo "last - Actualizará a la ultima version (DESARROLLO)" >&2
            cd $CWD
            exit 1
        fi
    fi
    
	echo "Limpiando los ejecutables de versiones anteriores ..."
    rm $INSTALL_DIR/target/*.jar
    
	echo "Compilando aplicacion ..."
    mvn package

	echo "Generando enlaces simbolicos ..."
    cd target
    ln -s filetransfer-*-jar-with-dependencies.jar filetransfer.jar
    
    cd $CWD
    exit 0
    
fi


if [ "$1" == "install" ]
then
	echo "Generando enlaces simbolicos /usr/bin/filetransfer y /usr/bin/ft"
    ln -fs $INSTALL_DIR/filetransfer.sh /usr/bin/filetransfer
    ln -fs /usr/bin/filetransfer /usr/bin/ft
fi


$JAVA $VMARGS -jar $JAR $*
exit $?

