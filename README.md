README

Proiect Minesweeper

Configurare

Prerechizite

Java Development Kit (JDK) 8 sau mai mare
Un IDE sau editor de text (ex: IntelliJ IDEA, Eclipse, VS Code)

Instalare

Clonează repository-ul:

git clone [https://github.com/your-repo/minesweeper.git](https://github.com/Drumgun23/minesweeper)
cd minesweeper

Compilează fișierele Java:

javac src/minesweeper/*.java


Rularea Aplicației
Pornește serverul:

java -cp src minesweeper.MinesweeperServer
Pornește clientul:


java -cp src minesweeper.MinesweeperClient
Utilizare
Server: Serverul va începe să asculte conexiunile clienților.
Client: Clientul se va conecta la server. Jucătorii pot dezvălui și marca celulele făcând clic pe tabla de joc.
Versiuni Software
Java: 8 sau mai mare
Note Adiționale
Asigură-te că serverul rulează înainte de a porni clientul.
Modifică SERVER_ADDRESS și SERVER_PORT în MinesweeperClient.java dacă este necesar pentru a se potrivi cu configurarea serverului tău.
d) Script pentru Rularea Proiectului
Creați un script run.sh pentru a automatiza procesul de compilare și rulare:


#!/bin/bash

 Compilează fișierele Java
javac src/minesweeper/*.java

 Pornește serverul
echo "Pornire server..."
java -cp src minesweeper.MinesweeperServer &

 Pornește clientul
echo "Pornire client..."
java -cp src minesweeper.MinesweeperClient
Asigurați-vă că scriptul run.sh este executabil:

chmod +x run.sh
Cu aceste instrucțiuni și structură, proiectul Minesweeper ar trebui să fie clar, bine documentat și ușor de înțeles și rulat pentru oricine descarcă arhiva.
