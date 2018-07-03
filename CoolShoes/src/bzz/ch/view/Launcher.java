package bzz.ch.view;

import bzz.ch.controller.StatusController;

public class Launcher {

	private static GUI gui;

	public static void main(String[] args) {
		
		gui = new GUI(new StatusController());

	}

}