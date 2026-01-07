package apps.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

import entities.*;
import models.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SettingsPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable jtableAccountList;
	private DefaultTableModel tableModel;

	private JComboBox<Role> jcomboBoxRole;
	private JComboBox<Department> jcomboBoxDepartment;

	private Map<Integer, Role> roleMap = new HashMap<>();
	private Map<Integer, Department> departmentMap = new HashMap<>();
	private JButton jbuttonDelete;
	private JButton jbuttonAdd;
	private JButton jbuttonEdit;

	public SettingsPanel() {
		setLayout(new BorderLayout());
		initUI();
		loadComboData();
		loadTableData();
		initRenderer();
	}

	// ================= UI =================
	private void initUI() {

		// ===== NORTH - FILTER =====
		JPanel jpanelFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

		jpanelFilter.add(new JLabel("Role:"));
		jcomboBoxRole = new JComboBox<>();
		jcomboBoxRole.setPreferredSize(new Dimension(150, 25));
		jpanelFilter.add(jcomboBoxRole);

		jpanelFilter.add(new JLabel("Department:"));
		jcomboBoxDepartment = new JComboBox<>();
		jcomboBoxDepartment.setPreferredSize(new Dimension(180, 25));
		jpanelFilter.add(jcomboBoxDepartment);

		JButton jbuttonSearch = new JButton("Search");
		jbuttonSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_jbuttonSearch_actionPerformed(e);
			}
		});
		jpanelFilter.add(jbuttonSearch);

		add(jpanelFilter, BorderLayout.NORTH);

		// ===== CENTER - TABLE =====
		String[] columns = { "ID", "Max borrow_days", "deposit fee per loan", "late fee per day", "lost compensation fee", "damaged compensation fee" };

		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		jtableAccountList = new JTable(tableModel);
		jtableAccountList.setRowHeight(25);

		jtableAccountList.getTableHeader().setReorderingAllowed(false);
		add(new JScrollPane(jtableAccountList), BorderLayout.CENTER);

		// ===== SOUTH - ACTION =====
		JPanel jpanelAction = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

		jbuttonAdd = new JButton("Add");
		jbuttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_button_actionPerformed(e);
			}
		});
		jpanelAction.add(jbuttonAdd);
		jbuttonEdit = new JButton("Edit");
		jbuttonEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_button_1_actionPerformed(e);
			}
		});
		jpanelAction.add(jbuttonEdit);
		jbuttonDelete = new JButton("Delete");
		jbuttonDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_jbuttonDelete_actionPerformed(e);
			}
		});
		jpanelAction.add(jbuttonDelete);

		add(jpanelAction, BorderLayout.SOUTH);
	}

	private void loadComboData() {

		// ===== ROLE =====
		DefaultComboBoxModel<Role> roleModel = new DefaultComboBoxModel<>();
		roleModel.addElement(null); // ALL

		for (Role r : new RoleModel().findAll()) {
			roleModel.addElement(r);
			roleMap.put(r.getId(), r);
		}

		jcomboBoxRole.setModel(roleModel);
		jcomboBoxRole.setRenderer(new RoleComboRenderer());

		// ===== DEPARTMENT =====
		DefaultComboBoxModel<Department> deptModel = new DefaultComboBoxModel<>();
		deptModel.addElement(null); // ALL

		for (Department d : new DepartmentModel().findAll()) {
			deptModel.addElement(d);
			departmentMap.put(d.getId(), d);
		}

		jcomboBoxDepartment.setModel(deptModel);
		jcomboBoxDepartment.setRenderer(new DepartmentComboRenderer());
	}

	private void loadTableData() {
		tableModel.setRowCount(0);

		AccountModel model = new AccountModel();
		for (Account acc : model.findAll()) {

			tableModel.addRow(new Object[] { acc.getId(), acc.getEmployee_id(), acc.getUsername(), acc.getName(),
					acc.getPhone(), acc.getAddress(), acc.getBirthday(), acc.getRole_id(), // ID → renderer xử lý
					acc.getDepartment_id() });
		}
	}

	private void initRenderer() {
		jtableAccountList.getColumnModel().getColumn(7).setCellRenderer(new RoleTableRenderer());

		jtableAccountList.getColumnModel().getColumn(8).setCellRenderer(new DepartmentTableRenderer());
	}

	// ===== TABLE RENDERERS =====
	private class RoleTableRenderer extends DefaultTableCellRenderer {
		@Override
		protected void setValue(Object value) {
			Role r = roleMap.get(value);
			setText(r != null ? r.getName() : "");
		}
	}

	private class DepartmentTableRenderer extends DefaultTableCellRenderer {
		@Override
		protected void setValue(Object value) {
			Department d = departmentMap.get(value);
			setText(d != null ? d.getName() : "");
		}
	}

	public class RoleComboRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			if (value == null) {
				return super.getListCellRendererComponent(list, "ALL", index, isSelected, cellHasFocus);
			}

			Role r = (Role) value;
			return super.getListCellRendererComponent(list, r.getName(), index, isSelected, cellHasFocus);
		}
	}

	public class DepartmentComboRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			if (value == null) {
				return super.getListCellRendererComponent(list, "ALL", index, isSelected, cellHasFocus);
			}

			Department d = (Department) value;
			return super.getListCellRendererComponent(list, d.getName(), index, isSelected, cellHasFocus);
		}
	}
	protected void do_button_actionPerformed(ActionEvent e) {//button add
	}
	public void refreshTable() {
	    loadTableData();
	}
	
	protected void do_button_1_actionPerformed(ActionEvent e) { //button Edit
	   
	}
	protected void do_jbuttonDelete_actionPerformed(ActionEvent e) {
		
	}
	
	protected void do_jbuttonSearch_actionPerformed(ActionEvent e) {
	}
}