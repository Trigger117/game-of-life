Game of Life – Spring Boot

Simulatore di un mondo 10x10 popolato da creature che si muovono secondo regole definite.
Backend in Spring Boot, frontend statico HTML/JS, database H2 con Liquibase, container Docker.

✅ Requisiti

Per eseguire il progetto servono:

Java 17 o superiore

Maven 3+

(opzionale) Docker

▶️ Avvio locale
1️⃣ Compila:
mvn clean package

2️⃣ Avvia l’app:
java -jar target/game-of-life-0.0.1-SNAPSHOT.jar

3️⃣ Apri nel browser:
http://localhost:8080/

🐳 Avvio tramite Docker
1️⃣ Build dell’immagine:
docker build -t game-of-life .

2️⃣ Avvio container:
docker run --rm -p 8080:8080 game-of-life

3️⃣ Apri nel browser:
http://localhost:8080/

📁 Struttura del progetto
src/main/resources/static/index.html   ← UI
src/main/java/...                      ← Controller, Service, Logic
src/main/resources/db/changelog/       ← Liquibase
Dockerfile
pom.xml

❗ Note

Tutta la logica del gioco è nel backend.

Lo stato viene salvato nel DB a ogni step.

Ogni 20 turni viene aggiunta una nuova entità.