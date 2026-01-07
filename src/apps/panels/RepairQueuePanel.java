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
import models.ConnectDB; // ⚠ đổi package nếu khác

public class RepairQueuePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable table;
	private DefaultTableModel tableModel;

	private JPanel panelFilter;
	private JScrollPane scrollPane;

	private JTextField txtTitle;
	private JTextField txtAuthor;
	private JTextField txtCategory;

	public RepairQueuePanel() {
		setLayout(new BorderLayout());
		initUI();
//		loadTableData();
	}

	// ================= UI =================
	private void initUI() {

		// ===== NORTH - FILTER =====
		panelFilter = new JPanel(null);
		panelFilter.setPreferredSize(new Dimension(0, 100));

		JLabel lbTitle = new JLabel();
		lbTitle.setBorder(new TitledBorder("Search by title"));
		lbTitle.setBounds(10, 20, 300, 60);
		panelFilter.add(lbTitle);

		txtTitle = new JTextField();
		txtTitle.setBounds(25, 40, 200, 25);
		panelFilter.add(txtTitle);

		JLabel lbAuthor = new JLabel();
		lbAuthor.setBorder(new TitledBorder("Search by author"));
		lbAuthor.setBounds(330, 20, 220, 60);
		panelFilter.add(lbAuthor);

		txtAuthor = new JTextField();
		txtAuthor.setBounds(345, 40, 130, 25);
		panelFilter.add(txtAuthor);

		JLabel lbCategory = new JLabel();
		lbCategory.setBorder(new TitledBorder("Search by category"));
		lbCategory.setBounds(570, 20, 220, 60);
		panelFilter.add(lbCategory);

		txtCategory = new JTextField();
		txtCategory.setBounds(585, 40, 130, 25);
		panelFilter.add(txtCategory);

		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(730, 40, 80, 25);
		btnSearch.addActionListener(e -> loadTableData());
		panelFilter.add(btnSearch);

		add(panelFilter, BorderLayout.NORTH);

		// ===== CENTER - TABLE =====
		String[] columns = { "PHOTO", "ISBN", "TITLE", "AUTHOR", "CATEGORY", "STATUS", "ACTION", "BOOK_ID" };

		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 6; // ACTION
			}
		};

		table = new JTable(tableModel);
		table.setRowHeight(120);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);

		table.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());

		// hide BOOK_ID
		table.getColumnModel().removeColumn(table.getColumnModel().getColumn(7));

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
				int col = table.columnAtPoint(e.getPoint());

				if (col == 6) {
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
			String sql = "SELECT b.id, b.isbn, b.title, a.name AS author, c.name AS category, " + "b.photo "
					+ "FROM book b " + "JOIN author a ON b.author_id = a.id "
					+ "JOIN category c ON b.category_id = c.id " + "WHERE b.status = 'REPAIR' " + "AND b.title LIKE ? "
					+ "AND a.name LIKE ? " + "AND c.name LIKE ?";

			PreparedStatement ps = models.ConnectDB.connection().prepareStatement(sql);
			ps.setString(1, "%" + txtTitle.getText() + "%");
			ps.setString(2, "%" + txtAuthor.getText() + "%");
			ps.setString(3, "%" + txtCategory.getText() + "%");

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				tableModel.addRow(new Object[] { rs.getBytes("photo"), rs.getString("isbn"), rs.getString("title"),
						rs.getString("author"), rs.getString("category"), "REPAIR", "Fix", rs.getInt("id") });
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			models.ConnectDB.disconnect();
		}
	}

	// ================= ACTION =================
	private void confirmFix(int row) {
		int modelRow = table.convertRowIndexToModel(row);
		int bookId = (int) tableModel.getValueAt(modelRow, 7);

		int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận sách đã sửa xong?", "Confirm Repair",
				JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			try {
				// 1️⃣ update status
				PreparedStatement ps1 = models.ConnectDB.connection()
						.prepareStatement("UPDATE book SET status = 'NORMAL' WHERE id = ?");
				ps1.setInt(1, bookId);
				ps1.executeUpdate();

				// 2️⃣ increase available_quantity
				PreparedStatement ps2 = models.ConnectDB.connection()
						.prepareStatement("UPDATE book SET available_quantity = available_quantity + 1 WHERE id = ?");
				ps2.setInt(1, bookId);
				ps2.executeUpdate();

				JOptionPane.showMessageDialog(this, "✔ Đã cập nhật sách");

				loadTableData();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				models.ConnectDB.disconnect();
			}
		}
	}

	// ================= PUBLIC API =================
	// Được gọi từ CheckIn / LoanDetail dialog
	public void addRepairItem(byte[] photo, String isbn, String title, String author, String category, int bookId) {
		tableModel.addRow(new Object[] { photo, isbn, title, author, category, "REPAIR", "Fix", bookId });
	}

}
