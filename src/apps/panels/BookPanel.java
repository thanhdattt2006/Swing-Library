package apps.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import apps.renderers.ImageRenderer;

import java.awt.*;
import java.util.*;

import entities.*;
import models.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;

public class BookPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable jtableBooks;
	private DefaultTableModel tableModel;

	private JComboBox<Role> jcomboBoxRole;
	private JComboBox<Department> jcomboBoxDepartment;

	private Map<Integer, Role> roleMap = new HashMap<>();
	private Map<Integer, Department> departmentMap = new HashMap<>();
	private JPanel jpanelFilter;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JScrollPane scrollPane;
	private JButton jBtnCheckOut;
	
	private Map<String, Object> data;

	public BookPanel() {
		setLayout(new BorderLayout());
		initUI();
//		loadComboData();
		loadTableData();
	}

	// ================= UI =================
	private void initUI() {

		// ===== NORTH - FILTER =====
		jpanelFilter = new JPanel();
		jpanelFilter.setPreferredSize(new Dimension(0, 100));
						jpanelFilter.setLayout(null);
				
						JLabel label = new JLabel("");
						label.setBounds(6, 21, 394, 59);
						label.setBorder(new TitledBorder(null, "Search by title", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(59, 59, 59)));
						jpanelFilter.add(label);
		jcomboBoxDepartment = new JComboBox<>();
		jcomboBoxDepartment.setBounds(698, 54, 186, 26);
		jcomboBoxDepartment.setPreferredSize(new Dimension(180, 25));
		jpanelFilter.add(jcomboBoxDepartment);
				
						JButton jbuttonSearch = new JButton("Search");
						jbuttonSearch.setBounds(320, 38, 66, 28);
						jpanelFilter.add(jbuttonSearch);
		jcomboBoxRole = new JComboBox<>();
		jcomboBoxRole.setBounds(451, 54, 183, 26);
		jcomboBoxRole.setPreferredSize(new Dimension(150, 25));
		jpanelFilter.add(jcomboBoxRole);

		add(jpanelFilter, BorderLayout.NORTH);
		
		textField = new JTextField();
		textField.setBounds(27, 38, 281, 28);
		jpanelFilter.add(textField);
		textField.setColumns(10);
		
		JLabel label_3 = new JLabel("");
		label_3.setBorder(new TitledBorder(null, "Search by Author", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(59, 59, 59)));
		label_3.setBounds(436, 6, 214, 92);
		jpanelFilter.add(label_3);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(451, 23, 112, 28);
		jpanelFilter.add(textField_1);
		
		JButton jbuttonSearch_1 = new JButton("Search");
		jbuttonSearch_1.setBounds(571, 23, 66, 28);
		jpanelFilter.add(jbuttonSearch_1);
		
		JLabel label_3_1 = new JLabel("");
		label_3_1.setBorder(new TitledBorder(null, "Search by category", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(59, 59, 59)));
		label_3_1.setBounds(683, 6, 214, 92);
		jpanelFilter.add(label_3_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(698, 23, 112, 28);
		jpanelFilter.add(textField_2);
		
		JButton jbuttonSearch_1_1 = new JButton("Search");
		jbuttonSearch_1_1.setBounds(818, 23, 66, 28);
		jpanelFilter.add(jbuttonSearch_1_1);

		// ===== CENTER - TABLE =====
		String[] columns = { "PHOTO", "ISBN", "TITLE", "CALL NUMBER", "DESCRIPTION", "PRICE", "AUTHOR", "CATEGORY", "PUBLICATION YEAR",
				"STOCK", "ID" };

		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		jtableBooks = new JTable(tableModel);
		// 🔹 set chiều cao dòng (ảnh cần cao hơn 25)
		jtableBooks.setRowHeight(123);
		// chiều dài cột
		TableColumnModel columnModel = jtableBooks.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(106);
		columnModel.getColumn(0).setMinWidth(106);
		columnModel.getColumn(0).setMaxWidth(106);
		columnModel.getColumn(1).setPreferredWidth(200);
		columnModel.getColumn(1).setMinWidth(100);
		columnModel.getColumn(1).setMaxWidth(100);
		columnModel.getColumn(2).setPreferredWidth(230);
		columnModel.getColumn(2).setMinWidth(230);
		columnModel.getColumn(2).setMaxWidth(250);
		columnModel.getColumn(3).setPreferredWidth(130);
		columnModel.getColumn(3).setMinWidth(100);
		columnModel.getColumn(3).setMaxWidth(100);
		columnModel.getColumn(4).setPreferredWidth(330);
		columnModel.getColumn(4).setMinWidth(300);
		columnModel.getColumn(4).setMaxWidth(330);
		
		columnModel.getColumn(8).setPreferredWidth(80);
		columnModel.getColumn(8).setMinWidth(75);
		columnModel.getColumn(8).setMaxWidth(80);
		columnModel.getColumn(9).setPreferredWidth(60);
		columnModel.getColumn(9).setMinWidth(55);
		columnModel.getColumn(9).setMaxWidth(60);
		
		columnModel.removeColumn(columnModel.getColumn(10));

		// 🔹 chỉ cho chọn 1 dòng
		jtableBooks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// ⭐⭐ GẮN ImageRenderer cho cột PHOTO (index = 0)
		jtableBooks.getColumnModel()
		             .getColumn(0)
		             .setCellRenderer(new ImageRenderer());

		// 🔹 thêm vào layout
		scrollPane = new JScrollPane(jtableBooks);
		add(scrollPane, BorderLayout.CENTER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(23);
		// ===== SOUTH - ACTION =====
		JPanel jpanelAction = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

		JButton button = new JButton("Add");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_button_actionPerformed(e);
			}
		});
		
		jBtnCheckOut = new JButton("CheckOut");
		jBtnCheckOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_jBtnCheckOut_actionPerformed(e);
			}
		});
		jpanelAction.add(jBtnCheckOut);
		jpanelAction.add(button);
		JButton button_1 = new JButton("Edit");
		jpanelAction.add(button_1);
		jpanelAction.add(new JButton("Delete"));
		jpanelAction.add(new JButton("Refresh"));

		add(jpanelAction, BorderLayout.SOUTH);
	}

