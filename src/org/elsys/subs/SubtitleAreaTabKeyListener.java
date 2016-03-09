package org.elsys.subs;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextArea;

public class SubtitleAreaTabKeyListener {
	public SubtitleAreaTabKeyListener (JTextArea subtitleArea) {
        subtitleArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    if (e.getModifiers() > 0) {
                        subtitleArea.transferFocusBackward();
                    } else {
                        subtitleArea.transferFocus();
                    }
                    e.consume();
                }
            }
        });
	}
}
