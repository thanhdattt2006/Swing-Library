package models;

import java.sql.*;

import entities.Settings;
import entities.Loan_Master;

class LoanMasterModel {
	public int createLoan(Loan_Master loan) {
		String sql = "INSERT INTO loan_master (account_id, borrow_date, due_date, "
				+ "total_compensation_fee, total_deposit_fee, total_late_fee, total_quantity, status) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection connect = ConnectDB.connection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			preparedStatement.setInt(1, loan.getAccount_id());
			preparedStatement.setDate(2, new java.sql.Date(loan.getBorrow_date().getTime()));
			preparedStatement.setDate(3, new java.sql.Date(loan.getDue_date().getTime()));
			preparedStatement.setDouble(4, loan.getTotal_compensation_fee());
			preparedStatement.setDouble(5, loan.getTotal_deposit_fee());
			preparedStatement.setDouble(6, loan.getTotal_late_fee());
			preparedStatement.setInt(7, loan.getTotal_quantity());
			preparedStatement.setString(8, loan.getStatus().toString());

			int result = preparedStatement.executeUpdate();
			if (result > 0) {
				try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
					if (rs.next()) {
						return rs.getInt(1);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public boolean updateStatus(int loanId, Loan_Master.Status status) {
		String sql = "UPDATE loan_master SET status = ? WHERE id = ?";
		try (Connection connect = ConnectDB.connection(); PreparedStatement preparedStatement = connect.prepareStatement(sql)) {

			preparedStatement.setString(1, status.toString());
			preparedStatement.setInt(2, loanId);
			return preparedStatement.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
