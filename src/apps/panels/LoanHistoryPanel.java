package apps.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator; 
import java.util.List;
import java.util.Objects;

import entities.Loan_Master;
import models.LoanMasterModel;
import models.MailModel;
import utils.DetailsButtonRender;
import utils.TableActionCellEditor;
import utils.TableActionEvent;

public class LoanHistoryPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cboFilterStatus;
    private List<Loan_Master> currentList; 
    private JButton btnWarn3Days, btnWarnDue, btnSearch;

    public LoanHistoryPanel() {
        initComponents();
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) { loadData(); }
        });
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10)); 
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlHeader.setBackground(SystemColor.activeCaption);
        pnlHeader.setPreferredSize(new Dimension(800, 50));
        
        JLabel lblTitle = new JLabel("Loan History Management");
        lblTitle.setForeground(SystemColor.text);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        initTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panelSouth = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panelSouth.add(new JLabel("Employee ID:"));
        txtSearch = new JTextField(10);
        panelSouth.add(txtSearch);

        panelSouth.add(new JLabel("Status:"));
        cboFilterStatus = new JComboBox<>(new String[] { "All", "Borrowing", "Completed" });
        panelSouth.add(cboFilterStatus);

        btnSearch = new JButton("Search");
        btnSearch.setBackground(Color.WHITE);
        btnSearch.addActionListener(e -> performSearch());
        panelSouth.add(btnSearch);

        panelSouth.add(new JSeparator(SwingConstants.VERTICAL));

        // --- CÁCH LOAD ICON TỪ RESOURCES ---
        // Mày nhớ thay đúng tên file ảnh mày đã bỏ vào folder resources nhé
        ImageIcon mailIcon = null;
        try {
            mailIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/mail_icon.png")));
            // Nếu muốn scale icon cho vừa nút:
            Image img = mailIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            mailIcon = new ImageIcon(img);
        } catch (Exception e) {
            System.err.println("Icon not found, using text only.");
        }

        btnWarn3Days = new JButton(" 3-Day Warning", mailIcon);
        btnWarn3Days.setBackground(new Color(255, 204, 0));
        btnWarn3Days.addActionListener(e -> processBulkMail(3));
        panelSouth.add(btnWarn3Days);

        btnWarnDue = new JButton(" Due Tomorrow", mailIcon);
        btnWarnDue.setBackground(new Color(255, 102, 102));
        btnWarnDue.setForeground(Color.WHITE);
        btnWarnDue.addActionListener(e -> processBulkMail(1));
        panelSouth.add(btnWarnDue);

        add(panelSouth, BorderLayout.SOUTH);
    }

    private void initTable() {
        String[] columns = { "ID", "Username", "Employee ID", "Borrow Date", "Status", "Total Fee", "Action" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return column == 6; }
        };
        table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(35);
        
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onAction(int row) {
                int masterId = (int) table.getValueAt(row, 0);
                openDetailsDialog(masterId);
            }
            // onDelete đã được gỡ bỏ để tránh lỗi Interface supertype
        };
        
        TableColumn actionCol = table.getColumnModel().getColumn(6);
        actionCol.setCellRenderer(new DetailsButtonRender());
        actionCol.setCellEditor(new TableActionCellEditor(event));
        actionCol.setMinWidth(100);
        actionCol.setMaxWidth(100);
    }

    private void processBulkMail(int type) {
        if (currentList == null || currentList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "The list is empty!", "Notification", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String confirmMsg = (type == 3) ? "Confirm sending 3-day reminders?" : "Confirm sending final notices?";
        int confirm = JOptionPane.showConfirmDialog(this, confirmMsg, "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        // Dialog hiện Loading để thủ thư không bấm loạn
        JDialog loadingDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Processing", true);
        loadingDialog.setLayout(new BorderLayout());
        loadingDialog.add(new JLabel(" Sending emails, please wait...", JLabel.CENTER), BorderLayout.CENTER);
        loadingDialog.setSize(300, 100);
        loadingDialog.setLocationRelativeTo(this);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        // Dùng SwingWorker để gửi mail ở luồng phụ, tránh dead máy
        SwingWorker<int[], Void> worker = new SwingWorker<>() {
            @Override
            protected int[] doInBackground() {
                toggleButtons(false); // Khóa nút khi đang chạy
                MailModel mailModel = new MailModel();
                int sent = 0, fail = 0, skipped = 0;
                LocalDate today = LocalDate.now();

                for (Loan_Master m : currentList) {
                    // Logic lọc Borrowing
                    String statusStr = (m.getStatus() != null) ? m.getStatus().toString().trim() : "";
                    if (!"Borrowing".equalsIgnoreCase(statusStr)) {
                        skipped++; continue;
                    }

                    if (m.getDue_date() != null) {
                        try {
                            LocalDate dueDate = m.getDue_date().toLocalDate();
                            long diff = ChronoUnit.DAYS.between(today, dueDate);
                            
                            // Logic tính ngày: 3 ngày nhắc sớm, 1 ngày hạn cuối
                            boolean shouldSend = (type == 3 && diff >= 2 && diff <= 3) || (type == 1 && diff >= 0 && diff <= 1);

                            if (shouldSend) {
                                String template = (type == 3) ? "reminder_3days.html" : "final_notice.html";
                                String subject = (type == 3) ? "[Library] Reminder" : "[Library] Urgent Final Notice";
                                String html = mailModel.readTemplate("src/mail_template/" + template);
                                
                                html = html.replace("{{name}}", m.getUsername())
                                           .replace("{{id}}", String.valueOf(m.getId()))
                                           .replace("{{due_date}}", new SimpleDateFormat("dd-MM-yyyy").format(m.getDue_date()));

                                if (mailModel.send("thanhdattt2006@gmail.com", m.getUsername(), subject, html)) sent++;
                                else fail++;
                            } else { skipped++; }
                        } catch (Exception ex) { fail++; }
                    } else { skipped++; }
                }
                return new int[]{sent, fail, skipped};
            }

            @Override
            protected void done() {
                try {
                    int[] res = get();
                    loadingDialog.dispose(); 
                    toggleButtons(true); // Mở lại nút
                    String resultMsg = String.format("Process completed!\n- Sent: %d\n- Failed: %d\n- Skipped: %d", res[0], res[1], res[2]);
                    JOptionPane.showMessageDialog(LoanHistoryPanel.this, resultMsg, "Result", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    loadingDialog.dispose();
                    toggleButtons(true);
                    JOptionPane.showMessageDialog(LoanHistoryPanel.this, "System error occurred!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
        loadingDialog.setVisible(true); 
    }

    private void toggleButtons(boolean enabled) {
        btnWarn3Days.setEnabled(enabled);
        btnWarnDue.setEnabled(enabled);
        btnSearch.setEnabled(enabled);
        setCursor(enabled ? Cursor.getDefaultCursor() : Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    private void loadData() {
        try {
            currentList = new LoanMasterModel().findAll();
            fillTable(currentList);
        } catch (Exception e) { 
            JOptionPane.showMessageDialog(this, "Error loading data from database!", "Database Error", JOptionPane.ERROR_MESSAGE); 
        }
    }

    private void performSearch() {
        String kw = txtSearch.getText().trim();
        String st = (cboFilterStatus.getSelectedItem() != null) ? cboFilterStatus.getSelectedItem().toString() : "All";
        currentList = new LoanMasterModel().search(kw, st);
        fillTable(currentList);
    }

    private void fillTable(List<Loan_Master> list) {
        tableModel.setRowCount(0);
        if (list == null) return;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        list.sort(Comparator.comparingInt(Loan_Master::getId));
        for (Loan_Master m : list) {
            tableModel.addRow(new Object[] { 
                m.getId(), m.getUsername(), m.getEmployeeIdDisplay(), 
                (m.getBorrow_date() != null ? sdf.format(m.getBorrow_date()) : ""),
                m.getStatus(), (m.getTotal_late_fee() + m.getTotal_compensation_fee()), "Details" 
            });
        }
    }

    private void openDetailsDialog(int loanId) {
        try {
            JDialog dialog = new JDialog();
            dialog.setTitle("Details #" + loanId);
            dialog.setModal(true); 
            dialog.setContentPane(new LoanDetailsPanel(loanId));
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
            loadData();
        } catch (Exception e) { e.printStackTrace(); }
    }
}