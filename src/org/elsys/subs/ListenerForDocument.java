package org.elsys.subs;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ListenerForDocument implements DocumentListener {

	private JTable subtitleTable;
	private JTextArea subtitleArea;

	public ListenerForDocument(JTable subtitleTable, JTextArea subtitleArea) {
		this.subtitleTable = subtitleTable;
		this.subtitleArea = subtitleArea;
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) {
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		int currentSelectedRow = subtitleTable.getSelectedRow();
		if(currentSelectedRow != -1) {
			subtitleTable.setValueAt(subtitleArea.getText(), currentSelectedRow, 3);
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		int currentSelectedRow = subtitleTable.getSelectedRow();
		if(currentSelectedRow != -1) {
			subtitleTable.setValueAt(subtitleArea.getText(), currentSelectedRow, 3);
		}
	}

}
