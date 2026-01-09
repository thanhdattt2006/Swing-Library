package apps.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.mindrot.jbcrypt.BCrypt;

import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;

import apps.panels.AccountPanel.DepartmentComboRenderer;
import apps.panels.AccountPanel.RoleComboRenderer;
import entities.Account;
import entities.Department;
import entities.Role;
import models.AccountModel;
import models.DepartmentModel;
import models.RoleModel;

import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;

public class AddEmployPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField jEmployeeID;
	private JTextField jUsername;
	private JTextField jName;
	private JTextField jPhone;
	private JTextField jAddress;
	private JPasswordField jpasswordField;
	private JDateChooser jdateChooser;
	private JComboBox jcomboBoxDepartment;
	private JButton btnNewButton;
	private JButton JCancel;
	private Dialog parentDialog;
	private Map<Integer, Department> departmentMap = new HashMap<>();
	private Map<Integer, Role> roleMap = new HashMap<>();
	private JComboBox jcomboBoxRole;
	private JLabel lblRole;
	private AccountPanel accountPanel;
	
	/**
	 * Create the panel.
	 */
	public AddEmployPanel() {
		setLayout(null);
		// Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 519, 81);
        headerPanel.setBackground(SystemColor.activeCaption);
        headerPanel.setLayout(null);
        JLabel lblTitle = new JLabel("Add New Employee");
        lblTitle.setBounds(124, 28, 228, 28);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        add(headerPanel);

        
        JLabel lblNewLabel = new JLabel("Employee ID: ");
        lblNewLabel.setBounds(57, 125, 81, 23);
        add(lblNewLabel);
        
        JLabel lblNewLabel_1 = new JLabel("Username(Emali):");
        lblNewLabel_1.setBounds(57, 158, 136, 23);
        add(lblNewLabel_1);
        
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(57, 191, 81, 23);
        add(lblPassword);
        
        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(57, 225, 81, 23);
        add(lblName);
        
        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setBounds(57, 258, 81, 23);
        add(lblPhone);
        
        JLabel lblBrithday = new JLabel("Birthday");
        lblBrithday.setBounds(57, 291, 81, 23);
        add(lblBrithday);
        
        JLabel lblDepartment = new JLabel("Department:");
        lblDepartment.setBounds(57, 363, 81, 23);
        add(lblDepartment);
        
        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setBounds(57, 396, 81, 23);
        add(lblAddress);
        
        jEmployeeID = new JTextField();
        jEmployeeID.setBounds(169, 125, 225, 26);
        add(jEmployeeID);
        jEmployeeID.setColumns(10);
        
        jUsername = new JTextField();
        jUsername.setColumns(10);
        jUsername.setBounds(168, 158, 226, 26);
        add(jUsername);
        
        jName = new JTextField();
        jName.setColumns(10);
        jName.setBounds(168, 225, 226, 26);
        add(jName);
        
        jPhone = new JTextField();
        jPhone.setColumns(10);
        jPhone.setBounds(168, 258, 226, 26);
        add(jPhone);
        
        jAddress = new JTextField();
        jAddress.setColumns(10);
        jAddress.setBounds(168, 396, 226, 26);
        add(jAddress);
        
        jdateChooser = new JDateChooser();
        jdateChooser.setDateFormatString("dd/MM/yyyy");
        jdateChooser.setBounds(169, 291, 225, 29);
        add(jdateChooser);
        
        jpasswordField = new JPasswordField();
        jpasswordField.setBounds(169, 192, 225, 26);
        add(jpasswordField);
        
        jcomboBoxDepartment = new JComboBox();
        jcomboBoxDepartment.setBounds(169, 363, 225, 26);
        add(jcomboBoxDepartment);
        
        btnNewButton = new JButton("Add");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		do_btnNewButton_actionPerformed(e);
        	}
        });
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
        btnNewButton.setBounds(130, 457, 81, 23);
        add(btnNewButton);
        
        JCancel = new JButton("Cancel");
        JCancel.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		do_btnNewButton_1_actionPerformed(e);
        	}
        });
        JCancel.setFont(new Font("Tahoma", Font.PLAIN, 17));
        JCancel.setForeground(Color.BLACK);
        JCancel.setBounds(250, 457, 81, 23);
        add(JCancel);
        
        jcomboBoxRole = new JComboBox();
        jcomboBoxRole.setBounds(169, 330, 225, 26);
        add(jcomboBoxRole);
        
        lblRole = new JLabel("Role:");
        lblRole.setBounds(57, 330, 81, 23);
        add(lblRole);
    }
	public AddEmployPanel(Dialog dialog, AccountPanel accountPanel) {
		  this();
		  this.parentDialog = dialog;
		  this.accountPanel = accountPanel;
		  init();
	}
	private void init() {
		DefaultComboBoxModel<Department> deptModel = new DefaultComboBoxModel<>();
		deptModel.addElement(null); // ALL

		for (Department d : new DepartmentModel().findAll()) {
			deptModel.addElement(d);
			departmentMap.put(d.getId(), d);
		}

		jcomboBoxDepartment.setModel(deptModel);
		jcomboBoxDepartment.setRenderer(new DepartmentComboRenderer());
		
		DefaultComboBoxModel<Role> roleModel = new DefaultComboBoxModel<>();
		roleModel.addElement(null); // nếu cần item rỗng

		for (Role r : new RoleModel().findAll()) {
		    if (!"Admin".equalsIgnoreCase(r.getName())) {
		        roleModel.addElement(r);
		        roleMap.put(r.getId(), r);
		    }
		}

		jcomboBoxRole.setModel(roleModel);
		jcomboBoxRole.setRenderer(new RoleComboRenderer());

	}
	private class RoleComboRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			if (value == null) {
				return super.getListCellRendererComponent(list, "", index, isSelected, cellHasFocus);
			}

			Role r = (Role) value;
			return super.getListCellRendererComponent(list, r.getName(), index, isSelected, cellHasFocus);
		}
	}
	private class DepartmentComboRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			if (value == null) {
				return super.getListCellRendererComponent(list, "", index, isSelected, cellHasFocus);
			}

			Department d = (Department) value;
			return super.getListCellRendererComponent(list, d.getName(), index, isSelected, cellHasFocus);
		}
	}
	
		
	
	
