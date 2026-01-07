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
import apps.panels.AccountPanel.DepartmentComboRenderer;
import apps.panels.AccountPanel.RoleComboRenderer;
import entities.Account;
import entities.Author;
import entities.Department;
import entities.Role;
import models.AccountModel;
import models.AuthorsModel;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;

public class AddAuthorPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Dialog parentDialog;
	private Map<Integer, Department> departmentMap = new HashMap<>();
	private Map<Integer, Role> roleMap = new HashMap<>();
	private AuthorPanel authorPanel;
	private JTextField jTextFieldName;
	private JLabel avatar1;
	private File file;
	private JLabel avatar;
	private JTextArea jTextAreaBio;
	private JButton btnNewButton;
	private byte[] imageData;

	
	/**
	 * Create the panel.
	 */
	public AddAuthorPanel() {
		setToolTipText("");
        setLayout(new BorderLayout(0, 0));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(SystemColor.activeCaption);
        add(headerPanel, BorderLayout.NORTH);
        FlowLayout fl_headerPanel = new FlowLayout(FlowLayout.CENTER, 5, 5);
        fl_headerPanel.setAlignOnBaseline(true);
        headerPanel.setLayout(fl_headerPanel);
        
        JLabel lblTitle = new JLabel("Add Author");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(lblTitle);
        
        JPanel panel = new JPanel();
        add(panel, BorderLayout.CENTER);
        panel.setLayout(null);
        
        avatar1 = new JLabel("");
        avatar1.setBounds(33, 37, 164, 148);
        panel.add(avatar1);
        avatar1.setBorder(new TitledBorder(null, "Avatar", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        
        jTextFieldName = new JTextField();
        jTextFieldName.setBounds(226, 60, 247, 28);
        panel.add(jTextFieldName);
        jTextFieldName.setColumns(10);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(226, 115, 247, 70);
        panel.add(scrollPane);
        
        jTextAreaBio = new JTextArea();
        jTextAreaBio.setLineWrap(true);
        jTextAreaBio.setWrapStyleWord(true);
        scrollPane.setViewportView(jTextAreaBio);
        
        JLabel lblNewLabel = new JLabel("BIO");
        lblNewLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNewLabel.setBounds(228, 92, 51, 22);
        panel.add(lblNewLabel);
        
        JLabel lblNewLabel_2 = new JLabel("Name");
        lblNewLabel_2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNewLabel_2.setBounds(228, 37, 51, 22);
        panel.add(lblNewLabel_2);
        
        btnNewButton = new JButton("Add");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		do_btnNewButton_actionPerformed(e);
        	}
        });
        btnNewButton.setBackground(new Color(53, 255, 53));
        btnNewButton.setBounds(415, 210, 58, 28);
        panel.add(btnNewButton);
        
        JButton btnNewButton_1 = new JButton("Close");
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		do_btnNewButton_1_actionPerformed(e);
        	}
        });
        btnNewButton_1.setBounds(345, 210, 58, 28);
        panel.add(btnNewButton_1);
        
        JButton btnNewButton_2 = new JButton("File");
        btnNewButton_2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		do_btnNewButton_2_actionPerformed(e);
        	}
        });
        btnNewButton_2.setBounds(145, 185, 51, 28);
        panel.add(btnNewButton_2);
        
        avatar = new JLabel("");
        avatar.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        avatar.setBackground(new Color(0, 0, 0));
        avatar.setBounds(48, 54, 136, 119);
        panel.add(avatar);
    }
	public AddAuthorPanel(Dialog dialog, AuthorPanel authorPanel) {
		  this();
		  this.parentDialog = dialog;
		  this.authorPanel = authorPanel;
	}
	
	
	protected void do_btnNewButton_2_actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser("E:\\anh");
		
		fileChooser.setDialogTitle("Chon file anh");
		
		//Đóng laij, cấm chọn mặc định All file
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPG File(*.jpg)", "jpg"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("GIF File(*.gif)", "gif"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG File(*.png)", "png"));
		
//		var result = fileChooser.showOpenDialog(this);
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
		        // 👉 lưu file để dùng sau (nếu cần)
		        this.file = file;

		        // 👉 đọc ảnh thành byte[] để lưu DB
		        this.imageData = Files.readAllBytes(file.toPath());

		        // 👉 preview ảnh
		        Image image = new ImageIcon(file.getAbsolutePath())
		                .getImage()
		                .getScaledInstance(
		                        avatar.getWidth(),
		                        avatar.getHeight(),
		                        Image.SCALE_SMOOTH
		                );

		        avatar.setIcon(new ImageIcon(image));

		    } catch (Exception ex) {
		        JOptionPane.showMessageDialog(this,
		                "Cannot read image file",
		                "Error",
		                JOptionPane.ERROR_MESSAGE);
		    }
		}

	}
	
	//button-save
	protected void do_btnNewButton_actionPerformed(ActionEvent e) {
		try {
			 if (!validateForm()) {
			        return;
			    }
			AuthorsModel autMd = new AuthorsModel();
			Author aut = new Author();
			aut.setName(jTextFieldName.getText());
			aut.setBio(jTextAreaBio.getText());
			if( this.file != null) {
				aut.setPhoto(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
			} 
			
			if(autMd.create(aut)) {
				JOptionPane.showMessageDialog(null, "Success", "Add ", JOptionPane.INFORMATION_MESSAGE);
				if (authorPanel != null) {
			        authorPanel.reloadTable();
			    }
				//ẩn đi addAuthorPanel
				SwingUtilities.getWindowAncestor(this).setVisible(false);
			}else {
				JOptionPane.showMessageDialog(null, "Failed", " Add ", JOptionPane.ERROR_MESSAGE );
			}
		} catch(Exception e2) {
			JOptionPane.showMessageDialog(null, "Failed", " Add ", JOptionPane.ERROR_MESSAGE );
			e2.printStackTrace();
		}
	}

	//method validate
	private boolean validateForm() {
	    String name = jTextFieldName.getText().trim();

	    if (name.isEmpty()) {
	        JOptionPane.showMessageDialog(
	            this,
	            "Name cannot be empty!",
	            "Validation Error",
	            JOptionPane.ERROR_MESSAGE
	        );
	        jTextFieldName.requestFocus();
	        return false;
	    }
	    return true;
	}
	
	//button-close
	protected void do_btnNewButton_1_actionPerformed(ActionEvent e) {
		SwingUtilities.getWindowAncestor(this).setVisible(false);
	}
}
