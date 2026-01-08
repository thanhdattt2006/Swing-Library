package apps.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;

import apps.panels.AccountPanel.DepartmentComboRenderer;
import apps.panels.AccountPanel.RoleComboRenderer;
import entities.Account;
import entities.Department;
import entities.Role;
import entities.Settings;
import models.AccountModel;
import models.DepartmentModel;
import models.RoleModel;
import models.SettingModel;

import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;

public class EditSetting extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField jMaxBorrowDays;
	private JTextField jDepositFeePerLoan;
	private JTextField jLostCompensationFee;
	private JTextField jDamaged;
	private JButton btnNewButton;
	private JButton JCancel;
	private Dialog parentDialog;
	private Map<Integer, Department> departmentMap = new HashMap<>();
	private Map<Integer, Role> roleMap = new HashMap<>();
	private SettingsPanel settingsPanel;
	private JTextField jLateFeePerDay;
	private Map<String, Object> data;
	private JTextField jId;
	

	/**
	 * Create the panel.
	 */
	public EditSetting() {
		setLayout(null);
		// Header Panel
		JPanel headerPanel = new JPanel();
		headerPanel.setBounds(0, 0, 519, 81);
		headerPanel.setBackground(SystemColor.activeCaption);
		headerPanel.setLayout(null);
		JLabel lblTitle = new JLabel("Add Settings");
		lblTitle.setBounds(124, 28, 228, 28);
		lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitle.setForeground(Color.WHITE);
		headerPanel.add(lblTitle);
		add(headerPanel);

		JLabel lblNewLabel = new JLabel("Max borrow days:");
		lblNewLabel.setBounds(20, 172, 102, 23);
		add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Deposit fee per loan:");
		lblNewLabel_1.setBounds(20, 220, 154, 23);
		add(lblNewLabel_1);

		JLabel lblPassword = new JLabel("Late fee per day:");
		lblPassword.setBounds(20, 269, 102, 23);
		add(lblPassword);

		JLabel lblName = new JLabel("Lost compensation fee:");
		lblName.setBounds(20, 315, 131, 23);
		add(lblName);

		JLabel lblPhone = new JLabel("Damage Fee");
		lblPhone.setBounds(20, 363, 152, 23);
		add(lblPhone);

		jMaxBorrowDays = new JTextField();
		jMaxBorrowDays.setBounds(161, 170, 262, 26);
		add(jMaxBorrowDays);
		jMaxBorrowDays.setColumns(10);

		jDepositFeePerLoan = new JTextField();
		jDepositFeePerLoan.setColumns(10);
		jDepositFeePerLoan.setBounds(161, 218, 262, 26);
		add(jDepositFeePerLoan);

		jLostCompensationFee = new JTextField();
		jLostCompensationFee.setColumns(10);
		jLostCompensationFee.setBounds(161, 313, 262, 26);
		add(jLostCompensationFee);

		jDamaged = new JTextField();
		jDamaged.setColumns(10);
		jDamaged.setBounds(161, 363, 262, 26);
		add(jDamaged);

		btnNewButton = new JButton("Submit");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_btnNewButton_actionPerformed(e);
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton.setBounds(109, 431, 92, 23);
		add(btnNewButton);

		JCancel = new JButton("Cancel");
		JCancel.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        do_btnNewButton_1_actionPerformed(e);
		    }
		});

		JCancel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		JCancel.setForeground(Color.BLACK);
		JCancel.setBounds(250, 431, 81, 23);
		add(JCancel);

		jLateFeePerDay = new JTextField();
		jLateFeePerDay.setColumns(10);
		jLateFeePerDay.setBounds(161, 270, 262, 26);
		add(jLateFeePerDay);
		
		JLabel lblId = new JLabel("Id");
		lblId.setBounds(20, 123, 102, 23);
		add(lblId);
		
		jId = new JTextField();
		jId.setEnabled(false);
		jId.setColumns(10);
		jId.setBounds(161, 121, 262, 26);
		add(jId);
	}

	public EditSetting(Dialog dialog, SettingsPanel settingsPanel, Map<String, Object> data ) {
		this();
		this.parentDialog = dialog;
		this.settingsPanel = settingsPanel;
		this.data = data;
		init();
	}

	private void init() {
		SettingModel settingModel = new SettingModel();
		int id = Integer.parseInt(data.get("id").toString());
		Settings settings = settingModel.findById(id);
		jId.setText(String.valueOf(id));
		jMaxBorrowDays.setText(String.valueOf(settings.getMax_borrow_days()));
		jDepositFeePerLoan.setText(String.valueOf(settings.getDeposit_fee_per_loan()));
		jLateFeePerDay.setText(String.valueOf(settings.getLate_fee_per_day()));
		jLostCompensationFee.setText(String.valueOf(settings.getLost_compensation_fee()));
		jDamaged.setText(String.valueOf(settings.getDamaged_compensation_fee()));

	}



	protected void do_btnNewButton_actionPerformed(ActionEvent e) {
		try {
			Settings setting = new Settings();
			setting.setId(Integer.parseInt(jId.getText().trim()));
			setting.setMax_borrow_days(Integer.parseInt(jMaxBorrowDays.getText().trim()));
			setting.setDeposit_fee_per_loan(Double.parseDouble(jDepositFeePerLoan.getText().trim()));
			setting.setLate_fee_per_day(Double.parseDouble(jLateFeePerDay.getText().trim()));
			setting.setLost_compensation_fee(Double.parseDouble(jLostCompensationFee.getText().trim()));
			setting.setDamaged_compensation_fee(Double.parseDouble(jDamaged.getText().trim()));
			SettingModel settingModel = new SettingModel();
			if (settingModel.update(setting)) {
				JOptionPane.showMessageDialog(this, " Edit success!", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				if (settingsPanel != null) {
					settingsPanel.refreshTable();
				}
				parentDialog.dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Edit failed!", "Error", JOptionPane.ERROR_MESSAGE);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void do_btnNewButton_1_actionPerformed(ActionEvent e) {
		parentDialog.dispose();
	}
}
