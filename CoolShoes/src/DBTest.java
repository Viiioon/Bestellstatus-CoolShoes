import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTest {

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
			// Eine Abfrage an die Datenbank richten
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Mitarbeiter;");
			// die Anzahl der Einträge des Datensatzes lesen
			int column = rs.getMetaData().getColumnCount();
			// Datensatz umd Datensatz abarbeiten
			while (rs.next()) {
				for (int i = 1; i <= column; i++) // Beginnt mit Position 1 !!!
					System.out.print(rs.getString(i)); // hier erfolgt die Verarbeitung der Daten
			}
			rs.close(); // nach Gebrauch sind des ResultSet...
			stmt.close(); // ...und das Statement zu schliessen.
		} catch (SQLException err) {
			System.out.println("ungültiger SQL-Befehl");
		}
	}

	public void write() {
		try {
			// Einen insert an die Datenbank richten
			Statement stmt = conn.createStatement();
			stmt.execute("INSERT INTO Kunde (…, …, …) VALUES ('…', …, '…');");
			stmt.close();
		} catch (SQLException err) {
			System.out.println("ungültiger SQL-Befehl");
		}

	}

	public static void main(String[] args) {
		DBTest dbt = new DBTest();
		dbt.connect();
		dbt.read();

	}
}
