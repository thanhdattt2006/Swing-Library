package models;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

import entities.LoanStatus;
import entities.Loan_Master;
import entities.Settings;

public class LoanMasterModel {
	private Loan_Master mapResultSet(ResultSet rs) throws SQLException {
        Loan_Master master = new Loan_Master();
        master.setId(rs.getInt("id"));
        master.setAccount_id(rs.getInt("account_id"));
        master.setBorrow_date(rs.getDate("borrow_date"));
        master.setUsername(rs.getString("username"));
        master.setEmployeeIdDisplay(rs.getString("employee_id")); 
        String dbStatusString = rs.getString("status");
        String dbStatus = rs.getString("status"); 
        master.setStatus(LoanStatus.fromString(dbStatus));
        master.setTotal_deposit_fee(rs.getDouble("total_late_fee"));
        return master;
    }

	public List<Loan_Master> findAll() {
	    List<Loan_Master> list = new ArrayList<>();
	    String sql = "SELECT m.*, a.username, a.employee_id " +
	                 "FROM loan_master m " +
	                 "JOIN account a ON m.account_id = a.id " + 
	                 "ORDER BY m.borrow_date DESC";

	    try (Connection conn = ConnectDB.connection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {
	            list.add(mapResultSet(rs));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return list;
	}

    public Loan_Master findById(int id) {
        String sql = "SELECT * FROM loan_master WHERE id = ?";
        try (Connection conn = ConnectDB.connection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapResultSet(rs);
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public List<Loan_Master> search(String keyword, String status) {
        List<Loan_Master> list = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT l.*, a.username, a.employee_id ");
        sql.append("FROM loan_master l ");
        sql.append("JOIN account a ON l.account_id = a.id ");
        sql.append("WHERE 1=1 "); 

        if (keyword != null && !keyword.isEmpty()) {
            sql.append("AND (a.employee_id LIKE ? OR a.username LIKE ?) ");
        }

        if (status != null && !status.equals("All")) {
            sql.append("AND l.status = ? ");
        }

        sql.append("ORDER BY l.borrow_date DESC");

        try (Connection conn = ConnectDB.connection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (keyword != null && !keyword.isEmpty()) {
                String searchPattern = "%" + keyword + "%";
                ps.setString(index++, searchPattern);
                ps.setString(index++, searchPattern);
            }

            if (status != null && !status.equals("All")) {
                ps.setString(index++, status);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
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

	public boolean updateStatus(int loanId, String status) {
		String sql = "UPDATE loan_details SET status = ? WHERE id = ?";
		try (Connection connect = ConnectDB.connection(); PreparedStatement preparedStatement = connect.prepareStatement(sql)) {

			preparedStatement.setString(1, status.toString());
			preparedStatement.setInt(2, loanId);
			return preparedStatement.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void updateMasterAfterReturn(int loanMasterId) {
	    Connection conn = null;
	    try {
	        conn = ConnectDB.connection();

	        String sqlSum = "SELECT SUM(late_fee), SUM(compensation_fee) FROM loan_details WHERE loan_master_id = ?";
	        double totalLate = 0;
	        double totalComp = 0;
	        
	        try (PreparedStatement ps = conn.prepareStatement(sqlSum)) {
	            ps.setInt(1, loanMasterId);
	            ResultSet rs = ps.executeQuery();
	            if (rs.next()) {
	                totalLate = rs.getDouble(1);
	                totalComp = rs.getDouble(2);
	            }
	        }
	        
	        String sqlCheck = "SELECT COUNT(*) FROM loan_details WHERE loan_master_id = ? AND return_date IS NULL";
	        boolean isFinished = false;
	        try (PreparedStatement ps = conn.prepareStatement(sqlCheck)) {
	            ps.setInt(1, loanMasterId);
	            ResultSet rs = ps.executeQuery();
	            if (rs.next()) {
	                isFinished = (rs.getInt(1) == 0);
	            }
	        }

	        String statusMaster = isFinished ? "Completed" : "Active";
	        String sqlUpdate = "UPDATE loan_master SET total_late_fee = ?, total_compensation_fee = ?, status = ? WHERE id = ?";
	        
	        try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
	            ps.setDouble(1, totalLate);
	            ps.setDouble(2, totalComp);
	            ps.setString(3, statusMaster);
	            ps.setInt(4, loanMasterId);
	            ps.executeUpdate();
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
}
