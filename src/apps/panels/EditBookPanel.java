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

public class EditBookPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Dialog parentDialog;

	private BookPanel bookPanel;
	private JTextField jTextFieldPublicationYear;
	private JLabel avatar1;
	private File file;
	private JLabel photo;
	private JTextArea jTextAreaDesc;
	private byte[] imageData;
	private JTextField jTextFieldIsbn;
	private JTextField jTextFieldCallNumber;
	private JTextField jTextFieldTitle;
	private JTextField jTextFieldStock;
	private JTextField jTextFieldPrice;
	private JLabel validateTitle;
	private JLabel validateStock;
	private JLabel validatePrice;
	private JLabel validatePubYear;
	private JPanel validateCategory;
	private JLabel validateCate;
	private JTextField jTextFieldAvailable;

	private Map<String, Object> data;
	private JTextField jTextFieldAuhor;
	private JTextField jTextFieldCategory;
	private JButton jBtnUpdate;
	private int stock;
	
	/**
	 * Create the panel.
	 */
	public EditBookPanel() {
		setToolTipText("");
        setLayout(new BorderLayout(0, 0));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(SystemColor.activeCaption);
        add(headerPanel, BorderLayout.NORTH);
        FlowLayout fl_headerPanel = new FlowLayout(FlowLayout.CENTER, 5, 5);
        fl_headerPanel.setAlignOnBaseline(true);
        headerPanel.setLayout(fl_headerPanel);
        
        JLabel lblTitle = new JLabel("Edit Book");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(lblTitle);
        
        validateCategory = new JPanel();
        add(validateCategory, BorderLayout.CENTER);
        validateCategory.setLayout(null);
        
        avatar1 = new JLabel("");
        avatar1.setBounds(63, 362, 164, 148);
        validateCategory.add(avatar1);
        avatar1.setBorder(new TitledBorder(null, "Photo", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        
        jTextFieldPublicationYear = new JTextField();
        jTextFieldPublicationYear.setBounds(64, 98, 319, 28);
        validateCategory.add(jTextFieldPublicationYear);
        jTextFieldPublicationYear.setColumns(10);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(63, 279, 320, 71);
        validateCategory.add(scrollPane);
        
        jTextAreaDesc = new JTextArea();
        jTextAreaDesc.setLineWrap(true);
        jTextAreaDesc.setWrapStyleWord(true);
        scrollPane.setViewportView(jTextAreaDesc);
        
        JLabel lblNewLabel = new JLabel("Description");
        lblNewLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNewLabel.setBounds(65, 256, 121, 22);
        validateCategory.add(lblNewLabel);
        
        JLabel lblNewLabel_2 = new JLabel("Publication year");
        lblNewLabel_2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNewLabel_2.setBounds(65, 74, 113, 22);
        validateCategory.add(lblNewLabel_2);
        
        JButton btnNewButton_1 = new JButton("Close");
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		do_btnNewButton_1_actionPerformed(e);
        	}
        });
        btnNewButton_1.setBounds(243, 634, 58, 28);
        validateCategory.add(btnNewButton_1);
        
        JButton btnNewButton_2 = new JButton("File");
        btnNewButton_2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		do_btnNewButton_2_actionPerformed(e);
        	}
        });
        btnNewButton_2.setBounds(232, 362, 51, 28);
        validateCategory.add(btnNewButton_2);
        
        photo = new JLabel("");
        photo.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        photo.setBackground(new Color(0, 0, 0));
        photo.setBounds(78, 379, 136, 119);
        validateCategory.add(photo);
        
        JLabel lblNewLabel_2_1 = new JLabel("Isbn");
        lblNewLabel_2_1.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNewLabel_2_1.setBounds(65, 132, 51, 22);
        validateCategory.add(lblNewLabel_2_1);
        
        jTextFieldIsbn = new JTextField();
        jTextFieldIsbn.setEditable(false);
        jTextFieldIsbn.setColumns(10);
        jTextFieldIsbn.setBounds(64, 156, 163, 28);
        validateCategory.add(jTextFieldIsbn);
        
        JLabel lblNewLabel_2_1_1 = new JLabel("Call number");
        lblNewLabel_2_1_1.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNewLabel_2_1_1.setBounds(227, 133, 84, 22);
        validateCategory.add(lblNewLabel_2_1_1);
        
        jTextFieldCallNumber = new JTextField();
        jTextFieldCallNumber.setEditable(false);
        jTextFieldCallNumber.setColumns(10);
        jTextFieldCallNumber.setBounds(227, 156, 156, 28);
        validateCategory.add(jTextFieldCallNumber);
        
        jTextFieldTitle = new JTextField();
        jTextFieldTitle.setEditable(false);
        jTextFieldTitle.setColumns(10);
        jTextFieldTitle.setBounds(64, 40, 319, 28);
        validateCategory.add(jTextFieldTitle);
        
        JLabel lblNewLabel_2_2 = new JLabel("Title");
        lblNewLabel_2_2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNewLabel_2_2.setBounds(65, 16, 51, 22);
        validateCategory.add(lblNewLabel_2_2);
        
        jTextFieldStock = new JTextField();
        jTextFieldStock.setColumns(10);
        jTextFieldStock.setBounds(63, 538, 163, 28);
        validateCategory.add(jTextFieldStock);
        
        JLabel lblNewLabel_2_1_2 = new JLabel("Stock");
        lblNewLabel_2_1_2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNewLabel_2_1_2.setBounds(64, 514, 51, 22);
        validateCategory.add(lblNewLabel_2_1_2);
        
        JLabel lblNewLabel_2_1_1_1 = new JLabel("Available");
        lblNewLabel_2_1_1_1.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNewLabel_2_1_1_1.setBounds(227, 514, 84, 22);
        validateCategory.add(lblNewLabel_2_1_1_1);
        
        jTextFieldAvailable = new JTextField();
        jTextFieldAvailable.setEditable(false);
        jTextFieldAvailable.setColumns(10);
        jTextFieldAvailable.setBounds(226, 538, 157, 28);
        validateCategory.add(jTextFieldAvailable);
        
        JLabel lblNewLabel_2_3 = new JLabel("Price");
        lblNewLabel_2_3.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNewLabel_2_3.setBounds(64, 570, 52, 22);
        validateCategory.add(lblNewLabel_2_3);
        
        jTextFieldPrice = new JTextField();
        jTextFieldPrice.setColumns(10);
        jTextFieldPrice.setBounds(63, 594, 320, 28);
        validateCategory.add(jTextFieldPrice);
        
        validateTitle = new JLabel("");
        validateTitle.setForeground(new Color(255, 0, 0));
        validateTitle.setFont(new Font("SansSerif", Font.PLAIN, 9));
        validateTitle.setBounds(99, 27, 282, 16);
        validateCategory.add(validateTitle);
        
        validateStock = new JLabel("");
        validateStock.setForeground(Color.RED);
        validateStock.setFont(new Font("SansSerif", Font.PLAIN, 9));
        validateStock.setBounds(107, 520, 113, 16);
        validateCategory.add(validateStock);
        
        validatePrice = new JLabel("");
        validatePrice.setForeground(Color.RED);
        validatePrice.setFont(new Font("SansSerif", Font.PLAIN, 9));
        validatePrice.setBounds(107, 580, 276, 16);
        validateCategory.add(validatePrice);
        
        validatePubYear = new JLabel("");
        validatePubYear.setForeground(Color.RED);
        validatePubYear.setFont(new Font("SansSerif", Font.PLAIN, 9));
        validatePubYear.setBounds(173, 84, 208, 16);
        validateCategory.add(validatePubYear);
        
        validateCate = new JLabel("");
        validateCate.setForeground(new Color(255, 0, 0));
        validateCate.setFont(new Font("SansSerif", Font.PLAIN, 9));
        validateCate.setBounds(131, 256, 252, 16);
        validateCategory.add(validateCate);
        
        JLabel lblNewLabel_2_1_3 = new JLabel("Author");
        lblNewLabel_2_1_3.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNewLabel_2_1_3.setBounds(64, 194, 51, 22);
        validateCategory.add(lblNewLabel_2_1_3);
        
        jTextFieldAuhor = new JTextField();
        jTextFieldAuhor.setEditable(false);
        jTextFieldAuhor.setColumns(10);
        jTextFieldAuhor.setBounds(63, 218, 163, 28);
        validateCategory.add(jTextFieldAuhor);
        
        JLabel lblNewLabel_2_1_1_2 = new JLabel("Category");
        lblNewLabel_2_1_1_2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNewLabel_2_1_1_2.setBounds(227, 195, 68, 22);
        validateCategory.add(lblNewLabel_2_1_1_2);
        
        jTextFieldCategory = new JTextField();
        jTextFieldCategory.setEditable(false);
        jTextFieldCategory.setColumns(10);
        jTextFieldCategory.setBounds(226, 218, 156, 28);
        validateCategory.add(jTextFieldCategory);
        
        jBtnUpdate = new JButton("Update");
        jBtnUpdate.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		do_jBtnUpdate_actionPerformed(e);
        	}
        });
        jBtnUpdate.setBackground(new Color(0, 153, 76));
        jBtnUpdate.setBounds(315, 634, 68, 28);
        validateCategory.add(jBtnUpdate);
    }
	
	public EditBookPanel(Map<String, Object>data, Dialog dialog, BookPanel bookPanel) {
		  this();
		  this.parentDialog = dialog;
		  this.data = data;
		  this.bookPanel = bookPanel;
		  init();
	}
	
	private void init() {
		try {
			int id = ((Number) data.get("id")).intValue();

			var bookMd = new BooksModel();
			var book = bookMd.findById(id);
			jTextFieldTitle.setText(book.getTitle());
			jTextFieldPublicationYear.setText(String.valueOf(book.getPublication_year()));
			jTextFieldIsbn.setText(book.getIsbn());
			jTextFieldCallNumber.setText(book.getCall_number());
			jTextFieldCategory.setText(book.getCategory_name());
			jTextFieldAuhor.setText(book.getAuthor_name());

			stock = book.getStock();
			
			jTextAreaDesc.setText(book.getDescription());
			jTextFieldAvailable.setText(String.valueOf(book.getAvailable_quantity()));
			jTextFieldStock.setText(String.valueOf(book.getStock()));
			jTextFieldPrice.setText(String.valueOf(book.getPrice()));

			//checking photo have null 
			if(book.getPhoto() != null) {
				Image image = new ImageIcon(book.getPhoto()).getImage().getScaledInstance(photo.getWidth(),
				photo.getHeight(), Image.SCALE_DEFAULT);
				photo.setIcon(new ImageIcon(image));
			}
		} catch(Exception e2) {
			e2.printStackTrace();
		}
		
		((AbstractDocument) jTextFieldPrice.getDocument())
        .setDocumentFilter(new DecimalFilter());
        ((AbstractDocument) jTextFieldPublicationYear.getDocument())
        .setDocumentFilter(new NumberOnlyFilter());
        ((AbstractDocument) jTextFieldStock.getDocument())
        .setDocumentFilter(new NumberOnlyFilter());
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
		        // 👉 saving file for after using 
		        this.file = file;

		        // 👉 read photo to bytes[] 
		        this.imageData = Files.readAllBytes(file.toPath());

		        // 👉 preview photo
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
	
	//update-button
	protected void do_jBtnUpdate_actionPerformed(ActionEvent e) {
		try {
			if (!validateForm()) {
		        return;
		    }

			int id = ((Number) data.get("id")).intValue();
			var bookMd = new BooksModel();
			Book book =  bookMd.findById(id);

		book.setId(id);
		
		book.setPublication_year(
		    Integer.parseInt(jTextFieldPublicationYear.getText().trim())
		);
		book.setDescription(jTextAreaDesc.getText().trim());
		book.setStock(
		    Integer.parseInt(jTextFieldStock.getText().trim())
		);
		book.setPrice(
		    Double.parseDouble(jTextFieldPrice.getText().trim())
		);
		
		// available_quantity can't set again ,if having not private logic 
		int ava = Integer.parseInt(jTextFieldAvailable.getText().trim());
		int sto = Integer.parseInt(jTextFieldStock.getText().trim());
		book.setAvailable_quantity((sto - stock) + ava);
		
		if (this.file != null) {
		    book.setPhoto(
		        Files.readAllBytes(Paths.get(file.getAbsolutePath()))
		    );
		} else {
		    // keep old photo
		    book.setPhoto(bookMd.findById(id).getPhoto());
		}
		
		if (bookMd.update(book)) {
		    JOptionPane.showMessageDialog(null, "Success", "Edit Book",
		            JOptionPane.INFORMATION_MESSAGE);
		
		    if (bookPanel != null) {
		        bookPanel.reloadTable();
		    }
		
		    SwingUtilities.getWindowAncestor(this).setVisible(false);
		} else {
		    JOptionPane.showMessageDialog(null, "Update failed",
		            "Edit Book", JOptionPane.ERROR_MESSAGE);
		}
		
		} catch (Exception ex) {
		ex.printStackTrace();
		JOptionPane.showMessageDialog(null, "Error",
		        "Edit Book", JOptionPane.ERROR_MESSAGE);
		}
	}

	//method validate
	private boolean validateForm() {
	    String pubYear = jTextFieldPublicationYear.getText().trim();
	    String sto = jTextFieldStock.getText().trim();
	    String price = jTextFieldPrice.getText().trim();

	    boolean fal = true;
	    
	    if (pubYear.isEmpty()) {
	    	validatePubYear.setText("Publication year cannot be empty!");
	    	fal = false;
	    }else {
	    	validatePubYear.setText("");
	    }
	    
	    if (sto.isEmpty()) {
	    	validateStock.setText("Stock  cannot be empty!");
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