//	private void loadComboData() {
//
//		// ===== ROLE =====
//		DefaultComboBoxModel<Role> roleModel = new DefaultComboBoxModel<>();
//		roleModel.addElement(null); // ALL
//
//		for (Role r : new RoleModel().findAll()) {
//			roleModel.addElement(r);
//			roleMap.put(r.getId(), r);
//		}
//
//		jcomboBoxRole.setModel(roleModel);
//		jcomboBoxRole.setRenderer(new RoleComboRenderer());
//
//		// ===== DEPARTMENT =====
//		DefaultComboBoxModel<Department> deptModel = new DefaultComboBoxModel<>();
//		deptModel.addElement(null); // ALL
//
//		for (Department d : new DepartmentModel().findAll()) {
//			deptModel.addElement(d);
//			departmentMap.put(d.getId(), d);
//		}
//
//		jcomboBoxDepartment.setModel(deptModel);
//		jcomboBoxDepartment.setRenderer(new DepartmentComboRenderer());
//	}

	private void loadTableData() {
		tableModel.setRowCount(0);

		var booksModel = new BooksModel();
		for (Book book : booksModel.findAll()) {

			tableModel.addRow(new Object[] { book.getPhoto(), book.getIsbn(), book.getTitle(), book.getCall_number(), book.getDescription(),
					book.getPrice(), book.getAuthor_name(), book.getCategory_name(), book.getPublication_year(), book.getStock(), book.getId() });
		}
	}

//	public class RoleComboRenderer extends DefaultListCellRenderer {
//		@Override
//		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
//				boolean cellHasFocus) {
//
//			if (value == null) {
//				return super.getListCellRendererComponent(list, "ALL", index, isSelected, cellHasFocus);
//			}
//
//			Role r = (Role) value;
//			return super.getListCellRendererComponent(list, r.getName(), index, isSelected, cellHasFocus);
//		}
//	}

//	public class DepartmentComboRenderer extends DefaultListCellRenderer {
//		@Override
//		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
//				boolean cellHasFocus) {
//
//			if (value == null) {
//				return super.getListCellRendererComponent(list, "ALL", index, isSelected, cellHasFocus);
//			}
//
//			Department d = (Department) value;
//			return super.getListCellRendererComponent(list, d.getName(), index, isSelected, cellHasFocus);
//		}
//	}
	protected void do_button_actionPerformed(ActionEvent e) {
		 // Tạo JDialog
	    JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Thêm Nhân Viên Mới", true);
	    
	    // Tạo JPanel cho form
	    AddEmployPanel addPanel = new AddEmployPanel( );
	    ////
	    // Thêm panel vào dialog
	    dialog.getContentPane().add(addPanel);
	    
	    // Cài đặt dialog
	    dialog.setSize(630, 620);
	    dialog.setLocationRelativeTo(this); // Hiển thị giữa màn hình
	    dialog.setVisible(true);
	}
	public void refreshTable() {
	    loadTableData();
	}
	
	protected void do_jBtnCheckOut_actionPerformed(ActionEvent e) {
		int selectedRow = jtableBooks.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a row to check out");
            return;
        }
        
        int modelRow = jtableBooks.convertRowIndexToModel(selectedRow);

        int bookId = (int) tableModel.getValueAt(modelRow, 10);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", bookId);
		
		
//		var catPn = new CheckOutTest(data);
//		jPanelMain.add(catPn);
//		catPn.setVisible(true); 
	}
	
}