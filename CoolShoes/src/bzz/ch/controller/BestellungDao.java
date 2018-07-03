/*
 * @author Vion Hasaj, Dominic Wyss, Lisi Useini
 * @version 1.0
 */

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

public class BestellungDao extends Observable {

	String query = "select BestellStatus.BSID, BestellStatus.Bestellnummer, BestellStatus.Status, BestellStatus.Bearbeitung, BestellStatus.Lieferunggeplant,"
			+ "Kunde.KID KID, Kunde.kName KNAME, Kunde.kVorname KVORNAME, Kunde.kAdresse KADRESSE, Kunde.kPLZ KPLZ, Kunde.kOrt KORT, Kunde.kEMailadresse KEMAIL, Kunde.kPwd KPWD, Mitarbeiter.MID MID, Mitarbeiter.maName MNAME, Mitarbeiter.maVorname MVORNAME "
			+ "from BestellStatus, Kunde, Mitarbeiter "
			+ "where BestellStatus.FKKunde=Kunde.KID and BestellStatus.FKMitarbeiter=Mitarbeiter.MID "
			+ "order by BestellStatus.Bestellnummer desc, BestellStatus.Bearbeitung desc";

	/**
	 * @return Collection<Bestellung>
	 */
	public Collection<Bestellung> getAlleBestellungen() {
		DBConnection connecter = new DBConnection();
		Connection con = connecter.getConnection();
		Collection<Bestellung> res = new ArrayList<Bestellung>();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				res.add(new Bestellung(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getDate(5),
						new Mitarbeiter(rs.getInt("MID"), rs.getString("MNAME"), rs.getString("MVORNAME")),
						new Kunde(rs.getInt("KID"), rs.getString("KNAME"), rs.getString("KVORNAME"),
								rs.getString("KADRESSE"), rs.getInt("KPLZ"), rs.getString("KORT"),
								rs.getString("KEMAIL"), rs.getString("KPWD"))));
			}
		} catch (SQLException err) {
			err.printStackTrace();
		}
		return res;
	}

	/**
	 * @param bestellNummer
	 * @return Bestellung
	 */
	public Collection<Bestellung> getHistory(String bestellNummer) {
		String query;
		Collection<Bestellung> resulutat = new ArrayList<Bestellung>();
		if (bestellNummer.charAt(bestellNummer.length() - 1) >= 'A') {
			query = "select BestellStatus.BSID, BestellStatus.Bestellnummer, BestellStatus.Status, BestellStatus.Bearbeitung, BestellStatus.Lieferunggeplant,"
					+ "Kunde.KID KID, Kunde.kName KNAME, Kunde.kVorname KVORNAME, Kunde.kAdresse KADRESSE, Kunde.kPLZ KPLZ, Kunde.kOrt KORT, Kunde.kEMailadresse KEMAIL, Kunde.kPwd KPWD, Mitarbeiter.MID MID, Mitarbeiter.maName MNAME, Mitarbeiter.maVorname MVORNAME "
					+ "from BestellStatus, Kunde, Mitarbeiter "
					+ "where BestellStatus.FKKunde=Kunde.KID and BestellStatus.FKMitarbeiter=Mitarbeiter.MID "
					+ "and (BestellStatus.bestellnummer like '"
					+ "[A-Z]' or BestellStatus.bestellnummer like '"
					+ "') " + "order by BestellStatus.Bearbeitung asc";
		} else {
			query = "select BestellStatus.BSID, BestellStatus.Bestellnummer, BestellStatus.Status, BestellStatus.Bearbeitung, BestellStatus.Lieferunggeplant,"
					+ "Kunde.KID KID, Kunde.kName KNAME, Kunde.kVorname KVORNAME, Kunde.kAdresse KADRESSE, Kunde.kPLZ KPLZ, Kunde.kOrt KORT, Kunde.kEMailadresse KEMAIL, Kunde.kPwd KPWD, Mitarbeiter.MID MID, Mitarbeiter.maName MNAME, Mitarbeiter.maVorname MVORNAME "
					+ "from BestellStatus, Kunde, Mitarbeiter "
					+ "where BestellStatus.FKKunde=Kunde.KID and BestellStatus.FKMitarbeiter=Mitarbeiter.MID "
					+ "and (BestellStatus.bestellnummer like '" + bestellNummer
					+ "[A-Z]' or BestellStatus.bestellnummer like '" + bestellNummer + "') "
					+ "order by BestellStatus.Bearbeitung asc";
		}

		DBConnection connecter = new DBConnection();
		Connection con = connecter.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				resulutat.add(new Bestellung(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getDate(5),
						new Mitarbeiter(rs.getInt("MID"), rs.getString("MNAME"), rs.getString("MVORNAME")),
						new Kunde(rs.getInt("KID"), rs.getString("KNAME"), rs.getString("KVORNAME"),
								rs.getString("KADRESSE"), rs.getInt("KPLZ"), rs.getString("KORT"),
								rs.getString("KEMAIL"), rs.getString("KPWD"))));
			}
		} catch (SQLException err) {
			err.printStackTrace();
		}
		return resulutat;
	}

	/**
	 * @param bestellungen
	 */
	public void insert(Bestellung... bestellungen) {
		String geplanteLieferung;
		DBConnection connecter = new DBConnection();
		Connection con = connecter.getConnection();
		String query = "insert into bestellstatus ( bsid, bestellnummer, status, bearbeitung, lieferunggeplant, fkkunde, fkmitarbeiter ) values ";
		for (Bestellung b : bestellungen) {
			if(b.getLieferungGeplant() != null) {
				geplanteLieferung = "to_date('" + b.getLieferungGeplant();
			}
			else {
				geplanteLieferung = null;
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(b.getBearbeitungsDatum());
		}
		query = query.substring(0, query.length() - 2);
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(query);
			con.commit();
			System.out.println(query);
		} catch (SQLException error) {
			try {
				System.out.println(error);
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			error.printStackTrace();
		}
	}
	
	/**
	 * @return Bestellung
	 */
	public Collection<Bestellung> getActive() {
		Collection<Bestellung> bestellungen = getAlleBestellungen();
		String bestellNummern = " ";
		Collection<Bestellung> resultat = new ArrayList<Bestellung>();
		for (Bestellung b : bestellungen) {
			if (!bestellNummern.contains(' ' + b.getBestellNr() + ' ') && !bestellNummern.matches(".* " + b.getBestellNr() + "[A-Z] .*")) {
				resultat.add(b);
				bestellNummern += b.getBestellNr() + ' ';
			}
		}
		return resultat;
	}
}
