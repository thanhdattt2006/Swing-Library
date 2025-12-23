package apps;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JMenuBar;
import java.awt.BorderLayout;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JToolBar;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.Color;

public class JFrameMain extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel jpanelMainPage;

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
					JFrameMain frame = new JFrameMain();
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
	public JFrameMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 614, 570);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel jpanelTopPage = new JPanel();
		contentPane.add(jpanelTopPage, BorderLayout.NORTH);
		jpanelTopPage.setLayout(new BorderLayout(0, 0));
		
		JPanel jpanelNameSystem = new JPanel();
		jpanelTopPage.add(jpanelNameSystem, BorderLayout.CENTER);
		
		JLabel lblNewLabel_1_1 = new JLabel("Library Management System");
		lblNewLabel_1_1.setFont(new Font("SansSerif", Font.PLAIN, 17));
		jpanelNameSystem.add(lblNewLabel_1_1);
		
		JPanel jpanelNameCompany = new JPanel();
		jpanelTopPage.add(jpanelNameCompany, BorderLayout.NORTH);
		
		JLabel lblNewLabel_1 = new JLabel("Mohan Ltd");
		lblNewLabel_1.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 25));
		jpanelNameCompany.add(lblNewLabel_1);
		
		JPanel jpanelNavigate = new JPanel();
		jpanelNavigate.setBackground(SystemColor.activeCaption);
		jpanelTopPage.add(jpanelNavigate, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("New button");
		jpanelNavigate.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("New button");
		jpanelNavigate.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("New button");
		jpanelNavigate.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("New button");
		jpanelNavigate.add(btnNewButton_3);
		
		jpanelMainPage = new JPanel();
		contentPane.add(jpanelMainPage, BorderLayout.CENTER);
		jpanelMainPage.setLayout(new BorderLayout(0, 0));
		
		JPanel jpanelBottomPage = new JPanel();
		jpanelBottomPage.setBackground(SystemColor.windowText);
		contentPane.add(jpanelBottomPage, BorderLayout.SOUTH);
		jpanelBottomPage.setLayout(new BorderLayout(0, 0));
		
		JPanel jpanelBotLine1 = new JPanel();
		jpanelBotLine1.setBackground(SystemColor.desktop);
		jpanelBottomPage.add(jpanelBotLine1, BorderLayout.SOUTH);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setForeground(SystemColor.activeCaption);
		jpanelBotLine1.add(lblNewLabel);
		
		JPanel jpanelBotLine2 = new JPanel();
		jpanelBotLine2.setBackground(SystemColor.desktop);
		jpanelBottomPage.add(jpanelBotLine2, BorderLayout.NORTH);
		
		JLabel lblNewLabel_3 = new JLabel("New label");
		lblNewLabel_3.setForeground(SystemColor.activeCaption);
		jpanelBotLine2.add(lblNewLabel_3);
		
		JPanel jpanelBotLine3 = new JPanel();
		jpanelBotLine3.setBackground(SystemColor.desktop);
		jpanelBottomPage.add(jpanelBotLine3, BorderLayout.CENTER);
		
		JLabel lblNewLabel_4 = new JLabel("New label");
		lblNewLabel_4.setForeground(SystemColor.activeCaption);
		jpanelBotLine3.add(lblNewLabel_4);

		setExtendedState(JFrame.MAXIMIZED_BOTH);

	}
}
