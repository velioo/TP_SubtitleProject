package org.elsys.subs;

import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class TableKeyListener implements KeyListener{

	private static Object[] obj = { "", "", "", "" };
	private int keyCtrl = 17, keyV = 86, keyC = 67, keyX = 88, keyZ = 90;
	private boolean valueCtrl = false, valueV = false, valueC = false, valueX = false, valueZ = false;
	private boolean cut = false;
	private JTable subtitleTable;
	private JTextArea subtitleArea;
	private JTextField subtitleNumTextField;
	private JFormattedTextField startTextField;
	private JTextField durationTextField;
	private JFormattedTextField endTextField;
	private UndoListener undoListener = null;
	
	public TableKeyListener(JTable subtitleTable, JTextArea subtitleArea, JTextField subtitleNumTextField, JFormattedTextField startTextField, JTextField durationTextField, JFormattedTextField endTextField) {
		this.subtitleTable = subtitleTable;
		this.subtitleArea = subtitleArea;
		this.subtitleNumTextField = subtitleNumTextField;
		this.startTextField = startTextField;
		this.durationTextField = durationTextField;
		this.endTextField = endTextField;
		this.undoListener = new UndoListener(subtitleArea, subtitleTable, subtitleNumTextField, startTextField, durationTextField, endTextField);
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
		if(keyZ == e.getKeyCode()) {
			valueZ = false;
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		//System.out.println(e.getKeyCode());
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
		if(keyZ == e.getKeyCode()) {
			valueZ = true;
		}
		
		if(valueCtrl == true && valueC == true) {
			//System.out.println("Ctrl + C");
			copy();
		}
		if(valueCtrl == true && valueV == true) {
			//System.out.println("Ctrl + V");
			paste();
		}
		if(valueCtrl == true && valueX == true) {
			//System.out.println("Ctrl + X");
			cut();
		}
		if(valueCtrl == true && valueZ == true) {
			//System.out.println("Ctrl + Z");
			undoListener.undo();
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {}
	
	public void copy() {
		EventQueue.invokeLater(new Runnable() {

			@SuppressWarnings("unused")
			@Override
			public void run() {
				DefaultTableModel model = (DefaultTableModel) subtitleTable.getModel();
				int selectedRowIndex = subtitleTable.getSelectedRow();
				
				obj = new Object[4];
				
				for(Object o : obj)
					o = "";
				
				if(selectedRowIndex == -1) {
					return;
				} else {
					for (int i = 0; i < 4; i++) {
						obj[i] = model.getValueAt(selectedRowIndex, i);
					}
					//System.out.println(obj[0] + ", " + obj[1] + "," + obj[2] + ", " + obj[3]);
				}
			}
		});

	}

	public void paste() {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				if(obj != null) {
					DefaultTableModel model = (DefaultTableModel) subtitleTable.getModel();
					int selectedRowIndex = subtitleTable.getSelectedRow();
	
					//System.out.println(obj[0] + ", " + obj[1] + "," + obj[2] + ", " + obj[3]);
					if(selectedRowIndex == -1) {
						return;
					} else {
						
						String temp = "ins|";
						
						temp = temp + selectedRowIndex + "|" + subtitleTable.getValueAt(selectedRowIndex, 1) + "|" + 
						subtitleTable.getValueAt(selectedRowIndex, 2) + "|";
						String temp3 = subtitleTable.getValueAt(selectedRowIndex, 3).toString();
						temp3 = temp3.replaceAll("\n", "");
						temp = temp + temp3 + "\n";
						
						UndoListener.undoStack.push(temp);
						
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
					if(cut == true) {
						cut = false;
						obj = null;
					}
				}
			}
		});

	}
	
	private void cut() {
		copy();
		DeleteSubsFromTable deleteSubsFromTable = new DeleteSubsFromTable(subtitleArea, subtitleTable, subtitleNumTextField, startTextField, durationTextField, endTextField);
		deleteSubsFromTable.delete();
		cut = true;
	}
}
