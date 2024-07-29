# Prova finale di Ingegneria del Software - Anno Accademico 2019-2020
![Santorini box](src/main/resources/GUI/santorini_img.jpg)

Il progetto consiste nello sviluppo di una versione software distribuita del board game [Santorini](http://www.craniocreations.it/prodotto/santorini/). 
E' stato applicato il pattern architetturale Model-View-Controller. L'applicazione prevede un **server**, che accetta le richieste di connessione dei client e gestisce 
lo svolgimento delle partite, e un **client**, realizzato sia in versione CLI che in versione GUI usando JavaFX.

## Documentazione del progetto

### UML

Si riportano collegamenti alle cartelle con i diagrammi UML realizzati all'inizio dello sviluppo, più di alto livello, e i diagrammi UML finali, generati con tool automatici

- [Diagrammi UML iniziali](https://github.com/tataboxxe/ing-sw-2020-Pagano-Somaschini-Tegli/tree/master/deliverables/UML/Initial)
- [Diagrammi UML di alto livello](https://github.com/tataboxxe/ing-sw-2020-Pagano-Somaschini-Tegli/tree/master/deliverables/UML/High%20Level)
- [Diagrammi UML finali](https://github.com/tataboxxe/ing-sw-2020-Pagano-Somaschini-Tegli/tree/master/deliverables/UML/Final)

### Librerie utilizzate
|Libreria|Descrizione|
|---------------|-----------|
|__junit__|framework Java per lo unit testing|
|__maven__|build automation e dependency management tool|
|__JavaFX__|libreria grafica di Java|

### JavaDoc
Il codice è stato documentato secondo la notazione Javadoc. I file di documentazione sono reperibili [qui](https://github.com/tataboxxe/ing-sw-2020-Pagano-Somaschini-Tegli/tree/master/docs)

### Jars

Abbiamo prodotto 
- un executable jar per il server
- un executable jar per il client su sistema Windows
- un executable jar per il client su sistema Unix-like

I 3 jar si trovano [qui](https://github.com/tataboxxe/ing-sw-2020-Pagano-Somaschini-Tegli/tree/master/deliverables/jars)

## Funzionalità
### Funzionalità Sviluppate
- Regole Complete
- CLI
- GUI
- Socket
- 2 FA

### Funzionalità Aggiuntive Sviluppate
- Partite multiple
- Advanced Gods


Come leggera aggiunta alla specifica abbiamo fatto in modo che una disconnessione (non durante l'inizializzazione) venga interpretata come sconfitta e non necessariamente faccia terminare la partita. 
Per esempio, se in una partita da 3 giocatori qualcuno si disconnette la partita continua e il giocatore disconnesso viene semplicemente dichiarato sconfitto

## Esecuzione dei JAR

### Client

*I client per sistemi Windows e Unix-like funzionano allo stesso modo.*

I jar client permettono di specificare, con opportuni arguments passati
- tipologia di interfaccia utente (CLI o GUI)
- indirizzo IP a cui raggiungere il server
- numero di porta su cui è in ascolto il server

La specifica della tipologia di interfaccia è **indispensabile** e avviene indicando "cli" o "gui" (case-insensitive) dopo il nome dell'eseguibile jar

Il secondo e il terzo argomento sono invece opzionali e in caso non vengano specificati il programma utilizzerà i valori di default
- *localhost* per 
l'IP
- *12345* per il numero di porta

Affinchè un client possa avviarsi è **necessario** che ci sia un server attivo all'indirizzo e porta specificati

#### CLI
Per una migliore esperienza di gioco da linea di comando è consigliato l'utilizzo di un terminale che supporti gli ANSI escape. 
Il comando per avviare il client in modalità CLI è perciò il seguente
```
java -jar Client-jar-with-dependencies.jar cli [ip] [port]
```

I comandi principali supportati dalla CLI sono:
- **quit** per disconnettersi dal server (e concedere la partita, se è terminata la fase di inizializzazione)
- **description NOME_CARTA** per visualizzare una breve descrizione del potere divinità specificato
- **r,c** (dove r e c sono interi compresi tra 0 e 4, estremi inclusi) per indicare una posizione sul tabellone. Questo serve praticamente in tutte le fasi di 
gioco, per esempio per selezionare un worker e la casella su cui muoverlo e farlo costruire

Quando c'è la possibilità di utilizzare il potere divinità, l'applicazione pone una domanda binaria, a cui l'utente deve rispondere digitand una delle alternative proposte

Durante l'inizializzazione di una partita viene richiesto di scegliere un potere divinità ed eventualmente il primo player a giocare (se chi sta leggendo ha 
la fortuna di essere il challenger). In entrambi i casi, ciò si fa semplicemente digitando il nome della carta o del player
#### GUI

La GUI supporta le stesse modalità di interazione indicate per la CLI, con l'aggiunta di qualche widget e abbreviazione, come 
la possibilità di cliccare direttamente su una casella del tabellone. Il comando per avviare il client in modalità GUI è perciò il 
seguente

```
java -jar Client-jar-with-dependencies.jar gui [ip] [port]
```

### Server

Il jar server permette di specificare un numero di porta custom, con un argomento opzionale passato all'eseguibile. Se non viene specificato alcun numero di porta 
personalizzato o ne viene indicato uno non valido, l'applicazione utilizzerà quello di default (*12345*). Il comando per avviare il server è perciò il seguente
```
java -jar Server-jar-with-dependencies.jar [port]
```

## Testing

Sono stati testati il Model e il Controller del progetto. In particolare, sono stati effettuati diversi test di unità e di integrazione rispetto al funzionamento generale 
del sistema e agli aspetti dei poteri divinità, comuni o specifici delle singole carte

#### Test Coverage del package it.polimi.ingsw.model

Coverage raggiunto: 94%

![](src/main/resources/Test%20Coverage/Test%20Coverage%20it.polimi.ingsw.model.png)

#### Test Coverage del package it.polimi.ingsw.model.cards

Coverage raggiunto: 96%


![](src/main/resources/Test%20Coverage/Test%20Coverage%20it.polimi.ingsw.model.cards.png)

#### Test Coverage del package it.polimi.ingsw.controller

Coverage raggiunto: 82%


![](src/main/resources/Test%20Coverage/Test%20Coverage%20it.polimi.ingsw.controller.png)

## Componenti del gruppo
- [__Gabriele Pagano__](https://github.com/gabrielepagano)
- [__Marco Somaschini__](https://github.com/MarcoSomaschini)
- [__Gabriele Tegli__](https://github.com/tataboxxe)
