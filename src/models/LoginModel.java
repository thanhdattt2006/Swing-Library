package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import entities.Account;

public class LoginModel {

	public Account login(String username, String password) {
		try {
			String sql = """
					    SELECT *
					    FROM account
					    WHERE username = ? AND password = ?
					""";

			PreparedStatement ps = ConnectDB.connection().prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				Account acc = new Account();
				acc.setId(rs.getInt("id"));
				acc.setEmployee_id(rs.getString("employee_id"));
				acc.setUsername(rs.getString("username"));
				acc.setPassword(rs.getString("password"));
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
