package apps.renderers;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRenderer extends JPanel implements TableCellRenderer {

    private JButton button;

    public ButtonRenderer() {
        setLayout(new GridBagLayout());
        setOpaque(true);

        button = new JButton("Fix");
        button.setPreferredSize(new Dimension(60, 25));
        button.setFont(new Font("Arial", Font.PLAIN, 11));
        button.setFocusable(false);

        add(button);
     // 🎨 Màu
        button.setBackground(new Color(248, 178, 27)); // xanh
//        button.setForeground(Color.WHITE);

        // 📐 Kích thước nhỏ hơn
//        setPreferredSize(new Dimension(60, 28));

        // 📍 Canh chữ giữa
//        setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
        }
//    	  if (isSelected) {
//              setBackground(new Color(41, 128, 185));
//          } else {
//              setBackground(new Color(52, 152, 219));
//          }

        return this;
    }
}


