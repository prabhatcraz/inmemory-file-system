# inmemory-file-system
A simulation of file system in memory

The interface has been wrapped into a commandline program and all the run time exceptions are being swallowed and the message is displayed.
## Instructions
Build
```bash
mvn clean install
```

Start the program
```bash
java -jar target/inmemory-file-system-0.0.1-SNAPSHOT.jar
```

e.g.
```bash
mkdir a
cd a
touch b
ls
mkdir c
cd c
touch d
rm d
rm -r /a/c
rm d
```