package apps.panels;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import entities.Account;
import models.AccountModel;

public class MyInforEmployeePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextField txtEmployeeId;
	private JTextField txtUsername;
	private JTextField txtName;
	private JTextField txtGender;
	private JTextField txtBirthday;
	private JTextField txtPhone;
	private JTextField txtAddress;
	private JTextField txtDepartment;

	private final SimpleDateFormat birthdayFormat = new SimpleDateFormat("dd/MM/yyyy");

	public MyInforEmployeePanel(int accountId) {
		setLayout(new BorderLayout());
		setBackground(UIManager.getColor("Panel.background"));
		initUI();
		loadData(accountId);
	}

	// ================= UI =================
	private void initUI() {

		Color mainBg = UIManager.getColor("Panel.background");

		// ===== TOP WRAPPER =====
		JPanel topWrapper = new JPanel();
		topWrapper.setLayout(new BoxLayout(topWrapper, BoxLayout.Y_AXIS));
		topWrapper.setBackground(mainBg);

		// ===== HEADER =====
		JPanel header = new JPanel(new GridBagLayout());
		header.setBackground(new Color(173, 198, 219));
		header.setPreferredSize(new Dimension(0, 70));

		JLabel lblTitle = new JLabel("My Information");
		lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
		lblTitle.setForeground(Color.WHITE);
		header.add(lblTitle);

		topWrapper.add(header);
		add(topWrapper, BorderLayout.NORTH);

		// ===== CENTER =====
		JPanel centerWrapper = new JPanel(new GridBagLayout());
		centerWrapper.setBackground(mainBg);

		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(12, 20, 12, 20);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		int row = 0;

		addRow(formPanel, gbc, row++, "Employee ID", txtEmployeeId = createField());
		addRow(formPanel, gbc, row++, "Username (Email)", txtUsername = createField());
		addRow(formPanel, gbc, row++, "Name", txtName = createField());
		addRow(formPanel, gbc, row++, "Gender", txtGender = createField());
		addRow(formPanel, gbc, row++, "Birthday", txtBirthday = createField());
		addRow(formPanel, gbc, row++, "Phone", txtPhone = createField());
		addRow(formPanel, gbc, row++, "Department", txtDepartment = createField());
		addRow(formPanel, gbc, row++, "Address", txtAddress = createField());

		centerWrapper.add(formPanel);
		add(centerWrapper, BorderLayout.CENTER);
	}

	private JTextField createField() {
		JTextField tf = new JTextField();
		tf.setPreferredSize(new Dimension(240, 30));
		tf.setEditable(false);
		tf.setBackground(Color.WHITE);
		return tf;
	}

	private void addRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {

		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.weightx = 0;

		JLabel lbl = new JLabel(labelText + ":");
		lbl.setPreferredSize(new Dimension(150, 30));
		panel.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 1;
		panel.add(field, gbc);
	}

	// ================= DATA =================
	private void loadData(int accountId) {

		Account acc = new AccountModel().findbyId(accountId);

		if (acc == null) {
			JOptionPane.showMessageDialog(this, "Account not found!");
			return;
		}

		txtEmployeeId.setText(acc.getEmployee_id());
		txtUsername.setText(acc.getUsername());
		txtName.setText(acc.getName());
		txtGender.setText(acc.getGender());
		txtPhone.setText(acc.getPhone());
		txtAddress.setText(acc.getAddress());
		txtDepartment.setText(acc.getDepartment_name());

		txtBirthday.setText(formatBirthday(acc.getBirthday()));
	}

	private String formatBirthday(Date date) {
		if (date == null) {
			return "";
		}
		return birthdayFormat.format(date);
	}
}
