package models;

import java.sql.*;
import java.util.*;
import entities.Department;

public class DepartmentModel {

	public List<Department> findAll() {
		List<Department> list = new ArrayList<>();

		try {
			String sql = "SELECT id, name FROM department";
			PreparedStatement ps = ConnectDB.connection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Department d = new Department();
				d.setId(rs.getInt("id"));
				d.setName(rs.getString("name"));
				list.add(d);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
