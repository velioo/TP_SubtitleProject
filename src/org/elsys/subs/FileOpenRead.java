package org.elsys.subs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.mozilla.universalchardet.UniversalDetector;

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
			
			byte[] buf = new byte[4096];
			UniversalDetector detector= new UniversalDetector(null);
			FileInputStream fis = new FileInputStream(chooser.getSelectedFile().getAbsolutePath());
			int nread;
			while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
			  detector.handleData(buf, 0, nread);
			detector.dataEnd();
			String encoding = detector.getDetectedCharset();
			
			//System.out.println(encoding);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(chooser.getSelectedFile().getAbsolutePath()), encoding));
			String line;
			Object[] obj = {"", "", "", ""};
			int i = 0, step = 0, nextSubNum = 0;
			
			model.setRowCount(0);
			
			while ((line = reader.readLine()) != null) {
				if (line.trim().length() > 0) {
					if(isNextSub(reader, line)) {
						if(step != 0) 
							model.addRow(obj);
						obj[0] = ++nextSubNum + "\n";
						obj[3] = "";
						i = 0;
						step = 1;
					}
					else if(line.contains("-->")) {
						String temp[] = line.split("-->");
						obj[1] = temp[0].trim();
						obj[2] = temp[1].trim();
						step = 2;
					}
					else {
						if(i != 0) 
							obj[3] = obj[3] + line + "\n";
						else 
							obj[3] = line + " \n";
						i++;
						step = 3;
					}
				}
				
			}
			model.addRow(obj);
			if(mediaPlayerComponent.isValid())
				mediaPlayerComponent.getMediaPlayer().setSubTitleFile(chooser.getSelectedFile().getAbsolutePath());
		}
		}
	}
	
	@SuppressWarnings("unused")
	private boolean isNextSub(BufferedReader reader, String line) throws IOException {
		BufferedReader r = reader;
		String l1 = line, l2;
		if (l1.matches("[0-9]+")) {
			r.mark(200);
			if((l2 = r.readLine()).contains("-->")) {
				r.reset();
				return true;
			}
		}
		return false;
	}
}