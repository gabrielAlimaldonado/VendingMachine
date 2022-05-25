import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class ParseJSON {
	char recentRow		      = '\0';
	int  recentCol            = 1;
	int  defaultRows		  = 0;
	int  defaultColumns		  = 0;
	VendingLogger vl  = new VendingLogger();
	
	public HashMap<String, Item> parseFile(String filename) {
		HashMap<String, Item> inv = new HashMap<>();
		JSONParser jsonP = new JSONParser();
		try {
			// tries to get input from the folder of the project
			// but if the user has typed a path outside of the project
			// the file is not available as a resource
			InputStream file = getClass().getResourceAsStream(filename);
			
			if (file == null) {
				// look for a file outside of the project folder
				file = new FileInputStream(filename);
			}
			InputStreamReader streamReader = new InputStreamReader(file);
			BufferedReader 	  reader	   = new BufferedReader(streamReader);
			JSONObject 		  jsonO        = (JSONObject) jsonP.parse(reader);
			
			JSONArray  items  = (JSONArray)  jsonO.get("items");
			JSONObject config = (JSONObject) jsonO.get("config");
			
			String rowsLimit    = config.get("rows").toString();
			defaultRows     	= tryParseNum(rowsLimit, 4);
			if (defaultRows > 26) {
				vl.logInvalidRowsCount(defaultRows, 26);
				defaultRows = 26;
			} else if (defaultRows < 1) {
				vl.logInvalidRowsCount(defaultRows, 1);
				defaultRows = 1;
			}
			String columnsLimit = config.get("columns").toString();
			defaultColumns  	= tryParseNum(columnsLimit, 8);

			if (defaultColumns < 1) {
				vl.logInvalidColsCount(defaultColumns, 1);
				defaultColumns = 1;
			}
			char abcd       = 'A';
			int  nextLetter = 1;
			int  colCount   = 1;
			for (Object item : items) {
				
				if (colCount == defaultColumns+1 && abcd <= abcd+defaultRows) {
					colCount  = 1;
					abcd	 += nextLetter;
				}
				
				JSONObject nestedItem = (JSONObject) item;
				String itemName = nestedItem.get("name").toString();
				int    amount   = tryParseNum(nestedItem.get("amount").toString(), 10);
				float  priceNo$ = tryParseNum(nestedItem.get("price").toString().substring(1), 0.00f);

				StringBuilder sb = new StringBuilder();
				sb.append(abcd);
				sb.append(colCount);
				String rc  = sb.toString();
				recentRow  = abcd;
				recentCol  = colCount; 
				inv.put(rc, new Item(itemName, amount, priceNo$));

				colCount++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return inv;
	}
	
	private static int tryParseNum(String value, int defaultVal) {
	    try {
	        return Integer.parseInt(value);
	    } catch (NumberFormatException e) {
	        return defaultVal;
	    }
	}
	
	private static float tryParseNum(String value, float defaultVal) {
	    try {
	        return Float.parseFloat(value);
	    } catch (NumberFormatException e) {
	        return defaultVal;
	    }
	}
	
	public int getDefaultRows() {
		return this.defaultRows;
	}
	
	public int getDefaultColumns() {
		return this.defaultColumns;
	}
}
