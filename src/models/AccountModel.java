package models;

import java.sql.*;
import java.util.*;
import entities.Account;

public class AccountModel {

	public List<Account> findAll() {
		List<Account> list = new ArrayList<>();

		try {
			String sql = """
					    SELECT *
					    FROM account
					""";

			PreparedStatement ps = ConnectDB.connection().prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Account acc = new Account();
				acc.setId(rs.getInt("id"));
				acc.setEmployee_id(rs.getString("employee_id"));
				acc.setUsername(rs.getString("username"));
				acc.setName(rs.getString("name"));
				acc.setPhone(rs.getString("phone"));
				acc.setAddress(rs.getString("address"));
				acc.setBirthday(rs.getDate("birthday"));
				acc.setRole_id(rs.getInt("role_id"));
				acc.setDepartment_id(rs.getInt("department_id"));

				list.add(acc);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
