# filetransfer
Programa para transferencia de ficheros que soporta varios protocolos

## Protocolos soportados
- FTP: File Transfer Protocol
- FTPS: Secure FTP (FTP con TLS)
- SFTP: FTP over SSH
- SCP: El protocolo SCP
- SMB: Protocolo CIFS de intercambio de ficheros

## Instalación

Desplegaremos y compilaremos la última version de la aplicación
```
git clone https://github.com/AlejandroAbad/filetransfer
cd filetransfer
cp filetransfer.sh.example filetransfer.sh
chmod a+x filetransfer.sh
mvn package
```

Llegados a este punto, tenemos la aplicación compilada, nos falta configurarla
según nuestras necesidades. Para ello, editaremos el fichero `filetransfer.sh`.

Una vez configurado, ejecutaremos `./filetransfer.sh install`, una rutina
que creará los enlaces necesarios para tener disponible el comando `filetransfer` en el PATH 
(¡ y el comando `ft` que es un alias !)


## Actualización
Ejecuta `filetransfer update` y la propia aplicación te mostrará el camino :)


## Uso
¡ Ejecuta `filetransfer --help` para una guía completa de parámetros !
