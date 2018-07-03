package bzz.ch.model;

public class BestellStatus {
	
	public String generiereBestellungsNummer(String bestellungNummer) {
		if(bestellungNummer.charAt(bestellungNummer.length()-1) >= 'A') {
			return bestellungNummer.substring(0, bestellungNummer.length()-1) + ((char)(bestellungNummer.charAt(bestellungNummer.length()-1)+1));
		} else {
			return bestellungNummer + "A";
		}
	}
	
}
