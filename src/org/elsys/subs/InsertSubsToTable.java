package org.elsys.subs;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;


public class InsertSubsToTable {
	
	private JTextArea subtitleArea;
	private JTable subtitleTable;
	//private JTextField subtitleNumTextField;
	//private JFormattedTextField startTextField;
	//private JTextField durationTextField;
	//private JFormattedTextField endTextField;
	// , JTextField subtitleNumTextField, JFormattedTextField startTextField, JTextField durationTextField, JFormattedTextField endTextField
	public InsertSubsToTable(JTextArea subtitleArea, JTable subtitleTable) {
		this.subtitleArea = subtitleArea;
		this.subtitleTable = subtitleTable;
		//this.subtitleNumTextField = subtitleNumTextField;
		//this.startTextField = startTextField;
		//this.durationTextField = durationTextField;
		//this.endTextField = endTextField;
	}
	
	@SuppressWarnings("unused")
	protected void insert(){
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
					
					DeleteSubsFromTable.isForOn = true;
					
					if (prevRow < 0) {
						obj[1] = model.getValueAt(currentSelectedRow, 2);
					} else {
						obj[1] = model.getValueAt(prevRow, 2);
					}
					obj[2] = model.getValueAt(currentSelectedRow, 1);
					
					obj[3] = " ";
					
					model.insertRow(currentSelectedRow, obj);
					
					String temp = "ins|";
					
					temp = temp + currentSelectedRow + "|" + subtitleTable.getValueAt(currentSelectedRow, 1) + "|" + 
					subtitleTable.getValueAt(currentSelectedRow, 2) + "|";
					String temp3 = subtitleTable.getValueAt(currentSelectedRow, 3).toString();
					temp3 = temp3.replaceAll("\n", "");
					temp = temp + temp3 + "\n";
					
					UndoListener.undoStack.push(temp);
					
					DeleteSubsFromTable.isForOn = false;
					
					for (int i = 0; i < subtitleTable.getRowCount(); i++) {
						model.setValueAt(i + 1 + "\n", i, 0);
					}
					
					subtitleTable.removeRowSelectionInterval(0, subtitleTable.getRowCount() - 1);
					subtitleTable.addRowSelectionInterval(currentSelectedRow, currentSelectedRow);
					subtitleTable.setValueAt(" ", currentSelectedRow, 3);
					subtitleArea.setText("");
				}
			});
		}
	}
	
	protected void insert(boolean isUndoable, String[] splitedArgs) {
		DefaultTableModel model = (DefaultTableModel) subtitleTable.getModel();
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				splitedArgs[3] = splitedArgs[3] + "\n";
				model.insertRow(Integer.parseInt(splitedArgs[0]), splitedArgs);
				
				for (int i = 0; i < subtitleTable.getRowCount(); i++) {
					model.setValueAt(i + 1 + "\n", i, 0);
				}
				
				subtitleTable.removeRowSelectionInterval(Integer.parseInt(splitedArgs[0]), Integer.parseInt(splitedArgs[0]));
				subtitleTable.addRowSelectionInterval(Integer.parseInt(splitedArgs[0]), Integer.parseInt(splitedArgs[0]));
				subtitleTable.removeRowSelectionInterval(0, subtitleTable.getRowCount() - 1);
				subtitleTable.addRowSelectionInterval(Integer.parseInt(splitedArgs[0]), Integer.parseInt(splitedArgs[0]));
			}
		});
		
	}

}
