package org.elsys.subs;

import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class WriteSubsToTable {
	private int clear = 0;
	@SuppressWarnings("unused")
	public WriteSubsToTable(JTextArea subtitleArea, JTable subtitleTable, JTextField subtitleNumTextField, JFormattedTextField startTextField, JTextField durationTextField, JFormattedTextField endTextField) {
		clear = 0;
		DefaultTableModel model = (DefaultTableModel) subtitleTable.getModel();
		Object[] obj = {"", "", "", ""};
		Integer rowCount = subtitleTable.getRowCount();

		if(subtitleTable.getSelectedRow() == -1) {
			obj[0] = rowCount + 1 + "\n";
			obj[1] = startTextField.getText();
			obj[2] = endTextField.getText();
			obj[3] = subtitleArea.getText() + "\n";
			model.addRow(obj);
			
			rowCount = subtitleTable.getRowCount();
			obj[0] = model.getValueAt(rowCount - 1, 2);
			startTextField.setText((String) obj[0]);
			startTextField.setValue(new String((String) obj[0]));
	
			rowCount++;
			subtitleNumTextField.setEditable(true);
			subtitleNumTextField.setText(rowCount.toString());
			subtitleNumTextField.setEditable(false);
			ListenerForDuration durationListener = new ListenerForDuration(subtitleTable, startTextField, endTextField, durationTextField);
			
		} else {
			clear = 1;
			int columnNum;
			columnNum = subtitleTable.getSelectedRow();
			Integer getRow = subtitleTable.getSelectedRow() + 1;
			obj[0] = getRow.toString() + "\n";
			obj[1] = startTextField.getText();
			obj[2] = endTextField.getText();
			obj[3] = subtitleArea.getText() + "\n";
			
			for(int i = 0; i < 4; i++) 
				model.setValueAt(obj[i], columnNum, i);
			
			Integer nextRow = columnNum + 1;
			if(nextRow < subtitleTable.getRowCount())
				subtitleTable.setRowSelectionInterval(nextRow, nextRow);
			else {
				subtitleTable.clearSelection();
				clear = 0;
			}
		}	
	}
	
	public int clearValue() {
		return clear;
	}
}
