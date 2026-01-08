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
    private JTextField jtextFieldAccount;
    private JTextField jtextFieldEmployeeID;
    private JTextField jtextFieldUsername;
    private JTextField jtextFieldDepartment;
    private JTextField jtextFieldName;

    public CheckOutPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Check Out Infomation"));

        String[] columns = {"Book Id", "Title", "Author", "Category", "Deposit Fee"};
        cartModel = new DefaultTableModel(columns, 0);
        tableCart = new JTable(cartModel);
        
        JScrollPane scrollPane = new JScrollPane(tableCart);
        scrollPane.setPreferredSize(new Dimension(600, 300)); 
        add(scrollPane, BorderLayout.NORTH);

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
        
                btnPay.addActionListener(e -> {
                    if (cartModel.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(this, "Chưa có sách nào để thanh toán!");
                        return;
                    }
                    JOptionPane.showMessageDialog(this, "Success!");
                    
                    JPanel panel = new JPanel();
                    add(panel, BorderLayout.CENTER);
                    panel.setLayout(null);
                    
                    JLabel lblNewLabel = new JLabel("Employee ID / Username");
                    lblNewLabel.setBounds(6, 12, 134, 16);
                    panel.add(lblNewLabel);
                    
                    jtextFieldAccount = new JTextField();
                    jtextFieldAccount.setBounds(145, 6, 112, 28);
                    panel.add(jtextFieldAccount);
                    jtextFieldAccount.setColumns(10);
                    
                    JButton jButtonSearch = new JButton("Search");
                    jButtonSearch.setBounds(262, 6, 66, 28);
                    jButtonSearch.addActionListener(new ActionListener() {
                    	public void actionPerformed(ActionEvent e) {
                    		do_jButtonSearch_actionPerformed(e);
                    	}
                    });
                    panel.add(jButtonSearch);
                    
                    JLabel lblNewLabel_1 = new JLabel("Employee Id");
                    lblNewLabel_1.setBounds(6, 51, 67, 16);
                    panel.add(lblNewLabel_1);
                    
                    jtextFieldEmployeeID = new JTextField();
                    jtextFieldEmployeeID.setBounds(78, 45, 112, 28);
                    jtextFieldEmployeeID.setEditable(false);
                    panel.add(jtextFieldEmployeeID);
                    jtextFieldEmployeeID.setColumns(10);
                    
                    JLabel lblNewLabel_2 = new JLabel("Username");
                    lblNewLabel_2.setBounds(202, 52, 61, 16);
                    panel.add(lblNewLabel_2);
                    
                    jtextFieldUsername = new JTextField();
                    jtextFieldUsername.setBounds(275, 46, 141, 28);
                    jtextFieldUsername.setEditable(false);
                    panel.add(jtextFieldUsername);
                    jtextFieldUsername.setColumns(10);
                    
                    JLabel lblNewLabel_3 = new JLabel("Department ");
                    lblNewLabel_3.setBounds(205, 85, 67, 16);
                    panel.add(lblNewLabel_3);
                    
                    jtextFieldDepartment = new JTextField();
                    jtextFieldDepartment.setBounds(277, 79, 139, 28);
                    panel.add(jtextFieldDepartment);
                    jtextFieldDepartment.setColumns(10);
                    
                    JLabel lblNewLabel_2_1 = new JLabel("Name");
                    lblNewLabel_2_1.setBounds(6, 85, 55, 16);
                    panel.add(lblNewLabel_2_1);
                    
                    jtextFieldName = new JTextField();
                    jtextFieldName.setBounds(79, 79, 112, 28);
                    jtextFieldName.setEditable(false);
                    jtextFieldName.setColumns(10);
                    panel.add(jtextFieldName);
                    cartModel.setRowCount(0);
                    updateTotal();
                });
        
        panelButtons.add(btnRemove);
        panelBottom.add(panelButtons, BorderLayout.CENTER);

        add(panelBottom, BorderLayout.SOUTH);

        btnRemove.addActionListener(e -> {
            int selectedRow = tableCart.getSelectedRow();
            if (selectedRow != -1) {
                cartModel.removeRow(selectedRow);
                updateTotal();
            } else {
                JOptionPane.showMessageDialog(this, "Choose one book to delete!");
            }
        });

        addSampleData();
    }

    
    private void updateTotal() {
        double total = 0;
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            Object value = cartModel.getValueAt(i, 4);
            if (value instanceof Number) {
                total += ((Number) value).doubleValue();
            }
        }
        DecimalFormat df = new DecimalFormat("#,### $");
        lblTotalAmount.setText(df.format(total));
    }

    public void addBookToCart(String id, String name, String author, String category, double price) {
        for (int i=0; i < cartModel.getRowCount(); i++) {
            if (cartModel.getValueAt(i, 0).equals(id)) {
                JOptionPane.showMessageDialog(this, "This book has already been selected!");
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
	protected void do_jButtonSearch_actionPerformed(ActionEvent e) {
	}
}