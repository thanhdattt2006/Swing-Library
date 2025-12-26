package apps.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class CheckOutPanel extends JPanel {
    private JTable tableCart;
    private DefaultTableModel cartModel;
    private JLabel lblTotalAmount;
    private JButton btnPay, btnRemove;

    public CheckOutPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Thông Tin Thanh Toán Cọc"));

        // --- PHẦN 1: BẢNG SÁCH ĐÃ CHỌN ---
        // Cột: Mã, Tên, Tác giả, Thể loại, Tiền cọc
        String[] columns = {"Mã Sách", "Tên Sách", "Tác Giả", "Thể Loại", "Tiền Cọc (VND)"};
        cartModel = new DefaultTableModel(columns, 0);
        tableCart = new JTable(cartModel);
        
        JScrollPane scrollPane = new JScrollPane(tableCart);
        scrollPane.setPreferredSize(new Dimension(600, 300)); // Kích thước bảng
        add(scrollPane, BorderLayout.CENTER);

        // --- PHẦN 2: KHU VỰC TÍNH TIỀN & NÚT BẤM (Bên dưới) ---
        JPanel panelBottom = new JPanel(new BorderLayout());
        panelBottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Hiển thị tổng tiền
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lblText = new JLabel("Tổng tiền cọc phải trả: ");
        lblText.setFont(new Font("Arial", Font.BOLD, 16));
        
        lblTotalAmount = new JLabel("0 VND");
        lblTotalAmount.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotalAmount.setForeground(Color.RED);
        
        panelTotal.add(lblText);
        panelTotal.add(lblTotalAmount);

        // Các nút chức năng
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRemove = new JButton("Xóa sách chọn");
        btnPay = new JButton("Xác nhận Thanh toán");
        btnPay.setBackground(new Color(0, 153, 76)); // Màu xanh lá
        btnPay.setForeground(Color.WHITE);
        
        panelButtons.add(btnRemove);
        panelButtons.add(btnPay);

        panelBottom.add(panelTotal, BorderLayout.NORTH);
        panelBottom.add(panelButtons, BorderLayout.SOUTH);

        add(panelBottom, BorderLayout.SOUTH);

        // --- SỰ KIỆN XỬ LÝ ---
        // 1. Nút Xóa sách khỏi danh sách cọc
        btnRemove.addActionListener(e -> {
            int selectedRow = tableCart.getSelectedRow();
            if (selectedRow != -1) {
                cartModel.removeRow(selectedRow);
                updateTotal(); // Tính lại tiền sau khi xóa
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sách cần xóa!");
            }
        });

        // 2. Nút Thanh toán
        btnPay.addActionListener(e -> {
            if (cartModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Chưa có sách nào để thanh toán!");
                return;
            }
            // Code lưu vào database sẽ viết ở đây
            JOptionPane.showMessageDialog(this, "Thanh toán thành công! In hóa đơn...");
            cartModel.setRowCount(0); // Xóa hết sau khi thanh toán
            updateTotal();
        });
        
        // --- DỮ LIỆU GIẢ (Demo khi chạy thử) ---
        addSampleData();
    }

    // Hàm cập nhật tổng tiền
    private void updateTotal() {
        double total = 0;
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            // Lấy giá trị cột "Tiền Cọc" (Cột index 4)
            Object value = cartModel.getValueAt(i, 4);
            if (value instanceof Number) {
                total += ((Number) value).doubleValue();
            }
        }
        DecimalFormat df = new DecimalFormat("#,### VND");
        lblTotalAmount.setText(df.format(total));
    }

    // Hàm thêm sách vào bảng (Sẽ được gọi từ CheckOutDetails sau này)
    public void addBookToCart(String id, String name, String author, String category, double price) {
        // Kiểm tra xem sách đã có trong giỏ chưa (tùy chọn)
        for (int i=0; i < cartModel.getRowCount(); i++) {
            if (cartModel.getValueAt(i, 0).equals(id)) {
                JOptionPane.showMessageDialog(this, "Sách này đã được chọn rồi!");
                return;
            }
        }
        cartModel.addRow(new Object[]{id, name, author, category, price});
        updateTotal();
    }

    private void addSampleData() {
        addBookToCart("B001", "Dế Mèn Phiêu Lưu Ký", "Tô Hoài", "Truyện", 50000);
        addBookToCart("B005", "Đắc Nhân Tâm", "Dale Carnegie", "Kỹ năng", 75000);
    }
}