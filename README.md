# CRAWL! - A Super Entertaining 2D Arcade Game 

## Game Description

crawlin TO DO make game descrption

<video controls src="2026-03-24 17-01-43.mkv" title="Title"></video>

---

## Features

- Core gameplay features
- Special mechanics
- UI features
- Technical features

---

## Tech Stack / Libraries

- Language: Java
- Frameworks:
  - JUnit (testing)
  - Maven (build tool)
- Libraries (ONLY real external ones, not Java standard library)

---

## Project Structure

TODO put some brackets beside some notable directories with an explanbation or just leave as is idk 
```
.
├── Design
│   ├── CMPT276_Group7TeamContract.pdf
│   ├── CMPT276_Group7_UML.pdf
│   ├── Phase 1 Game plan CMPT 276.pdf
│   └── uimockup.pdf
├── Documents
│   └── Phase2Report.pdf
├── README.md
├── [Help
├── mvn
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   └── resources
    └── test
        ├── java
        └── resources
```

## How to Build

Explain how to compile the project using Maven:

```bash
mvn clean install
```

## How to Run 
To run the game, use Maven from the project root directory:
```bash
mvn clean compile exec:java -Dexec.mainClass="ca.sfu.cmpt276.team7.Main"
```

## How to Test
```
mvn test
```
yap about where tests are, what type of tests exist, unit and integration 

## Test Coverage 
Tools used: jacoco
How to generate report:
```
mvn verify
```
Where to find it:
```target/site.jacoco/index.html```

TODO someone start riffing on how good this coverage is pls (objectively)
![alt text](image.png)


## Spring2026Team7

Members:  
MacKinnon, James,	jam48@sfu.ca  
Matsumoto, Yui,	yma99@sfu.ca  
Ng, Auston,	acn8@sfu.ca  
Pakravani, Rammy,	rpa89@sfu.ca  
Sandilands, Xavier,	njs12@sfu.ca  