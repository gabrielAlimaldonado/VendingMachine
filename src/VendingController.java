import java.util.HashMap;
import java.util.Set;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileSystemView;

public class VendingController implements ActionListener, ItemListener {
	VendingModel model;
	VendingView view;

	ParseJSON parseJ = new ParseJSON();


	String fileName;
	String itemDesc = "";

	private JTextArea letterArea = new JTextArea(1, 1);
	private JTextArea numberArea = new JTextArea(1, 2);
	private JTextArea payArea    = new JTextArea(1, 3);

	private float fundAmt = 0;
	private float payAmount = 0;

	public VendingController(String fileName) {
		model = new VendingModel(fileName);
		view  = new VendingView(this);

		this.fileName = fileName;
		
		model.logInventoryLoading(fileName);
	}

	public HashMap<String, Item> getInventory() {
		return model.getInventory();
	}

	public Set<String> getOptions() {
		return model.getItemOptions();
	}
	
	public Item getItem(String item) {
		return model.getItem(item);
	}

	public int getMaxRowCount() {
		return model.getDefaultRows();
	}

	public int getMaxColumnCount() {
		return model.getDefaultColumns();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source instanceof JButton) {
			JButton btn = (JButton) source;
			try {
				
				if (btn == view.clear) {
					if (letterArea.isEnabled() && numberArea.isEnabled() && view.itemDescriptor.getText().length() == 0) {
						letterArea.setText("");
						numberArea.setText("");
						view.enter.setEnabled(false);
					}
					payArea.setText("");
				} else if (btn == view.loadButton) {
					
					JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
					
					int dialogue = fc.showOpenDialog(null);
					
					if (dialogue == JFileChooser.APPROVE_OPTION) {
						fileName = fc.getSelectedFile().getAbsolutePath();
						VendingModel vendModel = new VendingModel(fileName);
						
						this.model = vendModel;
						
						model.loadNewInventory(fileName);
						
						view.fileResult.setText(fileName);
						view.fileResult.setVisible(true);
						view.rebuildLettersPanel();
						view.refillOptions();
						
						resetComponents(1000);
					} else {
						view.fileResult.setText("no file selected");
						view.fileResult.setVisible(true);
						view.buildLettersPanel();
						resetComponents(1000);
					}
					
				} else if (btn == view.yes) {
					String la = letterArea.getText();
					String na = numberArea.getText();
					StringBuilder sb = new StringBuilder();
					sb.append(la);
					sb.append(na);
					numberArea.setEnabled(false);
					letterArea.setEnabled(false);
					view.enter.setEnabled(false);
					view.yes.setEnabled(false);
					view.no.setEnabled(false);
					view.period.setEnabled(true);
					
					if (model.getItemQuantity(sb.toString()) == 0) {
						view.itemDescriptor.setText("Aplogies. There are no more of: " + model.getItemName(sb.toString()) + ".");
						view.itemDescriptor.setVisible(true);					
						
						view.payAmt.setVisible(false);
						payArea.setVisible(false);
						view.pay.setEnabled(false);
						resetComponents(5000);
					} else {
						view.itemDescriptor.setText(itemDesc);
						view.itemDescriptor.setVisible(true);
						disableLetters();
						view.payAmt.setText("Pay: $");
						view.payAmt.setVisible(true);
						payArea.setVisible(true);
					}
					model.logChoice(model.getItem(sb.toString()), "Yes");
				} else if (btn != view.pay && payArea.isVisible()) {
					payArea.append(btn.getText());
					view.payAmt.setText("Pay: $");
					view.payAmt.setVisible(true);
					
					
					StringBuilder sb = new StringBuilder();
					sb.append(letterArea.getText());
					sb.append(numberArea.getText());

					String rowCol = sb.toString();

					float itemPrice = model.getItemPrice(rowCol);
					
					if (validateArea(payArea) && Float.parseFloat(payArea.getText()) >= itemPrice && fundAmt >= Float.parseFloat(payArea.getText())) {
						
						view.pay.setEnabled(true);
					} else {
						view.pay.setEnabled(false);
					}
				} else if (btn == view.pay) {
					payAmount = Float.parseFloat(payArea.getText());
					view.payAmt.setText("Pay: $" + payAmount);

					StringBuilder sb = new StringBuilder();
					sb.append(letterArea.getText());
					sb.append(numberArea.getText());

					String rowCol = sb.toString();

					float itemPrice = model.getItemPrice(rowCol);
					Item vitem 		= model.getItem(rowCol);

					if (fundAmt >= itemPrice) {
						Item item = model.purchaseItem(rowCol, payAmount);
						if (item != null) {
							fundAmt -= payAmount;
							float change = payAmount - itemPrice;
							fundAmt += change;
							DecimalFormat df = new DecimalFormat();
							df.setMaximumFractionDigits(2);
							view.fundsAmt.setText("$" + df.format(fundAmt));
							view.pay.setEnabled(false);
							letterArea.setVisible(false);
							numberArea.setVisible(false);

							view.itemDescriptor.setText("Vending " + item.getName() + "...");
							
							payArea.setVisible(false);
							view.payAmt.setVisible(false);
							resetComponents(5000);
						}
					} else {
						model.logLimitedFunds(fundAmt, vitem);
					}
					view.refillOptions();
					payArea.setVisible(false);
				} else if (btn == view.no) {
					StringBuilder sb = new StringBuilder();
					sb.append(letterArea.getText());
					sb.append(numberArea.getText());
					
					letterArea.setText("");
					numberArea.setText("");
					view.itemDescriptor.setText("");

					model.logChoice(model.getItem(sb.toString()), "No");
					
					view.yes.setEnabled(false);
					view.no.setEnabled(false);
					view.pay.setEnabled(false);
					enableLetters();
				} else if (view.numbers.contains(btn) && numberArea.isEnabled()) {
					numberArea.append(btn.getText());
					if (letterArea.getText().length() > 0 && numberArea.getText().length() > 0 && !payArea.isVisible()
							&& !view.enter.isEnabled()) {
						view.enter.setEnabled(true);
					}
				} else if (view.letters.contains(btn) && letterArea.isEnabled()) {
					letterArea.setText(btn.getText());
					if (letterArea.getText().length() > 0 && numberArea.getText().length() > 0 && !payArea.isVisible()
							&& !view.enter.isEnabled()) {
						view.enter.setEnabled(true);
					}
				} else if (btn == view.enter) {
					StringBuilder sb = new StringBuilder();
					sb.append(letterArea.getText());
					sb.append(numberArea.getText());

					
					String rowCol = sb.toString();
					Item getItem = model.getItem(rowCol);
					if (getItem == null) {
						letterArea.setText("");
						numberArea.setText("");
						view.itemDescriptor.setText("Apologies. There is no item matching what you have entered.");
						view.itemDescriptor.setVisible(true);
						enableLetters();
					} else {
						itemDesc = getItem.toString();
						view.itemDescriptor.setText(itemDesc + " Would you like to purchase?");
						view.yes.setEnabled(true);
						view.no.setEnabled(true);
						view.itemDescriptor.setVisible(true);
						view.enter.setEnabled(false);
						disableLetters();
					}
				}
			} catch (NumberFormatException ne) {
				model.logError(ne.toString());
				ne.printStackTrace();
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			Object source = e.getSource();

			if (source instanceof JComboBox) {
				JComboBox<String> cbox = (JComboBox<String>) source;

				if (cbox.getSelectedIndex() > -1) {
					DecimalFormat df = new DecimalFormat();
					df.setMaximumFractionDigits(2);
					Object bill = cbox.getSelectedItem();
					String value = bill.toString().substring(1);
					float val = Float.parseFloat(value);
					int insertedFunds = view.fundsAmt.getText().substring(1).length();
					if (insertedFunds > 0) {
						float amt = Float.parseFloat(view.fundsAmt.getText().substring(1));
						float sum = amt + val;

						view.fundsAmt.setText("$" + df.format(sum));
					} else {
						view.fundsAmt.setText("$" + df.format(val));
					}
					
					// allows for user to reselect drop down item
					fundAmt = Float.parseFloat(view.fundsAmt.getText().substring(1));
					if (!view.pay.isEnabled() && payArea.isVisible()) {
						StringBuilder currentOption = new StringBuilder();
						currentOption.append(letterArea.getText());
						currentOption.append(numberArea.getText());
						
						float itemPrice = model.getItemPrice(currentOption.toString());
						if (validateArea(payArea) && Float.parseFloat(payArea.getText()) >= itemPrice && fundAmt >= Float.parseFloat(payArea.getText())) {
							view.pay.setEnabled(true);
						}
					}
					model.logInsertedFunds(val, fundAmt);
					cbox.setSelectedIndex(-1);
				}
			}
		}
	}

