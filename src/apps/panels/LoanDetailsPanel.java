package apps.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.List;

import entities.Loan_Details;
import models.LoanDetailsModel;
import utils.TableActionCellEditor;
import utils.TableActionCellRender;
import utils.TableActionEvent;

public class LoanDetailsPanel extends JPanel {

	private JTable table;
	private DefaultTableModel tableModel;
	private int currentMasterId;
	private JLabel lblTitle;

	public LoanDetailsPanel(int masterId) {
		this.currentMasterId = masterId;
		initComponents();
		loadData(masterId);
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		// --- HEADER ---
		JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.CENTER));
		pnlHeader.setBackground(SystemColor.activeCaption);
		lblTitle = new JLabel("Loan Details #" + currentMasterId);
		lblTitle.setForeground(SystemColor.text);
		lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
		pnlHeader.add(lblTitle);
		add(pnlHeader, BorderLayout.NORTH);

		initTable();
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		// --- FOOTER ---
		JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton btnBack = new JButton("Back");
		pnlFooter.add(btnBack);
		add(pnlFooter, BorderLayout.SOUTH);

		btnBack.addActionListener(e -> {
			Window window = SwingUtilities.getWindowAncestor(LoanDetailsPanel.this);
			if (window != null) {
				window.dispose();
			}
		});
	}

	private void initTable() {
		String[] columns = { "ID", "Book Title", "Return Date", "Status", "Late Fee", "Compensation Fee", "Action" };

		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 6;
			}
		};

		table = new JTable(tableModel);
		table.setRowHeight(35);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);

		TableActionEvent event = new TableActionEvent() {
			@Override
			public void onAction(int row) {
				int detailId = (int) table.getValueAt(row, 0);

				String returnDateStr = (String) table.getValueAt(row, 2);

				if (returnDateStr != null && !returnDateStr.trim().isEmpty()) {
					JOptionPane.showMessageDialog(LoanDetailsPanel.this, "This book has already been returned!",
							"Action Denied", JOptionPane.WARNING_MESSAGE);
					return;
				}

				Window parentWindow = SwingUtilities.getWindowAncestor(LoanDetailsPanel.this);

				JFrameCheckIn checkInDialog = new JFrameCheckIn(parentWindow, detailId);

				checkInDialog.setVisible(true);

				loadData(currentMasterId);

			}
		};

		TableColumn actionCol = table.getColumnModel().getColumn(6);
		actionCol.setCellRenderer(new TableActionCellRender());
		actionCol.setCellEditor(new TableActionCellEditor(event));
		actionCol.setMinWidth(100);
	}

	public void loadData(int masterId) {
		this.currentMasterId = masterId;
		lblTitle.setText("Loan Details #" + masterId);

		LoanDetailsModel model = new LoanDetailsModel();
		List<Loan_Details> list = model.findByMasterId(masterId);

		tableModel.setRowCount(0);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		for (Loan_Details d : list) {
			String statusDisplay = "Unknown";
			if (d.getStatus() != null) {
				statusDisplay = d.getStatus().getDbValue();
			}

			String dateString = "";
			if (d.getReturn_date() != null) {
				dateString = sdf.format(d.getReturn_date());
			}

			tableModel.addRow(new Object[] { d.getId(), d.getBookTitle(), dateString, statusDisplay, d.getLate_fee(),
					d.getCompensation_fee(), "Check In" });
		}
	}
}