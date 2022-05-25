import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Box;
import javax.swing.BoxLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

public class VendingView extends JFrame {
	
	VendingController master;
	
	public static final int WIDTH  = 625;
	public static final int HEIGHT = 575;
	
	JPanel buttonsPanel;
	JPanel filePanel;
    JPanel lettersPanel;
    JPanel areaPanel;
    JPanel optionsPanel;
    
	JFrame frame;
	
	JLabel itemDescriptor;
	JLabel payAmt;
	JLabel fundOpt;
	JLabel fundsAmt;
	JLabel fileResult;
	
	ArrayList<JButton> numbers = new ArrayList<JButton>();
	ArrayList<JButton> letters = new ArrayList<JButton>();
	
	JButton yes;
	JButton no;
	JButton enter;
	JButton pay;
	JButton clear;
	JButton period;
	
	
	JButton loadButton;
	
	JComboBox<String> addFunds;
	
	
	JTextArea options;
	
	JScrollPane optionsScroll;
	JScrollPane lettersScroll;
    
    JButton ok;
    
	public VendingView(VendingController master) {
		
		this.master = master;
		
		setTitle("Vending Machine");
		setSize(WIDTH, HEIGHT);
		BoxLayout viewLayout = new BoxLayout (getContentPane(), BoxLayout.Y_AXIS);
		setLayout(viewLayout);

		buildButtonsPanel();
		buildUserOptionsPanel();
		buildLettersPanel();
		buildOptionsPanel();
		buildAreaPanel();
		add(optionsPanel);
		add(areaPanel);
		add(lettersScroll);
		add(buttonsPanel);
		add(filePanel);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void buildOptionsPanel() {
		optionsPanel = new JPanel();
		
		options  				= new JTextArea();
		optionsScroll 			= new JScrollPane(options, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		optionsScroll.setPreferredSize(new Dimension(400, 200));
	    options.setEnabled(false);

	    refillOptions();
	    
		fundOpt  = new JLabel("Add Funds: ");
		fundsAmt = new JLabel("$");
	    addFunds = new JComboBox<String>();

	    options.setDisabledTextColor(Color.BLACK);
	    
	    
	    addFunds.addItem("$1");
	    addFunds.addItem("$5");
	    addFunds.addItemListener(master);
	    
	    
		optionsPanel.add(Box.createHorizontalGlue());
		optionsPanel.add(optionsScroll);
		optionsPanel.add(fundOpt);
		optionsPanel.add(addFunds);
		optionsPanel.add(fundsAmt);
	    addFunds.setSelectedIndex(-1);
	}
	
	public void refillOptions() {
		
		int maxRows = master.getMaxRowCount();
		int maxCols = master.getMaxColumnCount();
		
		 StringBuilder sb = new StringBuilder();
		
		for (int r = 0; r < maxRows; r++) {
			for (int c = 1; c < maxCols; c++) {
				StringBuilder option = new StringBuilder();
				char row = 'A';
				
				row += r;
				
				option.append(row);
				option.append(c);
				
				if (master.getItem(option.toString()) != null) {
					sb.append(option.toString());
					sb.append(master.getItem(option.toString()));
				} else
					break;
			}
		}
	    
	    options.setText(sb.toString());
	}
	
	public void buildAreaPanel() {
		areaPanel = new JPanel();
		
		itemDescriptor = new JLabel();
		payAmt	   	   = new JLabel("Pay: ");
		
		payAmt.setVisible(false);
		

		areaPanel.add(Box.createHorizontalGlue());
		areaPanel.add(master.getLetterArea());
		areaPanel.add(master.getNumberArea());
		areaPanel.add(itemDescriptor);
		areaPanel.add(payAmt);
		areaPanel.add(master.getPayArea());
	}
	
	public void buildLettersPanel() {
		lettersPanel  = new JPanel();
		lettersScroll = new JScrollPane(lettersPanel);
		
	    lettersPanel.add(Box.createHorizontalGlue());
	    
	    int amountOfLetters = master.getMaxRowCount();
	    
	    

	    char alph = 'A';
	    for (int l = 0; l < amountOfLetters; l++) {
	    	
	    	StringBuilder sb = new StringBuilder();
	    	sb.append(alph);
	    	
	    	String let = sb.toString();
	    	
	    	JButton letter = new JButton(let);
	    	letters.add(letter);
	    	letter.addActionListener(master);
	    	lettersPanel.add(letter);
	    	alph += 1;
	    }
	}
	
	public void rebuildLettersPanel() {
		lettersPanel.removeAll();
		
	    lettersPanel.add(Box.createHorizontalGlue());
	    
	    int amountOfLetters = master.getMaxRowCount();

	    char alph = 'A';
	    for (int l = 0; l < amountOfLetters; l++) {
	    	
	    	StringBuilder sb = new StringBuilder();
	    	sb.append(alph);
	    	
	    	String let = sb.toString();
	    	
	    	JButton letter = new JButton(let);
	    	letters.add(letter);
	    	letter.addActionListener(master);
	    	lettersPanel.add(letter);
	    	alph += 1;
	    }
	    lettersPanel.repaint();
	}
	
	public void buildButtonsPanel() {
		buttonsPanel  = new JPanel(new GridLayout(4, 3));
		for (int i = 1; i <= 10; i++) {
			StringBuilder sb = new StringBuilder();
			if (i == 10) {
				sb.append(0);
			} else {
				sb.append(i);
			}
			JButton num = new JButton(sb.toString());
			num.addActionListener(master);
			numbers.add(num);
			buttonsPanel.add(num);
		}

	    period   = new JButton(".");
	    enter    = new JButton("Enter");

	    period.setEnabled(false);
	    enter.setEnabled(false);
	    
	    period.addActionListener(master);
	    enter.addActionListener(master);
	    
	    buttonsPanel.add(period);
	    buttonsPanel.add(enter);
	}
	
	public void buildUserOptionsPanel() {
		filePanel = new JPanel();
		
	    yes        = new JButton("Yes");
	    no         = new JButton("No");
	    pay        = new JButton("Pay");
	    clear	   = new JButton("Clear Input");	
		loadButton = new JButton("Load Inventory");
		
		fileResult = new JLabel("no file selected.");
		
		fileResult.setVisible(false);
	    yes.setEnabled(false);
	    no.setEnabled(false);
	    pay.setEnabled(false);
		
		loadButton.addActionListener(master);
	    yes.addActionListener(master);
	    no.addActionListener(master);
	    pay.addActionListener(master);
	    clear.addActionListener(master);
	    
	    filePanel.add(fileResult);
		filePanel.add(loadButton);
		filePanel.add(yes);
		filePanel.add(no);
	    filePanel.add(pay);
	    filePanel.add(clear);
		
	}
}
