import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class GUI extends JComboBox {
    private List<String> array;

    public GUI(List<String> array) {
        super(array.toArray());
        this.array = array;
        this.setEditable(true);
        final JTextField textfield = (JTextField) this.getEditor().getEditorComponent();
        textfield.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        comboFilter(textfield.getText());
                    }
                });
            }
        });

    }

    public void comboFilter(String enteredText) {
        List<String> filterArray= new ArrayList<String>();
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).toLowerCase().contains(enteredText.toLowerCase())) {
                filterArray.add(array.get(i));
            }
        }
        if (filterArray.size() > 0) {
            this.setModel(new DefaultComboBoxModel(filterArray.toArray()));
            this.setSelectedItem(enteredText);
            this.showPopup();
        }
        else {
            this.hidePopup();
        }
    }

    /* Testing Codes */
    public static List<String> populateArray() {
        List<String> test = new ArrayList<String>();
        test.add("");
        test.add("Mountain Flight");
        test.add("Mount Climbing");
        test.add("Trekking");
        test.add("Rafting");
        test.add("Jungle Safari");
        test.add("Bungie Jumping");
        test.add("Para Gliding");
        return test;
    }

    public static void makeUI() {
        JFrame frame = new JFrame("Adventure in Nepal - Combo Filter Test");
        GUI acb = new GUI(populateArray());
        frame.getContentPane().add(acb);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {

        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        makeUI();
    }
}


//import java.awt.BorderLayout;
//import java.awt.Dimension;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import javax.swing.JTextField;
//
//public class GUI extends JFrame{
//
//	private JTextField title;
//	private JButton showHistory;
//	
//	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//		new GUI();
//	}
//	
//	public GUI() {
//		super("CoolShoes");
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//		init();
//
//		this.setSize(2000, 800);
//		this.setVisible(true);
//	}
//
//	private void init() {
//		
//		JPanel mainPanel = new JPanel(new BorderLayout());
//		
//		title = new JTextField("CoolShoes");
//		showHistory = new JButton("Show History");
//		
//		title.setEditable(false);
//		title.setFont(title.getFont().deriveFont((float) 200));
//		
//		String [] numbersList = {"11", "12", "13", "14", "5"};
//
////		ComboBoxModel<String> listElements = 
//		JComboBox numbers = new JComboBox(numbersList);
//		numbers.setSelectedIndex (3);
//		numbers.setEditable (true);
//		numbers.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				
//				this. = numbersList;
//				
//			}
//		});
//		
//		
//		mainPanel.add(title, BorderLayout.NORTH);
//		mainPanel.add(numbers, BorderLayout.WEST);
//		mainPanel.add(showHistory, BorderLayout.SOUTH);
//		
//		this.getContentPane().add(mainPanel);
//	}
//	
//	public void search() {
//		
//	}
//
//}
