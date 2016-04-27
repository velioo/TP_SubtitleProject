package org.elsys.subs;

import java.awt.Cursor;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ButtonsProperties {
	public ButtonsProperties(JButton backwardsButton, JButton forwardsButton) {
		backwardsButton.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		Image img = new ImageIcon(this.getClass().getResource("/backwards.png")).getImage();
		backwardsButton.setIcon(new ImageIcon(img));
		backwardsButton.setBorder(BorderFactory.createEmptyBorder());
		backwardsButton.setContentAreaFilled(false);
		backwardsButton.setFocusPainted(false);
		backwardsButton.setBounds(397, 7, 28, 28);	
		
		img = new ImageIcon(this.getClass().getResource("/forwards.png")).getImage();
		forwardsButton.setIcon(new ImageIcon(img));
		forwardsButton.setBorder(BorderFactory.createEmptyBorder());
		forwardsButton.setContentAreaFilled(false);
		forwardsButton.setFocusPainted(false);
		forwardsButton.setBounds(485, 7, 28, 28);
	}
}
