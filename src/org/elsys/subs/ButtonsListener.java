package org.elsys.subs;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.swing.JTable;
import javax.swing.JTextField;

public class ButtonsListener {
	public ButtonsListener(JTable subtitleTable, JTextField changeTextField, String direction) {

		if (changeTextField.getText().isEmpty()) {
			return;
		} else {
			try {
				String text = changeTextField.getText();
				Object start, end;

				Double secondsForChange = Double.parseDouble(text);
				Double startDuration = 0.0, endDuration = 0.0;

				double temp1 = 0, temp2 = 0;
				int indices[] = subtitleTable.getSelectedRows();
				int numberOfSelectedRows = subtitleTable.getSelectedRowCount();

				String numsStart[] = null;
				String numsEnd[] = null;

				for (int i = 0; i < numberOfSelectedRows; i++) {

					start = subtitleTable.getValueAt(indices[i], 1);
					end = subtitleTable.getValueAt(indices[i], 2);

					numsStart = start.toString().split(":");
					numsStart[2] = numsStart[2].replaceAll(",", ".");

					numsEnd = end.toString().split(":");
					numsEnd[2] = numsEnd[2].replaceAll(",", ".");

					temp1 = 0;
					temp2 = 0;

					for (int k = 0; k < 3; k++) {
						if (k == 0) {
							temp1 = Double.parseDouble(numsStart[k]) * 3600;
							temp2 = Double.parseDouble(numsEnd[k]) * 3600;
						} else if (k == 1) {
							temp1 = Double.parseDouble(numsStart[k]) * 60;
							temp2 = Double.parseDouble(numsEnd[k]) * 60;
						}

						else if (k == 2) {
							temp1 = Double.parseDouble(numsStart[k]);
							temp2 = Double.parseDouble(numsEnd[k]);
						}
						startDuration += temp1;
						endDuration += temp2;
					}

					Double newStartDuration;
					Double newEndDuration;
					if (direction.equals("backwards")) {
						newStartDuration = startDuration - secondsForChange;
						newEndDuration = endDuration - secondsForChange;
					} else {
						newStartDuration = startDuration + secondsForChange;
						newEndDuration = endDuration + secondsForChange;
					}

					if (newStartDuration >= 0) {

						Integer hours = 0;
						Integer minutes = 0;
						Integer seconds = 0;

						hours = 0;
						minutes = 0;
						seconds = 0;

						while (newStartDuration >= 3600) {
							hours++;
							newStartDuration -= 3600;
						}
						while (newStartDuration >= 60) {
							minutes++;
							newStartDuration -= 60;
						}
						while (newStartDuration >= 1) {
							seconds++;
							newStartDuration--;
						}

						if (hours < 10)
							numsStart[0] = "0" + hours.toString();
						else
							numsStart[0] = hours.toString();

						if (minutes < 10)
							numsStart[1] = "0" + minutes.toString();
						else
							numsStart[1] = minutes.toString();

						if (seconds < 10)
							numsStart[2] = "0" + seconds.toString();
						else
							numsStart[2] = seconds.toString();

						String sDecimals;
						DecimalFormat df = new DecimalFormat("#.###");
						df.setRoundingMode(RoundingMode.HALF_EVEN);
						sDecimals = df.format(newStartDuration).replaceAll(",", ".");
						newStartDuration = Double.parseDouble(sDecimals);

						if (newStartDuration == 0.0)
							sDecimals = "," + df.format(newStartDuration) + "00";
						else
							sDecimals = df.format(newStartDuration).replaceFirst("0", "");
						
						if (sDecimals.length() == 2)
							sDecimals = sDecimals + "00";
						if (sDecimals.length() == 3)
							sDecimals = sDecimals + "0";
						
						hours = 0;
						minutes = 0;
						seconds = 0;

						while (newEndDuration >= 3600) {
							hours++;
							newEndDuration -= 3600;
						}
						while (newEndDuration >= 60) {
							minutes++;
							newEndDuration -= 60;
						}
						while (newEndDuration >= 1) {
							seconds++;
							newEndDuration--;
						}

						if (hours < 10)
							numsEnd[0] = "0" + hours.toString();
						else
							numsEnd[0] = hours.toString();

						if (minutes < 10)
							numsEnd[1] = "0" + minutes.toString();
						else
							numsEnd[1] = minutes.toString();

						if (seconds < 10)
							numsEnd[2] = "0" + seconds.toString();
						else
							numsEnd[2] = seconds.toString();

						String eDecimals;
						eDecimals = df.format(newEndDuration).replaceAll(",", ".");
						newEndDuration = Double.parseDouble(eDecimals);

						if (newEndDuration == 0.0)
							eDecimals = "," + df.format(newEndDuration) + "00";
						else
							eDecimals = df.format(newEndDuration).replaceFirst("0", "");
						
						
						if (eDecimals.length() == 2)
							eDecimals = eDecimals + "00";
						if (eDecimals.length() == 3)
							eDecimals = eDecimals + "0";

					
						String finalStart = numsStart[0] + ':' + numsStart[1] + ':' + numsStart[2] + sDecimals;
						String finalEnd = numsEnd[0] + ':' + numsEnd[1] + ':' + numsEnd[2] + eDecimals;

						subtitleTable.setValueAt(finalStart, indices[i], 1);
						subtitleTable.setValueAt(finalEnd, indices[i], 2);
					}

					newStartDuration = 0.0;
					startDuration = 0.0;
					endDuration = 0.0;
					newEndDuration = 0.0;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
