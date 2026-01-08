package utils;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TableActionCellRender extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

    	Object statusObj = table.getValueAt(row, 4); 
        
        String status = "";
        if (statusObj != null) {
            status = statusObj.toString();
        }
        JButton btn = new JButton("Check In");
        
        if (status != null && (status.equalsIgnoreCase("Returned") || status.equalsIgnoreCase("Lost"))) {
            btn.setText("Finished");
            btn.setEnabled(false);
            btn.setBackground(Color.GRAY);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
        } else {
            btn.setBackground(new Color(255, 255, 255)); 
            btn.setForeground(Color.BLACK);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
        }
        return btn;
    }
}