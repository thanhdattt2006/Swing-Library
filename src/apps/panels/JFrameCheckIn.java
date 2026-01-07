package apps.panels;

import java.awt.EventQueue;



import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import apps.JFrameMain;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import entities.Account;
import models.ConnectDB;
import models.LoanDetailsModel;
import models.LoanMasterModel;
import models.LoginModel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JComboBox;
import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Component;

public class JFrameCheckIn extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField jtextFieldId;
    private JTextField jtextFieldLoanMasterID;
    private JTextField jtextFieldBookID;
    private JTextField jtextFieldLateFee;
    private JTextField jtextFieldCompensationFee;
    private JDateChooser jdateChooserReturnDate;
    private JComboBox<String> jcomboBoxStatus; // Sửa thành Generic <String>

    // Biến lưu trữ dữ liệu tính toán
    private double lateFeePerDay;
    private double lostPercent;
    private double damagedPercent;
    
    private int currentDetailId; // ID đang xử lý
    private int masterId;
    private int bookId;
    private double bookPrice;
    private LocalDate dueDate;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Throwable e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(() -> {
            try {
                // Test với ID = 1 (Bạn thay số này để test)
                JFrameCheckIn frame = new JFrameCheckIn(1); 
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    // Cập nhật Constructor nhận detailId
    public JFrameCheckIn(int detailId) {
        this.currentDetailId = detailId;

        setTitle("Check In");
        // Sửa thành DISPOSE để không tắt cả chương trình chính
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setBounds(100, 100, 497, 449);
        setLocationRelativeTo(null); // Ra giữa màn hình

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblNewLabel = new JLabel("Id");
        lblNewLabel.setBounds(72, 108, 20, 16);
        contentPane.add(lblNewLabel);
        
        jtextFieldId = new JTextField();
        jtextFieldId.setEditable(false); // Readonly
        jtextFieldId.setBounds(209, 102, 201, 28);
        contentPane.add(jtextFieldId);
        jtextFieldId.setColumns(10);
        
        JLabel lblNewLabel_1 = new JLabel("Loan Master Id");
        lblNewLabel_1.setBounds(72, 135, 100, 16);
        contentPane.add(lblNewLabel_1);
        
        jtextFieldLoanMasterID = new JTextField();
        jtextFieldLoanMasterID.setEditable(false); // Readonly
        jtextFieldLoanMasterID.setBounds(209, 135, 201, 28);
        contentPane.add(jtextFieldLoanMasterID);
        jtextFieldLoanMasterID.setColumns(10);
        
        JLabel lblNewLabel_2 = new JLabel("Book Id");
        lblNewLabel_2.setBounds(72, 174, 60, 16);
        contentPane.add(lblNewLabel_2);
        
        jtextFieldBookID = new JTextField();
        jtextFieldBookID.setEditable(false); // Readonly
        jtextFieldBookID.setBounds(209, 168, 201, 28);
        contentPane.add(jtextFieldBookID);
        jtextFieldBookID.setColumns(10);
        
        JLabel lblNewLabel_3 = new JLabel("Late Fee");
        lblNewLabel_3.setBounds(72, 207, 80, 16);
        contentPane.add(lblNewLabel_3);
        
        jtextFieldLateFee = new JTextField();
        jtextFieldLateFee.setEditable(false); // Tự động tính, không cho sửa
        jtextFieldLateFee.setBounds(209, 201, 201, 28);
        contentPane.add(jtextFieldLateFee);
        jtextFieldLateFee.setColumns(10);
        
        JLabel lblNewLabel_4 = new JLabel("Compensation Fee");
        lblNewLabel_4.setBounds(72, 240, 120, 16);
        contentPane.add(lblNewLabel_4);
        
        jtextFieldCompensationFee = new JTextField();
        jtextFieldCompensationFee.setEditable(false); // Tự động tính
        jtextFieldCompensationFee.setBounds(209, 234, 201, 28);
        contentPane.add(jtextFieldCompensationFee);
        jtextFieldCompensationFee.setColumns(10);
        
        JLabel lblNewLabel_5 = new JLabel("Return Date");
        lblNewLabel_5.setBounds(72, 267, 80, 16);
        contentPane.add(lblNewLabel_5);
        
        jdateChooserReturnDate = new JDateChooser();
        jdateChooserReturnDate.setDateFormatString("yyyy-MM-dd");
        jdateChooserReturnDate.setBounds(209, 267, 201, 28);
        contentPane.add(jdateChooserReturnDate);
        
        // Sự kiện: Khi đổi ngày trả -> Tự tính lại tiền
        jdateChooserReturnDate.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    calculateAndShowFees();
                }
            }
        });
        
        JLabel lblNewLabel_6 = new JLabel("Status");
        lblNewLabel_6.setBounds(72, 305, 50, 16);
        contentPane.add(lblNewLabel_6);
        
        // --- SỬA LỖI SHADOWING ---
        // Bỏ chữ "JComboBox" ở đầu để dùng biến toàn cục
        jcomboBoxStatus = new JComboBox<>(); 
        jcomboBoxStatus.setBounds(209, 300, 201, 26);
        // Thêm các lựa chọn
        jcomboBoxStatus.addItem("Normal");
        jcomboBoxStatus.addItem("Damaged");
        jcomboBoxStatus.addItem("Lost");
        contentPane.add(jcomboBoxStatus);
        
        // Sự kiện: Khi đổi trạng thái -> Tự tính lại tiền bồi thường
        jcomboBoxStatus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateAndShowFees();
            }
        });
        
        JButton jButtonConfirm = new JButton("Confirm");
        jButtonConfirm.setBounds(160, 338, 80, 28);
        contentPane.add(jButtonConfirm);
        
        JButton jbuttonCancel = new JButton("Cancel");
        jbuttonCancel.setBounds(271, 338, 80, 28);
        contentPane.add(jbuttonCancel);
        
        // Sự kiện nút Cancel
        jbuttonCancel.addActionListener(e -> dispose());
        
        // Sự kiện nút Confirm
        jButtonConfirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processCheckIn();
            }
        });
        
        JPanel panel = new JPanel();
        panel.setAlignmentY(Component.TOP_ALIGNMENT);
        panel.setBackground(SystemColor.activeCaption);
        panel.setBounds(0, 0, 495, 68);
        contentPane.add(panel);
        panel.setLayout(null);
        
        JLabel lblNewLabel_7 = new JLabel("Check In");
        lblNewLabel_7.setBounds(0, 17, 495, 28);
        lblNewLabel_7.setForeground(SystemColor.text);
        lblNewLabel_7.setHorizontalTextPosition(SwingConstants.CENTER);
        lblNewLabel_7.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_7.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(lblNewLabel_7);

        // --- LOAD DỮ LIỆU ---
        // Constructor không tham số chỉ để cho WindowBuilder render
        if (detailId > 0) { 
            loadSettings();
            loadData();
        }
    }
    
    // Constructor mặc định cho WindowBuilder
    public JFrameCheckIn() {
        this(0);
    }

    private void loadSettings() {
        try (Connection conn = ConnectDB.connection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM settings LIMIT 1")) {
            
            if (rs.next()) {
                this.lateFeePerDay = rs.getDouble("late_fee_per_day");
                this.lostPercent = rs.getDouble("lost_compensation_fee");
                this.damagedPercent = rs.getDouble("damaged_compensation_fee");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        String sql = "SELECT d.id, d.loan_master_id, d.book_id, m.due_date, b.price " +
                     "FROM loan_details d " +
                     "JOIN loan_master m ON d.loan_master_id = m.id " +
                     "JOIN book b ON d.book_id = b.id " +
                     "WHERE d.id = ?";
        
        try (Connection conn = ConnectDB.connection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, this.currentDetailId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                // Fill Text UI
                jtextFieldId.setText(String.valueOf(rs.getInt("id")));
                jtextFieldLoanMasterID.setText(String.valueOf(rs.getInt("loan_master_id")));
                jtextFieldBookID.setText(String.valueOf(rs.getInt("book_id")));
                
                // Save hidden data
                this.masterId = rs.getInt("loan_master_id");
                this.bookId = rs.getInt("book_id");
                this.bookPrice = rs.getDouble("price");
                
                java.sql.Date sqlDate = rs.getDate("due_date");
                if (sqlDate != null) {
                    this.dueDate = sqlDate.toLocalDate();
                } else {
                    this.dueDate = LocalDate.now();
                }

                // Set Default UI Values
                jdateChooserReturnDate.setDate(new Date()); // Hôm nay
                calculateAndShowFees(); // Tính toán luôn phí ban đầu
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu!");
        }
    }

    // 3. Hàm tính toán và hiển thị (chưa lưu)
    private void calculateAndShowFees() {
        if (jdateChooserReturnDate.getDate() == null) return;

        LocalDate returnDate = jdateChooserReturnDate.getDate().toInstant()
                                .atZone(ZoneId.systemDefault()).toLocalDate();
        String status = (String) jcomboBoxStatus.getSelectedItem();

        double lateFee = 0;
        double compFee = 0;

        // Tính trễ hạn
        if (dueDate != null && returnDate.isAfter(dueDate)) {
            long days = ChronoUnit.DAYS.between(dueDate, returnDate);
            lateFee = days * this.lateFeePerDay;
        }

        // Tính bồi thường
        if ("Lost".equals(status)) {
            compFee = bookPrice * (this.lostPercent / 100.0);
        } else if ("Damaged".equals(status)) {
            compFee = bookPrice * (this.damagedPercent / 100.0);
        }

        // Hiển thị (định dạng số đẹp 1 chút)
        jtextFieldLateFee.setText(String.format("%.0f", lateFee));
        jtextFieldCompensationFee.setText(String.format("%.0f", compFee));
    }

    // 4. Xử lý khi bấm CONFIRM
    private void processCheckIn() {
        if (jdateChooserReturnDate.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày trả!");
            return;
        }

        // Lấy lại giá trị đã tính toán từ giao diện (hoặc tính lại cho chắc)
        double lateFee = Double.parseDouble(jtextFieldLateFee.getText());
        double compFee = Double.parseDouble(jtextFieldCompensationFee.getText());
        LocalDate returnDate = jdateChooserReturnDate.getDate().toInstant()
                                .atZone(ZoneId.systemDefault()).toLocalDate();
        String statusUI = (String) jcomboBoxStatus.getSelectedItem();
        
        // Map status UI sang DB & Book Status
        String statusDB = "Good";
        String bookStatus = "Available";
        
        if ("Lost".equals(statusUI)) {
            statusDB = "Lost";
            bookStatus = "Lost";
        } else if ("Damaged".equals(statusUI)) {
            statusDB = "Bad"; 
            bookStatus = "Damaged";
        }

        // --- TRANSACTION UPDATE DB ---
        Connection conn = null;
        try {
            conn = ConnectDB.connection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // B1: Update Loan Detail
            String sqlUpdateDetail = "UPDATE loan_details SET return_date = ?, status = ?, late_fee = ?, compensation_fee = ? WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateDetail)) {
                ps.setDate(1, java.sql.Date.valueOf(returnDate));
                ps.setString(2, statusDB);
                ps.setDouble(3, lateFee);
                ps.setDouble(4, compFee);
                ps.setInt(5, currentDetailId);
                ps.executeUpdate();
            }

            // B2: Update Book Status
            String sqlUpdateBook = "UPDATE book SET status = ? WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateBook)) {
                ps.setString(1, bookStatus);
                ps.setInt(2, bookId);
                ps.executeUpdate();
            }

            // B3: Tính tổng Master & Cập nhật
            updateMaster(conn, masterId);

            conn.commit(); // Thành công hết thì Commit
            
            JOptionPane.showMessageDialog(this, "Trả sách thành công!");
            this.dispose(); // Đóng cửa sổ

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu: " + e.getMessage());
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
    }

    // Helper: Update Master (Logic tổng hợp)
    private void updateMaster(Connection conn, int mId) throws SQLException {
        // Tính tổng tiền
        String sqlSum = "SELECT SUM(late_fee), SUM(compensation_fee) FROM loan_details WHERE loan_master_id = ?";
        double totalLate = 0, totalComp = 0;
        try (PreparedStatement ps = conn.prepareStatement(sqlSum)) {
            ps.setInt(1, mId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalLate = rs.getDouble(1);
                totalComp = rs.getDouble(2);
            }
        }

        // Kiểm tra đã xong hết chưa
        String sqlCheck = "SELECT COUNT(*) FROM loan_details WHERE loan_master_id = ? AND return_date IS NULL";
        boolean isFinished = false;
        try (PreparedStatement ps = conn.prepareStatement(sqlCheck)) {
            ps.setInt(1, mId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) isFinished = (rs.getInt(1) == 0);
        }

        String masterStatus = isFinished ? "Completed" : "Active";
        String sqlUpd = "UPDATE loan_master SET total_late_fee = ?, total_compensation_fee = ?, status = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlUpd)) {
            ps.setDouble(1, totalLate);
            ps.setDouble(2, totalComp);
            ps.setString(3, masterStatus);
            ps.setInt(4, mId);
            ps.executeUpdate();
        }
    }
}
