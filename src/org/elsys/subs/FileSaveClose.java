package org.elsys.subs;

import java.io.File;
import java.io.FileWriter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileSaveClose {

	private JTable subtitleTable;
	
	public FileSaveClose(JTable subtitleTable) {
		this.subtitleTable = subtitleTable;
	}
	
	public void saveSubtitles() {
		
		JFileChooser chooser = new JFileChooser(){
			private static final long serialVersionUID = 1L;

			@Override
		    public void approveSelection(){
		        File f = getSelectedFile();
		        if(f.exists() && getDialogType() == SAVE_DIALOG){
		            int result = JOptionPane.showConfirmDialog(this,"The file exists, overwrite?","Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
		            switch(result){
		                case JOptionPane.YES_OPTION:
		                    super.approveSelection();
		                    return;
		                case JOptionPane.NO_OPTION:
		                    return;
		                case JOptionPane.CLOSED_OPTION:
		                    return;
		                case JOptionPane.CANCEL_OPTION:
		                    cancelSelection();
		                    return;
		            }
		        }

		        super.approveSelection();
		    }        
		};
		
		
		chooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".srt", "srt");
		chooser.addChoosableFileFilter(filter);
		filter = new FileNameExtensionFilter(".ass", "ass");
		chooser.addChoosableFileFilter(filter);
		filter = new FileNameExtensionFilter(".sub", "sub");
		chooser.addChoosableFileFilter(filter);
		filter = new FileNameExtensionFilter(".txt", "txt");
		chooser.addChoosableFileFilter(filter);
		chooser.setMultiSelectionEnabled(false);
		int option = chooser.showSaveDialog(null);
		if (option == JFileChooser.APPROVE_OPTION) {
			try {
				Object obj [] = {"", "", "", ""};
				FileWriter fw = null;
				String splittedFileName [] = {"", ""};
				String temp;
				
				String fFilter = chooser.getFileFilter().getDescription();
				
				if(chooser.getSelectedFile().getName().contains(".")) {
					temp = chooser.getSelectedFile().getName();
					splittedFileName = temp.split("\\.");	
					
					temp = chooser.getSelectedFile().getAbsolutePath();
					String filePath = temp.
						    substring(0,temp.lastIndexOf(File.separator));
					
					fw = new FileWriter(filePath + "\\" + splittedFileName[0] + fFilter);
					System.out.println(filePath + "\\" + splittedFileName[0] + fFilter);
				}
				else
					fw = new FileWriter(chooser.getSelectedFile() + fFilter);
				
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
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
