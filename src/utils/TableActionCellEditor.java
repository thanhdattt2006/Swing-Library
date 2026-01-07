package utils;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

public class TableActionCellEditor extends DefaultCellEditor {
    private TableActionEvent event;

    public TableActionCellEditor(TableActionEvent event) {
        super(new JCheckBox());
        this.event = event;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        TableActionCellRender render = new TableActionCellRender();
        Component c = render.getTableCellRendererComponent(table, value, true, true, row, column);
        if (c instanceof javax.swing.JButton) {
            ((javax.swing.JButton) c).addActionListener(e -> {
                event.onAction(row);
                fireEditingStopped(); 
            });
        }
        return c;
    }
}