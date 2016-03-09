package org.elsys.subs;

import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ListenerForTable {
	@SuppressWarnings("unused")
	public ListenerForTable(JTextArea subtitleArea, JTable subtitleTable, JTextField subtitleNumTextField, JFormattedTextField startTextField, JFormattedTextField endTextField, JTextField durationTextField) {
		
		try {
			DefaultTableModel model = (DefaultTableModel) subtitleTable.getModel();
			int selectedRowIndex = subtitleTable.getSelectedRow();
			
			Object[] obj = { "", "", "", "" };

			for (int i = 0; i < 4; i++) {
				obj[i] = model.getValueAt(selectedRowIndex, i);
			}

			subtitleNumTextField.setEditable(true);
			subtitleNumTextField.setText((String) obj[0]);
			subtitleNumTextField.setEditable(false);

			startTextField.setText((String) obj[1]);
			startTextField.setValue(new String((String) obj[1]));
			endTextField.setText((String) obj[2]);
			endTextField.setValue(new String((String) obj[2]));
			subtitleArea.setText((String) obj[3]);
			ListenerForStartEnd startListener = new ListenerForStartEnd(subtitleTable, startTextField, endTextField, durationTextField);
		} catch(Exception e) {
			System.out.println("Nothing");
		}
		
	}
}
