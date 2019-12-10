-- User

INSERT INTO User(Email, Passwort, Land)
VALUES ('entwickler1@gmail.com', 'Ads123TTT', 'Deutschland');
INSERT INTO User(Email, Passwort, Land)
VALUES ('entwickler2@gmail.com', 'asdasd2', 'Frankreich');
INSERT INTO User(Email, Passwort, Land)
VALUES ('entwickler3@gmail.com', 'Ads142TTT', 'Amerika');

INSERT INTO User(Email, Passwort, Land)
VALUES ('kunde1@gmail.com', 'asd!das', 'Spanien');
INSERT INTO User(Email, Passwort, Land)
VALUES ('kunde2@gmail.com', 'âaaaaaaa', 'Italien');
INSERT INTO User(Email, Passwort, Land)
VALUES ('kunde3@gmail.com', 'Ads142TTT', 'Amerika');
INSERT INTO User(Email, Passwort, Land)
VALUES ('kunde4@gmail.com', 'Ads142TTT', 'Amerika');
INSERT INTO User(Email, Passwort, Land)
VALUES ('kunde5@gmail.com', 'Ads142TTT', 'Amerika');


-- Entwickler
INSERT INTO Entwickler(Email, Homepage, Studioname)
VALUES ('entwickler1@gmail.com', 'http://blizzard.com', 'Blizzard');
INSERT INTO Entwickler(Email, Homepage, Studioname)
VALUES ('entwickler2@gmail.com', 'http://https://www.activision.com', 'Activision');
INSERT INTO Entwickler(Email, Homepage, Studioname)
VALUES ('entwickler3@gmail.com', 'http://www.lucasfilm.com/what-we-do/games/', 'Lucas Arts');

--Kunde
INSERT INTO Kunde(Email, Benutzername,Nachname, Vorname)
VALUES ('kunde1@gmail.com', 'syin', 'lore', 'ipsum');
INSERT INTO Kunde(Email, Benutzername,Nachname, Vorname)
VALUES ('kunde2@gmail.com', 'syim', 'Ipsum', 'lore');
INSERT INTO Kunde(Email, Benutzername,Nachname, Vorname)
VALUES ('kunde3@gmail.com', 'Peter', 'Ipsum', 'lore');
INSERT INTO Kunde(Email, Benutzername,Nachname, Vorname)
VALUES ('kunde4@gmail.com', 'Baum', 'Ipsum', 'lore');
INSERT INTO Kunde(Email, Benutzername,Nachname, Vorname)
VALUES ('kunde5@gmail.com', 'Lustig', 'Ipsum', 'lore');

-- Kategorie
INSERT INTO Kategorie(Bezeichnung)
VALUES ('Soundtrack');
INSERT INTO Kategorie(Bezeichnung)
VALUES ('Addon');
INSERT INTO Kategorie(Bezeichnung)
VALUES ('Skinpack');


--Produkt

INSERT INTO Produkt(Name,Bild,Beschreibung, Email,Datum)
VALUES ('World of Warcraft',readfile('Bild.png'),'lore ipsum', 'entwickler1@gmail.com', '2018-11-22');


INSERT INTO Produkt(Name,Bild,Beschreibung, Email,Datum )
VALUES ('Starcraft',readfile('Bild.png'),'lore ipsum', 'entwickler1@gmail.com','2018-11-20');
INSERT INTO Produkt(Name,Bild,Beschreibung, Email,Datum)
VALUES ('Call of Duty',readfile('Bild.png'),'lore ipsum', 'entwickler2@gmail.com','2018-11-20' );
INSERT INTO Produkt(Name,Bild,Beschreibung, Email,Datum)
VALUES ('Call of Duty 2',readfile('Bild.png'),'lore ipsum', 'entwickler2@gmail.com' ,'2018-11-20');

INSERT INTO Produkt(Name,Bild,Beschreibung, Email,Datum)
VALUES ('Legion',readfile('Bild.png'),'lore ipsum', 'entwickler1@gmail.com','2018-11-20' );
INSERT INTO Produkt(Name,Bild,Beschreibung, Email,Datum)
VALUES ('Wings of Liberty',readfile('Bild.png'),'lore ipsum', 'entwickler1@gmail.com','2018-11-20' );
INSERT INTO Produkt(Name,Bild,Beschreibung, Email,Datum)
VALUES ('Call of Duty 3',readfile('Bild.png'),'lore ipsum', 'entwickler2@gmail.com' ,'2018-11-20');
INSERT INTO Produkt(Name,Bild,Beschreibung, Email,Datum)
VALUES ('Monkey Island',readfile('Bild.png'),'lore ipsum', 'entwickler3@gmail.com','2018-11-20' );
INSERT INTO Produkt(Name,Bild,Beschreibung, Email,Datum)
VALUES ('Musik',readfile('Bild.png'),'lore ipsum', 'entwickler2@gmail.com','2018-11-20' );


