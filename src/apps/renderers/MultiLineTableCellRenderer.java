package apps.renderers;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class MultiLineTableCellRenderer extends JTextArea implements TableCellRenderer {
    public MultiLineTableCellRenderer() {
        setLineWrap(true);       // Cho phép xuống dòng
        setWrapStyleWord(true);  // Xuống dòng theo nguyên từ (không cắt giữa chừng từ)
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        setText(value != null ? value.toString() : "");
        
        // Thiết lập màu sắc khi chọn hoặc bình thường
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
        
        // Cập nhật chiều cao của dòng dựa trên nội dung (tùy chọn)
        // Lưu ý: Việc tự động đổi chiều cao dòng cho từng ô có thể làm giảm hiệu năng nếu bảng quá lớn
        int width = table.getColumnModel().getColumn(column).getWidth();
        setSize(new Dimension(width, getPreferredSize().height));
        
        return this;
    }
}
