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
                	result.getInt("max_borrow_days"),  // ← SỬA: max_borrow_date → max_borrow_days
                	result.getDouble("deposit_fee_per_loan"),  // ← SỬA: borrow_fee_per_loan → deposit_fee_per_loan
                	result.getDouble("late_fee_per_day"),
                	result.getDouble("lost_compensation_fee"),  // ← SỬA: deposit_per_loan → lost_compensation_fee
                	result.getDouble("damaged_compensation_fee")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updateSettings(Settings setting) {
        String sql = "UPDATE SETTING SET max_borrow_days = ?, deposit_fee_per_loan = ?, "
                   + "late_fee_per_day = ?, lost_compensation_fee = ?, "
                   + "damaged_compensation_fee = ? WHERE id = ?";  // ← SỬA: thêm 2 cột còn thiếu
        try (Connection connect = ConnectDB.connection();
             PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
            
        	preparedStatement.setInt(1, setting.getMax_borrow_days());  // ← SỬA tên method
        	preparedStatement.setDouble(2, setting.getDeposit_fee_per_loan());  // ← SỬA tên method
        	preparedStatement.setDouble(3, setting.getLate_fee_per_day());
        	preparedStatement.setDouble(4, setting.getLost_compensation_fee());  // ← SỬA số thứ tự: 4 thay vì trùng
        	preparedStatement.setDouble(5, setting.getDamaged_compensation_fee());  // ← SỬA số thứ tự: 5 thay vì trùng
        	preparedStatement.setInt(6, setting.getId());  // ← SỬA số thứ tự: 6 thay vì 5
            
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}