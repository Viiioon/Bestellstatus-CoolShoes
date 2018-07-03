/*
 * @author Vion Hasaj, Dominic Wyss, Lisi Useini
 * @version 1.0
 */

package bzz.ch.controller;

import java.sql.Date;
import java.util.Observable;

import bzz.ch.view.*;
import bzz.ch.model.BestellStatus;
import bzz.ch.model.Bestellung;
import bzz.ch.model.Mitarbeiter;

/**
 * @author User
 *
 */
public class StatusController extends Observable {

	public BestellungDao bestellungDao;
	public MitarbeiterDao mitarbeiterDAO;
	public Mitarbeiter ma;
	public BestellStatus bs;
	
	public StatusController() {
		bestellungDao = new BestellungDao();
		mitarbeiterDAO = new MitarbeiterDao();
	}
	
	/**
	 * @param bestellung
	 * @param status
	 * Diese Methode verändert den Status
	 */
	public void updateStatus(Bestellung b, String status) {
		b.setBearbeitungsDatum(new Date(new java.util.Date().getTime()));
		b.setStatus(status);
		b.setMitarbeiter(ma);
		bestellungDao.insert(b);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * @param bestellung
	 * @param status
	 * @param geplanteLieferung
	 * Diese Methode verändert den Status
	 */
	public void updateStatus(Bestellung bestellung, String status, Date geplanteLieferung) {
		Bestellung teilBestellung1 = bestellung;
		teilBestellung1.setBearbeitungsDatum(new Date(new java.util.Date().getTime()));
		teilBestellung1.setBestellNummer(bs.generiereBestellungsNummer(bestellung.getBestellNr()));
		teilBestellung1.setMitarbeiter(ma);
		teilBestellung1.setStatus("Auftrag aufbereiten");
		
		Bestellung teilBestellung2 = new Bestellung(0, 
				bs.generiereBestellungsNummer(teilBestellung1.getBestellNr()), 
				status, new Date(new java.util.Date().getTime()), 
				geplanteLieferung, ma, bestellung.getKunde());
		
		bestellungDao.insert(teilBestellung1, teilBestellung2);
		setChanged();
		notifyObservers();
	}
	
	 /**
	 * @return mitarbeiter
	 */
	public Mitarbeiter getMitarbeiter() {
		 return ma;
	 }
	 
	 /**
	 * @param mitarbeiter
	 * set Mitarbeiter
	 */
	public void setMitarbeiter(Mitarbeiter mitarbeiter) {
		 this.ma = mitarbeiter;
	 }
	 
	 public static void main(String[] args) {
		new GUI(new StatusController());
	}
}
