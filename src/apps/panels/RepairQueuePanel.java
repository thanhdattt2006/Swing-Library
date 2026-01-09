package apps.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import apps.renderers.ImageRenderer;
import models.ConnectDB;
import java.awt.Color;

public class RepairQueuePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable table;
	private DefaultTableModel tableModel;

	private JPanel panelFilter;
	private JScrollPane scrollPane;

	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	public RepairQueuePanel() {
		setLayout(new BorderLayout());
		initUI();
		// loadTableData(); // gọi khi cần
	}

	// ================= UI =================
	private void initUI() {

		// ===== NORTH - FILTER =====
		panelFilter = new JPanel(null);
		panelFilter.setPreferredSize(new Dimension(0, 100));
		add(panelFilter, BorderLayout.NORTH);

		JLabel label = new JLabel("");
		label.setBorder(new TitledBorder(null, "Search by title", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(59, 59, 59)));
		label.setBounds(0, 15, 394, 59);
		panelFilter.add(label);

		JButton jbuttonSearch = new JButton("Search");
		jbuttonSearch.setBounds(314, 32, 66, 28);
		panelFilter.add(jbuttonSearch);

		textField = new JTextField();
		textField.setBounds(21, 32, 281, 28);
		panelFilter.add(textField);

		JLabel label_3 = new JLabel("");
		label_3.setBorder(new TitledBorder(null, "Search by Author", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(59, 59, 59)));
		label_3.setBounds(430, 0, 214, 92);
		panelFilter.add(label_3);

		textField_1 = new JTextField();
		textField_1.setBounds(445, 17, 112, 28);
		panelFilter.add(textField_1);

		JButton jbuttonSearch_1 = new JButton("Search");
		jbuttonSearch_1.setBounds(565, 17, 66, 28);
		panelFilter.add(jbuttonSearch_1);

		JLabel label_3_1 = new JLabel("");
		label_3_1.setBorder(new TitledBorder(null, "Search by category", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(59, 59, 59)));
		label_3_1.setBounds(677, 0, 214, 92);
		panelFilter.add(label_3_1);

		textField_2 = new JTextField();
		textField_2.setBounds(692, 17, 112, 28);
		panelFilter.add(textField_2);

		JButton jbuttonSearch_1_1 = new JButton("Search");
		jbuttonSearch_1_1.setBounds(812, 17, 66, 28);
		panelFilter.add(jbuttonSearch_1_1);

		JComboBox<String> jcomboBoxRole = new JComboBox<String>();
		jcomboBoxRole.setPreferredSize(new Dimension(150, 25));
		jcomboBoxRole.setBounds(445, 48, 186, 26);
		panelFilter.add(jcomboBoxRole);

		JComboBox<String> jcomboBoxRole_1 = new JComboBox<String>();
		jcomboBoxRole_1.setPreferredSize(new Dimension(150, 25));
		jcomboBoxRole_1.setBounds(692, 48, 186, 26);
		panelFilter.add(jcomboBoxRole_1);

		// ===== CENTER - TABLE =====
		String[] columns = { "PHOTO", "ISBN", "TITLE", "CALL NUMBER", "AUTHOR", "CATEGORY", "STATUS", "ACTION",
				"BOOK_ID" };

		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 7; // ACTION
			}
		};

		table = new JTable(tableModel);
		table.setRowHeight(120);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);

		table.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());

		// hide BOOK_ID (index 8)
		table.getColumnModel().removeColumn(table.getColumnModel().getColumn(8));

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
				int col = table.columnAtPoint(e.getPoint());

				if (col == 7) {
					confirmFix(row);
				}
			}
		});

		scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
	}

	// ================= DB =================
	private void loadTableData() {
		tableModel.setRowCount(0);

		try {
			String sql = """
					    SELECT
					        b.id,
					        b.isbn,
					        b.title,
					        b.call_number,
					        a.name AS author,
					        c.name AS category,
					        b.photo
					    FROM book b
					    JOIN author a ON b.author_id = a.id
					    JOIN category c ON b.category_id = c.id
					    WHERE b.status = 'REPAIR'
					      AND b.title LIKE ?
					      AND a.name LIKE ?
					      AND c.name LIKE ?
					""";

			PreparedStatement ps = ConnectDB.connection().prepareStatement(sql);
			ps.setString(1, "%" + textField.getText() + "%");
			ps.setString(2, "%" + textField_1.getText() + "%");
			ps.setString(3, "%" + textField_2.getText() + "%");

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				tableModel.addRow(new Object[] { rs.getBytes("photo"), rs.getString("isbn"), rs.getString("title"),
						rs.getString("call_number"), rs.getString("author"), rs.getString("category"), "REPAIR", "Fix",
						rs.getInt("id") });
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectDB.disconnect();
		}
	}

	// ================= ACTION =================
	private void confirmFix(int row) {
		int modelRow = table.convertRowIndexToModel(row);
		int bookId = (int) tableModel.getValueAt(modelRow, 8);

		int confirm = JOptionPane.showConfirmDialog(this, "Confirm that the book has been corrected?", "Confirm Repair",
				JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			try {
				PreparedStatement ps1 = ConnectDB.connection()
						.prepareStatement("UPDATE book SET status = 'GOOD' WHERE id = ?");
				ps1.setInt(1, bookId);
				ps1.executeUpdate();

				PreparedStatement ps2 = ConnectDB.connection()
						.prepareStatement("UPDATE book SET available_quantity = available_quantity + 1 WHERE id = ?");
				ps2.setInt(1, bookId);
				ps2.executeUpdate();

				JOptionPane.showMessageDialog(this, "Book updated");

				loadTableData();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ConnectDB.disconnect();
			}
		}
	}

	// ================= PUBLIC API =================
	public void addRepairItem(byte[] photo, String isbn, String title, String callNumber, String author,
			String category, int bookId) {
		tableModel.addRow(new Object[] { photo, isbn, title, callNumber, author, category, "REPAIR", "Fix", bookId });
	}
}
