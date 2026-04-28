# Progetto Scuola

Applicazione desktop sviluppata con JavaFX per la gestione di:
- studenti
- insegnanti
- corsi

Il progetto utilizza Maven per compilazione ed esecuzione e adotta file JSON per la persistenza dei dati.

## Stack Tecnologico

- Java 11 (compilazione con `release 11`)
- JavaFX (`javafx-controls`, `javafx-fxml`)
- Maven
- Jackson (`jackson-databind`, `jackson-datatype-jsr310`)

## Prerequisiti

Prima di avviare il progetto è necessario disporre dei seguenti componenti.

1. Java JDK 11 o compatibile
	- Sito Oracle: `https://www.oracle.com/java/technologies/downloads/`
	- Distribuzione OpenJDK (Adoptium): `https://adoptium.net/`
2. Maven 3.8+
	- Download ufficiale: `https://maven.apache.org/download.cgi`
	- Guida di installazione: `https://maven.apache.org/install.html`

### Verifica installazione

Aprire un terminale e verificare:

```powershell
java -version
mvn -version
```

I comandi devono restituire una versione valida di Java e Maven, correttamente configurate nel `PATH`.

## Installazione Del Progetto

1. Clonare la repository:

```bash
git clone https://github.com/DrGufo/progettoScuola.git
cd progettoScuola
```

2. Scaricare le dipendenze e compilare:

```bash
mvn clean compile
```

Al primo avvio Maven scarica plugin e librerie da remoto, incluse le dipendenze JavaFX.

## Avvio applicazione

Esegui:

```bash
mvn javafx:run
```

Classe di avvio configurata: `it.unife.lp.MainApp`.

## Struttura Dati E Persistenza

L'applicazione salva e carica i dati tramite tre file JSON distinti:
- `*_studenti.json`
- `*_insegnanti.json`
- `*_corsi.json`

Quando viene selezionato un file (ad esempio `scuola.json`), l'applicazione utilizza il nome base (`scuola`) e opera sui tre file con i suffissi indicati sopra.

### Dati Di Default

Nel progetto sono inclusi dataset iniziali in `src/main/resources/it/unife/lp/`:
- `default_studenti.json`
- `default_insegnanti.json`
- `default_corsi.json`

Comportamento all'avvio:
1. Se esiste un ultimo file salvato valido, viene ricaricato automaticamente.
2. In assenza di un file precedente valido, vengono caricati i file di default.

### Configurazione Ultimo File Aperto

L'applicazione memorizza il percorso dell'ultimo dataset in:
- Windows: `%USERPROFILE%\scuola_config.txt`

Per avviare una sessione senza auto-ricaricamento dell'ultimo file, è sufficiente eliminare il file di configurazione indicato sopra.

## Utilizzo Dell'applicazione

Menu principali:
- `File`: New, Open, Save, Save As
- `View`: Students, Teachers, Courses

Funzionalità principali:
- gestione CRUD studenti, insegnanti, corsi
- assegnazione insegnante a corso
- iscrizione/rimozione studenti dai corsi

### Scenario A: Utilizzo Dei Dati Di Default

1. Avviare l'applicazione con `mvn javafx:run`.
2. Operare sui dati caricati automaticamente all'avvio.
3. Salvare con `File -> Save As` per creare un dataset personalizzato.

### Scenario B: Creazione Di Un Dataset Nuovo

1. Aprire una vista (`Students`, `Teachers`, `Courses`) e selezionare `File -> New` per svuotare i dati della vista attiva.
2. Ripetere l'operazione su tutte e tre le viste per ottenere un dataset completamente vuoto.
3. Selezionare `File -> Save As` e indicare un nome base (esempio `nuovo_anno.json`).
4. Procedere con l'inserimento dei dati e salvare periodicamente.

Nota: il salvataggio è organizzato per tipologia di dato. Per mantenere coerenti i tre file (`studenti/insegnanti/corsi`), è consigliato utilizzare sempre lo stesso nome base.

## Comandi Maven Utili

- Compilare: `mvn clean compile`
- Eseguire app: `mvn javafx:run`
- Creare jar (senza bundling runtime): `mvn clean package`

## Problemi comuni

1. `mvn: command not found`
	- Maven non risulta nel `PATH`. Reinstallare o aggiornare le variabili di ambiente.
2. Versione Java errata
	- Il progetto compila con Java 11. Impostare `JAVA_HOME` su JDK 11.
3. Primo avvio lento
	- Comportamento previsto: Maven scarica dipendenze e plugin.
4. I dati non sono quelli attesi all'avvio
	- Verificare `%USERPROFILE%\scuola_config.txt`, che potrebbe puntare a un dataset precedente.

## Moduli E Dipendenze

Modulo Java dichiarato in `src/main/java/module-info.java`:
- `javafx.controls`
- `javafx.fxml`
- `javafx.graphics`
- `javafx.base`
- `com.fasterxml.jackson.databind`
- `com.fasterxml.jackson.datatype.jsr310`

## Buone Pratiche Operative

- Effettuare salvataggi frequenti dopo modifiche strutturali (nuovi corsi, nuovi insegnanti, iscrizioni).
- Conservare i tre file JSON dello stesso dataset nella medesima cartella.
- Evitare di rinominare manualmente un solo file della tripletta, per non compromettere la coerenza del caricamento.