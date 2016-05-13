package org.elsys.subs;

import java.util.Stack;

import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class UndoListener {

	public static Stack<String> undoStack = new Stack<>();
	
	private JTextArea subtitleArea;
	private JTable subtitleTable;
	private JTextField subtitleNumTextField;
	private JFormattedTextField startTextField;
	private JTextField durationTextField;
	private JFormattedTextField endTextField;
	
	public UndoListener(JTextArea subtitleArea, JTable subtitleTable, JTextField subtitleNumTextField, JFormattedTextField startTextField, JTextField durationTextField, JFormattedTextField endTextField) {
		this.subtitleArea = subtitleArea;
		this.subtitleTable = subtitleTable;
		this.subtitleNumTextField = subtitleNumTextField;
		this.startTextField = startTextField;
		this.durationTextField = durationTextField;
		this.endTextField = endTextField;
	}

	protected void undo() {
		if (!undoStack.isEmpty()) {
			String lastCommand = undoStack.pop();
			String[] splitedCommands = lastCommand.split("\n");
			String[] splitedArgs = splitedCommands[0].split("\\|");
			
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
				delSubs.delete(false, splitedArgs);
			} else if(splitedArgs[0].equals("chg")) {
				int index = Integer.parseInt(splitedArgs[1]);
				for (int i = 1; i < splitedArgs.length; i++) {
					subtitleTable.setValueAt(splitedArgs[i], index, i-1);
				}
			}

		}
	}
}
