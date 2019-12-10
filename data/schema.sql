PRAGMA auto_vacuum = 1;
PRAGMA encoding = "UTF-8";
PRAGMA foreign_keys = 1;
PRAGMA journal_mode = WAL;
PRAGMA synchronous = NORMAL;


CREATE TABLE 'User'(
'EMail' varchar COLLATE NOCASE not null check(length(substr(EMail,1,instr(EMail,'@')-1))> 0 AND substr(EMail,1,instr(EMail,'@')-1)NOT GLOB '[^a-zA-Z0-9]'
AND length(substr(substr(EMail,instr(EMail,'@')+1),1,instr(substr(EMail,instr(EMail,'@')+1),'.')-1))>0
AND substr(substr(EMail,instr(EMail,'@')+1),1,instr(substr(EMail,instr(EMail,'@')+1),'.')-1) NOT GLOB '[^a-zA-Z0-9]'
AND length(substr(substr(EMail,instr(EMail,'@')+1),instr(substr(EMail,instr(EMail,'@')+1),'.')+1))> 0
AND substr(substr(EMail,instr(EMail,'@')+1),instr(substr(EMail,instr(EMail,'@')+1),'.')+1) NOT GLOB '[^a-zA-Z]'),
'Passwort'VARCHAR(64) NOT NULL CHECK(length(Passwort)>5),
'Land'VARCHAR(64) NOT NULL CHECK(Land NOT GLOB '*[^ -~]*' AND length(Land)>0),
PRIMARY KEY('Email')
);

