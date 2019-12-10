-- Geben Sie genau die Entwickler an, die mehr als ein Spiel veroentlicht haben.

SELECT Entwickler.*
FROM Entwickler 
JOIN Produkt ON Entwickler.Email = Produkt.Email
JOIN Spiel ON Produkt.ProduktID = Spiel.ProduktID
GROUP BY Entwickler.EMail
HAVING (count(Spiel.ProduktID)>1);

-- Geben Sie genau die Spiele an, die keine Zusatzinhalte besitzen und einen Nachfolger haben.

SELECT Spiel.*
FROM Spiel
JOIN S_Nachfolger_S ON Spiel.ProduktID = S_Nachfolger_S.ProduktIDEins 
WHERE Spiel.ProduktID NOT IN (
	Select Spiel.ProduktID 
	FROM Spiel
	JOIN Zusatzinhalt ON Spiel.ProduktID = Zusatzinhalt.SpielID);


-- Geben Sie genau die Kunden an, die ein Spiel auf ihrer Wunschliste haben, welches mindestens 2 andere Kunden besitzen.

SELECT Kunde.*
FROM Kunde
JOIN Wunschliste ON Kunde.EMail = Wunschliste.EMail
JOIN W_enthaelt_P ON Wunschliste.WID = W_enthaelt_P.WID
WHERE W_enthaelt_P.ProduktID IN(
	SELECT ProduktID
	FROM Kunde
	JOIN K_kauft_P ON Kunde.EMail = K_kauft_P.EMail
	GROUP BY K_kauft_P.ProduktID
	HAVING (count(K_kauft_P.EMail)>1));