-- Spiel

INSERT INTO Spiel(ProduktID, FSK)
VALUES ('1', '12');
INSERT INTO Spiel(ProduktID, FSK)
VALUES ('2', '6');
INSERT INTO Spiel(ProduktID, FSK)
VALUES ('3', '16');
INSERT INTO Spiel(ProduktID, FSK)
VALUES ('4', '18');
INSERT INTO Spiel(ProduktID, FSK)
VALUES ('7', '18');
INSERT INTO Spiel(ProduktID, FSK)
VALUES ('8', '0');


--Zusatzinhalt

INSERT INTO Zusatzinhalt(ProduktID,Bezeichnung,KBezeichnung,SpielID)
VALUES ('5','lore ipsum', 'Addon', '1');
INSERT INTO Zusatzinhalt(ProduktID,Bezeichnung,KBezeichnung,SpielID)
VALUES ('6','lore ipsum', 'Skinpack', '2');
INSERT INTO Zusatzinhalt(ProduktID,Bezeichnung,KBezeichnung,SpielID)
VALUES ('9','lore ipsum', 'Soundtrack', '4');

-- Wunschliste

INSERT INTO Wunschliste(Titel,Email)
VALUES ('ListeEins','kunde1@gmail.com');
INSERT INTO Wunschliste(Titel,Email)
VALUES ('ListeZwei','kunde1@gmail.com');
INSERT INTO Wunschliste(Titel,Email)
VALUES ('ListeDrei','kunde2@gmail.com');
INSERT INTO Wunschliste(Titel,Email)
VALUES ('ListeVier','kunde5@gmail.com');

-- K_freund_K

INSERT INTO K_freund_K(EmailEins, EmailZwei)
VALUES ('kunde1@gmail.com', 'kunde2@gmail.com');
INSERT INTO K_freund_K(EmailEins, EmailZwei)
VALUES ('kunde2@gmail.com', 'kunde1@gmail.com');

-- W_enthaelt_P

INSERT INTO W_enthaelt_P(WID,ProduktID)
VALUES ('1','1');
INSERT INTO W_enthaelt_P(WID,ProduktID)
VALUES ('1','2');
INSERT INTO W_enthaelt_P(WID,ProduktID)
VALUES ('1','5');
INSERT INTO W_enthaelt_P(WID,ProduktID)
VALUES ('2','4');
INSERT INTO W_enthaelt_P(WID,ProduktID)
VALUES ('3','6');
INSERT INTO W_enthaelt_P(WID,ProduktID)
VALUES ('4','8');

-- K_kauft_P

INSERT INTO K_kauft_P(Email,ProduktID,Gutscheincode)
VALUES ('kunde1@gmail.com', '1', 'A12345678a');
INSERT INTO K_kauft_P(Email,ProduktID,Gutscheincode)
VALUES ('kunde1@gmail.com', '5', 'Abcdefghi2');
INSERT INTO K_kauft_P(Email,ProduktID,Gutscheincode)
VALUES ('kunde2@gmail.com', '3', 'B12345678b');
INSERT INTO K_kauft_P(Email,ProduktID,Gutscheincode)
VALUES ('kunde3@gmail.com', '8', 'B22345678b');
INSERT INTO K_kauft_P(Email,ProduktID,Gutscheincode)
VALUES ('kunde4@gmail.com', '8', 'B32345678b');

--K_bewertet_P

INSERT INTO K_bewertet_P(Email,ProduktID,Note,Text)
VALUES ('kunde1@gmail.com', '1', '1', 'lore ipsum');
INSERT INTO K_bewertet_P(Email,ProduktID,Note)
VALUES ('kunde2@gmail.com', '3', '3');
--neu
INSERT INTO K_bewertet_P(Email,ProduktID,Note)
VALUES ('kunde1@gmail.com', '5', '5');
INSERT INTO K_bewertet_P(Email,ProduktID,Note)
VALUES ('kunde3@gmail.com', '8', '1');
INSERT INTO K_bewertet_P(Email,ProduktID,Note)
VALUES ('kunde4@gmail.com', '8', '1');





-- S_Nachfolger_S


INSERT INTO S_Nachfolger_S(ProduktIDEins,ProduktIDZwei)
VALUES ('3','4');
INSERT INTO S_Nachfolger_S(ProduktIDEins,ProduktIDZwei)
VALUES ('4','7');




















