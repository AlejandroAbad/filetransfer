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
./filetransfer.sh install
```
Una vez instalada, ya tendremos disponible el comando `filetransfer` en el PATH (y el comando `ft` que es un alias !)

## Actualuzación
Ejecuta `filetransfer update` y la propia aplicación te mostrará el camino :)

## Configuración
La configuración de la aplicación se encuentra en el propio script `filetransfer.sh`.

## Uso
¡ Ejecuta `filetransfer --help` para una guía completa de parámetros !

## Bugs conocidos
- Cuando se especifica el puerto de destino, este se ignora y se utiliza el puerto por defecto para el protocolo especificado
