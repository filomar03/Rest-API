# Progetto Sistemi Distribuiti 2023-2024 - API REST

## `/domains`

### GET

**Descrizione**: restituisce le informazioni su tutti i domini.

**Parametri**: nessuno.

**Header**: nessuno.

**Body richiesta**: nessuno.

**Risposta**: 


**Codici di stato restituiti**: `200 OK`.


## `/domains/{domain}`

### GET

**Descrizione**: restituisce le informazioni sullo stato di registrazione di un certo dominio.

**Parametri**: `{domain}` specifica il nome del dominio di cui si vogliono ottenere informazioni.

**Header**: nessuno.

**Body richiesta**: nessuno.

**Risposta**: 
- `name` (stringa): nome del dominio.
- `state` (stringa): stato di acquisizione del dominio.
- `owner` (stringa): userId proprietario.
- `start` (stringa): data di inizio del periodo di acquisizione (ISO-8601 format).
- `end` (stringa): data di fine del periodo di acquisizione (ISO-8601 format).

**Codici di stato restituiti**: `200 OK`.


## `/domains/register`

### POST

**Descrizione**: permette di bloccare temporaneamente un dominio per procedere all'acquisto.

**Parametri**: nessuno.

**Header**: 
- `userId` (stringa): id dell'utente.

**Body richiesta**: 
- `domain` (stringa): nome del dominio.

**Risposta**: 
- `message` (stringa): indica il risultato dell'operazione.

**Codici di stato restituiti**: 
- `200 OK`.
- `409 CONFLICT`: quando il dominio non e' disponibile o gia' in fase d'acquisto da parte di un altro utente.
- `401 UNAUTHORIZED`: richiesta proveniente da un utente non registrato.


## `/domains/confirm`

### POST

**Descrizione**: conferma l'acquisto di un dominio precedentemente bloccato.

**Parametri**: nessuno.

**Header**: 
- `userId` (stringa): id dell'utente.

**Body richiesta**: 
- `domain` (stringa): nome del dominio.
- `start` (stringa): data di inizio del periodo di acquisizione (ISO-8601 format).
- `end` (stringa): data di fine del periodo di acquisizione (ISO-8601 format).

**Risposta**: 
- `message` (stringa): indica il risultato dell'operazione.

**Codici di stato restituiti**: 
- `200 OK`.
- `409 CONFLICT`: quando il dominio non e' stato prima bloccato o e' passato troppo tempo (piu' di 10 minuti).
- `401 UNAUTHORIZED`: richiesta proveniente da un utente non registrato.


## `/domains/renew`

### POST

**Descrizione**: rinnova un dominio precedentemente acquistato.

**Parametri**: nessuno.

**Header**: 
- `userId` (stringa): id dell'utente.

**Body richiesta**: 
- `domain` (stringa): nome del dominio.
- `end` (stringa): data di fine del periodo di acquisizione (ISO-8601 format).

**Risposta**: 
- `message` (stringa): indica il risultato dell'operazione.

**Codici di stato restituiti**:
- `200 OK`.
- `403 FORBIDDEN`: quando si cerca di rinnovare un dominio che non si possiede.
- `401 UNAUTHORIZED`: richiesta proveniente da un utente non registrato.


## `/orders/{userId}`

### GET

**Descrizione**: mostra gli oridini di un utente.

**Parametri**: `{userId}` specifica l'utente del quali si vogliono visualizzare gli oridini.

**Header**: nessuno.

**Body richiesta**:  nessuno.

**Risposta**: 
- `orders`: rappresenta una lista di oridni effettuati.

**Codici di stato restituiti**: 
- `200 OK`: se lo user esiste.
- `404 NOT FOUND`: se non esiste lo user.


## `/users/{userId}`

### GET

**Descrizione**: verifica l'esistenza dell'utente.

**Parametri**: `{userId}` specifica l'utente che si vuole verificare.

**Header**: nessuno.

**Body richiesta**:  nessuno.

**Risposta**: 
- `user` (string): indica se l'utente e' stato identificato o meno.

**Codici di stato restituiti**: 
- `200 OK`: utente verificato.
- `404 NOT FOUND`: utente non verificato.


## `/users/register`

### POST

**Descrizione**: registra un nuovo utente.

**Parametri**: nessuno.

**Header**: nessuno.

**Body richiesta**:
- `name` (stringa): nome utente. 
- `surname` (stringa): cognome utente. 
- `mail` (stringa): mail utente. 

**Risposta**: 
- `user` (string): indica se l'utente e' stato registrato o meno.

**Codici di stato restituiti**: 
- `200 OK`.
- `409 CONFLICT`: quando la mail e' gia registrata.