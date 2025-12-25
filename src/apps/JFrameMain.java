package apps;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import apps.panels.AccountPanel;
import app.enums.MenuType;
import entities.Account;

public class JFrameMain extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JPanel panelNavigate;
	private JPanel jpanelMainPage;
	private CardLayout cardLayout;

	private Account currentAccount;

	// ===== MENU CONFIG =====
	private final List<MenuType> ADMIN_MENU = List.of(MenuType.ACCOUNT, MenuType.SETTINGS, MenuType.LOGOUT);

	public JFrameMain(Account account) {
		this.currentAccount = account;
		initUI();
		initMenuByRole();
	}

	// chỉ dùng khi test giao diện
	public JFrameMain() {
		initUI();
		buildMenu(ADMIN_MENU);
		jpanelMainPage.add(new AccountPanel(), MenuType.ACCOUNT.name());
		cardLayout.show(jpanelMainPage, MenuType.ACCOUNT.name());
	}

	private void initUI() {
		setTitle("Library Management System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		contentPane = new JPanel(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		// ===== TOP =====
		JPanel jpanelTopPage = new JPanel(new BorderLayout());

		JPanel jpanelNameCompany = new JPanel();
		JLabel lblCompany = new JLabel("Mohan Ltd");
		lblCompany.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 25));
		jpanelNameCompany.add(lblCompany);

		JPanel jpanelNameSystem = new JPanel();
		JLabel lblSystem = new JLabel("Library Management System");
		lblSystem.setFont(new Font("SansSerif", Font.PLAIN, 17));
		jpanelNameSystem.add(lblSystem);

		panelNavigate = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		panelNavigate.setBackground(SystemColor.activeCaption);

		jpanelTopPage.add(jpanelNameCompany, BorderLayout.NORTH);
		jpanelTopPage.add(jpanelNameSystem, BorderLayout.CENTER);
		jpanelTopPage.add(panelNavigate, BorderLayout.SOUTH);

		contentPane.add(jpanelTopPage, BorderLayout.NORTH);

		// ===== MAIN PAGE =====
		cardLayout = new CardLayout();
		jpanelMainPage = new JPanel(cardLayout);
		contentPane.add(jpanelMainPage, BorderLayout.CENTER);

		setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	// ===== ROLE HANDLING =====
	private void initMenuByRole() {
		int roleId = currentAccount.getRole_id();

		jpanelMainPage.removeAll();

		// 1 = ADMIN
		if (roleId == 1) {
			jpanelMainPage.add(new AccountPanel(), MenuType.ACCOUNT.name());
			// sau này add thêm SettingsPanel

			buildMenu(ADMIN_MENU);
			cardLayout.show(jpanelMainPage, MenuType.ACCOUNT.name());
		}

		jpanelMainPage.revalidate();
		jpanelMainPage.repaint();
	}

	// ===== BUILD MENU =====
	private void buildMenu(List<MenuType> menus) {
		panelNavigate.removeAll();

		for (MenuType type : menus) {
			JButton btn = new JButton(type.name());
			btn.addActionListener(e -> onMenuClick(type));
			panelNavigate.add(btn);
		}

		panelNavigate.revalidate();
		panelNavigate.repaint();
	}

	private void onMenuClick(MenuType type) {
		switch (type) {
		case ACCOUNT:
			cardLayout.show(jpanelMainPage, MenuType.ACCOUNT.name());
			break;

		case LOGOUT:
			logout();
			break;

		default:
			JOptionPane.showMessageDialog(this, "Chưa implement: " + type);
		}
	}

	private void logout() {
		dispose();
		new JFrameLogin().setVisible(true);
	}
}
