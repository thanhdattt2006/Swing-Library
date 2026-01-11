package models;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import entities.Account;
import models.ConnectDB;

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
			PreparedStatement preparedStatement = ConnectDB.connection().prepareStatement("INSERT INTO account ("
					+ "employee_id, username, `password`, `name`, department_id, role_id, birthday, address, phone"
					+ ") VALUES (?,?,?,?,?,?,?,?,?)");

			preparedStatement.setString(1, account.getEmployee_id());
			preparedStatement.setString(2, account.getUsername());
			preparedStatement.setString(3, account.getPassword());
			preparedStatement.setString(4, account.getName());
			preparedStatement.setInt(5, account.getDepartment_id());
			preparedStatement.setInt(6, account.getRole_id());

			// FIX DATE
			if (account.getBirthday() != null) {
				preparedStatement.setDate(7, new java.sql.Date(account.getBirthday().getTime()));
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

//	public Account findbyId(int Id ) {
//		Account account = null;
//		try {
//			PreparedStatement preparedStatement = ConnectDB.connection()
//					.prepareStatement("select * from account where id =?");
//			preparedStatement.setInt(1, Id);
//			ResultSet resultSet = preparedStatement.executeQuery();
//			while (resultSet.next()) {
//				account = new Account();
//				account.setEmployee_id(resultSet.getString("employee_id"));
//				account.setUsername(resultSet.getString("username"));
//				account.setPassword(resultSet.getString("password"));
//				account.setName(resultSet.getString("name"));
//				account.setDepartment_id(resultSet.getInt("department_id"));
//				account.setRole_id(resultSet.getInt("role_id"));
//				account.setBirthday(resultSet.getDate("birthday"));
//				account.setEmployee_id(resultSet.getString("employee_id"));
//				account.setAddress(resultSet.getString("address"));
//				account.setPhone(resultSet.getString("phone"));
//				
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			account = null;
//			// TODO: handle exception
//		}
//		finally {
//			ConnectDB.disconnect();
//		}
//		
//		return account;
//	}
	public Account findbyId(int Id) {
		Account account = null;

		try {
			String sql = """
					    SELECT
					        a.*,
					        d.name AS department_name
					    FROM account a
					    LEFT JOIN department d ON a.department_id = d.id
					    WHERE a.id = ?
					""";

			PreparedStatement ps = ConnectDB.connection().prepareStatement(sql);
			ps.setInt(1, Id);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				account = new Account();
				account.setId(rs.getInt("id"));
				account.setEmployee_id(rs.getString("employee_id"));
				account.setUsername(rs.getString("username"));
				account.setName(rs.getString("name"));
				account.setGender(rs.getString("gender"));
				account.setBirthday(rs.getDate("birthday"));
				account.setAddress(rs.getString("address"));
				account.setPhone(rs.getString("phone"));
				account.setDepartment_id(rs.getInt("department_id"));
				account.setDepartment_name(rs.getString("department_name"));
				account.setRole_id(rs.getInt("role_id"));
				account.setPhoto(rs.getString("photo"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectDB.disconnect();
		}

		return account;
	}

	public boolean update(Account account) {
    try (Connection conn = ConnectDB.connection()) {

        String sql;
        PreparedStatement ps;

        if (account.getPassword() != null) {
            sql = "UPDATE account SET employee_id=?, username=?, password=?, name=?, phone=?, birthday=?, address=?, role_id=?, department_id=? WHERE id=?";
            ps = conn.prepareStatement(sql);

            ps.setString(1, account.getEmployee_id());
            ps.setString(2, account.getUsername());
            ps.setString(3, account.getPassword());
            ps.setString(4, account.getName());
            ps.setString(5, account.getPhone());
            ps.setDate(6, (Date) account.getBirthday());
            ps.setString(7, account.getAddress());
            ps.setInt(8, account.getRole_id());
            ps.setInt(9, account.getDepartment_id());
            ps.setInt(10, account.getId());

        } else {
            sql = "UPDATE account SET employee_id=?, username=?, name=?, phone=?, birthday=?, address=?, role_id=?, department_id=? WHERE id=?";
            ps = conn.prepareStatement(sql);

            ps.setString(1, account.getEmployee_id());
            ps.setString(2, account.getUsername());
            ps.setString(3, account.getName());
            ps.setString(4, account.getPhone());
            ps.setDate(5, (Date) account.getBirthday());
            ps.setString(6, account.getAddress());
            ps.setInt(7, account.getRole_id());
            ps.setInt(8, account.getDepartment_id());
            ps.setInt(9, account.getId());
        }

        return ps.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

	public boolean delete(int id) {
		boolean result = false;
		try {
			PreparedStatement preparedStatement = ConnectDB.connection()
					.prepareStatement("DELETE FROM account WHERE id = ?");

			preparedStatement.setInt(1, id);

			result = preparedStatement.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			ConnectDB.disconnect();
		}
		return result;
	}

	public List<Account> search(Integer roleId, Integer departmentId) {
		List<Account> result = new ArrayList<>();

		try {
			StringBuilder sql = new StringBuilder("SELECT * FROM account WHERE 1=1");

			if (roleId != null) {
				sql.append(" AND role_id = ?");
			}

			if (departmentId != null) {
				sql.append(" AND department_id = ?");
			}

			PreparedStatement preparedStatement = ConnectDB.connection().prepareStatement(sql.toString());

			int paramIndex = 1;
			if (roleId != null) {
				preparedStatement.setInt(paramIndex++, roleId);
			}
			if (departmentId != null) {
				preparedStatement.setInt(paramIndex++, departmentId);
			}

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Account acc = new Account();
				acc.setId(rs.getInt("id"));
				acc.setEmployee_id(rs.getString("employee_id"));
				acc.setUsername(rs.getString("username"));
				acc.setPassword(rs.getString("password"));
				acc.setName(rs.getString("name"));
				acc.setPhone(rs.getString("phone"));
				acc.setAddress(rs.getString("address"));
				acc.setBirthday(rs.getDate("birthday"));
				acc.setRole_id(rs.getInt("role_id"));
				acc.setDepartment_id(rs.getInt("department_id"));

				result.add(acc);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectDB.disconnect();
		}

		return result;
	}

	public Account findByUsernameOrEmployeeId(String keyword) {
	    Account acc = null;
	    Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        conn = ConnectDB.connection();
	        
	        String sql = "SELECT a.*, d.name AS dept_name " + "FROM account a "
	                   + "LEFT JOIN department d ON a.department_id = d.id " 
	                   + "WHERE a.username = ? OR a.employee_id = ?";

	        ps = conn.prepareStatement(sql);
	        ps.setString(1, keyword.trim());
	        ps.setString(2, keyword.trim());

	        rs = ps.executeQuery();

	        if (rs.next()) {
	            acc = new Account();
	            acc.setId(rs.getInt("id"));
	            acc.setUsername(rs.getString("username"));
	            acc.setName(rs.getString("name")); 	            
	            acc.setEmployee_id(rs.getString("employee_id"));
	            acc.setDepartment_name(rs.getString("dept_name"));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (ps != null) ps.close();
	            if (conn != null) conn.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return acc;
	}
}