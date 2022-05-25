import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class VendingLogger {
    private final Logger logger = Logger.getLogger(VendingLogger.class.getName());
	
    private FileHandler fh;
	
	public VendingLogger() {
		
		SimpleFormatter format = new SimpleFormatter();
		
		try {
			fh = new FileHandler("./transactions.log", true);
			
			fh.setLevel(Level.INFO);
			fh.setFormatter(format);
			logger.addHandler(fh);
			
			
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error occur in FileHandler.", e);
		}
	}
	
	public void logInvalidRowsCount(int count, int defaultCount) {
		logger.warning("Invalid row count detected: " + count + "\nDefaulting to: " + defaultCount);
	}
	
	public void logInvalidColsCount(int count, int defaultCount) {
		logger.warning("Invalid column count detected: " + count + "\nDefaulting to: " + defaultCount);
	}
	
	public void logInventoryLoading(String inv, String file) {
		logger.info("The inventory loaded from file: " + inv + "\nFile name: " + file);
	}
	
	public void userEntered(String item) {
		logger.info("User entered: " + item);
	}
	
	public void logChoice(Item item, String choice) {
		if (choice.equals("Yes"))
			logger.info("User chose to purchase: " + item.getName());
		else
			logger.info("User chose to not purchase: " + item.getName());
	}
	
	public void logTransaction(Item item) {
		logger.info("User has purchased: " + item.toString());
	}
	
	public void logOutOfStock(Item item) {
		logger.info("The item: " + item.getName() + " is out of stock.");
	}
	
	public void logInsertedFunds(float funds, float total) {
		logger.info("User has inserted: $" + funds + " of total: $" + total);
	}
	
	public void logLimitedFunds(float funds, Item item) {
		logger.info("User has attempted to purchase " + item.getName() + " with $" + funds +" < " + item.getPrice());
	}
	
	public void logError(String trace) {
		logger.severe(trace);
	}
}
