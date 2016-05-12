package org.elsys.subs;

import java.util.Stack;

import javax.swing.JTable;
import javax.swing.JTextArea;

public class UndoListener {

	public static Stack<String> undoStack = new Stack<>();
	private JTable subtitleTable;
	private JTextArea subtitleaArea;

	public UndoListener(JTable subtitleTable, JTextArea subtitleArea) {
		this.subtitleTable = subtitleTable;
		this.subtitleaArea = subtitleArea;
	}

	protected void undo() {
		if (!undoStack.isEmpty()) {
			String lastCommand = undoStack.pop();
			System.out.println(lastCommand);
			String[] splitedCommands = lastCommand.split("\n");
			String[] splitedArgs = splitedCommands[0].split("\\|");

			if (splitedArgs[0].equals("del")) {
				InsertSubsToTable insSubs = new InsertSubsToTable(subtitleaArea, subtitleTable);
				for (int i = 0; i < splitedCommands.length; i++) {
					System.out.println("Donee");
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
			}

		}
	}
}
