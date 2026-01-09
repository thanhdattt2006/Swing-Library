package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;
import entities.Account;

public class LoginModel {

	

	public Account login(String username, String passwordInput) {
	    try {
	        String sql = """
	            SELECT *
	            FROM account
	            WHERE username = ?
	        """;

	        PreparedStatement ps = ConnectDB.connection().prepareStatement(sql);
	        ps.setString(1, username);

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            String passwordHash = rs.getString("password");

	            // CHECK PASSWORD BẰNG BCRYPT
	            if (!BCrypt.checkpw(passwordInput, passwordHash)) {
	                return null; // sai mật khẩu
	            }

	            // ĐÚNG MẬT KHẨU → MAP ACCOUNT
	            Account acc = new Account();
	            acc.setId(rs.getInt("id"));
	            acc.setEmployee_id(rs.getString("employee_id"));
	            acc.setUsername(rs.getString("username"));
	            acc.setPassword(passwordHash); // hash
	            acc.setName(rs.getString("name"));
	            acc.setDepartment_id(rs.getInt("department_id"));
	            acc.setRole_id(rs.getInt("role_id"));
	            acc.setGender(rs.getString("gender"));
	            acc.setBirthday(rs.getDate("birthday"));
	            acc.setPhoto(rs.getString("photo"));
	            acc.setAddress(rs.getString("address"));
	            acc.setPhone(rs.getString("phone"));

	            return acc;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}

}
