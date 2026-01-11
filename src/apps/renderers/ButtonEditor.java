package apps.renderers;

import javax.swing.*;
import javax.swing.table.TableCellEditor;

import apps.panels.RepairQueuePanel;

import java.awt.*;
import java.awt.event.ActionEvent;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {

    private JPanel panel;
    private JButton button;
    private JTable table;
    private RepairQueuePanel bookPanel;

    public ButtonEditor(JTable table, RepairQueuePanel bookPanel) {
        this.table = table;
        this.bookPanel = bookPanel;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

        button = new JButton("Fix");
        button.setPreferredSize(new Dimension(60, 25));
        button.setFont(new Font("Arial", Font.PLAIN, 11));
        button.setFocusable(false);

        button.addActionListener(e -> {
            int row = table.getEditingRow();
//            System.out.println(row);
            bookPanel.confirmFix(row);
            fireEditingStopped();
        });
        
     // 🎨 Màu
        button.setBackground(new Color(252, 214, 131)); // đỏ
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        // 📐 Nhỏ lại
//        button.setPreferredSize(new Dimension(40, 28));

        // 📍 Panel để canh giữa
        panel = new JPanel(new GridBagLayout());
        panel.setOpaque(true);
        panel.add(button);


//        panel.add(button);
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value,
            boolean isSelected, int row, int column) {

        panel.setBackground(table.getSelectionBackground());
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "Fix";
    }
}


