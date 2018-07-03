/*
 * @author Vion Hasaj, Dominic Wyss, Lisi Useini
 * @version 1.0
 */

package bzz.ch.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
//import java.sql.Date;
//import java.time.LocalDate;
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
//import javax.swing.JOptionPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import bzz.ch.model.*;
import bzz.ch.controller.*;

public class GUI extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;

	private BestellungDao BestellungDao;
	private StatusController statusController;
	private JComboBox<Mitarbeiter> mitarbeiterAuswahl;
	private JPanel bestellungsTabelle;

	public GUI(StatusController statusController) {
		this.statusController = statusController;
		statusController.addObserver(this);
		bestellungsTabelle = new JPanel(new GridLayout(0, 10));
		init();
	}

	/**
	 * This method is used in order to make the GUI work
	 */
	public void init() {
		resetBestellungsTabelle();
		mitarbeiterAuswahl = new JComboBox<Mitarbeiter>(
				MitarbeiterDao.getAlleMitarbeiter().toArray(new Mitarbeiter[0]));
		mitarbeiterAuswahl.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					statusController.setMitarbeiter((Mitarbeiter) e.getItem());
				}
			}
		});
		statusController.setMitarbeiter((Mitarbeiter) mitarbeiterAuswahl.getSelectedItem());

		this.add(mitarbeiterAuswahl, BorderLayout.SOUTH);
		this.setSize(1000, 700);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocation(1000, 600);
	}
	
	/**
	 * @param bestellung
	 * @return resultat
	 */
	public JComboBox<String> generiereStatus(Bestellung bestellung) {
		String status = bestellung.getStatus();
		JComboBox<String> resultat = new JComboBox<String>();
		switch (status) {
		case "Auftrag bestellt":
			resultat.addItem("Auftrag bestellt");
			resultat.addItem("Auftrag aufbereiten");
			resultat.addItem("Teilauftrag verspätet");
			break;
		case "Auftrag aufbereiten":
			resultat.addItem("Auftrag aufbereiten");
			resultat.addItem("Auftrag versandbereit");
			break;
		case "Teilauftrag verspätet":
			resultat.addItem("Auftrag verspätet");
			resultat.addItem("Auftrag aufbereiten");
			break;
		case "Auftrag versandbereit":
			resultat.addItem("Auftrag versandbereit");
			resultat.addItem("Auftrag abgeholt");
			break;
		case "Auftrag abgeholt":
			resultat.addItem("Auftrag abgeholt");
			resultat.addItem("Auftrag geliefert");
			break;
		case "Auftrag geliefert":
			resultat.addItem("Auftrag geliefert");
			break;
		}
//		resultat.addItemListener(e -> {
//			if (e.getStateChange() == ItemEvent.SELECTED) {
//				if (((String) e.getItemSelectable().getSelectedObjects()[0]).equals("Teilauftrag verspätet")) {
//					String date = ((String) JOptionPane.showInputDialog(this, "Geplantes Lieferdatum: \n(YYYY-MM-DD)",
//							"Lieferungsdatum", JOptionPane.PLAIN_MESSAGE, null, null, null)).trim();
//					if (date != null && date.matches("[0-9][0-9][0-9][0-9]-(0|1)[0-9]-[0-3][0-9]")) {
//						statusController.updateStatus(bestellung,
//								(String) e.getItemSelectable().getSelectedObjects()[0],
//								Date.valueOf(LocalDate.parse(date)));
//					}
//				} else {
//					statusController.updateStatus(bestellung, (String) e.getItemSelectable().getSelectedObjects()[0]);
//				}
//			}
//		});
		return resultat;
	}

	/**
	 * Diese methode stellt die Tabelle wieder her
	 */
	public void resetBestellungsTabelle() {
		this.remove(bestellungsTabelle);
		bestellungsTabelle.removeAll();
		bestellungsTabelle.add(new JLabel("BestellNummer"));
		bestellungsTabelle.add(new JLabel("Kunde"));
		bestellungsTabelle.add(new JLabel("Status"));
		bestellungsTabelle.add(new JLabel("Lieferung geplant"));
		bestellungsTabelle.add(new JLabel("Bestellungsverlauf"));

		Collection<Bestellung> bestellungen = BestellungDao.getActive();
		for (Bestellung bestellung : bestellungen) {
			bestellungsTabelle.add(new JLabel(bestellung.getBestellNr()));
			bestellungsTabelle.add(new JLabel(bestellung.getKunde().getVorname() + bestellung.getKunde().getName()));
			JComboBox<String> comboBox = generiereStatus(bestellung);
			bestellungsTabelle.add(comboBox);
			if (bestellung.getLieferungGeplant() != null) {
				bestellungsTabelle.add(new JLabel(bestellung.getLieferungGeplant().toString()));
			} else {
				bestellungsTabelle.add(new JLabel(""));
			}
			JButton button = new JButton("Bestellungsverlauf");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					zeigeBestellungsverlaufFenster(bestellung);
				}
			});
			bestellungsTabelle.add(button);
		}
		this.add(bestellungsTabelle);
	}

	/**
	 * @param bestellung
	 * Diese methode zeigt jede Bestellung einzeln an
	 */
	public void zeigeBestellungsverlaufFenster(Bestellung bestellung) {
		JDialog meinDialog = new JDialog();
		meinDialog.setTitle(bestellung.getBestellNr() + ": Bestellungsverlauf");
		Collection<Object[]> bestellungsVerlaufCollection = new ArrayList<Object[]>();
		for (Bestellung bestellung1 : BestellungDao.getHistory(bestellung.getBestellNr())) {
			bestellungsVerlaufCollection.add(bestellung1.kreiereBestellungsverlaufArray());
		}
		String[] rowNames = { "BestellNr", "Status", "Kunde", "Mitarbeiter", "Bearbeitungsdatum",
				"Geplantes Lieferdatum" };
		JTable tabelle = new JTable(bestellungsVerlaufCollection.toArray(new Object[0][]), rowNames);
		meinDialog.add(new JScrollPane(tabelle));
		meinDialog.setSize(500, 300);
		meinDialog.setVisible(true);
	}

	@Override
	public void update(Observable observable, Object obj) {
		resetBestellungsTabelle();
		System.out.println("update");
		bestellungsTabelle.revalidate();
		bestellungsTabelle.repaint();
	}
}