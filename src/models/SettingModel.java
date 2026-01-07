package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

	public List<Settings> findAll() {
		List<Settings> list = new ArrayList<>();

		String sql = "SELECT * FROM settings";

		try (Connection connect = ConnectDB.connection();
				PreparedStatement ps = connect.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();) {
			while (rs.next()) {
				Settings s = new Settings();

				s.setId(rs.getInt("id"));
				s.setMax_borrow_days(rs.getInt("max_borrow_days"));
				s.setDeposit_fee_per_loan(rs.getDouble("deposit_fee_per_loan"));
				s.setLate_fee_per_day(rs.getDouble("late_fee_per_day"));
				s.setLost_compensation_fee(rs.getDouble("lost_compensation_fee"));
				s.setDamaged_compensation_fee(rs.getDouble("damaged_compensation_fee"));

				list.add(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public boolean create(Settings settings) {
		boolean result = false;
		try {
			PreparedStatement preparedStatement = ConnectDB.connection().prepareStatement(
					"INSERT INTO settings " + "(max_borrow_days, deposit_fee_per_loan, late_fee_per_day, "
							+ "lost_compensation_fee, damaged_compensation_fee) " + "VALUES (?, ?, ?, ?, ?)");

			preparedStatement.setInt(1, settings.getMax_borrow_days());
			preparedStatement.setDouble(2, settings.getDeposit_fee_per_loan());
			preparedStatement.setDouble(3, settings.getLate_fee_per_day());
			preparedStatement.setDouble(4, settings.getLost_compensation_fee());
			preparedStatement.setDouble(5, settings.getDamaged_compensation_fee());

			result = preparedStatement.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			ConnectDB.disconnect();
		}
		return result;
	}

	public boolean update(Settings settings) {
		boolean result = false;
		try {
			PreparedStatement preparedStatement = ConnectDB.connection()
					.prepareStatement("UPDATE settings SET " + "max_borrow_days = ?, " + "deposit_fee_per_loan = ?, "
							+ "late_fee_per_day = ?, " + "lost_compensation_fee = ?, " + "damaged_compensation_fee = ? "
							+ "WHERE id = ?");

			preparedStatement.setInt(1, settings.getMax_borrow_days());
			preparedStatement.setDouble(2, settings.getDeposit_fee_per_loan());
			preparedStatement.setDouble(3, settings.getLate_fee_per_day());
			preparedStatement.setDouble(4, settings.getLost_compensation_fee());
			preparedStatement.setDouble(5, settings.getDamaged_compensation_fee());
			preparedStatement.setInt(6, settings.getId()); // WHERE id = ?

			result = preparedStatement.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			ConnectDB.disconnect();
		}
		return result;
	}

	public boolean delete(int id) {
		boolean result = false;
		try {
			PreparedStatement preparedStatement = ConnectDB.connection()
					.prepareStatement("DELETE FROM settings WHERE id = ?");

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

}