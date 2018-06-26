package bzz.ch.model;
import java.util.Date;

public class Bestellung {
	
	private String bestellnummer;
	private Kunde kunde;
	private BestellStatus bestellstatus;
	private Date anfangsDatum;
	private Date verschiebungsDatum;
}
