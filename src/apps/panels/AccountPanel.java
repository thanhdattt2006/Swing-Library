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

public class AccountPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable jtableAccountList;
	private DefaultTableModel tableModel;

	private JComboBox<Role> jcomboBoxRole;
	private JComboBox<Department> jcomboBoxDepartment;

	private Map<Integer, Role> roleMap = new HashMap<>();
	private Map<Integer, Department> departmentMap = new HashMap<>();
	private JButton jbuttonDelete;
	private JButton jbuttonRefresh;
	private JButton jbuttonAdd;
	private JButton jbuttonEdit;

	public AccountPanel() {
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
		jpanelFilter.add(jbuttonSearch);

		add(jpanelFilter, BorderLayout.NORTH);

		// ===== CENTER - TABLE =====
		String[] columns = { "ID", "Employee ID", "Username", "Name", "Phone", "Address", "Birthday", "Role",
				"Department" };

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
		jbuttonRefresh = new JButton("Refresh");
		jpanelAction.add(jbuttonRefresh);

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
		 // Tạo JDialog
	    JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Add New Employee", true);
	    
	    // Tạo JPanel cho form
	    AddEmployPanel addPanel = new AddEmployPanel(dialog, this );
	    
	    // Thêm panel vào dialog
	    dialog.getContentPane().add(addPanel);
	    
	    // Cài đặt dialog
	    dialog.setSize(465, 570);
	    dialog.setResizable(false);
	    dialog.setLocationRelativeTo(this); // Hiển thị giữa màn hình
	    dialog.setVisible(true);
	}
	public void refreshTable() {
	    loadTableData();
	}
	
	protected void do_button_1_actionPerformed(ActionEvent e) { //button Edit
	    try {
	        // Kiểm tra xem có chọn hàng không
	        int selectedRow = jtableAccountList.getSelectedRow();
	        if (selectedRow == -1) {
	            throw new Exception("Please select an employee to edit!");
	        }
	        
	        // Lấy ID từ row được chọn
	        int id = Integer.parseInt(jtableAccountList.getValueAt(selectedRow, 0).toString());
	        Map<String, Object> data = new HashMap<String, Object>();
	        data.put("id", id);
	        
	        // Tạo JDialog
	        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Sửa Nhân Viên ", true);
	        
	        // Tạo JPanel cho form
	        EditEmployPanel addPanel = new EditEmployPanel(dialog, this, data);
	        
	        // Thêm panel vào dialog
	        dialog.getContentPane().add(addPanel);
	        
	        // Cài đặt dialog
	        dialog.setSize(511, 670);
	        dialog.setResizable(false);
	        dialog.setLocationRelativeTo(this);
	        dialog.setVisible(true);
	        
	    } catch (Exception ex) {
	        JOptionPane.showMessageDialog(this, 
	            ex.getMessage(), 
	            "Error", 
	            JOptionPane.ERROR_MESSAGE);
	    }
	}
	protected void do_jbuttonDelete_actionPerformed(ActionEvent e) {
		try {
	        int selectedRow = jtableAccountList.getSelectedRow();
	        if (selectedRow == -1) {
	            throw new Exception("Please select an employee to delete!");
	        }
	        
	        // Lấy ID từ row được chọn
	        int id = Integer.parseInt(jtableAccountList.getValueAt(selectedRow, 0).toString());
	        String name = jtableAccountList.getValueAt(selectedRow, 3).toString();
	        
	        // Xác nhận trước khi xóa
	        int confirm = JOptionPane.showConfirmDialog(this, 
	            "Are you sure you want to delete the employee: " + name + "?", 
	            "Confirm Delete", 
	            JOptionPane.YES_NO_OPTION,
	            JOptionPane.WARNING_MESSAGE);
	        
	        if (confirm == JOptionPane.YES_OPTION) {
	            AccountModel accountModel = new AccountModel();
	            if (accountModel.delete(id)) {
	                JOptionPane.showMessageDialog(this, 
	                		"Deleted successfully!",
	                		"Success",

	                    JOptionPane.INFORMATION_MESSAGE);
	                refreshTable(); // Làm mới bảng
	            } else {
	                JOptionPane.showMessageDialog(this, 
	                		"Deletion failed!",
	                		"Error",

	                    JOptionPane.ERROR_MESSAGE);
	            }
	        }
	        
	    } catch (Exception ex) {
	        JOptionPane.showMessageDialog(this, 
	            ex.getMessage(), 
	            "Error", 
	            JOptionPane.ERROR_MESSAGE);
	    }
	}
	
}