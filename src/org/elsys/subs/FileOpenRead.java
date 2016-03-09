package org.elsys.subs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class FileOpenRead extends JFrame {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("resource")
	public FileOpenRead(JTable subtitleTable) throws IOException {
		super("Choose a file");
		JFileChooser chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.srt,*.ass,*.sub,*.txt", "srt", "ass", "sub", "sub","txt");
		chooser.setFileFilter(filter);
		chooser.setMultiSelectionEnabled(false);
		int option = chooser.showOpenDialog(FileOpenRead.this);
		if (option == JFileChooser.APPROVE_OPTION) {
			DefaultTableModel model = (DefaultTableModel) subtitleTable.getModel();
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(chooser.getSelectedFile().getAbsolutePath())));
			String line;
			Object[] obj = {"", "", "", ""};
			int i = 0;
			
			model.setRowCount(0);
			
			while ((line = reader.readLine()) != null)
			{
				if (line.trim().length() > 0) {
					if(line.matches("[0-9]+")) {
						if(i != 0) 
							model.addRow(obj);
						obj[0] = line + "\n";
						i = 0;
					}
					else if(line.contains("-->")) {
						String temp[] = line.split("-->");
						obj[1] = temp[0].trim();
						obj[2] = temp[1].trim();
					}
					else {
						if(i != 0) 
							obj[3] = obj[3] + line + "\n";
						else 
							obj[3] = line + " \n";
						i++;
					}
				}
				
			}
			model.addRow(obj);
			
		}
	}
}