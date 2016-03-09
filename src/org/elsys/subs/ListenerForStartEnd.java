package org.elsys.subs;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextField;

public class ListenerForStartEnd {
	public ListenerForStartEnd(JTable subtitleTable, JFormattedTextField startTextField, JFormattedTextField endTextField, JTextField durationTextField) {

		try {
			String startText = startTextField.getText();
			String numsStart[] = startText.split(":");

			String endText = endTextField.getText();
			String numsEnd[] = endText.split(":");

			numsStart[2] = numsStart[2].replaceAll(",", ".");
			numsEnd[2] = numsEnd[2].replaceAll(",", ".");

			double endDuration = 0;
			double startDuration = 0;
			double temp1 = 0, temp2 = 0;

			Double durationValue;
			for (int i = 0; i < 3; i++) {
				if (i == 0) {
					temp1 = Double.parseDouble(numsEnd[i]) * 3600;
					temp2 = Double.parseDouble(numsStart[i]) * 3600;
				} else if (i == 1) {
					temp1 = Double.parseDouble(numsEnd[i]) * 60;
					temp2 = Double.parseDouble(numsStart[i]) * 60;
				} else if (i == 2) {
					temp1 = Double.parseDouble(numsEnd[i]);
					temp2 = Double.parseDouble(numsStart[i]);
				}
				endDuration += temp1;
				startDuration += temp2;
			}

			durationValue = endDuration - startDuration;
			DecimalFormat df = new DecimalFormat("#.###");
			df.setRoundingMode(RoundingMode.CEILING);
			String duration = df.format(durationValue).replaceAll(",", ".");
			
			durationTextField.setText(duration);
			
			try {
			if(subtitleTable.getSelectedRow() > -1) {
				subtitleTable.setValueAt(startTextField.getText(), subtitleTable.getSelectedRow(), 1);
				subtitleTable.setValueAt(endTextField.getText(), subtitleTable.getSelectedRow(), 2);		
			}
			} catch(Exception e) {
				System.out.print("Running StartEnd for 1-st time\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}