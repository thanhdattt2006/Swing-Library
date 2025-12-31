package models;

import java.sql.*;
import java.sql.Date;
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
	public boolean create(Account account) {
	    boolean result = false;
	    try {
	        PreparedStatement preparedStatement = ConnectDB.connection()
	            .prepareStatement(
	                "INSERT INTO account (" +
	                "employee_id, username, `password`, `name`, department_id, role_id, birthday, address, phone" +
	                ") VALUES (?,?,?,?,?,?,?,?,?)"
	            );

	        preparedStatement.setString(1, account.getEmployee_id());
	        preparedStatement.setString(2, account.getUsername());
	        preparedStatement.setString(3, account.getPassword());
	        preparedStatement.setString(4, account.getName());
	        preparedStatement.setInt(5, account.getDepartment_id());
	        preparedStatement.setInt(6, account.getRole_id());

	        // FIX DATE
	        if (account.getBirthday() != null) {
	            preparedStatement.setDate(
	                7,
	                new java.sql.Date(account.getBirthday().getTime())
	            );
	        } else {
	            preparedStatement.setNull(7, java.sql.Types.DATE);
	        }

	        preparedStatement.setString(8, account.getAddress());
	        preparedStatement.setString(9, account.getPhone());

	        result = preparedStatement.executeUpdate() > 0;

	    } catch (Exception e) {
	        e.printStackTrace();
	        result = false;
	    } finally {
	        ConnectDB.disconnect();
	    }
	    return result;
	}
	



}
