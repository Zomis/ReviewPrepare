package net.zomis.reviewprepare.util;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

/**
 * JavaFX clipboard utility
 *
 * @author Bhathiya
 */
public class FxClipboard {

	

	/**
	 * Copy plain text to clipboard 
	 * @param plainText plain text to be copied
	 */
	public static void copyToClipboard(String plainText) {
		//http://docs.oracle.com/javase/8/javafx/api/javafx/scene/input/Clipboard.html
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();
		content.putString(plainText);
		clipboard.setContent(content);
	}
}
