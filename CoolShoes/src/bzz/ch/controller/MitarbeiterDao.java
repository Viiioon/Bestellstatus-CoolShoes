package bzz.ch.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import bzz.ch.datenbank.*;
import bzz.ch.model.*;

public class MitarbeiterDao {

	public static Collection<Mitarbeiter> getAlleMitarbeiter() {
		DBConnection connecter = new DBConnection();
		Connection con = connecter.getConnection();
		Collection<Mitarbeiter> res = new ArrayList<Mitarbeiter>();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Mitarbeiter;");
			
			while (rs.next()) {
				res.add(new Mitarbeiter(rs.getInt(1), rs.getString(2), rs.getString(3)));
			}
			rs.close();
			stmt.close();
			
		} catch (SQLException err) {
			System.out.println(err);
			System.out.println("ungültiger SQL-Befehl");
		}
		return res;
	}
	
}
