package bzz.ch.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Observable;

import bzz.ch.datenbank.DBConnection;
import bzz.ch.model.Bestellung;
import bzz.ch.model.Kunde;
import bzz.ch.model.Mitarbeiter;

public class BestellDao extends Observable {

	public Collection<Bestellung> getAlleBestellungen() {
		DBConnection connecter = new DBConnection();
		Connection con = connecter.getConnection();
		Collection<Bestellung> res = new ArrayList<Bestellung>();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select BestellStatus.BSID, BestellStatus.Bestellnummer, BestellStatus.Status, BestellStatus.Bearbeitung, BestellStatus.Lieferunggeplant,"
					+ "Kunde.KID KID, Kunde.kName KNAME, Kunde.kVorname KVORNAME, Kunde.kAdresse KADRESSE, Kunde.kPLZ KPLZ, Kunde.kOrt KORT, Kunde.kEMailadresse KEMAIL, Kunde.kPwd KPWD, Mitarbeiter.MID MID, Mitarbeiter.maName MNAME, Mitarbeiter.maVorname MVORNAME "
					+ "from BestellStatus, Kunde, Mitarbeiter "
					+ "where BestellStatus.FKKunde=Kunde.KID and BestellStatus.FKMitarbeiter=Mitarbeiter.MID "
					+ "order by BestellStatus.Bestellnummer desc, BestellStatus.Bearbeitung desc");
			while(rs.next()) {
				res.add(new Bestellung(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getDate(5), 
					new Mitarbeiter(rs.getInt("MID"), rs.getString("MNAME"), rs.getString("MVORNAME")),
					new Kunde(rs.getInt("KID"), rs.getString("KNAME"), rs.getString("KVORNAME"), rs.getString("KADRESSE"), rs.getInt("KPLZ"), rs.getString("KORT"), rs.getString("KEMAIL"), rs.getString("KPWD"))
				));
			}
		} catch(SQLException err) {
			err.printStackTrace();
		}
		return res;
	}
	
	public Collection<Bestellung> getActive() {
		Collection<Bestellung> bestellungen = getAlleBestellungen();
		String bestellNrs = " ";
		Collection<Bestellung> res = new ArrayList<Bestellung>();
		for(Bestellung b : bestellungen) {
			if(!bestellNrs.contains(' ' + b.getBestellNr() + ' ') && !bestellNrs.matches(".* "+b.getBestellNr()+"[A-Z] .*")) {
				res.add(b);
				bestellNrs += b.getBestellNr() + ' ';
			}
		}
		return res;
	}
	
	public Collection<Bestellung> getHistory(String bestellNr) {
		String query;
		Collection<Bestellung> res = new ArrayList<Bestellung>();
		if(bestellNr.charAt(bestellNr.length()-1) >= 'A') {
			query = "select BestellStatus.BSID, BestellStatus.Bestellnummer, BestellStatus.Status, BestellStatus.Bearbeitung, BestellStatus.Lieferunggeplant,"
				+ "Kunde.KID KID, Kunde.kName KNAME, Kunde.kVorname KVORNAME, Kunde.kAdresse KADRESSE, Kunde.kPLZ KPLZ, Kunde.kOrt KORT, Kunde.kEMailadresse KEMAIL, Kunde.kPwd KPWD, Mitarbeiter.MID MID, Mitarbeiter.maName MNAME, Mitarbeiter.maVorname MVORNAME "
				+ "from BestellStatus, Kunde, Mitarbeiter "
				+ "where BestellStatus.FKKunde=Kunde.KID and BestellStatus.FKMitarbeiter=Mitarbeiter.MID "
				+ "and (BestellStatus.bestellnummer like '" + bestellNr.substring(0, bestellNr.length()-1) + "[A-Z]' or BestellStatus.bestellnummer like '" + bestellNr.substring(0, bestellNr.length()-1) + "') "
				+ "order by BestellStatus.Bearbeitung asc";
		} else {
			query = "select BestellStatus.BSID, BestellStatus.Bestellnummer, BestellStatus.Status, BestellStatus.Bearbeitung, BestellStatus.Lieferunggeplant,"
				+ "Kunde.KID KID, Kunde.kName KNAME, Kunde.kVorname KVORNAME, Kunde.kAdresse KADRESSE, Kunde.kPLZ KPLZ, Kunde.kOrt KORT, Kunde.kEMailadresse KEMAIL, Kunde.kPwd KPWD, Mitarbeiter.MID MID, Mitarbeiter.maName MNAME, Mitarbeiter.maVorname MVORNAME "
				+ "from BestellStatus, Kunde, Mitarbeiter "
				+ "where BestellStatus.FKKunde=Kunde.KID and BestellStatus.FKMitarbeiter=Mitarbeiter.MID "
				+ "and (BestellStatus.bestellnummer like '" + bestellNr + "[A-Z]' or BestellStatus.bestellnummer like '" + bestellNr + "') "
				+ "order by BestellStatus.Bearbeitung asc";
		}
	
		DBConnection connecter = new DBConnection();
		Connection con = connecter.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				res.add(new Bestellung(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getDate(5), 
					new Mitarbeiter(rs.getInt("MID"), rs.getString("MNAME"), rs.getString("MVORNAME")),
					new Kunde(rs.getInt("KID"), rs.getString("KNAME"), rs.getString("KVORNAME"), rs.getString("KADRESSE"), rs.getInt("KPLZ"), rs.getString("KORT"), rs.getString("KEMAIL"), rs.getString("KPWD"))
				));
			}
		} catch(SQLException err) {
			err.printStackTrace();
		}
		return res;
	}
	
	public void insert(Bestellung...bestellungen) {
		DBConnection connecter = new DBConnection();
		Connection con = connecter.getConnection();
		String query = "insert into bestellstatus ( bsid, bestellnummer, status, bearbeitung, lieferunggeplant, fkkunde, fkmitarbeiter ) values ";
		for(Bestellung b : bestellungen) {
			String lieferungGeplant = b.getLieferungGeplant() != null ? "to_date('" + b.getLieferungGeplant() + "','YYYY-MM-DD')" : null;
			Calendar cal = Calendar.getInstance();
			cal.setTime(b.getBearbeitungsDatum());
			query += "(" + b.getId() + ", '" + b.getBestellNr() + "', '" + b.getStatus() + "', " 
					+ "to_date('" + b.getBearbeitungsDatum() + ", " + cal.get(Calendar.HOUR_OF_DAY) 
					+ ':' + cal.get(Calendar.MINUTE) + ':' + cal.get(Calendar.SECOND) + "','YYYY-MM-DD, HH24:MI:SS'), " 
					+ lieferungGeplant + ", " + b.getKunde().getId() 
					+ ", " + b.getMitarbeiter().getId() + "), ";
		}
		query = query.substring(0, query.length()-2);
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(query);
			con.commit();
			System.out.println(query);
		} catch(SQLException err) {
			try {
				System.out.println(err);
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			err.printStackTrace();
		}
	}
}
