package apps.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import apps.renderers.ImageRenderer;
import models.ConnectDB;

public class LostBookPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable table;
	private DefaultTableModel tableModel;

	private JPanel panelFilter;
	private JScrollPane scrollPane;

	private JTextField textFieldTitle;
	private JTextField textFieldAuthor;
	private JTextField textFieldCategory;
	private JComboBox<String> jcomboBoxRole;
	private JComboBox<String> jcomboBoxRole_1;

	public LostBookPanel() {
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

		JLabel lblTitle = new JLabel();
		lblTitle.setBorder(new TitledBorder(null, "Search by title", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(59, 59, 59)));
		lblTitle.setBounds(0, 15, 394, 59);
		panelFilter.add(lblTitle);

		textFieldTitle = new JTextField();
		textFieldTitle.setBounds(21, 32, 281, 28);
		panelFilter.add(textFieldTitle);

		JButton btnSearchTitle = new JButton("Search");
		btnSearchTitle.setBounds(314, 32, 66, 28);
		btnSearchTitle.addActionListener(e -> loadTableData());
		panelFilter.add(btnSearchTitle);

		JLabel lblAuthor = new JLabel();
		lblAuthor.setBorder(new TitledBorder(null, "Search by Author", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(59, 59, 59)));
		lblAuthor.setBounds(430, 0, 214, 92);
		panelFilter.add(lblAuthor);

		textFieldAuthor = new JTextField();
		textFieldAuthor.setBounds(445, 17, 112, 28);
		panelFilter.add(textFieldAuthor);

		JButton btnSearchAuthor = new JButton("Search");
		btnSearchAuthor.setBounds(565, 17, 66, 28);
		btnSearchAuthor.addActionListener(e -> loadTableData());
		panelFilter.add(btnSearchAuthor);

		JLabel lblCategory = new JLabel();
		lblCategory.setBorder(new TitledBorder(null, "Search by category", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(59, 59, 59)));
		lblCategory.setBounds(677, 0, 214, 92);
		panelFilter.add(lblCategory);

		textFieldCategory = new JTextField();
		textFieldCategory.setBounds(692, 17, 112, 28);
		panelFilter.add(textFieldCategory);

		JButton btnSearchCategory = new JButton("Search");
		btnSearchCategory.setBounds(812, 17, 66, 28);
		btnSearchCategory.addActionListener(e -> loadTableData());
		panelFilter.add(btnSearchCategory);

		jcomboBoxRole = new JComboBox<String>();
		jcomboBoxRole.setPreferredSize(new Dimension(150, 25));
		jcomboBoxRole.setBounds(445, 48, 186, 26);
		panelFilter.add(jcomboBoxRole);

		jcomboBoxRole_1 = new JComboBox<String>();
		jcomboBoxRole_1.setPreferredSize(new Dimension(150, 25));
		jcomboBoxRole_1.setBounds(692, 48, 186, 26);
		panelFilter.add(jcomboBoxRole_1);

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

		// hide BOOK_ID
		table.getColumnModel().removeColumn(table.getColumnModel().getColumn(7));

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
					    WHERE b.status = 'LOST'
					      AND b.title LIKE ?
					      AND a.name LIKE ?
					      AND c.name LIKE ?
					""";

			PreparedStatement ps = ConnectDB.connection().prepareStatement(sql);
			ps.setString(1, "%" + textFieldTitle.getText() + "%");
			ps.setString(2, "%" + textFieldAuthor.getText() + "%");
			ps.setString(3, "%" + textFieldCategory.getText() + "%");

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				tableModel.addRow(new Object[] { rs.getBytes("photo"), rs.getString("isbn"), rs.getString("title"),
						rs.getString("call_number"), rs.getString("author"), rs.getString("category"), "LOST",
						rs.getInt("id") });
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectDB.disconnect();
		}
	}
}
