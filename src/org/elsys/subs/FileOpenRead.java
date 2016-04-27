package org.elsys.subs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

public class FileOpenRead {
	
	private JTable subtitleTable;
	
	public FileOpenRead(JTable subtitleTable) {
		this.subtitleTable = subtitleTable;
	}
	
	@SuppressWarnings("resource")
	public void OpenSubtitles(EmbeddedMediaPlayerComponent mediaPlayerComponent) throws IOException {
		JFileChooser chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(true);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.srt,*.ass,*.sub,*.txt", "srt", "ass", "sub", "sub","txt");
		chooser.setFileFilter(filter);
		chooser.setMultiSelectionEnabled(false);
		int option = chooser.showOpenDialog(null);
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
			if(mediaPlayerComponent.isValid())
				mediaPlayerComponent.getMediaPlayer().setSubTitleFile(chooser.getSelectedFile().getAbsolutePath());
		}
	}
}