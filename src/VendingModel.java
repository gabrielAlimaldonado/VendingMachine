import java.util.HashMap;
import java.util.Set;

public class VendingModel {
	private HashMap <String, Item> inventory;
	
	private int defaultRows;
	private int defaultColumns;
	
	
	private ParseJSON pjson;
	private Inventory inv;
	
	private VendingLogger vl;
	
	public VendingModel(String fileName) {
		inventory = new HashMap<String, Item>();
		
		pjson          = new ParseJSON();
		inventory      = pjson.parseFile(fileName);
		defaultRows    = pjson.getDefaultRows();
		defaultColumns = pjson.getDefaultColumns();
		
		inv = new Inventory(inventory, defaultRows, defaultColumns);
		
		vl  = new VendingLogger();
	}
	
	public HashMap<String, Item> getInventory() {
		return inventory;
	}
	
	public void loadNewInventory(String file) {
		ParseJSON pjson = new ParseJSON();
		this.pjson = pjson;
		inventory = pjson.parseFile(file);
		inv.inv   = inventory;
		vl.logInventoryLoading(inventory.toString(), file);
	}
	
	public Set<String> getItemOptions() {
		return inventory.keySet();
	}
	
	public Item getItem(String item) {
		return inventory.get(item);
	}
	
	public float getItemPrice(String item) {
		return inv.getItemPrice(item);
	}
	
	public String getItemName(String item) {
		return inventory.get(item).getName();
	}
	
	public int getItemQuantity(String item) {
		return inv.getItemQuantity(item);
	}
	
	public int getDefaultRows() {
		return this.defaultRows;
	}
	
	public int getDefaultColumns() {
		return this.defaultColumns;
	}
	
	public Item purchaseItem(String item, float funds) {
		int itemQuantity = inv.getItemQuantity(item);
		if (itemQuantity > 0) {
			Item i = inv.getItem(item);
			inv.setQuantity(item);
			
			vl.logTransaction(i);
			return i;
		}
		return null;
	}
	
	public void logInventoryLoading(String inv) {
		vl.logInventoryLoading(inventory.toString(), inv);
	}
	
	public void logUserEntry(String item) {
		vl.userEntered(item);
	}
	
	public void logError(String trace) {
		vl.logError(trace);
	}
	
	public void logChoice(Item item, String choice) {
		vl.logChoice(item, choice);
	}
	
	public void logInsertedFunds(float funds, float total) {
		vl.logInsertedFunds(funds, total);
	}
	
	public void logLimitedFunds(float funds, Item item) {
		vl.logLimitedFunds(funds, item);
	}
}
