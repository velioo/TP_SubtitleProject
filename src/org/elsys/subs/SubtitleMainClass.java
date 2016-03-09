package org.elsys.subs;

public class SubtitleMainClass {

	public static void main(String[] args) {
		try {
			GuiBase window = new GuiBase();
			window.frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	};
}
