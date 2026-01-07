package utils;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class DetailsButtonRender extends JButton implements TableCellRenderer {
    public DetailsButtonRender() {
        setOpaque(true);
        setBackground(new Color(255, 255, 255)); 
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setText("Details");
        return this;
    }
}