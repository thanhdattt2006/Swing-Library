package apps.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class CheckOutDetailsPanel extends JPanel {
    private JTextField txtSearchName, txtSearchAuthor, txtSearchCategory;
    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> rowSorter;

    public CheckOutDetailsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Kho Sách (Tìm kiếm & Chọn sách)"));

        // --- PHẦN TÌM KIẾM ---
        JPanel panelTop = new JPanel(new GridLayout(2, 3, 10, 5));
        panelTop.add(new JLabel("Tên sách:"));
        panelTop.add(new JLabel("Tác giả:"));
        panelTop.add(new JLabel("Thể loại:"));
        
        txtSearchName = new JTextField();
        txtSearchAuthor = new JTextField();
        txtSearchCategory = new JTextField();

        panelTop.add(txtSearchName);
        panelTop.add(txtSearchAuthor);
        panelTop.add(txtSearchCategory);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.add(panelTop, BorderLayout.CENTER);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(wrapperPanel, BorderLayout.NORTH);

        // --- BẢNG DỮ LIỆU ---
        String[] columnNames = {"Mã", "Tên Sách", "Tác Giả", "Thể Loại", "Giá Cọc"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- LOGIC TÌM KIẾM ---
        rowSorter = new TableRowSorter<>(model);
        table.setRowSorter(rowSorter);
        
        KeyAdapter keyListener = new KeyAdapter() {
            public void keyReleased(KeyEvent e) { filterData(); }
        };
        txtSearchName.addKeyListener(keyListener);
        txtSearchAuthor.addKeyListener(keyListener);
        txtSearchCategory.addKeyListener(keyListener);

        loadData();
    }

    private void filterData() {
        List<RowFilter<Object, Object>> filters = new ArrayList<>();
        if (!txtSearchName.getText().isEmpty()) filters.add(RowFilter.regexFilter("(?i)" + txtSearchName.getText(), 1));
        if (!txtSearchAuthor.getText().isEmpty()) filters.add(RowFilter.regexFilter("(?i)" + txtSearchAuthor.getText(), 2));
        if (!txtSearchCategory.getText().isEmpty()) filters.add(RowFilter.regexFilter("(?i)" + txtSearchCategory.getText(), 3));

        if (filters.isEmpty()) rowSorter.setRowFilter(null);
        else rowSorter.setRowFilter(RowFilter.andFilter(filters));
    }

    private void loadData() {
        model.addRow(new Object[]{"B001", "Dế Mèn Phiêu Lưu Ký", "Tô Hoài", "Truyện thiếu nhi", 50000});
        model.addRow(new Object[]{"B002", "Harry Potter", "Rowling", "Viễn tưởng", 150000});
        // Thêm dữ liệu khác...
    }
}