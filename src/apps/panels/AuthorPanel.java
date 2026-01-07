package apps.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import apps.panels.AccountPanel.RoleComboRenderer;
import apps.renderers.ImageRenderer;

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

public class AuthorPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable jtableAuthors;
	private DefaultTableModel tableModel;

	private Map<Integer, Author> AuthorMap = new HashMap<>();
	private JButton jBtnAdd;
	private JButton jBtnEdit;
	private JButton jBtnDelete;
	private JPanel jpanelFilter;
	private JTextField textFieldSearch;
	private JScrollPane scrollPane;
	private JComboBox<Author> jComboboxAuthor;
	
	private List<Author> allAuthors; // cache data


	public AuthorPanel() {
		setLayout(new BorderLayout());
		initUI();
		loadComboData();
		loadAuthorData();
		initAuthorSearch();
		loadTableData();
	}

	// ================= UI =================
	private void initUI() {

		// ===== NORTH - FILTER =====
		jpanelFilter = new JPanel();
		jpanelFilter.setPreferredSize(new Dimension(0, 70));
		jpanelFilter.setLayout(null);

		add(jpanelFilter, BorderLayout.NORTH);
		
		jComboboxAuthor = new JComboBox<Author>();
		jComboboxAuthor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_jComboboxAuthor_actionPerformed(e);
			}
		});
		jComboboxAuthor.setPreferredSize(new Dimension(150, 25));
		jComboboxAuthor.setBounds(176, 24, 183, 26);
		jpanelFilter.add(jComboboxAuthor);
		
		JLabel label = new JLabel("");
		label.setBorder(new TitledBorder(null, "Search by name", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(59, 59, 59)));
		label.setBounds(6, 6, 366, 64);
		jpanelFilter.add(label);
		
		textFieldSearch = new JTextField();
		textFieldSearch.setColumns(10);
		textFieldSearch.setBounds(21, 23, 150, 28);
		jpanelFilter.add(textFieldSearch);

		// ===== CENTER - TABLE =====
		String[] columns = { "PHOTO", "Name", "BIO", "ID"};

		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		jtableAuthors = new JTable(tableModel);

		// 🔹 set chiều cao dòng (ảnh cần cao hơn 25)
		jtableAuthors.setRowHeight(123);
		// chiều dài cột
		TableColumnModel columnModel = jtableAuthors.getColumnModel();
		columnModel.removeColumn(columnModel.getColumn(3));
		columnModel.getColumn(0).setPreferredWidth(106);
		columnModel.getColumn(0).setMinWidth(106);
		columnModel.getColumn(0).setMaxWidth(106);
		columnModel.getColumn(1).setPreferredWidth(200);
		columnModel.getColumn(1).setMinWidth(200);
		columnModel.getColumn(1).setMaxWidth(200);
		columnModel.getColumn(2).setPreferredWidth(300);

		// 🔹 chỉ cho chọn 1 dòng
		jtableAuthors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// ⭐⭐ GẮN ImageRenderer cho cột PHOTO (index = 0)
		jtableAuthors.getColumnModel()
		             .getColumn(0)
		             .setCellRenderer(new ImageRenderer());

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
		DefaultComboBoxModel<Author> autModel = new DefaultComboBoxModel<>();
		autModel.addElement(null); // ALL

		for (Author r : new AuthorsModel().findAll()) {
			autModel.addElement(r);
			AuthorMap.put(r.getId(), r);
		}
		
		jComboboxAuthor.setModel(autModel);
		jComboboxAuthor.setRenderer(new AuthorComboRenderer());
	}
	

	public class AuthorComboRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			if (value == null) {
				return super.getListCellRendererComponent(list, "ALL", index, isSelected, cellHasFocus);
			}

			var r = (Author) value;
			return super.getListCellRendererComponent(list, r.getName(), index, isSelected, cellHasFocus);
		}
	}

	private void loadTableData() {
		tableModel.setRowCount(0);
		

		var authorsModel = new AuthorsModel();
		for (Author aut : authorsModel.findAll()) {

			tableModel.addRow(new Object[] { aut.getPhoto(), aut.getName(), aut.getBio(), aut.getId()});
		}
	}
	
	private void loadTableDataByAuthor(Author author) {

	    tableModel.setRowCount(0);

	    var authorsModel = new AuthorsModel();
	    for (Author aut : authorsModel.findAll()) {

	        // nếu ALL
	        if (author == null) {
	            tableModel.addRow(new Object[]{
	                aut.getPhoto(),
	                aut.getName(),
	                aut.getBio(),
	                aut.getId()
	            });
	        }
	        // nếu chọn author cụ thể
	        else if (aut.getId() == author.getId()) {
	            tableModel.addRow(new Object[]{
	                aut.getPhoto(),
	                aut.getName(),
	                aut.getBio(),
	                aut.getId()
	            });
	        }
	    }
	}


