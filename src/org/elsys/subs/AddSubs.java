package org.elsys.subs;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

public class AddSubs {
	public int prevMax;
	private boolean shift = false, enter = false;
	public AddSubs(JFrame frame, JTextArea subtitleArea, JTable subtitleTable, JTextField subtitleNumTextField, JFormattedTextField startTextField, JFormattedTextField endTextField, JTextField durationTextField, JScrollPane scrollTablePane, JCheckBox synchCheckBox, EmbeddedMediaPlayerComponent mediaPlayerComponent, JButton backwardsButton, JButton forwardsButton, JTextField changeTextField, JCheckBox seekCheckBox, JMenuBar menuBar) {
		subtitleArea.addKeyListener(new KeyAdapter() {
			@SuppressWarnings("unused")
			@Override
			public void keyPressed(KeyEvent e) {
				
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					enter = true;
				}
				
				if(shift == true && enter == true) {
					subtitleArea.append("\n ");
					subtitleArea.grabFocus();
				}
				
				if (enter == true && shift == false) {
					if (e.getModifiers() > 0) {
					} else {
						WriteSubsToTable writeSubsToTable = new WriteSubsToTable(subtitleArea, subtitleTable, subtitleNumTextField ,startTextField, durationTextField, endTextField, synchCheckBox, mediaPlayerComponent);
						scrollTablePane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
							public void adjustmentValueChanged(AdjustmentEvent e) {
								if((e.getAdjustable().getMaximum() > prevMax) && (subtitleTable.getSelectedRow() == -1))
									e.getAdjustable().setValue(e.getAdjustable().getMaximum());
								prevMax = e.getAdjustable().getMaximum();
							}
						});
						int i = writeSubsToTable.clearValue();
						if(i == 0)
							subtitleArea.setText("");
					}
					e.consume();
				}	
					RevalidateComponents revalidateComponents = new RevalidateComponents(frame, subtitleNumTextField, startTextField, endTextField, durationTextField, backwardsButton, forwardsButton, changeTextField, synchCheckBox, seekCheckBox, menuBar);
					
				if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
					shift = true;
				}
				if(e.getKeyCode() == KeyEvent.VK_SPACE) {
					//System.out.println("RESET FALSE");
					UndoListener.reset = false;
					String temp = "area|";
					String[] splitedArea = subtitleArea.getText().split(" ");
					for(String s : splitedArea) {
						temp += s + "|";
					}
					temp = temp.substring(0, temp.length() - 1);
					
					UndoListener.undoStack.push(temp);
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(10 == e.getKeyCode()) {
					enter = false;
				}
				if(16 == e.getKeyCode()) {
					shift = false;
				}
			}
		});
	}

}
