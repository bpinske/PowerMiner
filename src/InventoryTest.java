import static org.junit.jupiter.api.Assertions.*;

import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

class InventoryTest {

	Inventory inventory;
	String testingInventoryDirectoryPath;
	
	public void initialize() throws AWTException, IOException {
		inventory = new Inventory();
		this.testingInventoryDirectoryPath = "/home/dpapp/Desktop/RunescapeAIPics/Tests/Inventory/";
	}
	
	@Test
	public void testGetNameInItemInventorySlot() throws IOException, AWTException {
		initialize();

		String[][] expectedItemNames0 = {{"willowLogs", "oakLogs", "oakLogs", "willowLogs", "willowLogs", "willowLogs", "willowLogs"},
				{"empty", "empty", "empty", "willowLogs", "willowLogs", "willowLogs", "willowLogs"}, 
				{"empty", "willowLogs", "logs", "logs", "empty", "willowLogs", "willowLogs"},
				{"willowLogs", "willowLogs", "willowLogs", "willowLogs", "willowLogs", "willowLogs", "empty"}};
		String[][] expectedItemNames1 = {{"oakLogs", "oakLogs", "willowLogs", "willowLogs", "willowLogs", "oakLogs", "logs"},
				{"empty", "willowLogs", "empty", "willowLogs", "logs", "empty", "logs"}, 
				{"oakLogs", "willowLogs", "oakLogs", "oakLogs", "runeAxe", "willowLogs", "willowLogs"},
				{"willowLogs", "logs", "logs", "oakLogs", "willowLogs", "logs", "empty"}};
		String[][] expectedItemNames2 = {{"oakLogs", "willowLogs", "willowLogs", "willowLogs", "oakLogs", "willowLogs", "logs"},
				{"empty", "oakLogs", "empty", "logs", "willowLogs", "empty", "willowLogs"}, 
				{"logs", "empty", "oakLogs", "oakLogs", "empty", "oakLogs", "empty"},
				{"willowLogs", "empty", "logs", "willowLogs", "empty", "logs", "logs"}};
		testInventory("inventory_0.png", expectedItemNames0);
		testInventory("inventory_1.png", expectedItemNames1);
		testInventory("inventory_2.png", expectedItemNames2);
	}
	
	public BufferedImage loadBufferedImage(String fileName) throws IOException {
		File itemFile = new File(this.testingInventoryDirectoryPath + fileName);
		BufferedImage itemImage = ImageIO.read(itemFile);
		return itemImage;
	}
	
	void testInventory(String inventoryFileName, String[][] expectedItemNames) throws IOException {
		BufferedImage testImage = loadBufferedImage(inventoryFileName);
		inventory.updateWithFakeImageForTests(testImage);
		
		for (int row = 0; row < 4; row++) {
			for (int column = 0; column < 7; column++) {
				assertEquals(inventory.getItemNameInInventorySlot(row, column), expectedItemNames[row][column]);
			}
		}
	}
	
}