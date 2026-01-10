package apps.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import entities.Account;
import models.AccountModel;
import models.ConnectDB;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class CheckOutPanel extends JPanel {
    private JTable tableCart;
    private DefaultTableModel cartModel;
    private JLabel lblTotalAmount;
    private JButton btnPay, btnRemove;
    
    private JTextField jtextFieldEmployeeID;
    private JTextField jtextFieldUsername;
    private JTextField jtextFieldName;
    private JTextField jtextFieldDepartment;
    private JTextField jtextFieldFindEmployee;

    private int currentAccountId = -1; 
    
    private double depositFeePerBook = 5.0; 
    private int maxBorrowDays = 7;

    public CheckOutPanel() {
        loadSettings();

        setLayout(new BorderLayout(10, 10));

        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(SystemColor.activeCaption);
        add(panelHeader, BorderLayout.NORTH);
        
        JLabel lblTitle = new JLabel("Check Out");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setForeground(SystemColor.text);
        panelHeader.add(lblTitle);

        String[] columns = {"Book Id", "Title", "Author", "Category", "Deposit Fee"};
        cartModel = new DefaultTableModel(columns, 0);
        tableCart = new JTable(cartModel);
        tableCart.getTableHeader().setReorderingAllowed(false);
        tableCart.getTableHeader().setResizingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tableCart);
        scrollPane.setPreferredSize(new Dimension(600, 300)); 
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBottom = new JPanel(new BorderLayout());
        panelBottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRemove = new JButton("Delete");
        
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButtons.add(panelTotal);
        JLabel lblText = new JLabel("Total Deposit Fee: ");
        lblText.setFont(new Font("Arial", Font.BOLD, 16));
        
        lblTotalAmount = new JLabel("0.0 $");
        lblTotalAmount.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotalAmount.setForeground(SystemColor.activeCaption);
        
        panelTotal.add(lblText);
        panelTotal.add(lblTotalAmount);
        
        btnPay = new JButton("Confirm");
        btnPay.setBackground(Color.WHITE); 
        btnPay.setForeground(new Color(0, 0, 0));
        panelButtons.add(btnPay); 
        btnPay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performCheckOut();
            }
        });
        
        panelButtons.add(btnRemove); 

        panelBottom.add(panelButtons, BorderLayout.SOUTH);

        JPanel panelInfoWrapper = new JPanel();
        panelBottom.add(panelInfoWrapper, BorderLayout.NORTH);
        panelInfoWrapper.setLayout(new BorderLayout(0, 0));
        
        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInfoWrapper.add(panelSearch, BorderLayout.NORTH);
        
        JLabel lblSearch = new JLabel("Employee ID / Username: ");
        panelSearch.add(lblSearch);
        
        jtextFieldFindEmployee = new JTextField();
        jtextFieldFindEmployee.setColumns(15);
        panelSearch.add(jtextFieldFindEmployee);
        
        JButton jbuttonSearch = new JButton("Search");
        panelSearch.add(jbuttonSearch);

        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInfoWrapper.add(panelInfo, BorderLayout.CENTER);
        
        JLabel lblEmpID = new JLabel("Emp ID:");
        panelInfo.add(lblEmpID);
        jtextFieldEmployeeID = new JTextField(8);
        jtextFieldEmployeeID.setEditable(false); 
        panelInfo.add(jtextFieldEmployeeID);
        
        JLabel lblUser = new JLabel("Username:");
        panelInfo.add(lblUser);
        jtextFieldUsername = new JTextField(10);
        jtextFieldUsername.setEditable(false);
        panelInfo.add(jtextFieldUsername);
        
        JLabel lblName = new JLabel("Name:");
        panelInfo.add(lblName);
        jtextFieldName = new JTextField(10);
        jtextFieldName.setEditable(false);
        panelInfo.add(jtextFieldName);
        
        JLabel lblDept = new JLabel("Dept:");
        panelInfo.add(lblDept);
        jtextFieldDepartment = new JTextField(8);
        jtextFieldDepartment.setEditable(false);
        panelInfo.add(jtextFieldDepartment);
        
        add(panelBottom, BorderLayout.SOUTH);

        jbuttonSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                do_jbuttonSearch_actionPerformed(e);
            }
        });

        btnRemove.addActionListener(e -> {
            int selectedRow = tableCart.getSelectedRow();
            if (selectedRow != -1) {
                cartModel.removeRow(selectedRow);
                updateTotalAmount(); 
            } else {
                JOptionPane.showMessageDialog(this, "Choose one book to delete!");
            }
        });
    }

    private void loadSettings() {
        String sql = "SELECT deposit_fee_per_loan, max_borrow_days FROM settings LIMIT 1";
        
        try (Connection conn = ConnectDB.connection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            if (rs.next()) {
                double fee = rs.getDouble("deposit_fee_per_loan");
                int days = rs.getInt("max_borrow_days");
                
                if(fee > 0) this.depositFeePerBook = fee;
                if(days > 0) this.maxBorrowDays = days; 
            }
        } catch (Exception e) {
            System.out.println("Could not load settings, using defaults (Fee: 5.0, Days: 7)");
        }
    }

    public void addBookToCart(int id, String title, String author, String category, double originalPrice) {
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            int existingId = Integer.parseInt(cartModel.getValueAt(i, 0).toString());
            if (existingId == id) {
                JOptionPane.showMessageDialog(this, "This book is already in the cart!");
                return;
            }
        }
        cartModel.addRow(new Object[]{id, title, author, category, this.depositFeePerBook});
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        double total = 0;
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            Object value = cartModel.getValueAt(i, 4); 
            if (value != null) {
                total += Double.parseDouble(value.toString());
            }
        }
        lblTotalAmount.setText(String.format("%.2f $", total));
    }

    protected void do_jbuttonSearch_actionPerformed(ActionEvent e) {
        String keyword = jtextFieldFindEmployee.getText().trim();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Username or Employee ID!");
            return;
        }

        AccountModel accountModel = new AccountModel();
        Account acc = accountModel.findByUsernameOrEmployeeId(keyword);

        if (acc != null) {
            this.currentAccountId = acc.getId(); 

            jtextFieldEmployeeID.setText(acc.getEmployee_id()); 
            jtextFieldUsername.setText(acc.getUsername());
            jtextFieldName.setText(acc.getName());
            jtextFieldDepartment.setText(acc.getDepartment_name());
        } else {
            JOptionPane.showMessageDialog(this, "Not found employee with: " + keyword);
            this.currentAccountId = -1;
            jtextFieldEmployeeID.setText("");
            jtextFieldUsername.setText("");
            jtextFieldName.setText("");
            jtextFieldDepartment.setText("");
        }
    }

    private void performCheckOut() {
        if (cartModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "The shopping cart is empty!");
            return;
        }
        
        if (currentAccountId == -1) {
            JOptionPane.showMessageDialog(null, "Please find and select the employee (borrower) first!");
            return;
        }

        double totalFee = 0;
        try {
            String totalStr = lblTotalAmount.getText().replace("$", "").replace(",", ".").trim();
            totalFee = Double.parseDouble(totalStr);
        } catch (Exception ex) { totalFee = 0; }

        Connection conn = null;
        PreparedStatement psMaster = null;
        PreparedStatement psDetail = null;
        PreparedStatement psUpdateBook = null;
        ResultSet rsKeys = null;

        try {
            conn = ConnectDB.connection();
            conn.setAutoCommit(false); 

            String sqlMaster = "INSERT INTO loan_master (account_id, borrow_date, due_date, total_deposit_fee, status) VALUES (?, ?, ?, ?, ?)";
            
            psMaster = conn.prepareStatement(sqlMaster, Statement.RETURN_GENERATED_KEYS);
            
            LocalDate loanDate = LocalDate.now();
            
            LocalDate dueDate = loanDate.plusDays(this.maxBorrowDays); 
            
            psMaster.setInt(1, currentAccountId); 
            psMaster.setDate(2, java.sql.Date.valueOf(loanDate));
            psMaster.setDate(3, java.sql.Date.valueOf(dueDate));
            psMaster.setDouble(4, totalFee);
            psMaster.setString(5, "Borrowing"); 
            
            int affectedRows = psMaster.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Create Loan Failed, No lines were added..");
            }
            
            int masterId = 0;
            rsKeys = psMaster.getGeneratedKeys();
            if (rsKeys.next()) {
                masterId = rsKeys.getInt(1);
            } else {
                throw new SQLException("Create Loan Failed, can not catch ID.");
            }

            String sqlDetail = "INSERT INTO loan_details (loan_master_id, book_id, status) VALUES (?, ?, ?)";
            String sqlUpdateBook = "UPDATE book SET available_quantity = available_quantity - 1 WHERE id = ?";

            psDetail = conn.prepareStatement(sqlDetail);
            psUpdateBook = conn.prepareStatement(sqlUpdateBook);

            for (int i = 0; i < cartModel.getRowCount(); i++) {
                int bookId = Integer.parseInt(cartModel.getValueAt(i, 0).toString());
                
                psDetail.setInt(1, masterId);
                psDetail.setInt(2, bookId);
                psDetail.setString(3, "Good"); 
                psDetail.addBatch();

                psUpdateBook.setInt(1, bookId);
                psUpdateBook.addBatch();
            }

            psDetail.executeBatch();
            psUpdateBook.executeBatch();

            conn.commit(); 
            
            JOptionPane.showMessageDialog(null, "Check Out Successfully! Loan ID: " + masterId + "\nDue Date: " + dueDate);
            
            cartModel.setRowCount(0); 
            updateTotalAmount();
            
            currentAccountId = -1;
            jtextFieldEmployeeID.setText("");
            jtextFieldUsername.setText("");
            jtextFieldName.setText("");
            jtextFieldDepartment.setText("");
            jtextFieldFindEmployee.setText("");

        } catch (Exception ex) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error Check Out: " + ex.getMessage());
        } finally {
            try {
                if (rsKeys != null) rsKeys.close();
                if (psMaster != null) psMaster.close();
                if (psDetail != null) psDetail.close();
                if (psUpdateBook != null) psUpdateBook.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}