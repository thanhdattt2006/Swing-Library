package apps.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import entities.Loan_Details;
import models.LoanDetailsModel;

public class MyLoansPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable table;
	private DefaultTableModel tableModel;
	private int accountId;

	public MyLoansPanel(int accountId) {
		this.accountId = accountId;
		setLayout(new BorderLayout());
		setBackground(UIManager.getColor("Panel.background"));
		initUI();
		loadData();
	}

	private void initUI() {

		Color mainBg = UIManager.getColor("Panel.background");

		// ===== TOP WRAPPER =====
		JPanel topWrapper = new JPanel();
		topWrapper.setLayout(new BoxLayout(topWrapper, BoxLayout.Y_AXIS));
		topWrapper.setBackground(mainBg);

		// ===== HEADER (GIỐNG MyInforEmployeePanel) =====
		JPanel header = new JPanel(new GridBagLayout());
		header.setBackground(new Color(173, 198, 219));
		header.setPreferredSize(new Dimension(0, 70));

		JLabel lblTitle = new JLabel("My Loans");
		lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
		lblTitle.setForeground(Color.WHITE);

		header.add(lblTitle);
		topWrapper.add(header);

		add(topWrapper, BorderLayout.NORTH);

		// ===== CENTER =====
		JPanel centerWrapper = new JPanel(new BorderLayout());
		centerWrapper.setBackground(mainBg);

		// 👉 tạo khoảng cách giữa header và table
		centerWrapper.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

		initTable();

		JScrollPane scrollPane = new JScrollPane(table);
		centerWrapper.add(scrollPane, BorderLayout.CENTER);

		add(centerWrapper, BorderLayout.CENTER);
	}

	private void initTable() {

		String[] columns = { "ID", "Book Title", "Return Date", "Status", "Late Fee", "Compensation Fee" };

		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // 👈 nhân viên chỉ xem
			}
		};

		table = new JTable(tableModel);
		table.setRowHeight(32);
		table.setFillsViewportHeight(true);
	}

	private void loadData() {

		LoanDetailsModel model = new LoanDetailsModel();
		List<Loan_Details> list = model.findByEmployeeId(accountId);

		tableModel.setRowCount(0);

		for (Loan_Details d : list) {

			String status = d.getStatus() != null ? d.getStatus().getDbValue() : "Unknown";

			String returnDate = (d.getReturn_date() == null) ? "Borrowing" : d.getReturn_date().toString();

			tableModel.addRow(new Object[] { d.getId(), d.getBookTitle(), returnDate, status, d.getLate_fee(),
					d.getCompensation_fee() });
		}
	}
}
