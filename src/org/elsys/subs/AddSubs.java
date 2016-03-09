package org.elsys.subs;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFormattedTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AddSubs {
	public int prevMax;
	public AddSubs(JTextArea subtitleArea, JTable subtitleTable, JTextField subtitleNumTextField, JFormattedTextField startTextField, JFormattedTextField endTextField, JTextField durationTextField, JScrollPane scrollTablePane) {
		subtitleArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (e.getModifiers() > 0) {
						subtitleArea.transferFocusBackward();
					} else {
						WriteSubsToTable writeSubsToTable = new WriteSubsToTable(subtitleArea, subtitleTable, subtitleNumTextField ,startTextField, durationTextField, endTextField);
						scrollTablePane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
							public void adjustmentValueChanged(AdjustmentEvent e) {
								if(e.getAdjustable().getMaximum() != prevMax)
									e.getAdjustable().setValue(e.getAdjustable().getMaximum());
								prevMax = e.getAdjustable().getMaximum();
							}
						});
						int i = writeSubsToTable.clearValue();
						if(i == 0)
							subtitleArea.setText(null);
					}
					e.consume();
				}
			}
		});
	}
}
