package org.elsys.subs;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class TableProperties {
	public TableProperties(JTable subtitleTable) {
		
		subtitleTable.setCellSelectionEnabled(false);
		subtitleTable.setRowSelectionAllowed(true);
		subtitleTable.getTableHeader().setReorderingAllowed(false);
		subtitleTable.getTableHeader().setResizingAllowed(false);
		subtitleTable.setBounds(1079, 228, -1075, 415);
		
		subtitleTable.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"#", " Start ", " End ", " Text "
			}
		){
			private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		});
		
		subtitleTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		subtitleTable.getColumnModel().getColumn(1).setPreferredWidth(190);
		subtitleTable.getColumnModel().getColumn(2).setPreferredWidth(190);
		subtitleTable.getColumnModel().getColumn(3).setPreferredWidth(2000);
		subtitleTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		subtitleTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		subtitleTable.getTableHeader().setBackground(new Color(165, 207, 231));
		subtitleTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		subtitleTable.getColumnModel().getColumn(0).setCellRenderer(renderer);
	}
}