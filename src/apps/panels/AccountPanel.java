package apps.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AccountPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable jtableAccountList;
	private DefaultTableModel tableModel;

	public AccountPanel() {
		setLayout(new BorderLayout());
		initUI();
	}

	private void initUI() {

		// ===== NORTH - FILTER =====
		JPanel panelFilter = new JPanel();
		panelFilter.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelFilter.add(new JLabel("Role:"));

		JComboBox<String> jcomboBoxRole = new JComboBox<>();
		jcomboBoxRole.setPreferredSize(new Dimension(40, 25));
		panelFilter.add(jcomboBoxRole);

		add(panelFilter, BorderLayout.NORTH);

		JLabel lblNewLabel = new JLabel("Department:");
		panelFilter.add(lblNewLabel);

		JComboBox jcomboBoxDepartment = new JComboBox();
		jcomboBoxDepartment.setPreferredSize(new Dimension(180, 25));
		panelFilter.add(jcomboBoxDepartment);

		JButton jbuttonSearch = new JButton("Search");
		panelFilter.add(jbuttonSearch);

		// ===== CENTER - TABLE =====
		String[] columns = { "ID", "Employee_Id", "Username", "Password", "Name", "Phone", "Address", "Birthday",
				"Photo", "Role", "Department" };

		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		jtableAccountList = new JTable(tableModel);
		JScrollPane jscrollPane = new JScrollPane(jtableAccountList);

		add(jscrollPane, BorderLayout.CENTER);

		// demo data
		addDemoData();
		JPanel jpanelAction = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

		JButton jbuttonAdd = new JButton("Add");
		JButton jbuttonEdit = new JButton("Edit");
		JButton jbuttonDelete = new JButton("Delete");
		JButton jbuttonRefresh = new JButton("Refresh");

		jpanelAction.add(jbuttonAdd);
		jpanelAction.add(jbuttonEdit);
		jpanelAction.add(jbuttonDelete);
		jpanelAction.add(jbuttonRefresh);

		add(jpanelAction, BorderLayout.SOUTH);

	}

	private void addDemoData() {

		tableModel.addRow(new Object[] { 1, // ID
				"EMP001", "admin@gmail.com", "******", "Nguyen Van Admin", "0909123456", "Ha Noi", "1995-01-01", null,
				"ADMIN", "IT" });

		tableModel.addRow(new Object[] { 2, "EMP002", "thuthu@gmail.com", "******", "Tran Thi Thu Thu", "0911222333",
				"Da Nang", "1997-06-12", null, "LIBRARIAN", "LIBRARY" });

		tableModel.addRow(new Object[] { 3, "EMP003", "nhanvien@gmail.com", "******", "Le Van Nhan Vien", "0988777666",
				"Ho Chi Minh", "2000-03-20", null, "EMPLOYEE", "SERVICE" });
	}

}
