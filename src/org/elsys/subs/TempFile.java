package org.elsys.subs;

import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JTable;

public class TempFile {

	public TempFile(JTable subtitleTable) {
		Object obj [] = {"", "", "", ""};
		FileWriter fw = null;
		
		try {
			fw = new FileWriter("temp.srt");
			for(int i = 0; i < subtitleTable.getRowCount(); i++) {
				obj[0] = subtitleTable.getValueAt(i, 0);
				obj[1] = subtitleTable.getValueAt(i, 1) + " --> ";
				obj[2] = subtitleTable.getValueAt(i, 2) + "\n";
				obj[3] = subtitleTable.getValueAt(i, 3) + "\n";
				fw.write((String) obj[0]);
				fw.write((String) obj[1]);
				fw.write((String) obj[2]);
				fw.write((String) obj[3]);
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
