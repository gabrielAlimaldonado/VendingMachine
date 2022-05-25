
public class Item {
	
	public String name;
	public int	  amount;	
	public float  price;
	
	
	public Item(String name, int amount, float price) {
		this.name 	= name;
		this.amount = amount;
		this.price  = price;
	}

	
	public int getAmount() {
		return this.amount;
	}
	
	public float getPrice() {
		return this.price;
	}
	
	public String getName() {
		return this.name;
	}
	
	
	public void setPrice(float price) {
		this.price = price;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return String.format(" Item name: %s | Quantity: %d | Price: $%.2f.\n", this.name, this.amount, this.price);
	}
}
