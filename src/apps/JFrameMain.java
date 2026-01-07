package apps;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import apps.panels.AccountPanel;
import apps.panels.AuthorPanel;
import apps.panels.BookPanel;
import apps.panels.CategoryPanel;
import apps.panels.CheckOutPanel;
import apps.panels.SettingsPanel;
import app.enums.MenuType;
import entities.Account;

public class JFrameMain extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JPanel panelNavigate;
	private JPanel jpanelMainPage;
	private CardLayout cardLayout;
	private Account currentAccount;

	public void showCustomPanel(String name, JPanel panel) {
		jpanelMainPage.add(panel, name);
		cardLayout.show(jpanelMainPage, name);
	}

	// ===== MENU CONFIG =====
	private final List<MenuType> ADMIN_MENU = List.of(MenuType.ACCOUNT, MenuType.SETTINGS, MenuType.LOGOUT);

	private final List<MenuType> LIBRARIAN_MENU = List.of(MenuType.AUTHOR, MenuType.CATEGORY, MenuType.BOOK,
			MenuType.CHECKOUT, MenuType.LOAN_HISTORY, MenuType.LOGOUT);

	private final List<MenuType> EMPLOYEE_MENU = List.of(MenuType.BOOK, MenuType.MY_LOANS, MenuType.MY_INFOR,
			MenuType.LOGOUT);

	// ===== CONSTRUCTOR =====
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

	// ===== INIT UI =====
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

	// ===== MENU BY ROLE =====
	private void initMenuByRole() {
		int roleId = currentAccount.getRole_id();

		jpanelMainPage.removeAll();

		switch (roleId) {

		case 1: // ADMIN
			jpanelMainPage.add(new AccountPanel(), MenuType.ACCOUNT.name());
			jpanelMainPage.add(new SettingsPanel(), MenuType.SETTINGS.name());
			buildMenu(ADMIN_MENU);
			cardLayout.show(jpanelMainPage, MenuType.ACCOUNT.name());
			break;

		case 2: // LIBRARIAN
			// panel placeholder để test
			jpanelMainPage.add(new JPanel(), MenuType.LOAN_HISTORY.name());
			jpanelMainPage.add(new BookPanel(this), MenuType.BOOK.name());

			jpanelMainPage.add(new AuthorPanel(), MenuType.AUTHOR.name());
			jpanelMainPage.add(new CategoryPanel(), MenuType.CATEGORY.name());
			jpanelMainPage.add(new CheckOutPanel(), MenuType.CHECKOUT.name());

			buildMenu(LIBRARIAN_MENU);
			cardLayout.show(jpanelMainPage, MenuType.BOOK.name());
			break;

		case 3: // EMPLOYEE
			BookPanel bookPanel = new BookPanel(this);
			bookPanel.hideActionPanel();
			jpanelMainPage.add(bookPanel, MenuType.BOOK.name());

			jpanelMainPage.add(bookPanel, MenuType.BOOK.name());
			jpanelMainPage.add(new JPanel(), MenuType.MY_LOANS.name());
			jpanelMainPage.add(new JPanel(), MenuType.MY_INFOR.name());

			buildMenu(EMPLOYEE_MENU);
			cardLayout.show(jpanelMainPage, MenuType.BOOK.name()); // 👈 Mặc định Book
			break;

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

	// ===== MENU CLICK =====
	private void onMenuClick(MenuType type) {

		if (type == MenuType.LOGOUT) {
			logout();
			return;
		}

		cardLayout.show(jpanelMainPage, type.name());
	}

	private void logout() {
		dispose();
		new JFrameLogin().setVisible(true);
	}
}
