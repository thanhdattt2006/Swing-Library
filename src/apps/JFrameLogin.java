package apps;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class JFrameLogin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField jtextFieldUsername;
	private JPasswordField jpasswordFieldPassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrameLogin frame = new JFrameLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
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
		jtextFieldUsername.setColumns(10);
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

	}
}
