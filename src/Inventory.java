import java.util.HashMap;

public class Inventory {
	
	HashMap<String, Item> inv;
	char recentRow		      = '\0';
	int  recentCol            = 1;
	int  defaultRows		  = 0;
	int  defaultColumns		  = 0;
	
	public Inventory(HashMap<String, Item> inv, int defaultRows, int defaultColumns) {
		this.inv 			= inv;
		this.defaultRows 	= defaultRows;
		this.defaultColumns = defaultColumns;
	}
	
	public int inventorySize() {
		return inv.size();
	}
	
	public void setQuantity(String item) {
		int quantity = getItemQuantity(item);
		
		if (quantity == 0)
			return;
		inv.get(item).setAmount(--quantity);
	}
	
	public Item getItem(String item) {
		return inv.get(item);
	}
	
	public int getItemQuantity(String item) {
		boolean itemExists = inv.containsKey(item);
		
		if (!itemExists)
			return 0;
		return inv.get(item).getAmount();
	}
	
	public float getItemPrice(String item) {
		boolean itemExists = inv.containsKey(item);
		
		if (!itemExists)
			return 0.00f;
		return inv.get(item).getPrice();
	}
	
	public void addItem(String itemName, int amount, float itemPrice) {
		Item in = new Item(itemName, amount, itemPrice);
		
		if (recentCol == defaultColumns) {
			recentRow += 1; 
			recentCol =  1;
		} else {
			recentCol += 1;
		}
		
		String newItem   = "";
		StringBuilder sb = new StringBuilder();
		sb.append(recentRow);
		sb.append(recentCol);

		newItem = sb.toString();
		
		inv.put(newItem, in);
	}
}