//	public static void main(String[] args) {
//	    SwingUtilities.invokeLater(() -> {
//	        JFrame frame = new JFrame("Test AddEmployPanel");
//	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//	        frame.setContentPane(new AddEmployPanel());
//	        frame.setSize(630, 620); // QUAN TRỌNG
//	        frame.setLocationRelativeTo(null);
//	        frame.setVisible(true);
//	    });
//	}

	protected void do_btnNewButton_actionPerformed(ActionEvent e) {
    try {
        // VALIDATION - KIỂM TRA CÁC TRƯỜNG BẮT BUỘC
        
        // 1. Employee ID
        if (jEmployeeID.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Employee ID must not be empty!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            jEmployeeID.requestFocus();
            return;
        }
        if (jUsername.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Username must not be empty!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            jUsername.requestFocus();
            return;
        }
        
        //  Password
        String password = new String(jpasswordField.getPassword()).trim();
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Password must not be empty!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            jpasswordField.requestFocus();
            return;
        }
        
        // . Name
        if (jName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Name must not be empty!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            jName.requestFocus();
            return;
        }
        
        // . Role
        Role role = (Role) jcomboBoxRole.getSelectedItem();
        if (role == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select a role!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // . Department
        Department dept = (Department) jcomboBoxDepartment.getSelectedItem();
        if (dept == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select a department!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // TẠO ACCOUNT SAU KHI VALIDATION XONG
        Account account = new Account();

        account.setEmployee_id(jEmployeeID.getText().trim());
        account.setUsername(jUsername.getText().trim());
        account.setPassword(BCrypt.hashpw(password, BCrypt.gensalt() )) ;
        //new String(jpasswordField.getPassword()));
        account.setName(jName.getText().trim());
        account.setPhone(jPhone.getText().trim());
        
        // Birthday có thể null
        Date utilDate = jdateChooser.getDate();
        if (utilDate != null) {
            account.setBirthday(new java.sql.Date(utilDate.getTime()));
        }

        account.setAddress(jAddress.getText().trim());
        account.setRole_id(role.getId());
        account.setDepartment_id(dept.getId());

        AccountModel accountModel = new AccountModel();
        if (accountModel.create(account)) {
            JOptionPane.showMessageDialog(this, 
                "Employee added successfully!", 
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            if (accountPanel != null) {
                accountPanel.refreshTable();
            }
            parentDialog.dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to add employee!", 
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            ex.getMessage(), 
            "Exception",
            JOptionPane.ERROR_MESSAGE);
    }
}
	

	protected void do_btnNewButton_1_actionPerformed(ActionEvent e) {
		parentDialog.dispose();
	}
}
