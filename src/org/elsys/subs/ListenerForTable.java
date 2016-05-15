package org.elsys.subs;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

public class ListenerForTable {
	
	//private String temp;
	
	@SuppressWarnings("unused")
	public ListenerForTable(JTextArea subtitleArea, JTable subtitleTable, JTextField subtitleNumTextField, JFormattedTextField startTextField, JFormattedTextField endTextField, JTextField durationTextField, boolean videoOpened, EmbeddedMediaPlayerComponent mediaPlayerComponent, JCheckBox seekCheckBox, int lastSelectedRow) {
		
		try {
			DefaultTableModel model = (DefaultTableModel) subtitleTable.getModel();
			int selectedRowIndex = subtitleTable.getSelectedRow();
			Object[] obj = { "", "", "", "" };
			
			UndoListener.reset = true;
			//String lastText = subtitleArea.getText();
			
/*			if(lastSelectedRow != -1) {
				if(!lastText.equals(subtitleTable.getValueAt(lastSelectedRow, 3).toString())) {
					temp = "chg|";
					
					temp = temp + lastSelectedRow + "|" + subtitleTable.getValueAt(lastSelectedRow, 1) + "|" + 
					subtitleTable.getValueAt(lastSelectedRow, 2) + "|";
					String temp3 = subtitleTable.getValueAt(lastSelectedRow, 3).toString();
					temp3 = temp3.replaceAll("\n", "");
					temp = temp + temp3 + "\n";
				}
			}*/
			
			if(selectedRowIndex != -1) {
				
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
				ListenerForStartEnd startListener = new ListenerForStartEnd(subtitleTable, startTextField, endTextField, durationTextField, selectedRowIndex);
				
				if(seekCheckBox.isSelected() && videoOpened == true) {
					setVideoTime(startTextField, mediaPlayerComponent, true);
				}
				
				MouseListener listeners [] = subtitleTable.getMouseListeners();
				if(listeners.length == 4)
					subtitleTable.removeMouseListener(listeners[3]);
				
				subtitleTable.addMouseListener(new MouseAdapter() {
				    public void mouseClicked(MouseEvent event) {
				    	if(SwingUtilities.isLeftMouseButton(event)) {
				    		if (event.getClickCount() == 2 && videoOpened == true && !seekCheckBox.isSelected()) {
				    			setVideoTime(startTextField, mediaPlayerComponent, false);
				    		}
				    	}
				    }
				});
			
/*			if(lastSelectedRow != -1 && !DeleteSubsFromTable.isForOn) {
				if(!lastText.equals(subtitleTable.getValueAt(lastSelectedRow, 3).toString())) {
					model.setValueAt(lastText, lastSelectedRow, 3);
					
					temp = temp + lastSelectedRow + "|" + subtitleTable.getValueAt(lastSelectedRow, 1) + "|" + 
					subtitleTable.getValueAt(lastSelectedRow, 2) + "|";
					String temp3 = subtitleTable.getValueAt(lastSelectedRow, 3).toString();
					temp3 = temp3.replaceAll("\n", "");
					temp = temp + temp3 + "\n";
					
					UndoListener.undoStack.push(temp);
				}
			}*/
			
		}
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Nothing!");
		}
		
		
	}
	
	public void setVideoTime(JFormattedTextField startTextField, EmbeddedMediaPlayerComponent mediaPlayerComponent, boolean pause) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				double time = 0.0;
				String numsStart[] = null;
				String startText = startTextField.getText();
				
				numsStart = startText.split(":");
				numsStart[2] = numsStart[2].replaceAll(",", ".");
		
				double temp = 0;
				
				for(int i = 0; i < 3; i++) {
					if (i == 0) 
						temp = Double.parseDouble(numsStart[i]) * 3600;
					if (i == 1) 
						temp = Double.parseDouble(numsStart[i]) * 60;
					if (i == 2) 
						temp = Double.parseDouble(numsStart[i]);
					
					time += temp;
				}
				
				time = time * 1000;

				mediaPlayerComponent.getMediaPlayer().setTime((long)time);
				
				if(mediaPlayerComponent.getMediaPlayer().isPlaying() && pause == true)
					mediaPlayerComponent.getMediaPlayer().pause();
			}
		});
	}
}
