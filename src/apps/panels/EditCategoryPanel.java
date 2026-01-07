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
import entities.Author;
import entities.Category;
import models.AuthorsModel;
import models.CategoriesModel;
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

public class EditCategoryPanel extends JPanel {

	private static final long serialVersionUID = 1L;
//	private Dialog parentDialog;
	private CategoryPanel categoryPanel;
	private JTextField jTextFieldName;
	private JTextArea jTextAreaDescription;
	private JButton btnNewButton;
	
	private Map<String, Object> data;

	
	/**
	 * Create the panel.
	 */
	public EditCategoryPanel() {
		setToolTipText("");
        setLayout(new BorderLayout(0, 0));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(SystemColor.activeCaption);
        add(headerPanel, BorderLayout.NORTH);
        FlowLayout fl_headerPanel = new FlowLayout(FlowLayout.CENTER, 5, 5);
        fl_headerPanel.setAlignOnBaseline(true);
        headerPanel.setLayout(fl_headerPanel);
        
        JLabel lblTitle = new JLabel("Add Category");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(lblTitle);
        
        JPanel panel = new JPanel();
        add(panel, BorderLayout.CENTER);
        panel.setLayout(null);
        
        jTextFieldName = new JTextField();
        jTextFieldName.setBounds(108, 37, 363, 28);
        panel.add(jTextFieldName);
        jTextFieldName.setColumns(10);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(108, 77, 363, 96);
        panel.add(scrollPane);
        
        jTextAreaDescription = new JTextArea();
        jTextAreaDescription.setLineWrap(true);
        jTextAreaDescription.setWrapStyleWord(true);
        scrollPane.setViewportView(jTextAreaDescription);
        
        JLabel lblNewLabel = new JLabel("Description");
        lblNewLabel.setBounds(31, 77, 76, 22);
        lblNewLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(lblNewLabel);
        
        JLabel lblNewLabel_2 = new JLabel("Name");
        lblNewLabel_2.setBounds(31, 37, 76, 22);
        lblNewLabel_2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(lblNewLabel_2);
        
        btnNewButton = new JButton("Add");
        btnNewButton.setBounds(411, 202, 58, 28);
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		do_btnNewButton_actionPerformed(e);
        	}
        });
        btnNewButton.setBackground(new Color(53, 255, 53));
        panel.add(btnNewButton);
        
        JButton btnNewButton_1 = new JButton("Close");
        btnNewButton_1.setBounds(341, 202, 58, 28);
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		do_btnNewButton_1_actionPerformed(e);
        	}
        });
        panel.add(btnNewButton_1);
    }
	public EditCategoryPanel(Map<String, Object>data, Dialog dialog,  CategoryPanel categoryPanel) {
		this();
		this.data = data;
		init();
		this.categoryPanel = categoryPanel;
	}
	
	private void init() {
		try {
			int id = ((Number) data.get("id")).intValue();

			var catMd = new CategoriesModel();
			Category cate = catMd.findById(id);
			jTextFieldName.setText(cate.getName());
			jTextAreaDescription.setText(cate.getDescription());

		} catch(Exception e2) {
			e2.printStackTrace();
		}
	}

	

	
	//update-button
	protected void do_btnNewButton_actionPerformed(ActionEvent e) {
		try {
			if (!validateForm()) {
		        return; // dừng lại nếu lỗi
		    }
			
			int id = ((Number) data.get("id")).intValue();

			var catMd = new CategoriesModel();
			var cate = catMd.findById(id);
			cate.setName(jTextFieldName.getText().trim());
			cate.setDescription(jTextAreaDescription.getText().trim());

		
			if(catMd.update(cate)) {
				JOptionPane.showMessageDialog(null, "Success", "Edit ", JOptionPane.INFORMATION_MESSAGE);
				if (categoryPanel != null) {
					categoryPanel.reloadTable();
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
