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
import java.text.DecimalFormat;
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

    public CheckOutPanel() {
        setLayout(new BorderLayout(10, 10));

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
                
                lblTotalAmount = new JLabel("$");
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
                if (cartModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "The shopping cart is empty!");
                    return;
                }
                
                String employeeIdStr = jtextFieldEmployeeID.getText().trim();
                if (employeeIdStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please find and select the employee (borrower) first!");
                    return;
                }

                int employeeId = Integer.parseInt(employeeIdStr); 
                double totalFee = 0;
                
                try {
                    String totalStr = lblTotalAmount.getText().replace("$", "").trim();
                    totalFee = Double.parseDouble(totalStr.replace(",", "."));
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
                    LocalDate dueDate = loanDate.plusDays(14); 
                    
                    psMaster.setInt(1, employeeId);
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
                    
                    JOptionPane.showMessageDialog(null, "Check Out thành công! Mã phiếu: " + masterId);
                    
                    cartModel.setRowCount(0); 
                    lblTotalAmount.setText("0.0 $");
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
                    JOptionPane.showMessageDialog(null, "Lỗi Check Out: " + ex.getMessage());
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
        });
        
        panelButtons.add(btnRemove);
        panelBottom.add(panelButtons, BorderLayout.SOUTH);

        add(panelBottom, BorderLayout.SOUTH);
        
        JPanel panel_1 = new JPanel();
        panelBottom.add(panel_1, BorderLayout.NORTH);
        panel_1.setLayout(new BorderLayout(0, 0));
        
        JPanel panel_2 = new JPanel();
        FlowLayout flowLayout_1 = (FlowLayout) panel_2.getLayout();
        flowLayout_1.setAlignment(FlowLayout.LEFT);
        panel_1.add(panel_2);
        
        JLabel lblNewLabel_1 = new JLabel("Employee ID");
        panel_2.add(lblNewLabel_1);
        
        jtextFieldEmployeeID = new JTextField();
        panel_2.add(jtextFieldEmployeeID);
        jtextFieldEmployeeID.setColumns(10);
        
        JLabel lblNewLabel_2 = new JLabel("Username");
        panel_2.add(lblNewLabel_2);
        
        jtextFieldUsername = new JTextField();
        panel_2.add(jtextFieldUsername);
        jtextFieldUsername.setColumns(10);
        
        JLabel lblNewLabel_3 = new JLabel("Name");
        panel_2.add(lblNewLabel_3);
        
        jtextFieldName = new JTextField();
        panel_2.add(jtextFieldName);
        jtextFieldName.setColumns(10);
        
        JLabel lblNewLabel_4 = new JLabel("Department");
        panel_2.add(lblNewLabel_4);
        
        jtextFieldDepartment = new JTextField();
        panel_2.add(jtextFieldDepartment);
        jtextFieldDepartment.setColumns(10);
        
        JPanel panel_3 = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel_3.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        panel_1.add(panel_3, BorderLayout.NORTH);
        
        JLabel lblNewLabel_5 = new JLabel("Employee ID / Username");
        panel_3.add(lblNewLabel_5);
        
        jtextFieldFindEmployee = new JTextField();
        panel_3.add(jtextFieldFindEmployee);
        jtextFieldFindEmployee.setColumns(10);
        
        JButton jbuttonSearch = new JButton("Search");
        jbuttonSearch.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		do_jbuttonSearch_actionPerformed(e);
        	}
        });
        panel_3.add(jbuttonSearch);

        btnRemove.addActionListener(e -> {
            int selectedRow = tableCart.getSelectedRow();
            if (selectedRow != -1) {
                cartModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Choose one book to delete!");
                
                JPanel panel = new JPanel();
                panel.setBackground(SystemColor.activeCaption);
                add(panel, BorderLayout.NORTH);
                
                JLabel lblNewLabel = new JLabel("Check Out");
                lblNewLabel.setFont(new Font("Arial", Font.BOLD, 26));
                lblNewLabel.setForeground(SystemColor.text);
                panel.add(lblNewLabel);
            }
        });
    }
    
    public void addBookToCart(int id, String title, String author, String category, double depositFee) {
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            int existingId = Integer.parseInt(cartModel.getValueAt(i, 0).toString());
            if (existingId == id) {
                JOptionPane.showMessageDialog(this, "This book is already in the cart!");
                return;
            }
        }

        cartModel.addRow(new Object[]{id, title, author, category, depositFee});
        tableCart.repaint(); 
        this.revalidate(); 
        this.repaint();
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
            jtextFieldEmployeeID.setText(String.valueOf(acc.getEmployee_id()));
            jtextFieldUsername.setText(acc.getUsername());
            jtextFieldName.setText(acc.getName());
            jtextFieldDepartment.setText(acc.getDepartment_name());
        } else {
            JOptionPane.showMessageDialog(this, "Not found employee with: " + keyword);
            jtextFieldEmployeeID.setText("");
            jtextFieldUsername.setText("");
            jtextFieldName.setText("");
            jtextFieldDepartment.setText("");
        }
    }
}