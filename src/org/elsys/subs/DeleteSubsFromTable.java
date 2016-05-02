package org.elsys.subs;

import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;


public class DeleteSubsFromTable {
	
	@SuppressWarnings("unused")
	public DeleteSubsFromTable(JTextArea subtitleArea, JTable subtitleTable, JTextField subtitleNumTextField, JFormattedTextField startTextField, JTextField durationTextField, JFormattedTextField endTextField) {
		
		DefaultTableModel model = (DefaultTableModel) subtitleTable.getModel();
		Object[] obj = {"", "", "", "", ""};
		Integer selectedRowCount = subtitleTable.getSelectedRowCount();
		int[] currentSelectedRows;
		

		if(subtitleTable.getSelectedRow() != -1) {
			currentSelectedRows = subtitleTable.getSelectedRows();
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					for (int i = 0; i < selectedRowCount; i++) {
						model.removeRow(currentSelectedRows[i]-i);
					}
					
					for (int i = 0; i < subtitleTable.getRowCount(); i++) {
						model.setValueAt(i + 1 + "\n", i, 0);
					}
					
					if(model.getRowCount() != 0) {
						if(currentSelectedRows[0] != 0) {
							subtitleTable.addRowSelectionInterval(currentSelectedRows[0] - 1, currentSelectedRows[0] - 1);
						} else {
							subtitleTable.addRowSelectionInterval(currentSelectedRows[0], currentSelectedRows[0]);
						}
					} else {
						subtitleArea.setText(null);
						startTextField.setText("00:00:00,000");
						startTextField.setValue(new String("00:00:00,000"));
						endTextField.setText("00:00:02,000");
						endTextField.setValue(new String("00:00:02,000"));
						subtitleNumTextField.setEditable(true);
						subtitleNumTextField.setText("1");
						subtitleNumTextField.setEditable(false);
						durationTextField.setText("2");
					}
					//subtitleArea.grabFocus();
				}
			});
		}
	}

}
