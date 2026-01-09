package apps.panels;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
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
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import models.ConnectDB;

public class JFrameCheckIn extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField jtextFieldId;
    private JTextField jtextFieldLoanMasterID;
    private JTextField jtextFieldBookTitle;
    private JTextField jtextFieldLateFee;
    private JTextField jtextFieldCompensationFee;
    private JDateChooser jdateChooserReturnDate;
    private JComboBox<String> jcomboBoxStatus;

    private double lateFeePerDay = 0;
    private double lostPercent = 0;
    private double damagedPercent = 0;

    private int currentDetailId;
    private int masterId;
    private int bookId;
    private double bookPrice;
    private LocalDate dueDate;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Throwable e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(() -> {
            try {
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
    public JFrameCheckIn(int detailId) {
        this.currentDetailId = detailId;

        setTitle("Check In Book");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 497, 449);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // --- UI COMPONENTS ---
        JLabel lblDetailId = new JLabel("Detail ID");
        lblDetailId.setBounds(72, 108, 60, 16);
        contentPane.add(lblDetailId);

        jtextFieldId = new JTextField();
        jtextFieldId.setEditable(false);
        jtextFieldId.setBounds(209, 102, 201, 28);
        contentPane.add(jtextFieldId);

        JLabel lblMasterId = new JLabel("Loan Master ID");
        lblMasterId.setBounds(72, 135, 100, 16);
        contentPane.add(lblMasterId);

        jtextFieldLoanMasterID = new JTextField();
        jtextFieldLoanMasterID.setEditable(false);
        jtextFieldLoanMasterID.setBounds(209, 135, 201, 28);
        contentPane.add(jtextFieldLoanMasterID);

        JLabel lblBookTitle = new JLabel("Book Title");
        lblBookTitle.setBounds(72, 174, 80, 16);
        contentPane.add(lblBookTitle);

        jtextFieldBookTitle = new JTextField();
        jtextFieldBookTitle.setEditable(false);
        jtextFieldBookTitle.setBounds(209, 168, 201, 28);
        contentPane.add(jtextFieldBookTitle);

        JLabel lblLateFee = new JLabel("Late Fee");
        lblLateFee.setBounds(72, 207, 80, 16);
        contentPane.add(lblLateFee);

        jtextFieldLateFee = new JTextField();
        jtextFieldLateFee.setEditable(true); 
        jtextFieldLateFee.setBounds(209, 201, 201, 28);
        contentPane.add(jtextFieldLateFee);

        JLabel lblCompFee = new JLabel("Compensation Fee");
        lblCompFee.setBounds(72, 240, 120, 16);
        contentPane.add(lblCompFee);

        jtextFieldCompensationFee = new JTextField();
        jtextFieldCompensationFee.setEditable(true);
        jtextFieldCompensationFee.setBounds(209, 234, 201, 28);
        contentPane.add(jtextFieldCompensationFee);

        JLabel lblReturnDate = new JLabel("Return Date");
        lblReturnDate.setBounds(72, 267, 80, 16);
        contentPane.add(lblReturnDate);

        jdateChooserReturnDate = new JDateChooser();
        jdateChooserReturnDate.setDateFormatString("dd-MM-yyyy");
        jdateChooserReturnDate.setBounds(209, 267, 201, 28);
        jdateChooserReturnDate.setDate(new Date()); 
        contentPane.add(jdateChooserReturnDate);

        jdateChooserReturnDate.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    calculateAndShowFees();
                }
            }
        });

        JLabel lblStatus = new JLabel("Status Checkin");
        lblStatus.setBounds(72, 305, 100, 16);
        contentPane.add(lblStatus);

        jcomboBoxStatus = new JComboBox<>();
        jcomboBoxStatus.setBounds(209, 300, 201, 26);
        jcomboBoxStatus.addItem("Good");
        jcomboBoxStatus.addItem("Damaged");
        jcomboBoxStatus.addItem("Lost");
        jcomboBoxStatus.addItem("Repaired");
        contentPane.add(jcomboBoxStatus);

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

        jbuttonCancel.addActionListener(e -> dispose());

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

        JLabel lblHeader = new JLabel("Check In Book");
        lblHeader.setBounds(0, 17, 495, 28);
        lblHeader.setForeground(SystemColor.text);
        lblHeader.setHorizontalTextPosition(SwingConstants.CENTER);
        lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(lblHeader);

        if (this.currentDetailId > 0) {
            loadSettings();
            loadData();
        }
    }

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
        String sql = "SELECT d.id, d.loan_master_id, d.book_id, m.due_date, b.price, b.title " +
                     "FROM loan_details d " +
                     "JOIN loan_master m ON d.loan_master_id = m.id " +
                     "JOIN book b ON d.book_id = b.id " +
                     "WHERE d.id = ?";

        try (Connection conn = ConnectDB.connection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, this.currentDetailId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                jtextFieldId.setText(String.valueOf(rs.getInt("id")));
                jtextFieldLoanMasterID.setText(String.valueOf(rs.getInt("loan_master_id")));
                jtextFieldBookTitle.setText(rs.getString("title"));

                this.masterId = rs.getInt("loan_master_id");
                this.bookId = rs.getInt("book_id");
                this.bookPrice = rs.getDouble("price");

                java.sql.Date sqlDate = rs.getDate("due_date");
                if (sqlDate != null) {
                    this.dueDate = sqlDate.toLocalDate();
                } else {
                    this.dueDate = LocalDate.now();
                }

                calculateAndShowFees();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data!");
        }
    }

    private void calculateAndShowFees() {
        if (jdateChooserReturnDate.getDate() == null) return;

        LocalDate returnDate = jdateChooserReturnDate.getDate().toInstant()
                                .atZone(ZoneId.systemDefault()).toLocalDate();
        String status = (String) jcomboBoxStatus.getSelectedItem();

        double lateFee = 0;
        double compFee = 0;

        if (dueDate != null && returnDate.isAfter(dueDate)) {
            long days = ChronoUnit.DAYS.between(dueDate, returnDate);
            lateFee = days * this.lateFeePerDay;
        }

        if ("Lost".equals(status)) {
            compFee = bookPrice * (this.lostPercent / 100.0);
        } else if ("Damaged".equals(status)) {
            compFee = bookPrice * (this.damagedPercent / 100.0);
        }

        jtextFieldLateFee.setText(String.format("%.0f", lateFee));
        jtextFieldCompensationFee.setText(String.format("%.0f", compFee));
    }

    private void processCheckIn() {
        if (jdateChooserReturnDate.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please choose Return Date!");
            return;
        }

        double lateFee = 0;
        double compFee = 0;

        try {
            String lateTxt = jtextFieldLateFee.getText().trim().replace(",", ".");
            if (!lateTxt.isEmpty()) lateFee = Double.parseDouble(lateTxt);

            String compTxt = jtextFieldCompensationFee.getText().trim().replace(",", ".");
            if (!compTxt.isEmpty()) compFee = Double.parseDouble(compTxt);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Fee format! Please enter numbers only.");
            return;
        }

        LocalDate returnDate = jdateChooserReturnDate.getDate().toInstant()
                                .atZone(ZoneId.systemDefault()).toLocalDate();
        String statusUI = (String) jcomboBoxStatus.getSelectedItem();

        String statusDetailDB = statusUI; 

        Connection conn = null;
        try {
            conn = ConnectDB.connection();
            conn.setAutoCommit(false); 

            String sqlUpdateDetail = "UPDATE loan_details SET return_date = ?, status = ?, late_fee = ?, compensation_fee = ? WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateDetail)) {
                ps.setDate(1, java.sql.Date.valueOf(returnDate));
                ps.setString(2, statusDetailDB);
                ps.setDouble(3, lateFee);
                ps.setDouble(4, compFee);
                ps.setInt(5, currentDetailId);
                ps.executeUpdate();
            }

            if ("Good".equalsIgnoreCase(statusUI) || "Repaired".equalsIgnoreCase(statusUI)) {
                String sqlUpdateBook = "UPDATE book SET available_quantity = available_quantity + 1 WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sqlUpdateBook)) {
                    ps.setInt(1, this.bookId);
                    ps.executeUpdate();
                }
            }

            updateMaster(conn, masterId);

            conn.commit(); 
            JOptionPane.showMessageDialog(this, "Check-in Successful!");
            this.dispose();

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Save Error: " + e.getMessage());
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
    }

    private void updateMaster(Connection conn, int mId) throws SQLException {
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

        String sqlCheck = "SELECT COUNT(*) FROM loan_details WHERE loan_master_id = ? AND return_date IS NULL";
        boolean isAllReturned = false;
        try (PreparedStatement ps = conn.prepareStatement(sqlCheck)) {
            ps.setInt(1, mId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) isAllReturned = (rs.getInt(1) == 0);
        }

        String masterStatus = isAllReturned ? "Completed" : "Borrowing";

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