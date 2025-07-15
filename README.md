# AutiWarrior – Backend API

> Sistema backend per un'app mobile pensata per supportare bambini con autismo, sviluppata con Java, Spring Boot e MySQL.

## 📌 Descrizione del progetto

**AutiWarrior** è un'applicazione mobile progettata per offrire supporto ai bambini con autismo e alle loro famiglie. Questo repository contiene l'intero sistema backend, costruito con **Java**, **Spring Boot** e **MySQL**, progettato per garantire sicurezza, affidabilità e comunicazione in tempo reale tra utenti con ruoli diversi.

L'applicazione gestisce due ruoli principali:

- **👨‍⚕️ Dottore** – Utente professionale che può caricare le proprie certificazioni.
- **👨‍👩‍👧 Parente** – Utente che può contattare i dottori disponibili tramite chat.

---

## 🚀 Funzionalità principali

- ✅ Autenticazione e registrazione sicura con Spring Security + JWT
- ✅ Login/Logout basato su token
- ✅ Verifica del numero di licenza per i dottori
- ✅ Sistema di chat in tempo reale tra parenti e dottori (WebSocket)
- ✅ Salvataggio dei messaggi nel database
- ✅ Completamento del profilo utente con foto e dati personali
- ✅ Recupero password via email con codice a scadenza
- ✅ Upload certificazioni PDF per dottori
- ✅ Visualizzazione dei dottori disponibili con recensioni
- ✅ Documentazione API interattiva con Swagger UI

---

## 🛠️ Stack Tecnologico

| Tecnologia           | Descrizione                                |
|----------------------|---------------------------------------------|
| **Java 17**          | Linguaggio principale                       |
| **Spring Boot**      | Framework per la creazione di API REST      |
| **MySQL**            | Database relazionale                        |
| **Spring Security**  | Autenticazione e autorizzazione             |
| **JWT**              | Gestione sicura dei token                   |
| **Spring Mail**      | Invio email automatico                      |
| **Thymeleaf**        | Template engine per email                   |
| **WebSocket**        | Comunicazione in tempo reale                |
| **Swagger/OpenAPI**  | Documentazione delle API                    |
| **Dotenv Java**      | Gestione delle variabili d’ambiente         |
| **Lombok**           | Riduzione del boilerplate code              |

---

## 📑 Endpoints principali

| Metodo | Endpoint                        | Descrizione                                      |
|--------|----------------------------------|--------------------------------------------------|
| POST   | `/api/auth/register`            | Registrazione nuovo utente                       |
| POST   | `/api/auth/login`               | Login e generazione JWT                          |
| POST   | `/api/users/complete-profile`   | Completamento profilo utente con foto            |
| POST   | `/api/chat/send`                | Inviare messaggi in chat                         |
| GET    | `/api/chat/history`             | Cronologia messaggi                              |
| POST   | `/api/password/forgot`          | Recupero password via email                      |
| POST   | `/api/doctors/upload-certificate` | Upload certificati in PDF per dottori           |
| GET    | `/api/doctors/available`        | Lista dei dottori disponibili                    |
| GET    | `/swagger-ui/index.html`        | Interfaccia Swagger per testare le API          |

---

## ⚙️ Come eseguire il progetto

Richiedi il file .env all’autore del progetto.
Contiene tutte le variabili ambientali necessarie per avviare correttamente l’applicazione (DB, email, JWT, ecc.).
