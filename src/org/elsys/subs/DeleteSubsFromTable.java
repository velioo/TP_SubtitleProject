package org.elsys.subs;

import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;


public class DeleteSubsFromTable {
	
	@SuppressWarnings("unused")
	public DeleteSubsFromTable(JTextArea subtitleArea, JTable subtitleTable, JTextField subtitleNumTextField, JFormattedTextField startTextField, JTextField durationTextField, JFormattedTextField endTextField, EmbeddedMediaPlayerComponent mediaPlayerComponent) {
		
		DefaultTableModel model = (DefaultTableModel) subtitleTable.getModel();
		Object[] obj = {"", "", "", "", ""};
		Integer rowCount = subtitleTable.getSelectedRowCount();
		int[] currentSelectedRows;
		

		if(subtitleTable.getSelectedRow() != -1) {
			currentSelectedRows = subtitleTable.getSelectedRows();
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					for (int i = 0; i < rowCount; i++) {
						model.removeRow(currentSelectedRows[i]-i);
					}
					
					for (int i = 0; i < subtitleTable.getRowCount(); i++) {
						model.setValueAt(i + 1 + "\n", i, 0);
					}
					
					if(subtitleTable.getRowCount() != 0) {
						if(currentSelectedRows[0] < subtitleTable.getRowCount()) {
							subtitleTable.addRowSelectionInterval(currentSelectedRows[0], currentSelectedRows[0]);
						}
						else {
							subtitleTable.addRowSelectionInterval(currentSelectedRows[0] - 1, currentSelectedRows[0] - 1);
						}
					}
					//subtitleArea.grabFocus();
					if(mediaPlayerComponent.isValid()) {
						System.out.println("hello from delete side");
						TempFile tempFile = new TempFile(subtitleTable);
						mediaPlayerComponent.getMediaPlayer().setSubTitleFile("temp.srt");
					}
				}
			});
		}
	}

}
