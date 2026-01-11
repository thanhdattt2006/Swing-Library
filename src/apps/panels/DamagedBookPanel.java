package apps.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import apps.panels.RepairQueuePanel.AuthorComboRenderer;
import apps.panels.RepairQueuePanel.CateComboRenderer;
import apps.renderers.ButtonEditor;
import apps.renderers.ButtonRenderer;
import apps.renderers.ImageRenderer;
import entities.Author;
import entities.Category;
import entities.Loan_Details;
import models.AuthorsModel;
import models.CategoriesModel;
import models.ConnectDB;
import models.LoanDetailsModel;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DamagedBookPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable table;
	private DefaultTableModel tableModel;

	private JPanel panelFilter;
	private JScrollPane scrollPane;

	private Map<Integer, Author> autMap = new HashMap<>();
	private Map<Integer, Category> cateMap = new HashMap<>();
	private JLabel label_1;
	private JButton jBtnSearch;
	private JTextField jTextFieldSearch;
	private JComboBox<Category> jCoboboxCategory;
	private JComboBox<Author> jComboboxAuthor;
	private JLabel label_2;
	private JButton jbuttonSearch_2;


	public DamagedBookPanel() {
		setLayout(new BorderLayout());
		initUI();
		loadTableData(); 
		loadComboData();
	}

	// ================= UI =================
	private void initUI() {

		// ===== NORTH - FILTER =====
		panelFilter = new JPanel(null);
		panelFilter.setPreferredSize(new Dimension(0, 65));
		add(panelFilter, BorderLayout.NORTH);
		
		label_1 = new JLabel("");
		label_1.setBorder(new TitledBorder(null, "Search by title", TitledBorder.LEADING, TitledBorder.TOP, null,
						new Color(59, 59, 59)));
		label_1.setBounds(0, 6, 394, 59);
		panelFilter.add(label_1);
		
		jBtnSearch = new JButton("Search");
		jBtnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_jTextFieldSearch_actionPerformed(e);
			}
		});
		jBtnSearch.setBounds(314, 23, 66, 28);
		panelFilter.add(jBtnSearch);
		
		jTextFieldSearch = new JTextField();
		jTextFieldSearch.setBounds(21, 23, 281, 28);
		panelFilter.add(jTextFieldSearch);
		jTextFieldSearch.getDocument().addDocumentListener(new DocumentListener() {

		    private void search() {
		        String keyword = jTextFieldSearch.getText().trim();
		        var model = new LoanDetailsModel();
			    List<Loan_Details> loaDt = model.searchByKeyWordDamaged(keyword);

			    tableModel.setRowCount(0);
			    for (Loan_Details b : loaDt) {
			        tableModel.addRow(new Object[] {
			            b.getPhoto(),
			            b.getIsbn(),
			            b.getTitle(),
			            b.getCall_number(),
			            b.getAuthor_name(),
			            b.getCategory_name(),
			            b.getId()
			        });
			    }
		    }

		    @Override
		    public void insertUpdate(DocumentEvent e) {
		        search();
		    }

		    @Override
		    public void removeUpdate(DocumentEvent e) {
		        search();
		    }

		    @Override
		    public void changedUpdate(DocumentEvent e) {
		        search();
		    }
		});
		
		jCoboboxCategory = new JComboBox<Category>();
		jCoboboxCategory.setPreferredSize(new Dimension(180, 25));
		jCoboboxCategory.setBounds(644, 22, 186, 26);
		panelFilter.add(jCoboboxCategory);
		
		jComboboxAuthor = new JComboBox<Author>();
		jComboboxAuthor.setPreferredSize(new Dimension(150, 25));
		jComboboxAuthor.setBounds(449, 22, 183, 26);
		panelFilter.add(jComboboxAuthor);
		
		label_2 = new JLabel("");
		label_2.setBorder(new TitledBorder(null, "Search by Author & Category", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(59, 59, 59)));
		label_2.setBounds(434, 6, 490, 56);
		panelFilter.add(label_2);
		
		jbuttonSearch_2 = new JButton("Search");
		jbuttonSearch_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_jbuttonSearch_2_actionPerformed(e);
			}
		});
		jbuttonSearch_2.setBounds(842, 20, 66, 28);
		panelFilter.add(jbuttonSearch_2);

		// ===== CENTER - TABLE =====
		String[] columns = { "PHOTO", "ISBN", "TITLE", "CALL NUMBER", "AUTHOR", "CATEGORY", "STATUS", "BOOK_ID" };

		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(tableModel);
		table.setRowHeight(120);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());
		table.getColumnModel().removeColumn(table.getColumnModel().getColumn(7));

		scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	//loadCombobox
		private void loadComboData() {

			// ===== ROLE =====
			DefaultComboBoxModel<Author> autModel = new DefaultComboBoxModel<>();
			autModel.addElement(null); // ALL

			for (Author r : new AuthorsModel().findAll()) {
				autModel.addElement(r);
				autMap.put(r.getId(), r);
			}
			
			jComboboxAuthor.setModel(autModel);
			jComboboxAuthor.setRenderer(new AuthorComboRenderer());
			
			// ===== ROLE =====
			DefaultComboBoxModel<Category> cateModel = new DefaultComboBoxModel<>();
			cateModel.addElement(null); // ALL

			for (Category r : new CategoriesModel().findAll()) {
				cateModel.addElement(r);
				cateMap.put(r.getId(), r);
			}
			jCoboboxCategory.setModel(cateModel);
			jCoboboxCategory.setRenderer(new CateComboRenderer());
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

	// ================= DB =================
	private void loadTableData() {
		tableModel.setRowCount(0);
		try {
			var loDtM = new LoanDetailsModel();
			for(Loan_Details bo : loDtM.findAllDamaged()) {
				tableModel.addRow(new Object[] { bo.getPhoto(), bo.getIsbn(), bo.getTitle(),
						bo.getCall_number(), bo.getAuthor_name(), bo.getCategory_name(), bo.getStatus(),
						bo.getId() });
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectDB.disconnect();
		}
	}
	//search-btn
	private void searchBooks() {
		Author author = (Author) jComboboxAuthor.getSelectedItem();
		Category category = (Category) jCoboboxCategory.getSelectedItem();

	    Integer authorId = (author != null) ? author.getId() : null;
	    Integer categoryId = (category != null) ? category.getId() : null;

	    var model = new LoanDetailsModel();
	    List<Loan_Details> loaDt = model.searchByStatusDamaged(authorId, categoryId);

	    tableModel.setRowCount(0);
	    for (Loan_Details b : loaDt) {
	        tableModel.addRow(new Object[] {
	            b.getPhoto(),
	            b.getIsbn(),
	            b.getTitle(),
	            b.getCall_number(),
	            b.getAuthor_name(),
	            b.getCategory_name(),
	            b.getId()
	        });
	    }
	}
	
	protected void do_jbuttonSearch_2_actionPerformed(ActionEvent e) {
		searchBooks();
	}
	
	//searchkeyword-btn
	protected void do_jTextFieldSearch_actionPerformed(ActionEvent e) {
		jTextFieldSearch.getDocument().addDocumentListener(new DocumentListener() {

		    private void search() {
		        String keyword = jTextFieldSearch.getText().trim();
		        var model = new LoanDetailsModel();
			    List<Loan_Details> loaDt = model.searchByKeyWordDamaged(keyword);

			    tableModel.setRowCount(0);
			    for (Loan_Details b : loaDt) {
			        tableModel.addRow(new Object[] {
			            b.getPhoto(),
			            b.getIsbn(),
			            b.getTitle(),
			            b.getCall_number(),
			            b.getAuthor_name(),
			            b.getCategory_name(),
			            b.getId()
			        });
			    }
		    }

		    @Override
		    public void insertUpdate(DocumentEvent e) {
		        search();
		    }

		    @Override
		    public void removeUpdate(DocumentEvent e) {
		        search();
		    }

		    @Override
		    public void changedUpdate(DocumentEvent e) {
		        search();
		    }
		});
	}
}
