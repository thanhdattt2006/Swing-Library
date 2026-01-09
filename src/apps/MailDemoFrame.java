package apps;

import javax.swing.*;
import java.awt.*;
import models.MailModel;

public class MailDemoFrame extends JFrame {
    private JTextField txtEmail, txtName, txtLateFee;

    public MailDemoFrame() {
        setTitle("Demo Gửi Mail Nhắc Nợ - Admin");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel(" Email người mượn:"));
        txtEmail = new JTextField("thanhdattt2006@gmail.com");
        add(txtEmail);

        add(new JLabel(" Tên nhân viên:"));
        txtName = new JTextField("Thành Đạt");
        add(txtName);

        add(new JLabel(" Phí trễ hạn (VNĐ):"));
        txtLateFee = new JTextField("50000");
        add(txtLateFee);

        JButton btnSend = new JButton("GỬI THÔNG BÁO");
        add(new JLabel("")); // Giữ chỗ
        add(btnSend);

        btnSend.addActionListener(e -> {
            sendMailLogic();
        });
    }

    private void sendMailLogic() {
        MailModel model = new MailModel();
        // Đường dẫn file template (mày check lại path cho đúng)
        String html = model.readTemplate("src/mail_template/library_notification.html");
        
        // Replace dữ liệu giả lập để demo
        html = html.replace("{{name}}", txtName.getText())
                   .replace("{{id}}", "123")
                   .replace("{{books}}", "Clean Code, Refactoring")
                   .replace("{{due_date}}", "2026-01-09")
                   .replace("{{deposit}}", "500000")
                   .replace("{{late_fee}}", txtLateFee.getText())
                   .replace("{{note}}", "Trả sớm đi không nó trừ hết tiền cọc giờ.");

        boolean ok = model.send("thanhdattt2006@gmail.com", txtEmail.getText(), "[CẢNH BÁO] Quá hạn trả sách", html);
        
        if(ok) JOptionPane.showMessageDialog(this, "Gửi mail thành công rồi mày!");
        else JOptionPane.showMessageDialog(this, "Thất bại, check lại log đi.");
    }

    public static void main(String[] args) {
        new MailDemoFrame().setVisible(true);
    }
}