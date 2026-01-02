package models;

import java.sql.*;

import entities.Settings;

public class SettingModel {
	public Settings getSettings() {
		String sql = "SELECT * FROM SETTING LIMIT 1";
		try (Connection connect = ConnectDB.connection();
				PreparedStatement ps = connect.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return new Settings(rs.getInt("id"), rs.getInt("max_borrow_days"), rs.getDouble("deposit_fee_per_loan"),
						rs.getDouble("late_fee_per_day"), rs.getDouble("lost_compensation_fee"),
						rs.getDouble("damaged_compensation_fee"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean updateSettings(Settings setting) {
		String sql = """
				    UPDATE SETTING
				    SET max_borrow_days = ?,
				        deposit_fee_per_loan = ?,
				        late_fee_per_day = ?,
				        lost_compensation_fee = ?,
				        damaged_compensation_fee = ?
				    WHERE id = ?
				""";

		try (Connection connect = ConnectDB.connection(); PreparedStatement ps = connect.prepareStatement(sql)) {

			ps.setInt(1, setting.getMax_borrow_days());
			ps.setDouble(2, setting.getDeposit_fee_per_loan());
			ps.setDouble(3, setting.getLate_fee_per_day());
			ps.setDouble(4, setting.getLost_compensation_fee());
			ps.setDouble(5, setting.getDamaged_compensation_fee());
			ps.setInt(6, setting.getId());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
