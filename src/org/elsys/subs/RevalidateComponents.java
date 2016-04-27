package org.elsys.subs;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class RevalidateComponents {

	public RevalidateComponents(JFrame frame, JTextField subtitleNumTextField, JFormattedTextField startTextField, JFormattedTextField endTextField, JTextField durationTextField, JButton backwardsButton, JButton forwardsButton, JTextField changeTextField, JCheckBox synchCheckBox, JCheckBox seekCheckBox, JMenuBar menuBar) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				seekCheckBox.revalidate();
				subtitleNumTextField.revalidate();
				startTextField.revalidate();
				endTextField.revalidate();
				durationTextField.revalidate();
				backwardsButton.revalidate();
				forwardsButton.revalidate();
				changeTextField.revalidate();
				synchCheckBox.revalidate();
				seekCheckBox.revalidate();
				menuBar.revalidate();
				frame.setVisible(true);
			}
		});
	}

}
