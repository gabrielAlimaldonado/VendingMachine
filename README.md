# VendingMachine
-------------------
Vending Machine Software. The program simulates a vending machine that receives a JSON file of items that fill a vending machine.


PROJECT DESCRIPTION
-------------------

	The application takes various input from the user similar to what a vending machine would take. For example, taking input for item selection, for fund insertion, for item selection confirmation and payment, etc. The input is taken with a GUI that simulates a vending machine.

	The application logs the user's actions. These logged actions encompasses logging items loaded from file, item selection, attempted purchase of an item, items loaded from another file, etc.


INSTALLATION
-------------------
	Inside Vending Machine directory:
	
	mvn package



CHALLENGE DETAILS
-------------------

	The approach taken with the vending machine included the design pattern of the Model, View, and Controller. The Controller handles the input from the user and updates the View accordingly, after user actions. The Model does the thinking of the vending machine and sending back data requested from the Controller. The thinking included the setting up of necessary pieces and pulling together of inventory data, of item data, of file data, etc. The View handles the display of the components that the user interacts with.

USAGE
-------------------
	If running from CLI, one could provide the absolute path of the JSON file containing an inventory. If no path of the file is provided, a default file will be used instead. If one decides to load a file from the file explorer, click on the "Load Inventory" button to pick a JSON file to load a new inventory to the vending machine. Loading an inventory can be done at any time.

	After initialization of the program or loading of an inventory, a list of items in a text area will appear with a dropdown with a descriptor of adding funds to the vending machine. Adding funds can be done at any moment.

	Two blank text areas below funds and items listing gets filled from the clicking of buttons with letter and numbers. The left text area takes a letter and the right text area takes a number. If an unintended button press has been made, pressing the "Clear Input" button will clear both letter and number text areas, including the text area that takes the amount paid to the machine.

	The "Enter" button will be enabled once both text areas containing the letter (row) and number (column) are filled. If a user selects an item not in the inventory, they will be notified of the absence of the item, otherwise an item description appears with a question asking whether they may proceed (clicking the "Yes" button) to either continue with the purchase of the item, or the cancellation (clicking the "No" button) of proceeding towards item purchase.

	If at the step of paying for an item, a user would have to have the necessary funds to complete the item purchase. Required funds to pay for the item is necessary to complete purchase so that the "Pay" button can be enabled. Appropriately formatted payment must be provided also for purchase to be available for the user. Once an item is purchased, the quantity of the item will decrease by 1 and items with quantity of zero will not be purchasable.
	
