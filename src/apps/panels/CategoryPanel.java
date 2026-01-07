package apps.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;



import java.awt.*;
import java.util.*;
import java.util.List;

import entities.*;
import models.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CategoryPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable jtableAuthors;
	private DefaultTableModel tableModel;

	private Map<Integer, Category> cateMap = new HashMap<>();
	private JButton jBtnAdd;
	private JButton jBtnEdit;
	private JButton jBtnDelete;
	private JPanel jpanelFilter;
	private JTextField jTextFieldSearch;
	private JScrollPane scrollPane;
	
	private List<Category> allCategory; // cache data
	private JComboBox<Category> jcomboBoxCategory;

	public CategoryPanel() {
		setLayout(new BorderLayout());
		initUI();
		loadComboData();
		loadCategoryData();
		initCategorySearch();
		loadTableData();
	}

	// ================= UI =================
	private void initUI() {

		// ===== NORTH - FILTER =====
		jpanelFilter = new JPanel();
		jpanelFilter.setPreferredSize(new Dimension(0, 70));
		jpanelFilter.setLayout(null);

		add(jpanelFilter, BorderLayout.NORTH);
		
		jcomboBoxCategory = new JComboBox<Category>();
		jcomboBoxCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_jcomboBoxRole_actionPerformed(e);
			}
		});
		jcomboBoxCategory.setPreferredSize(new Dimension(150, 25));
		jcomboBoxCategory.setBounds(176, 24, 183, 26);
		jpanelFilter.add(jcomboBoxCategory);
		
		JLabel label_3 = new JLabel("");
		label_3.setBorder(new TitledBorder(null, "Search by name", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(59, 59, 59)));
		label_3.setBounds(6, 6, 366, 64);
		jpanelFilter.add(label_3);
		
		jTextFieldSearch = new JTextField();
		jTextFieldSearch.setColumns(10);
		jTextFieldSearch.setBounds(21, 23, 150, 28);
		jpanelFilter.add(jTextFieldSearch);

		// ===== CENTER - TABLE =====
		String[] columns = { "Name", "DESCRIPTION", "ID"};

		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		jtableAuthors = new JTable(tableModel);

		// 🔹 set chiều cao dòng (ảnh cần cao hơn 25)
		jtableAuthors.setRowHeight(25);
		// chiều dài cột
		TableColumnModel columnModel = jtableAuthors.getColumnModel();
		columnModel.removeColumn(columnModel.getColumn(2));
		columnModel.getColumn(0).setPreferredWidth(206);
		columnModel.getColumn(0).setMinWidth(206);
		columnModel.getColumn(0).setMaxWidth(206);

		// 🔹 chỉ cho chọn 1 dòng
		jtableAuthors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// 🔹 thêm vào layout
		scrollPane = new JScrollPane(jtableAuthors);
		add(scrollPane, BorderLayout.CENTER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(23);

		// ===== SOUTH - ACTION =====
		JPanel jpanelAction = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

		jBtnAdd = new JButton("Add");
		jBtnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_button_actionPerformed(e);
			}
		});
		jpanelAction.add(jBtnAdd);
		
		jBtnEdit = new JButton("Edit");
		jBtnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_btnEdit_actionPerformed(e);
			}
		});
		jpanelAction.add(jBtnEdit);
		
		jBtnDelete = new JButton("Delete");
		jBtnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_jBtnDelete_actionPerformed(e);
			}
		});
		jpanelAction.add(jBtnDelete);

		add(jpanelAction, BorderLayout.SOUTH);
	}
	
	

	private void loadComboData() {

		// ===== ROLE =====
		DefaultComboBoxModel<Category> cateModel = new DefaultComboBoxModel<>();
		cateModel.addElement(null); // ALL

		for (Category r : new CategoriesModel().findAll()) {
			cateModel.addElement(r);
			cateMap.put(r.getId(), r);
		}
		jcomboBoxCategory.setModel(cateModel);
		jcomboBoxCategory.setRenderer(new CateComboRenderer());

	}

	private void loadTableData() {
		tableModel.setRowCount(0);
		

		var catMd = new CategoriesModel();
		for (Category cate : catMd.findAll()) {

			tableModel.addRow(new Object[] { cate.getName(), cate.getDescription(), cate.getId()});
		}
	}
	
	private void loadTableDataByCategory(Category category) {

	    tableModel.setRowCount(0);

	    var cateMd = new CategoriesModel();
	    for (Category cate : cateMd.findAll()) {

	        // nếu ALL
	        if (category == null) {
	            tableModel.addRow(new Object[]{
	            		cate.getName(),
	            		cate.getDescription(),
	            		cate.getId()
	            });
	        }
	        // nếu chọn cate cụ thể
	        else if (cate.getId() == category.getId()) {
	            tableModel.addRow(new Object[]{
	            		cate.getName(),
	            		cate.getDescription(),
	            		cate.getId()
	            });
	        }
	    }
	}


	public class CateComboRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			if (value == null) {
				return super.getListCellRendererComponent(list, "ALL", index, isSelected, cellHasFocus);
			}

			var r = (Category) value;
			return super.getListCellRendererComponent(list, r.getName(), index, isSelected, cellHasFocus);
		}
	}

	
	//AddAuthor
	protected void do_button_actionPerformed(ActionEvent e) {
		 // Tạo JDialog
	    JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Add Category", true);
	    
	    // 🔒 KHÓA KHÔNG CHO KÉO GIÃN
	    dialog.setResizable(false);
	    
	    // Tạo JPanel cho form
	    var addPanel = new AddCategoryPanel(dialog, this);
	    ////
	    // Thêm panel vào dialog
	    dialog.setContentPane(addPanel);
	    
	    // Cài đặt dialog
//	    dialog.setSize(630, 620);
	    dialog.setSize(530, 350);
	    dialog.setLocationRelativeTo(this); // Hiển thị giữa màn hình
	    dialog.setVisible(true);

	    
	}
	
	//EditButton
	protected void do_btnEdit_actionPerformed(ActionEvent e) {
		 // Tạo JDialog
	    JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Edit Category", true);
	    
	        int selectedRow = jtableAuthors.getSelectedRow();

	        if (selectedRow == -1) {
	            JOptionPane.showMessageDialog(this,
	                    "Please select a row to edit");
	            return;
	        }

	        int modelRow = jtableAuthors.convertRowIndexToModel(selectedRow);

	        int cateId = (int) tableModel.getValueAt(modelRow, 2);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("id", cateId);
			  // Tạo JPanel cho form
			var editPn = new EditCategoryPanel(data, dialog, this);
			
			 // Thêm panel vào dialog
		    dialog.setContentPane(editPn);
		    
		    // Cài đặt dialog
		    dialog.setSize(530, 350);
		    dialog.setLocationRelativeTo(this); // Hiển thị giữa màn hình
		    dialog.setVisible(true);
	}
	
	//reloadTable
	public void reloadTable() {
	    loadTableData();
	}
	
	//button-delete
	protected void do_jBtnDelete_actionPerformed(ActionEvent e) {
		try {
			int selectedRow = jtableAuthors.getSelectedRow();

	        if (selectedRow == -1) {
	            JOptionPane.showMessageDialog(this,
	                    "Please select a row to delete");
	            return;
	        }
			 int value = JOptionPane.showConfirmDialog(null, "Are you sure to delete?", "Delete", JOptionPane.YES_NO_OPTION);
			 var catMd = new CategoriesModel();
        
        if (value == JOptionPane.YES_OPTION) {
        	int modelRow = jtableAuthors.convertRowIndexToModel(selectedRow);
 	        int id = (int) tableModel.getValueAt(modelRow, 2);

            if (catMd.delete(id)) {
            	reloadTable();
                JOptionPane.showMessageDialog(null, "Success", "Delete Author", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed", "Delete Author", JOptionPane.ERROR_MESSAGE);
            }

        } else {
        	reloadTable();
        }

	    } catch (Exception e2) {
	        JOptionPane.showMessageDialog(null, "Failed", "Delete", JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	//search
	protected void do_jComboboxAuthor_actionPerformed(ActionEvent e) {
		Object selected = jcomboBoxCategory.getSelectedItem();
		
		if (selected == null) {
	        loadTableData(); // ALL
	    } else {
	    	loadTableDataByCategory((Category) selected);
	    }
	    
	    
	}
	
	private void initCategorySearch() {

		jTextFieldSearch.getDocument().addDocumentListener(new DocumentListener() {

	        private void filter() {
	            String keyword = jTextFieldSearch.getText().trim().toLowerCase();

	            jcomboBoxCategory.removeAllItems();

	            // luôn có ALL
	            jcomboBoxCategory.addItem(null);

	            for (Category a : allCategory) {
	                if (a.getName().toLowerCase().contains(keyword)) {
	                	jcomboBoxCategory.addItem(a);
	                }
	            }

	            // tự sổ combobox nếu có kết quả
	            if (jcomboBoxCategory.getItemCount() > 1) {
	            	jcomboBoxCategory.setPopupVisible(true);
	            } else {
	            	jcomboBoxCategory.setPopupVisible(false);
	            }
	        }

	        @Override
	        public void insertUpdate(DocumentEvent e) {
	            filter();
	        }

	        @Override
	        public void removeUpdate(DocumentEvent e) {
	            filter();
	        }

	        @Override
	        public void changedUpdate(DocumentEvent e) {
	            filter();
	        }
	    });
	}
	
	private void loadCategoryData() {
	    var model = new CategoriesModel();
	    allCategory = model.findAll();

	    jcomboBoxCategory.removeAllItems();
	    jcomboBoxCategory.addItem(null); // ALL

	    for (Category a : allCategory) {
	    	jcomboBoxCategory.addItem(a);
	    }
	}
	
	protected void do_jcomboBoxRole_actionPerformed(ActionEvent e) {
		Object selected = jcomboBoxCategory.getSelectedItem();
		
		if (selected == null) {
	        loadTableData(); // ALL
	    } else {
	        loadTableDataByCategory((Category) selected);
	    }
	}
}