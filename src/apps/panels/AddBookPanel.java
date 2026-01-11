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

import apps.JFrameTest;
import apps.filter.DecimalFilter;
import apps.filter.NumberOnlyFilter;
import apps.panels.AccountPanel.DepartmentComboRenderer;
import apps.panels.AccountPanel.RoleComboRenderer;
import entities.Account;
import entities.Author;
import entities.Book;
import entities.Category;
import entities.Department;
import entities.Role;
import models.AccountModel;
import models.AuthorsModel;
import models.BooksModel;
import models.CategoriesModel;
import models.ConnectDB;
import models.DepartmentModel;
import models.RoleModel;

import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AbstractDocument;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;

public class AddBookPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Dialog parentDialog;

	private BookPanel bookPanel;
	private JTextField jTextFieldPublicationYear;
	private JLabel avatar1;
	private File file;
	private JLabel photo;
	private JTextArea jTextAreaDesc;
	private JButton btnNewButton;
	private byte[] imageData;
	private JTextField jTextFieldTitle;
	private JTextField jTextFieldStock;
	private JTextField jTextFieldPrice;
	private JComboBox jComboBoxCategory;
	private JComboBox jComboBoxAuthor;
	private JLabel validateTitle;
	private JLabel validateStock;
	private JLabel validatePrice;
	private JLabel validatePubYear;
	private JLabel validateAuthor;
	private JPanel validateCategory;
	private JLabel validateCate;

	
	/**
	 * Create the panel.
	 */
	public AddBookPanel() {
		setToolTipText("");
        setLayout(new BorderLayout(0, 0));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(SystemColor.activeCaption);
        add(headerPanel, BorderLayout.NORTH);
        FlowLayout fl_headerPanel = new FlowLayout(FlowLayout.CENTER, 5, 5);
        fl_headerPanel.setAlignOnBaseline(true);
        headerPanel.setLayout(fl_headerPanel);
        
        JLabel lblTitle = new JLabel("Add Book");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(lblTitle);
        
        validateCategory = new JPanel();
        add(validateCategory, BorderLayout.CENTER);
        validateCategory.setLayout(null);
        
        avatar1 = new JLabel("");
        avatar1.setBounds(64, 352, 164, 148);
        validateCategory.add(avatar1);
        avatar1.setBorder(new TitledBorder(null, "Photo", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        
        jTextFieldPublicationYear = new JTextField();
        jTextFieldPublicationYear.setBounds(64, 98, 319, 28);
        validateCategory.add(jTextFieldPublicationYear);
        jTextFieldPublicationYear.setColumns(10);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(64, 269, 320, 71);
        validateCategory.add(scrollPane);
        
        jTextAreaDesc = new JTextArea();
        jTextAreaDesc.setLineWrap(true);
        jTextAreaDesc.setWrapStyleWord(true);
        scrollPane.setViewportView(jTextAreaDesc);
        
        JLabel lblNewLabel = new JLabel("Description");
        lblNewLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNewLabel.setBounds(66, 246, 121, 22);
        validateCategory.add(lblNewLabel);
        
        JLabel lblNewLabel_2 = new JLabel("Publication year");
        lblNewLabel_2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNewLabel_2.setBounds(65, 74, 113, 22);
        validateCategory.add(lblNewLabel_2);
        
        btnNewButton = new JButton("Add");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		do_btnNewButton_actionPerformed(e);
        	}
        });

        btnNewButton.setBackground(new Color(0, 153, 76));
        btnNewButton.setBounds(325, 641, 58, 28);
        validateCategory.add(btnNewButton);
        
        JButton btnNewButton_1 = new JButton("Close");
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		do_btnNewButton_1_actionPerformed(e);
        	}
        });
        btnNewButton_1.setBounds(255, 641, 58, 28);
        validateCategory.add(btnNewButton_1);
        
        JButton btnNewButton_2 = new JButton("File");
        btnNewButton_2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		do_btnNewButton_2_actionPerformed(e);
        	}
        });
        btnNewButton_2.setBounds(233, 352, 51, 28);
        validateCategory.add(btnNewButton_2);
        
        photo = new JLabel("");
        photo.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        photo.setBackground(new Color(0, 0, 0));
        photo.setBounds(79, 369, 136, 119);
        validateCategory.add(photo);
        
        jComboBoxAuthor = new JComboBox();
        jComboBoxAuthor.setBounds(65, 157, 319, 26);
        validateCategory.add(jComboBoxAuthor);
        
        JLabel lblNewLabel_2_1_2_1 = new JLabel("Category");
        lblNewLabel_2_1_2_1.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNewLabel_2_1_2_1.setBounds(66, 191, 73, 22);
        validateCategory.add(lblNewLabel_2_1_2_1);
        
        jComboBoxCategory = new JComboBox();
        jComboBoxCategory.setBounds(65, 214, 319, 26);
        validateCategory.add(jComboBoxCategory);
        
        JLabel lblNewLabel_2_1_2_1_1 = new JLabel("Author");
        lblNewLabel_2_1_2_1_1.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNewLabel_2_1_2_1_1.setBounds(66, 135, 50, 22);
        validateCategory.add(lblNewLabel_2_1_2_1_1);
        
        jTextFieldTitle = new JTextField();
        jTextFieldTitle.setColumns(10);
        jTextFieldTitle.setBounds(64, 40, 319, 28);
        validateCategory.add(jTextFieldTitle);
        
        JLabel lblNewLabel_2_2 = new JLabel("Title");
        lblNewLabel_2_2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNewLabel_2_2.setBounds(65, 16, 51, 22);
        validateCategory.add(lblNewLabel_2_2);
        
        jTextFieldStock = new JTextField();
        jTextFieldStock.setColumns(10);
        jTextFieldStock.setBounds(64, 528, 319, 28);
        validateCategory.add(jTextFieldStock);
        
        JLabel lblNewLabel_2_1_2 = new JLabel("Stock");
        lblNewLabel_2_1_2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNewLabel_2_1_2.setBounds(65, 504, 51, 22);
        validateCategory.add(lblNewLabel_2_1_2);
        
        JLabel lblNewLabel_2_3 = new JLabel("Price");
        lblNewLabel_2_3.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNewLabel_2_3.setBounds(65, 560, 52, 22);
        validateCategory.add(lblNewLabel_2_3);
        
        jTextFieldPrice = new JTextField();
        jTextFieldPrice.setColumns(10);
        jTextFieldPrice.setBounds(64, 584, 320, 28);
        validateCategory.add(jTextFieldPrice);
        
        validateTitle = new JLabel("");
        validateTitle.setForeground(new Color(255, 0, 0));
        validateTitle.setFont(new Font("SansSerif", Font.PLAIN, 9));
        validateTitle.setBounds(99, 27, 282, 16);
        validateCategory.add(validateTitle);
        
        validateStock = new JLabel("");
        validateStock.setForeground(Color.RED);
        validateStock.setFont(new Font("SansSerif", Font.PLAIN, 9));
        validateStock.setBounds(108, 512, 276, 16);
        validateCategory.add(validateStock);
        
        validatePrice = new JLabel("");
        validatePrice.setForeground(Color.RED);
        validatePrice.setFont(new Font("SansSerif", Font.PLAIN, 9));
        validatePrice.setBounds(108, 568, 276, 16);
        validateCategory.add(validatePrice);
        
        validatePubYear = new JLabel("");
        validatePubYear.setForeground(Color.RED);
        validatePubYear.setFont(new Font("SansSerif", Font.PLAIN, 9));
        validatePubYear.setBounds(173, 84, 208, 16);
        validateCategory.add(validatePubYear);
        
        validateAuthor = new JLabel("");
        validateAuthor.setForeground(Color.RED);
        validateAuthor.setFont(new Font("SansSerif", Font.PLAIN, 9));
        validateAuthor.setBounds(116, 141, 268, 22);
        validateCategory.add(validateAuthor);
        
        validateCate = new JLabel("");
        validateCate.setForeground(new Color(255, 0, 0));
        validateCate.setFont(new Font("SansSerif", Font.PLAIN, 9));
        validateCate.setBounds(132, 201, 252, 16);
        validateCategory.add(validateCate);
        
        JLabel lblpleaseReviewThe = new JLabel("*Please review the book title, category, and author carefully before confirming.");
        lblpleaseReviewThe.setForeground(new Color(92, 92, 92));
        lblpleaseReviewThe.setFont(new Font("SansSerif", Font.PLAIN, 9));
        lblpleaseReviewThe.setBounds(64, 627, 346, 16);
        validateCategory.add(lblpleaseReviewThe);
    }
	public AddBookPanel(Dialog dialog, BookPanel bookPanel) {
		  this();
		  this.parentDialog = dialog;
		  this.bookPanel = bookPanel;
		  init();
	}
	
	private void init() {
		var autMd = new AuthorsModel();
		DefaultComboBoxModel<Author> comboBoxModel = new DefaultComboBoxModel<>();
		comboBoxModel.addElement(null);
		for (Author aut : autMd.findAll()) {
			comboBoxModel.addElement(aut);
		}
		jComboBoxAuthor.setModel(comboBoxModel);
		jComboBoxAuthor.setRenderer(new AuthorCellRender());
		
		var catMd = new CategoriesModel();
		DefaultComboBoxModel<Category> comboBoxModel2 = new DefaultComboBoxModel<>();
		comboBoxModel2.addElement(null);
		for (Category cate : catMd.findAll()) {
			comboBoxModel2.addElement(cate);
		}
		jComboBoxCategory.setModel(comboBoxModel2);
		jComboBoxCategory.setRenderer(new CategoryCellRender());
		
		
        ((AbstractDocument) jTextFieldPublicationYear.getDocument())
        .setDocumentFilter(new NumberOnlyFilter());
        ((AbstractDocument) jTextFieldStock.getDocument())
        .setDocumentFilter(new NumberOnlyFilter());
        ((AbstractDocument) jTextFieldPrice.getDocument())
        .setDocumentFilter(new DecimalFilter());
	}
	
	private class AuthorCellRender extends DefaultListCellRenderer {

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			
			if (value == null) {
				return super.getListCellRendererComponent(list, "Null", index, isSelected, cellHasFocus);
			}
			
			Author aut = (Author) value;
			return super.getListCellRendererComponent(list, aut.getName(), index, isSelected, cellHasFocus);
		}

	}
	
	private class CategoryCellRender extends DefaultListCellRenderer {

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			
			if (value == null) {
				return super.getListCellRendererComponent(list, "Null", index, isSelected, cellHasFocus);
			}
			
			Category cate = (Category) value;
			return super.getListCellRendererComponent(list, cate.getName(), index, isSelected, cellHasFocus);
		}

	}
	
	protected void do_btnNewButton_2_actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser("E:\\anh");
		
		fileChooser.setDialogTitle("Chon file anh");
		
		//Đóng laij, cấm chọn mặc định All file
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG File(*.png)", "png"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPG File(*.jpg)", "jpg"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("GIF File(*.gif)", "gif"));
		
		var result = fileChooser.showOpenDialog(this);

		if (result == JFileChooser.APPROVE_OPTION) {
		    File file = fileChooser.getSelectedFile();
		    // check size 2MB
		    if (file.length() > 2 * 1024 * 1024) {
		        JOptionPane.showMessageDialog(this,
		                "The selected file exceeds 2MB. Please upload a smaller file");
		        return;
		    }

		    try {
		        this.file = file;
		        this.imageData = Files.readAllBytes(file.toPath());
		        // 👉 preview ảnh
		        Image image = new ImageIcon(file.getAbsolutePath())
		                .getImage()
		                .getScaledInstance(
		                        photo.getWidth(),
		                        photo.getHeight(),
		                        Image.SCALE_SMOOTH
		                );
		        photo.setIcon(new ImageIcon(image));
		    } catch (Exception ex) {
		        JOptionPane.showMessageDialog(this,
		                "Cannot read image file",
		                "Error",
		                JOptionPane.ERROR_MESSAGE);
		    }
		}

	}
	
	
	//generateCallNumber
	public String generateCallNumber(String title, String authorName) {
	    String titlePrefix = title.substring(0, Math.min(2, title.length())).toUpperCase();
	    String authorPrefix = authorName.substring(0, Math.min(2, authorName.length())).toUpperCase();
	    String prefix = titlePrefix + "-" + authorPrefix; 
	    int nextNumber = 1;
	    String sql = """
	        SELECT MAX(
	            CAST(SUBSTRING(call_number, -3) AS UNSIGNED)
	        ) AS max_suffix
	        FROM book
	        WHERE call_number LIKE ?
	    """;

	    try (
	        PreparedStatement ps = ConnectDB.connection().prepareStatement(sql)
	    ) {
	        ps.setString(1, prefix + "-%");

	        ResultSet rs = ps.executeQuery();
	        if (rs.next() && rs.getInt("max_suffix") > 0) {
	            nextNumber = rs.getInt("max_suffix") + 1;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    String suffix = String.format("%03d", nextNumber);

	    return prefix + "-" + suffix;
	}
	
	//generateIsbnByCategory
	public String generateIsbnByCategory(int categoryId) {
	    String categoryPrefix = String.format("%03d", categoryId);
	    int nextNumber = 1;
	    String sql = """
	        SELECT MAX(
	            CAST(SUBSTRING(isbn, -4) AS UNSIGNED)
	        ) AS max_suffix
	        FROM book
	        WHERE isbn LIKE ?
	    """;

	    try (
	        PreparedStatement ps = ConnectDB.connection().prepareStatement(sql)
	    ) {
	        ps.setString(1, categoryPrefix + "-%");
	        ResultSet rs = ps.executeQuery();
	        if (rs.next() && rs.getInt("max_suffix") > 0) {
	            nextNumber = rs.getInt("max_suffix") + 1;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    String suffix = String.format("%04d", nextNumber);
	    return categoryPrefix + "-" + suffix;
	}

	//button-save
	protected void do_btnNewButton_actionPerformed(ActionEvent e) {
		try {
			if (!validateForm()) {
		        return;
		    }
			
			var bookMd = new BooksModel();
			var book = new Book();
			
			book.setTitle(jTextFieldTitle.getText());
			book.setPublication_year(Integer.parseInt(jTextFieldPublicationYear.getText()));
			var category = (Category) jComboBoxCategory.getSelectedItem();
			book.setCategory_id(category.getId());
			var author = (Author) jComboBoxAuthor.getSelectedItem();
			book.setAuthor_id(author.getId());
			String isbn = generateIsbnByCategory(category.getId());
			book.setIsbn(isbn);
			String callNumber = generateCallNumber(book.getTitle(), author.getName());
			book.setCall_number(callNumber);
			book.setDescription(jTextAreaDesc.getText());
			book.setStock(Integer.parseInt(jTextFieldStock.getText()));
			book.setAvailable_quantity(Integer.parseInt(jTextFieldStock.getText()));
			book.setPrice(Double.parseDouble(jTextFieldPrice.getText()));
			if( this.file != null) {
				book.setPhoto(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
			} 
			int value = JOptionPane.showConfirmDialog(null, "Important: Please double-check the book title, category, and author before proceeding", "Confirm Book Information", JOptionPane.YES_NO_OPTION);			
			  if (value == JOptionPane.YES_OPTION) {
		        if (bookMd.create(book)) {
					JOptionPane.showMessageDialog(null, "Success", "Add Book", JOptionPane.INFORMATION_MESSAGE);
						if (bookPanel != null) {
							bookPanel.reloadTable();
					    }
						SwingUtilities.getWindowAncestor(this).setVisible(false);
					} else {
						JOptionPane.showMessageDialog(null, "Failed", "Add Book", JOptionPane.ERROR_MESSAGE);
					}

		        } else {
		        	return;
		        }
		} catch (Exception e2) {
			JOptionPane.showMessageDialog(null, "Failed", "Add Book", JOptionPane.ERROR_MESSAGE);
		}
	}

	//method validate
	private boolean validateForm() {
	    String pubYear = jTextFieldPublicationYear.getText().trim();
	    String title = jTextFieldTitle.getText().trim();
	    var aut = (Author) jComboBoxAuthor.getSelectedItem();
	    var cate = (Category) jComboBoxCategory.getSelectedItem();
	    String sto = jTextFieldStock.getText().trim();
	    String price = jTextFieldPrice.getText().trim();
	    boolean fal = true;
	    if (pubYear.isEmpty()) {
	    	validatePubYear.setText("Publication year cannot be empty!");
	    	fal = false;
	    }else {
	    	validatePubYear.setText("");
	    }
	    
	    if (title.isEmpty()) {
	    	validateTitle.setText("Title cannot be empty!");
	    	fal = false;
	    }else {
	    	validateTitle.setText("");
	    }
	    
	    if (aut == null) {
	    	validateAuthor.setText("Author cannot be Null!");
	    	fal = false;
	    }else {
	    	validateAuthor.setText("");
	    }
	    
	    if (cate == null) {
	    	validateCate.setText("Category cannot be Null!");
	    	fal = false;
	    }else {
	    	validateCate.setText("");
	    }
	    
	    if (sto.isEmpty()) {
	    	validateStock.setText("Stock cannot be empty!");
	    	fal = false;
	    }else {
	    	validateStock.setText("");
	    }
	    
	    if (price.isEmpty()) {
	    	validatePrice.setText("Price cannot be empty!");
	    	fal = false;
	    }else {
	    	validatePrice.setText("");
	    }
	    return fal;
	}
	
	//button-close
	protected void do_btnNewButton_1_actionPerformed(ActionEvent e) {
		SwingUtilities.getWindowAncestor(this).setVisible(false);
	}
	
}
