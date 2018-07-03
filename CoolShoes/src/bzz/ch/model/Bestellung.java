/*
 * @author Vion Hasaj, Dominic Wyss, Lisi Useini
 * @version 1.0
 */

package bzz.ch.model;

import java.util.Date;

import bzz.ch.model.Kunde;
import bzz.ch.model.Mitarbeiter;

public class Bestellung {

	private int id;
	private String bestellNummer;
	private String status;
	private Date bearbeitungsDatum;
	private Date geplanteLieferung;
	private Mitarbeiter mitarbeiter;
	private Kunde kunde;

	public Bestellung(int id, String bestellNummer, String status, Date bearbeitungsDatum, Date geplanteLieferung,
			Mitarbeiter mitarbeiter, Kunde kunde) {
		this.id = id;
		this.bestellNummer = bestellNummer;
		this.status = status;
		this.bearbeitungsDatum = bearbeitungsDatum;
		this.geplanteLieferung = geplanteLieferung;
		this.mitarbeiter = mitarbeiter;
		this.kunde = kunde;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBestellNr() {
		return bestellNummer;
	}

	public void setBestellNummer(String bestellNummer) {
		this.bestellNummer = bestellNummer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getBearbeitungsDatum() {
		return bearbeitungsDatum;
	}

	public void setBearbeitungsDatum(Date bearbeitungsDatum) {
		this.bearbeitungsDatum = bearbeitungsDatum;
	}

	public Date getLieferungGeplant() {
		return geplanteLieferung;
	}

	public void setLieferungGeplant(Date lieferungGeplant) {
		this.geplanteLieferung = lieferungGeplant;
	}

	public Mitarbeiter getMitarbeiter() {
		return mitarbeiter;
	}

	public void setMitarbeiter(Mitarbeiter mitarbeiter) {
		this.mitarbeiter = mitarbeiter;
	}

	public Kunde getKunde() {
		return kunde;
	}

	public void setKunde(Kunde kunde) {
		this.kunde = kunde;
	}

	public Object[] kreiereBestellungsverlaufArray() {
		Object[] resultat = { bestellNummer, status, kunde, mitarbeiter, bearbeitungsDatum, geplanteLieferung};
		return resultat;
	}
}