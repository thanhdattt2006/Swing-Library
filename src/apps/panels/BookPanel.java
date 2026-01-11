package apps.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import apps.JFrameMain;
import apps.renderers.ImageRenderer;
import apps.renderers.MultiLineTableCellRenderer;

import java.awt.*;
import java.util.*;

import entities.*;
import models.*;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;

public class BookPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable jtableBooks;
	private DefaultTableModel tableModel;

	private JPanel jpanelFilter;
	private JScrollPane scrollPane;
	private JButton jBtnCheckOut;
	private JPanel jpanelAction;
	private JComboBox<String> jcomboBoxRole;
	private JComboBox<String> jcomboBoxDepartment;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JFrameMain mainFrame;

	public BookPanel(JFrameMain mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new BorderLayout());
		initUI();
		loadTableData();
	}

	public BookPanel() {
		setLayout(new BorderLayout());
		initUI();
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
		label.setBorder(new TitledBorder(null, "Search by title", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(59, 59, 59)));
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
		label_3.setBorder(new TitledBorder(null, "Search by Author", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(59, 59, 59)));
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
		label_3_1.setBorder(new TitledBorder(null, "Search by category", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(59, 59, 59)));
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
		String[] columns = { "PHOTO", "ISBN", "TITLE", "CALL NUMBER", "AUTHOR", "CATEGORY", "DESCRIPTION",  "PRICE",
				"PUBLICATION YEAR", "STOCK", "AVAILABLE", "ID" };

		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		jtableBooks = new JTable(tableModel);
		jtableBooks.setRowHeight(123);
		jtableBooks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		TableColumnModel columnModel = jtableBooks.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(126);
		columnModel.getColumn(0).setMinWidth(126);
		columnModel.getColumn(0).setMaxWidth(126);
		columnModel.getColumn(1).setPreferredWidth(100);
		columnModel.getColumn(1).setMinWidth(95);
		columnModel.getColumn(1).setMaxWidth(95);
		columnModel.getColumn(2).setPreferredWidth(100);
		columnModel.getColumn(2).setMinWidth(135);
		columnModel.getColumn(2).setMaxWidth(135);
		columnModel.getColumn(3).setPreferredWidth(100);
		columnModel.getColumn(3).setMinWidth(110);
		columnModel.getColumn(3).setMaxWidth(110);
		columnModel.getColumn(7).setPreferredWidth(15);
		columnModel.getColumn(7).setMinWidth(85);
		columnModel.getColumn(7).setMaxWidth(85);
		columnModel.getColumn(5).setPreferredWidth(125);
		columnModel.getColumn(5).setMinWidth(155);
		columnModel.getColumn(5).setMaxWidth(155);
		columnModel.getColumn(4).setPreferredWidth(165);
		columnModel.getColumn(4).setMinWidth(165);
		columnModel.getColumn(4).setMaxWidth(165);
		columnModel.getColumn(8).setPreferredWidth(130);
		columnModel.getColumn(8).setMinWidth(120);
		columnModel.getColumn(8).setMaxWidth(120);
		columnModel.getColumn(9).setPreferredWidth(100);
		columnModel.getColumn(9).setMinWidth(60);
		columnModel.getColumn(9).setMaxWidth(60);
		columnModel.getColumn(10).setPreferredWidth(10);
		columnModel.getColumn(10).setMinWidth(85);
		columnModel.getColumn(10).setMaxWidth(85);
		columnModel.getColumn(6).setCellRenderer(new MultiLineTableCellRenderer());
		columnModel.removeColumn(columnModel.getColumn(11));
		jtableBooks.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());
		scrollPane = new JScrollPane(jtableBooks);
		scrollPane.getVerticalScrollBar().setUnitIncrement(23);
		add(scrollPane, BorderLayout.CENTER);

		// ===== SOUTH - ACTION =====
		jpanelAction = new JPanel(new BorderLayout());

		// ===== ROW 1: MAIN ACTION =====
		JPanel panelMainAction = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

		jBtnCheckOut = new JButton("CheckOut");
		jBtnCheckOut.addActionListener(this::doCheckOut);
		panelMainAction.add(jBtnCheckOut);

		JButton button = new JButton("Add");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_button_actionPerformed(e);
			}
		});
		panelMainAction.add(button);
		JButton button_1 = new JButton("Edit");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_button_1_actionPerformed(e);
			}
		});
		panelMainAction.add(button_1);
		JButton button_2 = new JButton("Delete");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_button_2_actionPerformed(e);
			}
		});
		panelMainAction.add(button_2);

		// ===== ROW 2: STATUS ACTION =====
		JPanel panelStatusAction = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

		JButton btnRepairQueue = new JButton("Repair Queue");
		btnRepairQueue.addActionListener(e -> {
			RepairQueuePanel panel = new RepairQueuePanel();
			mainFrame.showCustomPanel("REPAIR_QUEUE", panel);
		});

		panelStatusAction.add(btnRepairQueue);

		JButton btnDamagedList = new JButton("Damaged List");
		btnDamagedList.addActionListener(e -> {
			DamagedBookPanel panel = new DamagedBookPanel();
			mainFrame.showCustomPanel("DAMAGED", panel);
		});

		panelStatusAction.add(btnDamagedList);

		JButton btnLostList = new JButton("Lost List");
		btnLostList.addActionListener(e -> {
			LostBookPanel panel = new LostBookPanel();
			mainFrame.showCustomPanel("LOST", panel);
		});

		panelStatusAction.add(btnLostList);

		// ===== ADD TO ACTION PANEL =====
		jpanelAction.add(panelMainAction, BorderLayout.NORTH);
		jpanelAction.add(panelStatusAction, BorderLayout.SOUTH);

		add(jpanelAction, BorderLayout.SOUTH);
	}

	// ================= DATA =================
	private void loadTableData() {
		tableModel.setRowCount(0);

		BooksModel booksModel = new BooksModel();
		for (Book book : booksModel.findAll()) {
			tableModel.addRow(new Object[] { book.getPhoto(), book.getIsbn(), book.getTitle(), book.getCall_number(),
					  book.getAuthor_name(), book.getCategory_name(), book.getDescription(), book.getPrice(),
					book.getPublication_year(), book.getStock(), book.getAvailable_quantity(), 
					book.getId() });
		}
	}

	// ================= ACTIONS =================
	private void doCheckOut(ActionEvent e) {
		int selectedRow = jtableBooks.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book");
            return;
        }
        int modelRow = jtableBooks.convertRowIndexToModel(selectedRow);
        try {
            int bookId = Integer.parseInt(tableModel.getValueAt(modelRow, 11).toString()); 
            String title = tableModel.getValueAt(modelRow, 2).toString();   
            String author = tableModel.getValueAt(modelRow, 6).toString();  
            String category = tableModel.getValueAt(modelRow, 7).toString(); 
            
            double price = 0;
            Object priceObj = tableModel.getValueAt(modelRow, 5);
            if (priceObj != null) {
                price = Double.parseDouble(priceObj.toString());
            }

            if (JFrameMain.checkOutPanel != null) {
                JFrameMain.checkOutPanel.addBookToCart(bookId, title, author, category, price);
                this.mainFrame.showCheckOutPanel(); 
            } else {
                System.out.println("Error: JFrameMain.checkOutPanel not found!");
                JOptionPane.showMessageDialog(this, "System Error: Not found.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

	// ================= ROLE SUPPORT =================
	public void hideActionPanel() {
		if (jpanelAction != null) {
			jpanelAction.setVisible(false);
		}
	}
	
	//reloadTable
	public void reloadTable() {
	    loadTableData();
	}
	
	//add-btn
	protected void do_button_actionPerformed(ActionEvent e) {                                                           
	    JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Add Book", true);
	    dialog.setResizable(false);
	    var addPanel = new AddBookPanel(dialog, this);
	    dialog.setContentPane(addPanel);
	    dialog.setSize(456, 760);
	    dialog.setLocationRelativeTo(this);
	    dialog.setVisible(true);
	}
	
	//edit-btn
	protected void do_button_1_actionPerformed(ActionEvent e) {             
	    JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Edit Book", true);
	    int selectedRow = jtableBooks.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a row to edit");
            return;
        }
        int modelRow = jtableBooks.convertRowIndexToModel(selectedRow);
        int bookId = (int) tableModel.getValueAt(modelRow, 11);
	    dialog.setResizable(false);
	    Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", bookId);
	    var editPn = new EditBookPanel(data, dialog, this);
	    dialog.setContentPane(editPn);
	    dialog.setSize(456, 780);
	    dialog.setLocationRelativeTo(this);
	    dialog.setVisible(true);
	}
	
	//delete-btn
	protected void do_button_2_actionPerformed(ActionEvent e) {
		try {
			int selectedRow = jtableBooks.getSelectedRow();

	        if (selectedRow == -1) {
	            JOptionPane.showMessageDialog(this,
	                    "Please select a row to delete");
	            return;
	        }
			 int value = JOptionPane.showConfirmDialog(null, "Are you sure to delete?", "Delete", JOptionPane.YES_NO_OPTION);
			 var autMd = new BooksModel();
        
        if (value == JOptionPane.YES_OPTION) {
        	int modelRow = jtableBooks.convertRowIndexToModel(selectedRow);
 	        int id = (int) tableModel.getValueAt(modelRow, 11);

            if (autMd.delete(id)) {
            	reloadTable();
                JOptionPane.showMessageDialog(null, "Success", "Delete Book", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed", "Delete Book", JOptionPane.ERROR_MESSAGE);
            }

        } else {
        	reloadTable();
        }

	    } catch (Exception e2) {
	        JOptionPane.showMessageDialog(null, "Failed", "Delete", JOptionPane.ERROR_MESSAGE);
	    }
	}
}
