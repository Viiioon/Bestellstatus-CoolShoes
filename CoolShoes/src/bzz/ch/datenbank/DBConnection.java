package bzz.ch.datenbank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

	private String path = "CoolShoes.accdb";
	private Connection conn;

	public void connect() {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			conn = DriverManager.getConnection("jdbc:ucanaccess://" + path);
		} catch (ClassNotFoundException err) {
			System.out.println("Treiber kann nicht geladen werden");
		} catch (SQLException err) {
			System.out.println("Verbindung kann nicht aufgebaut werden");
		}

	}

	public void read() {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Mitarbeiter;");
			int column = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= column; i++)
					System.out.print(rs.getString(i) + " ");
			}
			rs.close();
			stmt.close();
		} catch (SQLException err) {
			System.out.println("ungültiger SQL-Befehl");
		}
	}

	public void write() {
		try {
			Statement stmt = conn.createStatement();
			stmt.execute("INSERT INTO Kunde (…, …, …) VALUES ('…', …, '…');");
			stmt.close();
		} catch (SQLException err) {
			System.out.println("ungültiger SQL-Befehl");
		}
	}
	
	public Connection getConnection() {
		if(conn == null) {
			connect();
		}
		return conn;
	}

	public static void main(String[] args) {
		DBConnection dbt = new DBConnection();
		dbt.connect();
		dbt.read();
	}
}
