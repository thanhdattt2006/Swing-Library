package models;
import java.sql.*;
import entities.Settings;

public class SettingModel {
<<<<<<< HEAD
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
=======
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
>>>>>>> 342b64ba374acbf8700c693efcd70df4263e893c
