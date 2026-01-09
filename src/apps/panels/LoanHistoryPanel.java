package apps.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

import entities.Loan_Master;
import models.LoanMasterModel;
import utils.DetailsButtonRender;
import utils.TableActionCellEditor;
import utils.TableActionCellRender;
import utils.TableActionEvent;

public class LoanHistoryPanel extends JPanel {

	private JTable table;
	private DefaultTableModel tableModel;
	private JTextField txtSearch;
	private JComboBox<String> cboFilterStatus;

	public LoanHistoryPanel() {
		initComponents();
		this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                loadData(); 
            }
        });
		loadData();
	}

	private void initComponents() {
		setLayout(new BorderLayout(10, 10)); // Padding 10px
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel pnlHeader = new JPanel();
		pnlHeader.setBackground(SystemColor.activeCaption);
		pnlHeader.setPreferredSize(new Dimension(800, 50));

		add(pnlHeader, BorderLayout.NORTH);
		pnlHeader.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblTitle = new JLabel("Loan History");
		pnlHeader.add(lblTitle);
		lblTitle.setForeground(SystemColor.text);
		lblTitle.setFont(new Font("Arial", Font.BOLD, 24));

		// --- 2. TABLE ---
		initTable();
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel label = new JLabel("Employee ID:");
		panel.add(label);

		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		txtSearch = new JTextField(10);
		panel_1.add(txtSearch);

		JLabel label_1 = new JLabel("Status:");
		panel_1.add(label_1);
		cboFilterStatus = new JComboBox<>(new String[] { "All", "Borrowing", "Completed" });
		panel_1.add(cboFilterStatus);

		JButton btnSearch = new JButton("Search");
		panel_1.add(btnSearch);
		btnSearch.setBackground(new Color(255, 255, 255));
		btnSearch.setForeground(Color.BLACK);
		btnSearch.addActionListener(e -> performSearch());
	}

	private void initTable() {

		String[] columns = { "ID", "Username", "Employee ID", "Borrow Date", "Status", "Total Fee", "Action" };

		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 6;
			}
		};

		table = new JTable(tableModel);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setRowHeight(35);
		table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
		TableActionEvent event = new TableActionEvent() {
			@Override
			public void onAction(int row) {
				int masterId = (int) table.getValueAt(row, 0);
				openDetailsDialog(masterId);
			}
		};
		TableColumn actionCol = table.getColumnModel().getColumn(6);
		actionCol.setCellRenderer(new DetailsButtonRender());
		actionCol.setCellEditor(new TableActionCellEditor(event));
		actionCol.setMinWidth(100);
		actionCol.setMaxWidth(100);
	}

	private void openDetailsDialog(int loanId) {
		try {
			javax.swing.JDialog dialog = new javax.swing.JDialog();
			dialog.setTitle("LoanDetails #" + loanId);
			dialog.setModal(true);
			dialog.setContentPane(new LoanDetailsPanel(loanId));
			dialog.pack();
			dialog.setSize(1000, 700);
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
		}
	}

	private void loadData() {
		LoanMasterModel model = new LoanMasterModel();
		List<Loan_Master> list = model.findAll();
		fillTable(list);
		this.revalidate(); 
        this.repaint();
	}

	private void performSearch() {
	    LoanMasterModel model = new LoanMasterModel();
	    
	    String keyword = txtSearch.getText().trim();
	    
	    String statusFilter = "All";
	    if (cboFilterStatus.getSelectedItem() != null) {
	        statusFilter = cboFilterStatus.getSelectedItem().toString();
	    }

	    List<Loan_Master> list = model.search(keyword, statusFilter);

	    fillTable(list);
	}

	private void fillTable(List<Loan_Master> list) {
		tableModel.setRowCount(0);

		for (Loan_Master m : list) {
			String statusDisplay = "Unknown";
			if (m.getStatus() != null) {
				statusDisplay = m.getStatus().getDbValue();
			}

			tableModel.addRow(new Object[] { m.getId(), m.getUsername(), m.getEmployeeIdDisplay(), m.getBorrow_date(),
					statusDisplay, m.getTotal_late_fee(), "Details" });
		}
	}
}