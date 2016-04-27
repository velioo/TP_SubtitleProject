package org.elsys.subs;

import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;


public class InsertSubsToTable {
	
	@SuppressWarnings("unused")
	public InsertSubsToTable(JTextArea subtitleArea, JTable subtitleTable, JTextField subtitleNumTextField, JFormattedTextField startTextField, JTextField durationTextField, JFormattedTextField endTextField) {
		
		DefaultTableModel model = (DefaultTableModel) subtitleTable.getModel();
		Object[] obj = {"", "", "", ""};
		Integer rowCount = subtitleTable.getSelectedRowCount();
		int currentSelectedRow;
		int prevRow;
		
		if(subtitleTable.getSelectedRow() != -1) {
			currentSelectedRow = subtitleTable.getSelectedRow();
			prevRow = currentSelectedRow - 1;
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					if (prevRow < 0) {
						obj[1] = model.getValueAt(currentSelectedRow, 2);
					} else {
						obj[1] = model.getValueAt(prevRow, 2);
					}
					obj[2] = model.getValueAt(currentSelectedRow, 1);
					
					model.insertRow(currentSelectedRow, obj);
					
					for (int i = 0; i < subtitleTable.getRowCount(); i++) {
						model.setValueAt(i + 1 + "\n", i, 0);
					}
					
					subtitleTable.removeRowSelectionInterval(0, subtitleTable.getRowCount() - 1);
					subtitleTable.addRowSelectionInterval(currentSelectedRow, currentSelectedRow);
					
					//subtitleArea.grabFocus();
				}
			});
		}
	}

}