//	// ===== TABLE RENDERERS =====
//	private class RoleTableRenderer extends DefaultTableCellRenderer {
//		@Override
//		protected void setValue(Object value) {
//			Author r = AuthorMap.get(value);
//			setText(r != null ? r.getName() : "");
//		}
//	}


	
	//AddAuthor
	protected void do_button_actionPerformed(ActionEvent e) {
		 // Tạo JDialog
	    JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Add Author", true);
	    
	    // 🔒 KHÓA KHÔNG CHO KÉO GIÃN
	    dialog.setResizable(false);
	    
	    // Tạo JPanel cho form
	    AddAuthorPanel addPanel = new AddAuthorPanel(dialog, this);
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
	    JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Edit Author", true);
	    
	        int selectedRow = jtableAuthors.getSelectedRow();

	        if (selectedRow == -1) {
	            JOptionPane.showMessageDialog(this,
	                    "Please select a row to edit");
	            return;
	        }

	        int modelRow = jtableAuthors.convertRowIndexToModel(selectedRow);

	        int authorId = (int) tableModel.getValueAt(modelRow, 3);
	        
//			String username = jtableData.getValueAt(selectedRow, 1).toString();
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("id", authorId);
			  // Tạo JPanel cho form
			EditAuthorPanel editPn = new EditAuthorPanel(data, dialog, this);
			
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
			 AuthorsModel autMd = new AuthorsModel();
        
        if (value == JOptionPane.YES_OPTION) {
//        	int selectedRow = jtableAuthors.getSelectedRow();
        	int modelRow = jtableAuthors.convertRowIndexToModel(selectedRow);
 	        int id = (int) tableModel.getValueAt(modelRow, 3);

            if (autMd.delete(id)) {
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
	
	protected void do_jComboboxAuthor_actionPerformed(ActionEvent e) {
		Object selected = jComboboxAuthor.getSelectedItem();
		
		if (selected == null) {
	        loadTableData(); // ALL
	    } else {
	        loadTableDataByAuthor((Author) selected);
	    }
	}
	
	private void initAuthorSearch() {

		textFieldSearch.getDocument().addDocumentListener(new DocumentListener() {

	        private void filter() {
	            String keyword = textFieldSearch.getText().trim().toLowerCase();

	            jComboboxAuthor.removeAllItems();

	            // luôn có ALL
	            jComboboxAuthor.addItem(null);

	            for (Author a : allAuthors) {
	                if (a.getName().toLowerCase().contains(keyword)) {
	                	jComboboxAuthor.addItem(a);
	                }
	            }

	            // tự sổ combobox nếu có kết quả
	            if (jComboboxAuthor.getItemCount() > 1) {
	            	jComboboxAuthor.setPopupVisible(true);
	            } else {
	            	jComboboxAuthor.setPopupVisible(false);
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

	
	private void loadAuthorData() {
	    AuthorsModel model = new AuthorsModel();
	    allAuthors = model.findAll();

	    jComboboxAuthor.removeAllItems();
	    jComboboxAuthor.addItem(null); // ALL

	    for (Author a : allAuthors) {
	    	jComboboxAuthor.addItem(a);
	    }
	}

}