package utils;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TableActionCellRender extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        String status = (String) table.getValueAt(row, 3);
        JButton btn = new JButton("Check In");
        
        if (status != null && (status.equalsIgnoreCase("Returned") || status.equalsIgnoreCase("Lost"))) {
            btn.setText("Finished");
            btn.setEnabled(false);
            btn.setBackground(Color.GRAY);
        } else {
            btn.setBackground(new Color(255, 255, 255)); 
            btn.setForeground(Color.BLACK);
        }
        return btn;
    }
}