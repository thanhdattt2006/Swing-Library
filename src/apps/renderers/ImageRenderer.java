package apps.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ImageRenderer  extends JLabel implements TableCellRenderer {
	 public ImageRenderer() {
	        setHorizontalAlignment(JLabel.CENTER);
	        setVerticalAlignment(JLabel.CENTER);
	        setOpaque(true);
	    }

	    @Override
	    public Component getTableCellRendererComponent(
	            JTable table, Object value,
	            boolean isSelected, boolean hasFocus,
	            int row, int column) {

	        if (value instanceof byte[]) {
	            byte[] imageBytes = (byte[]) value;

	            ImageIcon icon = new ImageIcon(imageBytes);

	            Image scaled = icon.getImage().getScaledInstance(
	                    100, 120, Image.SCALE_SMOOTH);

	            setIcon(new ImageIcon(scaled));
	        } else {
	            setIcon(null);
	        }

	        // background khi chọn
	        if (isSelected) {
	            setBackground(table.getSelectionBackground());
	        } else {
	            setBackground(Color.WHITE);
	        }

	        return this;
	    }
}
