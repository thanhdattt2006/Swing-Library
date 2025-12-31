package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entities.Loan_Details;
import entities.Settings;

public class LoanDetailsModel {
    public boolean createLoanDetail(Loan_Details detail) {
        String sql = "INSERT INTO loan_details (loan_master_id, deposit_fee, late_fee, return_date, status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection connect = ConnectDB.connection();
             PreparedStatement preparedStatement = connect.prepareStatement(sql)) {

        	preparedStatement.setInt(1, detail.getLoan_master_id());
        	preparedStatement.setDouble(2, detail.getDeposit_fee());
        	preparedStatement.setDouble(3, detail.getLate_fee());
            if (detail.getReturn_date() != null) {
            	preparedStatement.setDate(4, new java.sql.Date(detail.getReturn_date().getTime()));
            } else {
            	preparedStatement.setNull(4, Types.DATE);
            }
            preparedStatement.setBoolean(5, detail.isStatus());

            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean returnItem(int detailId, double lateFee) {
        String sql = "UPDATE loan_details SET return_date = CURRENT_DATE, late_fee = ?, status = ? WHERE id = ?";
        
        try (Connection connect = ConnectDB.connection();
             PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
            
        	preparedStatement.setDouble(1, lateFee);
        	preparedStatement.setBoolean(2, true);
        	preparedStatement.setInt(3, detailId);

            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public Loan_Details getDetailById(int id) {
        String sql = "SELECT * FROM SETTING LIMIT 1"; 
        try (Connection connect = ConnectDB.connection();
             PreparedStatement preparedStatement = connect.prepareStatement(sql);
             ResultSet result = preparedStatement.executeQuery()) {
            
            if (result.next()) {
                return new Loan_Details(
                	result.getInt("id"),
                	result.getInt("loan_master_id"),
                	result.getDouble("deposit_fee"),
                	result.getDouble("late_fee"),
                	result.getDate("return_date"),
                	result.getBoolean("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}