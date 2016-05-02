package org.elsys.subs;

import javax.swing.JTable;
import javax.swing.JTextArea;

public class TextStyleListener {

	private String signStart, signEnd;
	private int startPos;
	private int endPos;
	
	
	public TextStyleListener(JTextArea subtitleArea, JTable subtitleTable, String style) {
		if(style.equals("italic")) {
			signStart = "<i>";
			signEnd = "</i>";
		} else if(style.equals("bold")) {
			signStart = "<b>";
			signEnd = "</b>";
		} else {
			signStart = "<u>";
			signEnd = "</u>";
		}
		
		if(subtitleArea.getSelectedText() != null) {
			startPos = subtitleArea.getSelectionStart();
		} else {
			startPos = 0;
		}
		
		endPos = subtitleArea.getSelectionEnd();
		
		String text = subtitleArea.getText();
		String firstPart = text.substring(0, startPos);
		String secondPart = text.substring(endPos, text.length());
		String partToChange = text.substring(startPos, endPos);
		String newPart = signStart + partToChange + signEnd;
		
		text = firstPart + newPart + secondPart;
		subtitleArea.setText(text);
		subtitleArea.requestFocus();
		
	}

}