	public boolean validateArea(JTextArea area) {
		if (area.getText().length() == 0) {
			return false;
		}

		char text[];
		int count = 0;
		text = area.getText().toCharArray();
		for (int i = 0; i < text.length; i++) {
			if (text[i] == '.') {
				count++;
			}
		}
		if (count > 1) {
			area.setText("");
			return false;
		}
		return true;
	}

	public void disableLetters() {
		for (int l = 0; l < view.letters.size(); l++) {

			JButton letter = view.letters.get(l);
			letter.setEnabled(false);
		}
	}
	
	public void resetComponents(int time) {

		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				letterArea.setEnabled(true);
				letterArea.setText("");
				letterArea.setVisible(true);
				
				numberArea.setEnabled(true);
				numberArea.setText("");
				numberArea.setVisible(true);
				
				enableLetters();
				view.yes.setEnabled(false);
				view.no.setEnabled(false);
				view.enter.setEnabled(false);
				view.pay.setEnabled(false);
				view.period.setEnabled(false);
				
				view.itemDescriptor.setText("");
				view.itemDescriptor.setVisible(false);
				
				view.payAmt.setText("");
				view.payAmt.setVisible(false);
				
				view.payAmt.setText("");
				view.payAmt.setVisible(false);
				payArea.setText("");
				payArea.setVisible(false);
				
				view.fileResult.setText("");
				view.fileResult.setVisible(false);
				
			}
		}, time);
	}

	public void enableLetters() {
		for (int l = 0; l < view.letters.size(); l++) {

			JButton letter = view.letters.get(l);
			letter.setEnabled(true);
		}
	}

	public JTextArea getLetterArea() {
		letterArea.setMaximumSize(new Dimension(10, 10));
		letterArea.setEditable(false);
		return letterArea;
	}

	public JTextArea getNumberArea() {
		numberArea.setMaximumSize(new Dimension(10, 10));
		numberArea.setEditable(false);
		return numberArea;
	}

	public JTextArea getPayArea() {
		payArea.setMaximumSize(new Dimension(10, 10));
		payArea.setEditable(false);
		payArea.setVisible(false);
		return payArea;
	}
}