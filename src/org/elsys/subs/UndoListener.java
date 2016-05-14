package org.elsys.subs;

import java.util.Stack;

import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class UndoListener {

	public static Stack<String> undoStack = new Stack<>();
	public static Stack<String> redoStack = new Stack<>();
	
	
	private JTextArea subtitleArea;
	private JTable subtitleTable;
	private JTextField subtitleNumTextField;
	private JFormattedTextField startTextField;
	private JTextField durationTextField;
	private JFormattedTextField endTextField;
	private DefaultTableModel model = null;
	
	public UndoListener(JTextArea subtitleArea, JTable subtitleTable, JTextField subtitleNumTextField, JFormattedTextField startTextField, JTextField durationTextField, JFormattedTextField endTextField) {
		this.subtitleArea = subtitleArea;
		this.subtitleTable = subtitleTable;
		this.subtitleNumTextField = subtitleNumTextField;
		this.startTextField = startTextField;
		this.durationTextField = durationTextField;
		this.endTextField = endTextField;
	}

	protected void undo() {
		model = (DefaultTableModel) subtitleTable.getModel();
		if (!undoStack.isEmpty()) {
			
			String lastCommand = undoStack.pop();
			String[] splitedCommands = lastCommand.split("\n");
			String[] splitedArgs = splitedCommands[0].split("\\|");
			
			//System.out.println("Undo: " + lastCommand);
			
			redoStack.push(lastCommand);
			
			if (splitedArgs[0].equals("del")) {
				InsertSubsToTable insSubs = new InsertSubsToTable(subtitleArea, subtitleTable);
				for (int i = 0; i < splitedCommands.length; i++) {
					if (i == 0) { // because of "del"
						splitedArgs = splitedCommands[i].split("\\|");
						splitedArgs[0] = splitedArgs[1];
						splitedArgs[1] = splitedArgs[2];
						splitedArgs[2] = splitedArgs[3];
						splitedArgs[3] = splitedArgs[4];
					} else {
						splitedArgs = splitedCommands[i].split("\\|");
					}
					insSubs.insert(false, splitedArgs);
				}
				
				
			} else if(splitedArgs[0].equals("ins")) {
				DeleteSubsFromTable delSubs = new DeleteSubsFromTable(subtitleArea, subtitleTable, subtitleNumTextField, startTextField, durationTextField, endTextField);
				splitedArgs = splitedCommands[0].split("\\|");
				
				splitedArgs = splitedCommands[0].split("\\|");
				splitedArgs[0] = splitedArgs[1];
				splitedArgs[1] = splitedArgs[2];
				splitedArgs[2] = splitedArgs[3];
				splitedArgs[3] = splitedArgs[4];
				
				delSubs.delete(false, splitedArgs, 0);
			} else if(splitedArgs[0].equals("chg")) {
				int index = Integer.parseInt(splitedArgs[1]);
				for (int i = 1; i < splitedArgs.length; i++) {
					subtitleTable.setValueAt(splitedArgs[i], index, i-1);
				}
				for (int i = 0; i < subtitleTable.getRowCount(); i++) {
					model.setValueAt(i + 1 + "\n", i, 0);
				}
			}

		}
	}
	
	protected void redo() {
		if (!redoStack.isEmpty()) {
			String lastCommand = redoStack.pop();
			String[] splitedCommands = lastCommand.split("\n");
			String[] splitedArgs = splitedCommands[0].split("\\|");
			
			undoStack.push(lastCommand);
			
			//System.out.println("Redo: " + lastCommand);
			
			if (splitedArgs[0].equals("del")) {
				
				DeleteSubsFromTable delSubs = new DeleteSubsFromTable(subtitleArea, subtitleTable, subtitleNumTextField, startTextField, durationTextField, endTextField);
				
				for (int i = 0; i < splitedCommands.length; i++) {
					if (i == 0) { // because of "del"
						splitedArgs = splitedCommands[i].split("\\|");
						splitedArgs[0] = splitedArgs[1];
						splitedArgs[1] = splitedArgs[2];
						splitedArgs[2] = splitedArgs[3];
						splitedArgs[3] = splitedArgs[4];
					} else {
						splitedArgs = splitedCommands[i].split("\\|");
					}
					
					delSubs.delete(false, splitedArgs, i);
				}
				
				
			} else if(splitedArgs[0].equals("ins")) {
				
				InsertSubsToTable insSubs = new InsertSubsToTable(subtitleArea, subtitleTable);
				
				splitedArgs = splitedCommands[0].split("\\|");
				
				splitedArgs[0] = splitedArgs[1];
				splitedArgs[1] = splitedArgs[2];
				splitedArgs[2] = splitedArgs[3];
				splitedArgs[3] = splitedArgs[4];
				
				insSubs.insert(false, splitedArgs);
				
			} else if(splitedArgs[0].equals("chg")) {
				splitedArgs = splitedCommands[1].split("\\|");
				int index = Integer.parseInt(splitedArgs[0]);
				
				for (int i = 0; i < splitedArgs.length; i++) {
					subtitleTable.setValueAt(splitedArgs[i], index, i);
				}
				for (int i = 0; i < subtitleTable.getRowCount(); i++) {
					model.setValueAt(i + 1 + "\n", i, 0);
				}
			}

		}
	}
}





