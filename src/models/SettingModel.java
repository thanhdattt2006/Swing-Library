package models;

import java.sql.*;

import entities.Settings;

public class SettingModel {
    public Settings getSettings() {
        String sql = "SELECT * FROM SETTING LIMIT 1"; 
        try (Connection connect = ConnectDB.connection();
             PreparedStatement preparedStatement = connect.prepareStatement(sql);
             ResultSet result = preparedStatement.executeQuery()) {
            
            if (result.next()) {
                return new Settings(
                	result.getInt("id"),
                	result.getInt("max_borrow_date"), 
                	result.getDouble("borrow_fee_per_loan"),
                	result.getDouble("late_fee_per_day"),
                	result.getDouble("deposit_per_loan"),
                	result.getDouble("lost_compensation_fee"),
                	result.getDouble("damaged_compensation_fee")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateSettings(Settings setting) {
        String sql = "UPDATE SETTING SET max_borrow_date = ?, borrow_fee_per_loan = ?, "
                   + "late_fee_per_day = ?, deposit_per_loan = ? WHERE id = ?";
        try (Connection connect = ConnectDB.connection();
             PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
            
        	preparedStatement.setInt(1, setting.getMax_borrow_date());
        	preparedStatement.setDouble(2, setting.getBorrow_fee_per_loan());
        	preparedStatement.setDouble(3, setting.getLate_fee_per_day());
        	preparedStatement.setDouble(4, setting.getDeposit_per_loan());
        	preparedStatement.setDouble(4, setting.getLost_compensation_fee());
        	preparedStatement.setDouble(4, setting.getDamaged_compensation_fee());
        	preparedStatement.setInt(5, setting.getId());
            
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
