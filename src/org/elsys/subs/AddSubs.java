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
	public AddSubs(JFrame frame, JTextArea subtitleArea, JTable subtitleTable, JTextField subtitleNumTextField, JFormattedTextField startTextField, JFormattedTextField endTextField, JTextField durationTextField, JScrollPane scrollTablePane, JCheckBox synchCheckBox, EmbeddedMediaPlayerComponent mediaPlayerComponent, JButton backwardsButton, JButton forwardsButton, JTextField changeTextField, JCheckBox seekCheckBox, JMenuBar menuBar) {
		subtitleArea.addKeyListener(new KeyAdapter() {
			@SuppressWarnings("unused")
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (e.getModifiers() > 0) {
						subtitleArea.transferFocusBackward();
					} else {
						WriteSubsToTable writeSubsToTable = new WriteSubsToTable(subtitleArea, subtitleTable, subtitleNumTextField ,startTextField, durationTextField, endTextField, synchCheckBox, mediaPlayerComponent);
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
					
					RevalidateComponents revalidateComponents = new RevalidateComponents(frame, subtitleNumTextField, startTextField, endTextField, durationTextField, backwardsButton, forwardsButton, changeTextField, synchCheckBox, seekCheckBox, menuBar);
					
				}
			}
		});
	}

}
