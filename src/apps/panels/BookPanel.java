package apps.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import apps.JFrameMain;
import apps.renderers.ImageRenderer;

import java.awt.*;
import java.util.*;

import entities.*;
import models.*;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;

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
		String[] columns = { "PHOTO", "ISBN", "TITLE", "CALL NUMBER", "DESCRIPTION", "PRICE", "AUTHOR", "CATEGORY",
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

		// ❌ KHÔNG cho kéo cột
		jtableBooks.getTableHeader().setReorderingAllowed(false);
		jtableBooks.getTableHeader().setResizingAllowed(false);

		TableColumnModel columnModel = jtableBooks.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(106);

		// Ẩn cột ID
		columnModel.removeColumn(columnModel.getColumn(11));

		// Renderer ảnh
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

		panelMainAction.add(new JButton("Add"));
		panelMainAction.add(new JButton("Edit"));
		panelMainAction.add(new JButton("Delete"));

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
					book.getDescription(), book.getPrice(), book.getAuthor_name(), book.getCategory_name(),
					book.getPublication_year(), book.getStock(), book.getAvailable_quantity(), // ✅ NEW
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
		int bookId = (int) tableModel.getValueAt(modelRow, 11);

		JOptionPane.showMessageDialog(this, "Checkout book ID = " + bookId);
	}

	// ================= ROLE SUPPORT =================
	// Dùng cho EMPLOYEE
	public void hideActionPanel() {
		if (jpanelAction != null) {
			jpanelAction.setVisible(false);
		}
	}
}
