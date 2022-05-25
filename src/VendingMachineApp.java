import java.io.File;

public class VendingMachineApp {

	public static void main(String [] args) {
		String fileName = "input.json";
		
		if (args.length > 0) {
			
			String tempFileName = args[0];
			File f = new File(tempFileName);
			
			if (f.exists()) {
				fileName = tempFileName;
			} else {
				System.out.println("Could not find: " + fileName + "\nDid you include the full file path?");
			}
		}
		VendingController vendController = new VendingController(fileName);
	}
}
