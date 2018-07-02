package bzz.ch.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import bzz.ch.model.*;
import bzz.ch.controller.*;

public class GUI extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;

	private BestellDao bestellDao;
	private StatusController statusController;
	private JComboBox<Mitarbeiter> mitarbeiterAuswahl;
	private JPanel bestellTable;
	
	public GUI(StatusController StatusController) {
		super("CoolShoes");
		this.statusController = StatusController;
		StatusController.addObserver(this);
		bestellTable = new JPanel(new GridLayout(0, 5));
		init();
	}
	
	public void init() {
		resetBestellTable();
		
		mitarbeiterAuswahl = new JComboBox<Mitarbeiter>(MitarbeiterDao.getAlleMitarbeiter().toArray(new Mitarbeiter[0]));
		mitarbeiterAuswahl.addItemListener(e -> {
			if(e.getStateChange() == ItemEvent.SELECTED) {
				statusController.setMitarbeiter((Mitarbeiter)e.getItem());
			}
		});
		statusController.setMitarbeiter((Mitarbeiter)mitarbeiterAuswahl.getSelectedItem());
		
		this.add(mitarbeiterAuswahl, BorderLayout.NORTH);
		this.setSize(500, 400);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	public void resetBestellTable() {
		this.remove(bestellTable);
		bestellTable.removeAll();
		bestellTable.add(new JLabel("BestellNr"));
		bestellTable.add(new JLabel("Kunde"));
		bestellTable.add(new JLabel("Status"));
		bestellTable.add(new JLabel("Lieferung geplant"));
		bestellTable.add(new JLabel("History"));
		
		Collection<Bestellung> bestellungen = bestellDao.getActive();
		for(Bestellung b : bestellungen) {
			bestellTable.add(new JLabel(b.getBestellNr()));
			bestellTable.add(new JLabel(b.getKunde().getVorname() +' ' + b.getKunde().getName()));
			JComboBox<String> comboBox = generateStatus(b);
			bestellTable.add(comboBox);
			bestellTable.add(new JLabel((b.getLieferungGeplant() != null) ? 
					b.getLieferungGeplant().toString() : null));
			JButton button = new JButton("History");
			button.addActionListener(e -> {
				historyDialog(b);
			});
			bestellTable.add(button);
		}
		this.add(bestellTable);
	}
	
	public JComboBox<String> generateStatus(Bestellung b) {
		String status = b.getStatus();
		JComboBox<String> res = new JComboBox<String>();
		switch(status) {
			case "Auftrag bestellt":
				res.addItem("Auftrag bestellt");
				res.addItem("Auftrag aufbereiten");
				res.addItem("Teilauftrag verspätet");
				break;
			case "Auftrag aufbereiten":
				res.addItem("Auftrag aufbereiten");
				res.addItem("Auftrag versandbereit");
				break;
			case "Teilauftrag verspätet":
				res.addItem("Auftrag verspätet");
				res.addItem("Auftrag aufbereiten");
				break;
			case "Auftrag versandbereit":
				res.addItem("Auftrag versandbereit");
				res.addItem("Auftrag abgeholt");
				break;
			case "Auftrag abgeholt":
				res.addItem("Auftrag abgeholt");
				res.addItem("Auftrag geliefert");
				break;
			case "Auftrag geliefert":
				res.addItem("Auftrag geliefert");
				break;
		}
		res.addItemListener(e -> {
			if(e.getStateChange() == ItemEvent.SELECTED ) {
				if(((String)e.getItemSelectable().getSelectedObjects()[0]).equals("Teilauftrag verspätet")) {
					String date = ((String)JOptionPane.showInputDialog(
								this, 
								"Geplantes Lieferdatum: \n(YYYY-MM-DD)",
								"Lieferungsdatum",
								JOptionPane.PLAIN_MESSAGE,
								null,
								null,
								null
							)
						).trim();
					if(date != null && date.matches("[0-9][0-9][0-9][0-9]-(0|1)[0-9]-[0-3][0-9]")) {
						statusController.updateStatus(b, (String)e.getItemSelectable().getSelectedObjects()[0], Date.valueOf(LocalDate.parse(date)));
					}
				} else {
					statusController.updateStatus(b, (String)e.getItemSelectable().getSelectedObjects()[0]);
				}
			}
		});
		return res;
	}

	public void historyDialog(Bestellung b) {
		JDialog dialog = new JDialog();
		dialog.setTitle(b.getBestellNr() + ": History");
		Collection<Object[]> history = new ArrayList<Object[]>();
		for(Bestellung b1 : bestellDao.getHistory(b.getBestellNr())) {
			history.add(b1.toHistoryArray());
		}
		String[] columnNames = {
				"BestellNr",
				"Status",
				"Kunde",
				"Mitarbeiter",
				"Bearbeitungsdatum",
				"Geplantes Lieferdatum"
		};
		JTable table = new JTable(new MyTableModel(history.toArray(new Object[0][]), columnNames));
		dialog.add(new JScrollPane(table));
		dialog.setSize(500, 300);
		dialog.setVisible(true);
	}
	
	@Override
	public void update(Observable observable, Object obj) {
		resetBestellTable();
		System.out.println("update");
		bestellTable.revalidate();
		bestellTable.repaint();
	}
	
	private class MyTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;
		private Object[][] data;
		private String[] columnNames;
		
		public MyTableModel(Object[][] data, String[] columnNames) {
			System.out.println(columnNames);
			this.data = data;
			this.columnNames = columnNames;
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
		
		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public int getRowCount() {
			return data.length;
		}

		@Override
		public Object getValueAt(int row, int col) {
			return data[row][col];
		}
		
		@Override
		public String getColumnName(int col) {
			return columnNames[col];
		}
	}
}
