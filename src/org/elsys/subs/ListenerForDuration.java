package org.elsys.subs;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextField;

public class ListenerForDuration {

	public ListenerForDuration(JTable subtitleTable, JFormattedTextField startTextField ,JFormattedTextField endTextField, JTextField durationTextField, int selectedRow) {

		try {
			String startText = startTextField.getText();
			String numsStart[] = startText.split(":");

			numsStart[2] = numsStart[2].replaceAll(",", ".");

			Double startDuration = 0.0;
			double temp = 0;
			for (int i = 0; i < 3; i++) {
				if (i == 0)
					temp = Double.parseDouble(numsStart[i]) * 3600;

				else if (i == 1)
					temp = Double.parseDouble(numsStart[i]) * 60;

				else if (i == 2)
					temp = Double.parseDouble(numsStart[i]);

				startDuration += temp;
			}

			String durationText = durationTextField.getText();
			Double newEndDuration = Double.parseDouble(durationText) + Double.parseDouble(startDuration.toString());
			
			Integer hours = 0;
			Integer minutes = 0;
			Integer seconds = 0;
			while(newEndDuration >= 3600) {
				hours++;
				newEndDuration -= 3600;
			}
			while(newEndDuration >= 60) {
				minutes++;
				newEndDuration -= 60;
			}
			while(newEndDuration >= 1) {
				seconds++;
				newEndDuration--;
			}
	
			String decimals;
			DecimalFormat df = new DecimalFormat("#.###");
			df.setRoundingMode(RoundingMode.HALF_EVEN);
			decimals = df.format(newEndDuration).replaceAll(",", ".");
			newEndDuration = Double.parseDouble(decimals);
			
			if(newEndDuration == 0.0)
				decimals = "," + df.format(newEndDuration) + "00";
			else
				decimals = df.format(newEndDuration).replaceFirst("0", "");
			
			if(decimals.length() == 2)
				decimals = decimals + "00";
			if(decimals.length() == 3)
				decimals = decimals + "0";

			
			String sHours = hours.toString(), sMinutes = minutes.toString(), sSeconds = seconds.toString();
			if(hours.toString().length() == 1)
				sHours = "0" + hours.toString();
			if(minutes.toString().length() == 1)
				sMinutes = "0" + minutes.toString();
			if(seconds.toString().length() == 1)
				sSeconds = "0" + seconds.toString();
			
			endTextField.setText(sHours + ":" + sMinutes + ":" + sSeconds + decimals);
			endTextField.setValue(new String(sHours + ":" + sMinutes + ":" + sSeconds + decimals));
		
			if(subtitleTable.getSelectedRow() > -1) {
				subtitleTable.setValueAt(startTextField.getText(), selectedRow, 1);
				subtitleTable.setValueAt(endTextField.getText(), selectedRow, 2);
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}