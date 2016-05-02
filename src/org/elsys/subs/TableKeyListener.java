package org.elsys.subs;

import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TableKeyListener implements KeyListener{

	private static Object[] obj = { "", "", "", "" };
	private int keyCtrl = 17, keyV = 86, keyC = 67, keyX = 88;
	private boolean valueCtrl = false, valueV = false, valueC = false, valueX = false;
	private JTable subtitleTable;
	protected int pasteValue = 0;
	
	public TableKeyListener(JTable subtitleTable) {
		this.subtitleTable = subtitleTable;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(keyCtrl == e.getKeyCode()) {
			valueCtrl = false;
		}
		if(keyC == e.getKeyCode()) {
			valueC = false;
		}
		if(keyV == e.getKeyCode()) {
			valueV = false;
		}
		if(keyX == e.getKeyCode()) {
			valueX = false;
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(keyCtrl == e.getKeyCode()) {
			valueCtrl = true;
		}
		if(keyC == e.getKeyCode()) {
			valueC = true;
		}
		if(keyV == e.getKeyCode()) {
			valueV = true;
		}
		if(keyX == e.getKeyCode()) {
			valueX = true;
		}
		
		if(valueCtrl == true && valueC == true) {
			System.out.println("Ctrl + C");
			copy();
			
		}
		if(valueCtrl == true && valueV == true) {
			System.out.println("Ctrl + V");
			paste();
		}
		if(valueCtrl == true && valueX == true) {
			System.out.println("Ctrl + X");
			cut();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	
	public void copy() {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				DefaultTableModel model = (DefaultTableModel) subtitleTable.getModel();
				int selectedRowIndex = subtitleTable.getSelectedRow();
				if(selectedRowIndex == -1) {
					return;
				} else {
					for (int i = 0; i < 4; i++) {
						obj[i] = model.getValueAt(selectedRowIndex, i);
					}
					System.out.println(obj[0] + ", " + obj[1] + "," + obj[2] + ", " + obj[3]);
				}
			}
		});

	}

	public void paste() {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				DefaultTableModel model = (DefaultTableModel) subtitleTable.getModel();
				int selectedRowIndex = subtitleTable.getSelectedRow();

				System.out.println(obj[0] + ", " + obj[1] + "," + obj[2] + ", " + obj[3]);
				if(selectedRowIndex == -1) {
					return;
				} else {
					model.insertRow(selectedRowIndex, obj);
					
					for (int i = 0; i < subtitleTable.getRowCount(); i++) {
						model.setValueAt(i + 1 + "\n", i, 0);
					}
					
					subtitleTable.removeRowSelectionInterval(selectedRowIndex, selectedRowIndex);
					subtitleTable.addRowSelectionInterval(selectedRowIndex + 1, selectedRowIndex + 1);
					subtitleTable.removeRowSelectionInterval(selectedRowIndex + 1, selectedRowIndex + 1);
					subtitleTable.removeRowSelectionInterval(0, subtitleTable.getRowCount() - 1);
					subtitleTable.addRowSelectionInterval(selectedRowIndex, selectedRowIndex);
				}
			}
		});

	}
	
	public void cut() {
		
	}
}
