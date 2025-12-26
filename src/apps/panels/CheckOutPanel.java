package apps.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class CheckOutPanel extends JPanel {
    // Khai báo 3 ô tìm kiếm riêng biệt
    private JTextField txtSearchName;
    private JTextField txtSearchAuthor;
    private JTextField txtSearchCategory;
    
    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> rowSorter;

    public CheckOutPanel() {
        setLayout(new BorderLayout(10, 10));

        // --- PHẦN 1: TẠO KHUNG TÌM KIẾM (3 Ô) ---
        JPanel panelTop = new JPanel(new GridLayout(2, 3, 10, 5)); // Dùng GridLayout để chia cột đều đẹp
        
        // Tạo label và textfield cho Tên sách
        panelTop.add(new JLabel("Tên sách:"));
        panelTop.add(new JLabel("Tác giả:"));
        panelTop.add(new JLabel("Thể loại:"));
        
        txtSearchName = new JTextField();
        txtSearchAuthor = new JTextField();
        txtSearchCategory = new JTextField();

        panelTop.add(txtSearchName);
        panelTop.add(txtSearchAuthor);
        panelTop.add(txtSearchCategory);

        // Đặt panel tìm kiếm vào khung chính, thêm padding cho đẹp
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.add(panelTop, BorderLayout.CENTER);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // Căn lề
        add(wrapperPanel, BorderLayout.NORTH);

        // --- PHẦN 2: TẠO BẢNG (TABLE) ---
        String[] columnNames = {"Mã", "Tên Sách", "Tác Giả", "Thể Loại", "Giá Tiền"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        
        // Thêm thanh cuộn
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // --- PHẦN 3: XỬ LÝ LOGIC TÌM KIẾM ---
        rowSorter = new TableRowSorter<>(model);
        table.setRowSorter(rowSorter);

        // Tạo sự kiện lắng nghe khi gõ phím cho cả 3 ô
        KeyAdapter keyListener = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterData(); // Gọi hàm lọc chung
            }
        };

        txtSearchName.addKeyListener(keyListener);
        txtSearchAuthor.addKeyListener(keyListener);
        txtSearchCategory.addKeyListener(keyListener);

        // --- PHẦN 4: THÊM DỮ LIỆU GIẢ ---
        loadData();
    }

    // Hàm xử lý logic lọc kết hợp
    private void filterData() {
        List<RowFilter<Object, Object>> filters = new ArrayList<>();

        // 1. Lấy text từ ô Tên Sách -> Lọc cột thứ 1 (Index 1)
        String nameText = txtSearchName.getText();
        if (!nameText.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + nameText, 1));
        }

        // 2. Lấy text từ ô Tác Giả -> Lọc cột thứ 2 (Index 2)
        String authorText = txtSearchAuthor.getText();
        if (!authorText.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + authorText, 2));
        }

        // 3. Lấy text từ ô Thể Loại -> Lọc cột thứ 3 (Index 3)
        String categoryText = txtSearchCategory.getText();
        if (!categoryText.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + categoryText, 3));
        }

        // Kết hợp các bộ lọc lại với nhau (AND)
        if (filters.isEmpty()) {
            rowSorter.setRowFilter(null); // Nếu không nhập gì thì hiện hết
        } else {
            rowSorter.setRowFilter(RowFilter.andFilter(filters));
        }
    }

    private void loadData() {
        model.addRow(new Object[]{"B001", "Dế Mèn Phiêu Lưu Ký", "Tô Hoài", "Truyện thiếu nhi", 50000});
        model.addRow(new Object[]{"B002", "Harry Potter", "J.K. Rowling", "Viễn tưởng", 150000});
        model.addRow(new Object[]{"B003", "Lập trình Java", "FPT Poly", "Giáo trình", 90000});
        model.addRow(new Object[]{"B004", "Sherlock Holmes", "Conan Doyle", "Trinh thám", 120000});
        model.addRow(new Object[]{"B005", "Đắc Nhân Tâm", "Dale Carnegie", "Kỹ năng sống", 75000});
        model.addRow(new Object[]{"B006", "Java Nâng Cao", "FPT Poly", "Giáo trình", 95000});
    }
}