CREATE TABLE 'Entwickler' (
'EMail' VARCHAR PRIMARY KEY COLLATE NOCASE NOT NULL,
'Homepage'VARCHAR(64) COLLATE NOCASE NOT NULL CHECK(Homepage GLOB 'http://*'),
'Studioname'VARCHAR(64) NOT NULL CHECK(Studioname NOT GLOB '*[^ -~]*' AND length(Studioname)>0),
FOREIGN KEY ('EMail') REFERENCES 'User' ('EMail') ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE 'Kunde' (
'EMail' VARCHAR PRIMARY KEY COLLATE NOCASE NOT NULL,
'Benutzername'VARCHAR(64) UNIQUE NOT NULL CHECK(Benutzername NOT GLOB '*[^ -~]*' AND length(Benutzername)>0),
'Nachname'VARCHAR(64) NOT NULL CHECK(Nachname NOT GLOB '*[^ -~]*' AND length(Nachname)>0),
'Vorname'VARCHAR(64) NOT NULL CHECK(Vorname NOT GLOB '*[^ -~]*' AND length(Vorname)>0),
FOREIGN KEY ('EMail') REFERENCES 'User' ('EMail') ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE 'Kategorie'(
'Bezeichnung' VARCHAR(64) COLLATE NOCASE PRIMARY KEY UNIQUE NOT NULL CHECK (Bezeichnung=='Soundtrack' OR Bezeichnung=='Addon' OR Bezeichnung=='Skinpack')
);

CREATE TABLE 'Produkt'(
'ProduktID' INTEGER PRIMARY KEY AUTOINCREMENT,
'Name' VARCHAR(64) NOT NULL CHECK (Name NOT GLOB '*[^ -~]*' AND length (Name)>0),
'Bild' BLOB NOT NULL,
'Beschreibung' VARCHAR(64) NOT NULL CHECK (Beschreibung NOT GLOB '*[^ -~]*' AND length (Beschreibung)>0),
'EMail' VARCHAR COLLATE NOCASE NOT NULL CHECK (EMail NOT GLOB '*[^ -~]*' AND length (EMail)>0),
'Datum' DATE NOT NULL CHECK(Datum IS Date(Datum)) DEFAULT CURRENT_DATE,
FOREIGN KEY ('EMail') REFERENCES 'Entwickler' ('EMail') ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE 'Spiel'(
'ProduktID' INTEGER PRIMARY KEY UNIQUE NOT NULL,
'FSK' INTEGER NOT NULL CHECK(FSK==0 OR FSK==6 OR FSK==12 OR FSK==16 OR FSK==18),
FOREIGN KEY ('ProduktID') REFERENCES 'Produkt'('ProduktID')ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE 'Zusatzinhalt'(
'ProduktID' INTEGER PRIMARY KEY UNIQUE,
'Bezeichnung' VARCHAR(64) NOT NULL CHECK (Bezeichnung NOT GLOB '*[^ -~]*' AND length(Bezeichnung)>0),
'KBezeichnung' VARCHAR(64) NOT NULL CHECK (KBezeichnung NOT GLOB '*[^ -~]*' AND length(KBezeichnung)>0),
'SpielID' INTEGER NOT NULL CHECK(SpielID>0),
FOREIGN KEY ('ProduktID') REFERENCES 'Produkt'('ProduktID')ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY ('KBezeichnung') REFERENCES 'Kategorie'('Bezeichnung')ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY ('SpielID') REFERENCES 'Spiel'('ProduktID')ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE 'Wunschliste'(
'WID' INTEGER PRIMARY KEY AUTOINCREMENT,
'Titel' VARCHAR(64) NOT NULL CHECK (Titel NOT GLOB '*[^ -~]*' AND length (Titel)>0),
'Erstelldatum' DATE NOT NULL CHECK(Erstelldatum IS Date(Erstelldatum)) DEFAULT CURRENT_DATE,
'EMail' VARCHAR COLLATE NOCASE NOT NULL,
FOREIGN KEY ('EMail') REFERENCES 'Kunde' ('Email') ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE 'K_freund_K' (
'EMailEins' VARCHAR COLLATE NOCASE NOT NULL CHECK (EMailEins NOT GLOB '*[^ -~]*' AND length (EMailEins)>0),
'EMailZwei' VARCHAR COLLATE NOCASE NOT NULL CHECK (EMailZwei NOT GLOB '*[^ -~]*' AND length (EMailZwei)>0),
PRIMARY KEY ('EMailEins','EMailZwei'),
FOREIGN KEY ('EMailEins') REFERENCES 'Kunde' ('Email') ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY ('EMailZwei') REFERENCES 'Kunde' ('Email') ON UPDATE CASCADE ON DELETE CASCADE

);

CREATE TABLE 'W_enthaelt_P' (
'WID' INTEGER NOT NULL CHECK(WID>0),
'ProduktID' INTEGER NOT NULL CHECK(ProduktID>0),
PRIMARY KEY ('WID','ProduktID'),
FOREIGN KEY ('WID') REFERENCES 'Wunschliste' ('WID') ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY ('ProduktID') REFERENCES 'Produkt' ('ProduktID') ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE 'K_kauft_P' (
'EMail' VARCHAR COLLATE NOCASE NOT NULL CHECK (EMail NOT GLOB '*[^ -~]*' AND length (EMail)>0),
'ProduktID' INTEGER NOT NULL CHECK(ProduktID>0) ,
'Gutscheincode' VARCHAR(10) NOT NULL UNIQUE CHECK (Gutscheincode NOT GLOB '*[^ -~]*' AND length (Gutscheincode)==10 AND Gutscheincode GLOB '*[A-Z]*' AND Gutscheincode GLOB '*[0-9]*'),
PRIMARY KEY ('EMail','ProduktID'),
FOREIGN KEY ('EMail') REFERENCES 'Kunde' ('EMail') ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY ('ProduktID') REFERENCES 'Produkt' ('ProduktID') ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE 'K_bewertet_P' (
'EMail' VARCHAR COLLATE NOCASE NOT NULL CHECK (EMail NOT GLOB '*[^ -~]*' AND length (EMail)>0),
'ProduktID' INTEGER NOT NULL CHECK(ProduktID>0),
'Note' INTEGER NOT NULL CHECK (Note==1 OR Note==2 OR Note==3 OR Note==4 OR Note==5 OR Note==6),
'Text' TEXT CHECK (Text NOT GLOB '*[^ -~]*'),
PRIMARY KEY ('EMail','ProduktID'),
FOREIGN KEY ('EMail') REFERENCES 'Kunde' ('EMail') ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY ('ProduktID') REFERENCES 'Produkt' ('ProduktID') ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE 'S_Nachfolger_S' (
'ProduktIDEins' INTEGER NOT NULL CHECK(ProduktIDEins>0),
'ProduktIDZwei' INTEGER NOT NULL CHECK(ProduktIDZwei>0),
PRIMARY KEY ('ProduktIDEins'),
FOREIGN KEY ('ProduktIDEins') REFERENCES 'Spiel' ('ProduktID') ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY ('ProduktIDZwei') REFERENCES 'Spiel' ('ProduktID') ON UPDATE CASCADE ON DELETE CASCADE
);

-----------------------------------------------------
-- Trigger
-----------------------------------------------------

CREATE TRIGGER selbstBefreundet BEFORE INSERT ON K_freund_K
	WHEN (New.EMailEins = New.EMailZwei)
	BEGIN
          SELECT RAISE (ABORT, 'Kunde kann nicht mit sich selbst befreundet sein.');
    END;
	
	
CREATE TRIGGER dreiWunschlisten BEFORE INSERT ON Wunschliste
WHEN ((SELECT count(Wunschliste.Email)FROM Wunschliste WHERE Wunschliste.Email = New.Email)>=3)
	BEGIN
          SELECT RAISE (ABORT, 'Kunde kann nur drei Wunschlisten haben.');
    END;


CREATE TRIGGER bewertungKauf BEFORE INSERT ON K_bewertet_P
WHEN (New.ProduktID NOT IN(SELECT ProduktID FROM K_kauft_P WHERE EMail = New.EMail))
	BEGIN
          SELECT RAISE (ABORT, 'Kunde kann nur gekaufte Spiele bewerten.');
    END;























