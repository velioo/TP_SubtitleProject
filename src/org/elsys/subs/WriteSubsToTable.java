package org.elsys.subs;

import java.awt.EventQueue;
import java.text.DecimalFormat;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

public class WriteSubsToTable {
	private int clear = 0;
	@SuppressWarnings("unused")
	public WriteSubsToTable(JTextArea subtitleArea, JTable subtitleTable, JTextField subtitleNumTextField, JFormattedTextField startTextField, JTextField durationTextField, JFormattedTextField endTextField, JCheckBox synchCheckBox, EmbeddedMediaPlayerComponent mediaPlayerComponent) {
		clear = 0;
		DefaultTableModel model = (DefaultTableModel) subtitleTable.getModel();
		Object[] obj = {"", "", "", ""};
		Integer rowCount = subtitleTable.getRowCount();
		int currentSelectedRow = subtitleTable.getSelectedRow();

		if(subtitleTable.getSelectedRow() == -1) {
			
			if(synchCheckBox.isSelected() && mediaPlayerComponent.isValid()) {
				
				long miliSeconds = mediaPlayerComponent.getMediaPlayer().getTime();
				
				Double time = (double) miliSeconds;
				time = time / 1000;
				
				String numsStart[] = new String[3];
				
				Integer hours = 0;
				Integer minutes = 0;
				Integer seconds = 0;

				while (time >= 3600) {
					hours++;
					time -= 3600;
				}
				while (time >= 60) {
					minutes++;
					time -= 60;
				}
				while (time >= 1) {
					seconds++;
					time--;
				}

				if (hours < 10)
					numsStart[0] = "0" + hours.toString();
				else
					numsStart[0] = hours.toString();

				if (minutes < 10)
					numsStart[1] = "0" + minutes.toString();
				else
					numsStart[1] = minutes.toString();

				if (seconds < 10)
					numsStart[2] = "0" + seconds.toString();
				else
					numsStart[2] = seconds.toString();

				DecimalFormat df = new DecimalFormat("#.###");
				String Decimals;
				Decimals = df.format(time).replaceAll(",", ".");
				time = Double.parseDouble(Decimals);

				if (time == 0.0)
					Decimals = "," + df.format(time) + "00";
				else
					Decimals = df.format(time).replaceFirst("0", "");
				
				if (Decimals.length() == 2)
					Decimals = Decimals + "00";
				if (Decimals.length() == 3)
					Decimals = Decimals + "0";

				String finalStart = numsStart[0] + ':' + numsStart[1] + ':' + numsStart[2] + Decimals;
				
				startTextField.setText(finalStart);
				startTextField.setValue(finalStart);
				
				ListenerForDuration durationListener = new ListenerForDuration(subtitleTable, startTextField, endTextField, durationTextField, currentSelectedRow);
				
				obj[1] = finalStart;
				
				
			} else {
				obj[1] = startTextField.getText();
			}
			
			obj[0] = rowCount + 1 + "\n";
			obj[2] = endTextField.getText();
			obj[3] = subtitleArea.getText() + "\n";
			
			model.addRow(obj);
			
			rewriteTextFields(rowCount, subtitleTable, startTextField, endTextField, durationTextField, subtitleNumTextField, currentSelectedRow);
			
		} else {
			clear = 1;
			int columnNum;
			columnNum = subtitleTable.getSelectedRow();
			Integer getRow = columnNum + 1;
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
				rewriteTextFields(rowCount, subtitleTable, startTextField, endTextField, durationTextField, subtitleNumTextField, currentSelectedRow);
				clear = 0;
			}
		}
		
		
		if(mediaPlayerComponent.isValid()) {
			TempFile tempFile = new TempFile(subtitleTable);
			mediaPlayerComponent.getMediaPlayer().setSubTitleFile("temp.srt");
		}
		
	}
	
	public int clearValue() {
		return clear;
	}
	
	
	@SuppressWarnings("unused")
	public void rewriteTextFields(Integer rowCount, JTable subtitleTable, JFormattedTextField startTextField, JFormattedTextField endTextField, JTextField durationTextField, JTextField subtitleNumTextField, int currentSelectedRow) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Integer rowCountTemp = subtitleTable.getRowCount();
				Object obj[] = new Object[1];
				DefaultTableModel model = (DefaultTableModel) subtitleTable.getModel();
				obj[0] = model.getValueAt(rowCountTemp - 1, 2);
				startTextField.setText((String) obj[0]);
				startTextField.setValue(new String((String) obj[0]));

				subtitleNumTextField.setEditable(true);
				subtitleNumTextField.setText(rowCountTemp.toString());
				subtitleNumTextField.setEditable(false);
				
				ListenerForDuration durationListener = new ListenerForDuration(subtitleTable, startTextField, endTextField, durationTextField, currentSelectedRow);
			}
		});

	}
}
