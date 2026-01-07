package apps;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import entities.Account;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import models.LoginModel;

public class JFrameLogin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField jtextFieldUsername;
	private JPasswordField jpasswordFieldPassword;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}

		EventQueue.invokeLater(() -> {
			try {
				JFrameLogin frame = new JFrameLogin();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public JFrameLogin() {
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 408, 261);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setBounds(40, 46, 88, 16);
		contentPane.add(lblNewLabel);

		jtextFieldUsername = new JTextField();
		jtextFieldUsername.setBounds(140, 40, 210, 28);
		contentPane.add(jtextFieldUsername);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(40, 97, 88, 16);
		contentPane.add(lblPassword);

		jpasswordFieldPassword = new JPasswordField();
		jpasswordFieldPassword.setBounds(140, 91, 210, 28);
		contentPane.add(jpasswordFieldPassword);

		JButton jbuttonLogin = new JButton("Login");
		jbuttonLogin.setBounds(77, 155, 99, 28);
		contentPane.add(jbuttonLogin);

		JButton jbuttonCancel = new JButton("Cancel");
		jbuttonCancel.setBounds(200, 155, 99, 28);
		contentPane.add(jbuttonCancel);

		// ===== EVENTS =====
		jbuttonLogin.addActionListener(e -> doLogin());
		jbuttonCancel.addActionListener(e -> System.exit(0));
	}

	private void doLogin() {
		String username = jtextFieldUsername.getText().trim();
		String password = new String(jpasswordFieldPassword.getPassword());

		if (username.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter username and password");
			return;
		}

		LoginModel model = new LoginModel();
		Account account = model.login(username, password);

		if (account == null) {
			JOptionPane.showMessageDialog(this, "Invalid username or password");
			return;
		}

		// Login success
		JFrameMain mainFrame = new JFrameMain(account);
		mainFrame.setVisible(true);
		this.dispose();
	}
}
