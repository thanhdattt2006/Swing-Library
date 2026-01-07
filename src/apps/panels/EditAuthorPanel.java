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
import javax.swing.border.EtchedBorder;

public class EditAuthorPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Dialog parentDialog;
	private AuthorPanel authorPanel;
	private JLabel avatar1;
	private JLabel avatar;

	private Map<String, Object> data;
	private JTextField jTextFieldName;
	private JTextArea jTextArea;
	private JScrollPane scrollPane;
	
	private File file;
	private byte[] imageData;
	
	/**
	 * Create the panel.
	 */
	public EditAuthorPanel() {
        setLayout(new BorderLayout(0, 0));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(SystemColor.activeCaption);
        add(headerPanel, BorderLayout.NORTH);
        FlowLayout fl_headerPanel = new FlowLayout(FlowLayout.CENTER, 5, 5);
        fl_headerPanel.setAlignOnBaseline(true);
        headerPanel.setLayout(fl_headerPanel);
        
        JLabel lblTitle = new JLabel("Edit Author");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(lblTitle);
        
        JPanel panel = new JPanel();
        add(panel, BorderLayout.CENTER);
        panel.setLayout(null);
        
        JLabel lblNewLabel = new JLabel("BIO");
        lblNewLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNewLabel.setBounds(228, 92, 51, 22);
        panel.add(lblNewLabel);
        
        JLabel lblNewLabel_2 = new JLabel("Name");
        lblNewLabel_2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNewLabel_2.setBounds(228, 37, 51, 22);
        panel.add(lblNewLabel_2);
        
        JButton btnNewButton = new JButton("Update");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		do_btnNewButton_actionPerformed(e);
        	}
        });
        btnNewButton.setBackground(new Color(53, 255, 53));
        btnNewButton.setBounds(408, 208, 65, 28);
        panel.add(btnNewButton);
        
        JButton btnNewButton_1 = new JButton("Close");
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		do_btnNewButton_1_actionPerformed(e);
        	}
        });
        btnNewButton_1.setBounds(340, 208, 58, 28);
        panel.add(btnNewButton_1);
        
        avatar1 = new JLabel("");
        avatar1.setBorder(new TitledBorder(null, "Avatar", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        avatar1.setBounds(31, 37, 164, 148);
        panel.add(avatar1);
        
        avatar = new JLabel("");
        avatar.setBackground(Color.BLACK);
        avatar.setAlignmentY(1.0f);
        avatar.setBounds(45, 52, 136, 119);
        panel.add(avatar);
        
        JButton btnNewButton_2 = new JButton("File");
        btnNewButton_2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		do_btnNewButton_2_actionPerformed(e);
        	}
        });
        btnNewButton_2.setBounds(144, 183, 51, 28);
        panel.add(btnNewButton_2);
        
        jTextFieldName = new JTextField();
        jTextFieldName.setColumns(10);
        jTextFieldName.setBounds(226, 62, 247, 28);
        panel.add(jTextFieldName);
        
        scrollPane = new JScrollPane();
        scrollPane.setBounds(228, 115, 247, 70);
        panel.add(scrollPane);
        
        jTextArea = new JTextArea();
        jTextArea.setWrapStyleWord(true);
        jTextArea.setLineWrap(true);
        scrollPane.setViewportView(jTextArea);
    }
	
	public EditAuthorPanel(Map<String, Object>data, Dialog dialog, AuthorPanel authorPanel) {
		this();
		this.data = data;
		init();
		 this.authorPanel = authorPanel;
	}
	
	private void init() {
		try {
			int id = ((Number) data.get("id")).intValue();

			AuthorsModel autMd = new AuthorsModel();
			Author aut = autMd.findById(id);
			jTextFieldName.setText(aut.getName());
			jTextArea.setText(aut.getBio());

			//kiểm tra ảnh có null không
			if(aut.getPhoto() != null) {
				Image image = new ImageIcon(aut.getPhoto()).getImage().getScaledInstance(avatar.getWidth(),
				avatar.getHeight(), Image.SCALE_DEFAULT);
				avatar.setIcon(new ImageIcon(image));
			}
		} catch(Exception e2) {
			e2.printStackTrace();
		}
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
	
	//update-button
	protected void do_btnNewButton_actionPerformed(ActionEvent e) {
		try {
			if (!validateForm()) {
		        return; // dừng lại nếu lỗi
		    }
			
			int id = ((Number) data.get("id")).intValue();

			AuthorsModel autMd = new AuthorsModel();
			Author aut = autMd.findById(id);
			aut.setName(jTextFieldName.getText().trim());
			aut.setBio(jTextArea.getText().trim());
			
			if (file != null && file.length() > 0) {
				aut.setPhoto(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
			}
			

			//kiểm tra ảnh có null không
			if(aut.getPhoto() != null) {
				Image image = new ImageIcon(aut.getPhoto()).getImage().getScaledInstance(avatar.getWidth(),
				avatar.getHeight(), Image.SCALE_DEFAULT);
				avatar.setIcon(new ImageIcon(image));
			}
			if(autMd.update(aut)) {
				JOptionPane.showMessageDialog(null, "Success", "Edit ", JOptionPane.INFORMATION_MESSAGE);
				if (authorPanel != null) {
			        authorPanel.reloadTable();
			    }
				//ẩn đi editAuthorPanel
				SwingUtilities.getWindowAncestor(this).setVisible(false);
			}else {
				JOptionPane.showMessageDialog(null, "Failed", " Edit ", JOptionPane.ERROR_MESSAGE );
			}
		} catch(Exception e2) {
			JOptionPane.showMessageDialog(null, "Failed", " Edit ", JOptionPane.ERROR_MESSAGE );
			e2.printStackTrace();
		}
	}
	
	//button-close
	protected void do_btnNewButton_1_actionPerformed(ActionEvent e) {
		SwingUtilities.getWindowAncestor(this).setVisible(false);
	}
	
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
}
