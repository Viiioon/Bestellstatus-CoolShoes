package bzz.ch.model;
import java.util.Date;

import bzz.ch.model.Kunde;
import bzz.ch.model.Mitarbeiter;

public class Bestellung {
	
	private int id;
	private String bestellNr;
	private String status;
	private Date bearbeitungsDatum;
	private Date lieferungGeplant;
	private Mitarbeiter mitarbeiter;
	private Kunde kunde;
	
	public Bestellung(int id, String bestellNr, String status, Date bearbeitungsDatum, Date lieferungGeplant, Mitarbeiter mitarbeiter, Kunde kunde) {
		this.id = id;
		this.bestellNr = bestellNr;
		this.status = status;
		this.bearbeitungsDatum = bearbeitungsDatum;
		this.lieferungGeplant = lieferungGeplant;
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
		return bestellNr;
	}
	public void setBestellNr(String bestellNr) {
		this.bestellNr = bestellNr;
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
		return lieferungGeplant;
	}
	public void setLieferungGeplant(Date lieferungGeplant) {
		this.lieferungGeplant = lieferungGeplant;
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

	public Object[] toHistoryArray() {
		Object[] res = {
				bestellNr,
				status,
				kunde,
				mitarbeiter,
				bearbeitungsDatum,
				lieferungGeplant
		};
		return res;
	}
	
	@Override
	public String toString() {
		return "Bestellung [id=" + id + ", bestellNr=" + bestellNr + ", status=" + status + ", bearbeitungsDatum="
				+ bearbeitungsDatum + ", lieferungGeplant=" + lieferungGeplant + ", mitarbeiter=" + mitarbeiter
				+ ", kunde=" + kunde + "]";
	}
	
}