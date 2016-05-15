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
	private String temp;
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
			if(!subtitleArea.getText().toString().endsWith(" "))
				obj[3] = subtitleArea.getText() + "\n";
			else 
				obj[3] = subtitleArea.getText();
			
			if(obj[3].equals("") || obj[3] == null || obj[3].equals("\n")) 
				obj[3] = "\u200B";
			
			model.addRow(obj);
			
			String temp = "ins|";
			
			temp = temp + rowCount + "|" + subtitleTable.getValueAt(rowCount, 1) + "|" + 
			subtitleTable.getValueAt(rowCount, 2) + "|";
			String temp3 = subtitleTable.getValueAt(rowCount, 3).toString();
			temp3 = temp3.replaceAll("\n", "");
			temp = temp + temp3 + "\n";
			
			UndoListener.undoStack.push(temp);
			
			rewriteTextFields(rowCount, subtitleTable, startTextField, endTextField, durationTextField, subtitleNumTextField, currentSelectedRow);
			
		} else {
			String temp3 = "";
			if(currentSelectedRow != -1) {
				temp = "chg|";
				
				temp = temp + currentSelectedRow + "|" + subtitleTable.getValueAt(currentSelectedRow, 1) + "|" + 
				subtitleTable.getValueAt(currentSelectedRow, 2) + "|";
				temp3 = ListenerForTable.lastText;
				temp3 = temp3.replaceAll("\n", "");
				temp = temp + temp3 + "\n";
				
				System.out.println(temp3);
				
			}
			
			System.out.println("Temp3 = " + temp3);
			
			clear = 1;
			int columnNum;
			columnNum = subtitleTable.getSelectedRow();
			Integer getRow = columnNum + 1;
			obj[0] = getRow.toString() + "\n";
			obj[1] = startTextField.getText();
			obj[2] = endTextField.getText();
			if(!subtitleArea.getText().toString().endsWith(" "))
				obj[3] = subtitleArea.getText() + "\n";
			else 
				obj[3] = subtitleArea.getText();
			
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
			
			if(currentSelectedRow != -1) {
				
				temp = temp + currentSelectedRow + "|" + subtitleTable.getValueAt(currentSelectedRow, 1) + "|" + 
				subtitleTable.getValueAt(currentSelectedRow, 2) + "|";
				String temp4 = subtitleTable.getValueAt(currentSelectedRow, 3).toString();
				temp4 = temp4.replaceAll("\n", "");
				temp = temp + temp4 + "\n";
				
				System.out.println("Temp4 = " + temp4);
				
				if(!temp3.equals(temp4)) {
					System.out.println("It's not equal");
					UndoListener.undoStack.push(temp);
				}
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
