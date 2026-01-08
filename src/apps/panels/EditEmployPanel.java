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

public class EditEmployPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField jEmployeeID;
	private JTextField jUsername;
	private JTextField jName;
	private JTextField jPhone;
	private JTextField jAddress;
	private JPasswordField jpasswordField;
	private JDateChooser jBirthday;
	private JComboBox jcomboBoxDepartment;
	private JButton btnNewButton;
	private JButton JCancel;
	private Dialog parentDialog;
	private Map<Integer, Department> departmentMap = new HashMap<>();
	private Map<Integer, Role> roleMap = new HashMap<>();
	private JComboBox jcomboBoxRole;
	private JLabel lblRole;
	private AccountPanel accountPanel;
	private Map<String, Object> data;
	private JTextField jId;

	/**
	 * Create the panel.
	 */
	public EditEmployPanel() {
		setLayout(null);
		// Header Panel
		JPanel headerPanel = new JPanel();
		headerPanel.setBounds(0, 0, 616, 81);
		headerPanel.setBackground(SystemColor.activeCaption);
		headerPanel.setLayout(null);
		JLabel lblTitle = new JLabel("Edit Employee");
		lblTitle.setBounds(149, 26, 171, 28);
		lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitle.setForeground(Color.WHITE);
		headerPanel.add(lblTitle);
		add(headerPanel);

		JLabel lblNewLabel = new JLabel("Employee ID: ");
		lblNewLabel.setBounds(56, 159, 81, 23);
		add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Username(Emali):");
		lblNewLabel_1.setBounds(56, 193, 136, 23);
		add(lblNewLabel_1);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(56, 226, 81, 23);
		add(lblPassword);

		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(56, 259, 81, 23);
		add(lblName);

		JLabel lblPhone = new JLabel("Phone:");
		lblPhone.setBounds(56, 292, 81, 23);
		add(lblPhone);

		JLabel lblBrithday = new JLabel("Birthday");
		lblBrithday.setBounds(56, 325, 81, 23);
		add(lblBrithday);

		JLabel lblDepartment = new JLabel("Department:");
		lblDepartment.setBounds(56, 397, 81, 23);
		add(lblDepartment);

		JLabel lblAddress = new JLabel("Address");
		lblAddress.setBounds(56, 430, 81, 23);
		add(lblAddress);

		jEmployeeID = new JTextField();
		jEmployeeID.setBounds(168, 159, 225, 26);
		add(jEmployeeID);
		jEmployeeID.setColumns(10);

		jUsername = new JTextField();
		jUsername.setColumns(10);
		jUsername.setBounds(167, 193, 226, 26);
		add(jUsername);

		jName = new JTextField();
		jName.setColumns(10);
		jName.setBounds(167, 259, 226, 26);
		add(jName);

		jPhone = new JTextField();
		jPhone.setColumns(10);
		jPhone.setBounds(167, 292, 226, 26);
		add(jPhone);

		jAddress = new JTextField();
		jAddress.setColumns(10);
		jAddress.setBounds(167, 430, 226, 23);
		add(jAddress);

		jBirthday = new JDateChooser();
		jBirthday.setDateFormatString("dd/MM/yyyy");
		jBirthday.setBounds(168, 325, 225, 29);
		add(jBirthday);

		jpasswordField = new JPasswordField();
		jpasswordField.setBounds(168, 227, 225, 26);
		add(jpasswordField);

		jcomboBoxDepartment = new JComboBox();
		jcomboBoxDepartment.setBounds(168, 397, 225, 26);
		add(jcomboBoxDepartment);

		btnNewButton = new JButton("Submit");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_btnNewButton_actionPerformed(e);
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton.setBounds(126, 489, 100, 23);
		add(btnNewButton);

		JCancel = new JButton("Cancel");
		JCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_btnNewButton_1_actionPerformed(e);
			}
		});
		JCancel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		JCancel.setForeground(Color.BLACK);
		JCancel.setBounds(248, 489, 81, 23);
		add(JCancel);

		jcomboBoxRole = new JComboBox();
		jcomboBoxRole.setBounds(168, 364, 225, 26);
		add(jcomboBoxRole);

		lblRole = new JLabel("Role:");
		lblRole.setBounds(56, 364, 81, 23);
		add(lblRole);

		JLabel lblNewLabel_2 = new JLabel("ID");
		lblNewLabel_2.setBounds(56, 129, 46, 14);
		add(lblNewLabel_2);

		jId = new JTextField();
		jId.setEnabled(false);
		jId.setBounds(168, 126, 225, 23);
		add(jId);
		jId.setColumns(10);
		setBounds(0, 0, 464, 566);

	}

	public EditEmployPanel(Dialog dialog, AccountPanel accountPanel, Map<String, Object> data) {
		this();
		this.parentDialog = dialog;
		this.accountPanel = accountPanel;
		this.data = data;
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
		AccountModel accountModel = new AccountModel();
		int id = Integer.parseInt(data.get("id").toString());
		Account account = accountModel.findbyId(id);
		jId.setText(String.valueOf(id));
		jEmployeeID.setText(account.getEmployee_id());
		jUsername.setText(account.getUsername());
		jpasswordField.setText(account.getPassword());
		jName.setText(account.getName());
		jPhone.setText(account.getPhone());
		jBirthday.setDate(account.getBirthday());
		Role selectedRole = roleMap.get(account.getRole_id());
		jcomboBoxRole.setSelectedItem(selectedRole);

		// Lấy Department từ departmentMap bằng department_id
		Department selectedDept = departmentMap.get(account.getDepartment_id());
		jcomboBoxDepartment.setSelectedItem(selectedDept);
		jAddress.setText(account.getAddress());
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
			Account account = new Account();

			account.setId(Integer.parseInt(jId.getText().trim()));
			account.setEmployee_id(jEmployeeID.getText().trim());
			account.setUsername(jUsername.getText().trim());
			account.setPassword(new String(jpasswordField.getPassword()));
			account.setName(jName.getText().trim());
			account.setPhone(jPhone.getText().trim());
			Date utilDate = jBirthday.getDate();
			if (utilDate == null) {
				JOptionPane.showMessageDialog(this, "Please select birthday");
				return;
			}
			account.setBirthday(new java.sql.Date(utilDate.getTime()));

			account.setAddress(jAddress.getText().trim());

			Role role = (Role) jcomboBoxRole.getSelectedItem();
			if (role == null) {
				JOptionPane.showMessageDialog(this, "Please select role");
				return;
			}
			account.setRole_id(role.getId());

			Department dept = (Department) jcomboBoxDepartment.getSelectedItem();
			if (dept == null) {
				JOptionPane.showMessageDialog(this, "Please select department");
				return;
			}
			account.setDepartment_id(dept.getId());

			AccountModel accountModel = new AccountModel();
			if (accountModel.update(account)) {
				JOptionPane.showMessageDialog(this, "Edit success!", "Success", JOptionPane.INFORMATION_MESSAGE);
				if (accountPanel != null) {
					accountPanel.refreshTable();
				}

				// Đóng dialog
				parentDialog.dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Edit failed!", "Error", JOptionPane.ERROR_MESSAGE);
			}
			parentDialog.dispose();
			AccountPanel accountPanel = new AccountPanel();
			setVisible(true);

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void do_btnNewButton_1_actionPerformed(ActionEvent e) {
		parentDialog.dispose();
	}
}