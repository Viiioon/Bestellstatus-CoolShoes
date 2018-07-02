package bzz.ch.model;

public class Kunde {
	
	private int id;
	private String name;
	private String vorname;
	private String adresse;
	private int plz;
	private String ort;
	private String eMail;
	private String passwort;
	
	public Kunde(int id, String name, String vorname, String adresse, int plz, String ort, String eMail, String passwort) {
		this.id = id;
		this.name = name;
		this.vorname = vorname;
		this.adresse = adresse;
		this.plz = plz;
		this.ort = ort;
		this.eMail = eMail;
		this.passwort = passwort;
	}
	
	public int  getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVorname() {
		return vorname;
	}
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public int getPlz() {
		return plz;
	}
	public void setPlz(int plz) {
		this.plz = plz;
	}
	public String getOrt() {
		return ort;
	}
	public void setOrt(String ort) {
		this.ort = ort;
	}
	public String geteMail() {
		return eMail;
	}
	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	public String getPasswort() {
		return passwort;
	}
	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	@Override
	public String toString() {
		return vorname + " " + name;
	}
	
}
