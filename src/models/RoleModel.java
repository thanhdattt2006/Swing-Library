package models;

import java.sql.*;
import java.util.*;
import entities.Role;

public class RoleModel {

    public List<Role> findAll() {
        List<Role> list = new ArrayList<>();

        try {
            String sql = "SELECT id, name FROM role";
            PreparedStatement ps = ConnectDB.connection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Role r = new Role();
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
