package bzz.ch.controller;

import java.sql.Date;
import java.util.Observable;

import bzz.ch.view.*;
import bzz.ch.model.Bestellung;
import bzz.ch.model.Mitarbeiter;

public class StatusController extends Observable {

	public BestellDao bestellDAO;
	public MitarbeiterDao mitarbeiterDAO;
	public Mitarbeiter mitarbeiter;
	
	public StatusController() {
		bestellDAO = new BestellDao();
		mitarbeiterDAO = new MitarbeiterDao();
	}
	
	public void updateStatus(Bestellung b, String status) {
		b.setBearbeitungsDatum(new Date(new java.util.Date().getTime()));
		b.setStatus(status);
		b.setMitarbeiter(mitarbeiter);
		bestellDAO.insert(b);
		setChanged();
		notifyObservers();
	}
	
	public void updateStatus(Bestellung b, String status, Date lieferungGeplant) {
		Bestellung teilBestellungA = b;
		teilBestellungA.setBearbeitungsDatum(new Date(new java.util.Date().getTime()));
		teilBestellungA.setBestellNr(generateBestellNr(b.getBestellNr()));
		teilBestellungA.setMitarbeiter(mitarbeiter);
		teilBestellungA.setStatus("Auftrag aufbereiten");
		
		Bestellung teilBestellungB = new Bestellung(0, 
				generateBestellNr(teilBestellungA.getBestellNr()), 
				status, new Date(new java.util.Date().getTime()), 
				lieferungGeplant, mitarbeiter, b.getKunde());
		
		bestellDAO.insert(teilBestellungA, teilBestellungB);
		setChanged();
		notifyObservers();
	}
	
	public String generateBestellNr(String bestellNr) {
		if(bestellNr.charAt(bestellNr.length()-1) >= 'A') {
			return bestellNr.substring(0, bestellNr.length()-1) + ((char)(bestellNr.charAt(bestellNr.length()-1)+1));
		} else {
			return bestellNr + 'A';
		}
	}
	
	 public Mitarbeiter getMitarbeiter() {
		 return mitarbeiter;
	 }
	 
	 public void setMitarbeiter(Mitarbeiter mitarbeiter) {
		 System.out.println(mitarbeiter.getVorname());
		 this.mitarbeiter = mitarbeiter;
	 }
	 
	 public static void main(String[] args) {
		new GUI(new StatusController());
	}
}
