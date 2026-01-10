package models;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entities.DetailStatus;
import entities.Loan_Details;
import entities.Loan_Master;
import entities.Settings;

public class LoanDetailsModel {

	private Loan_Details mapRow(ResultSet rs) throws SQLException {
		Loan_Details d = new Loan_Details();
		d.setId(rs.getInt("id"));
		d.setLoan_master_id(rs.getInt("loan_master_id"));
		d.setBook_id(rs.getInt("book_id"));
		d.setReturn_date(rs.getDate("return_date"));
		String dbStatus = rs.getString("status");
		d.setStatus(DetailStatus.fromString(dbStatus));
		d.setLate_fee(rs.getDouble("late_fee"));
		d.setCompensation_fee(rs.getDouble("compensation_fee"));

		return d;
	}

	public List<Loan_Details> findAll() {
		List<Loan_Details> list = new ArrayList<>();
		String sql = "SELECT * FROM loan_details";
		try (Connection conn = ConnectDB.connection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql)) {
			while (rs.next())
				list.add(mapRow(rs));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public Loan_Details findById(int id) {
		String sql = "SELECT * FROM loan_details WHERE id = ?";
		try (Connection conn = ConnectDB.connection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return mapRow(rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Loan_Details> findByEmployeeId(int accountId) {
		List<Loan_Details> list = new ArrayList<>();

		String sql = "SELECT d.*, b.title " + "FROM loan_details d " + "JOIN loan_master m ON d.loan_master_id = m.id "
				+ "JOIN book b ON d.book_id = b.id " + "WHERE m.account_id = ?";

		try (Connection conn = ConnectDB.connection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, accountId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Loan_Details d = new Loan_Details();

				d.setId(rs.getInt("id"));
				d.setLoan_master_id(rs.getInt("loan_master_id"));
				d.setBook_id(rs.getInt("book_id"));
				d.setBookTitle(rs.getString("title")); // ✅ QUAN TRỌNG

				d.setReturn_date(rs.getDate("return_date"));
				d.setStatus(DetailStatus.fromString(rs.getString("status")));
				d.setLate_fee(rs.getDouble("late_fee"));
				d.setCompensation_fee(rs.getDouble("compensation_fee"));

				list.add(d);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<Loan_Details> findByStatus(String status) {
		List<Loan_Details> list = new ArrayList<>();
		String sql = "SELECT * FROM loan_details WHERE status = ?";
		try (Connection conn = ConnectDB.connection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, status);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				list.add(mapRow(rs));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Loan_Details> findByMasterId(int masterId) {
		List<Loan_Details> list = new ArrayList<>();
		String sql = "SELECT d.*, b.title " + "FROM loan_details d " + "JOIN book b ON d.book_id = b.id "
				+ "WHERE d.loan_master_id = ?";

		try (Connection conn = ConnectDB.connection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, masterId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Loan_Details d = new Loan_Details();
				d.setId(rs.getInt("id"));
				d.setLoan_master_id(rs.getInt("loan_master_id"));
				d.setBook_id(rs.getInt("book_id"));
				d.setBookTitle(rs.getString("title"));

				String statusStr = rs.getString("status");
				d.setStatus(DetailStatus.fromString(statusStr));

				d.setReturn_date(rs.getDate("return_date"));

				d.setLate_fee(rs.getDouble("late_fee"));
				d.setCompensation_fee(rs.getDouble("compensation_fee"));

				list.add(d);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean createLoanDetail(Loan_Details detail) {
		String sql = "INSERT INTO loan_details (loan_master_id, deposit_fee, late_fee, return_date, status) VALUES (?, ?, ?, ?, ?)";
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
			preparedStatement.setString(5, detail.getStatus().getDbValue());
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
			preparedStatement.setString(2, entities.DetailStatus.GOOD.getDbValue());
			preparedStatement.setInt(3, detailId);

			return preparedStatement.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Loan_Details getDetailById(int id) {
		String sql = "SELECT * FROM loan_details WHERE id = ?";

		try (Connection connect = ConnectDB.connection()) {
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, id);

			ResultSet result = preparedStatement.executeQuery();

			if (result.next()) {
				Loan_Details d = new Loan_Details();

				d.setId(result.getInt("id"));
				d.setLoan_master_id(result.getInt("loan_master_id"));
				d.setBook_id(result.getInt("book_id"));
				d.setLate_fee(result.getDouble("late_fee"));
				d.setCompensation_fee(result.getDouble("compensation_fee"));
				d.setReturn_date(result.getDate("return_date"));
				String dbStatus = result.getString("status");
				d.setStatus(entities.DetailStatus.fromString(dbStatus));

				return d;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void checkInBook(int detailId, int bookId, String statusUI, LocalDate returnDate, double lateFee,
			double compFee) {
		Connection conn = null;
		PreparedStatement psDetail = null;
		PreparedStatement psBook = null;

		try {
			conn = ConnectDB.connection();
			conn.setAutoCommit(false);

			String statusEnum = "Good";
			if (statusUI != null) {
				if (statusUI.equalsIgnoreCase("Lost"))
					statusEnum = "Lost";
				else if (statusUI.equalsIgnoreCase("Damaged"))
					statusEnum = "Damaged";
				else if (statusUI.equalsIgnoreCase("Repaired"))
					statusEnum = "Repaired";
			}

			String sqlUpdateDetail = "UPDATE loan_details SET return_date = ?, status = ?, late_fee = ?, compensation_fee = ? WHERE id = ?";
			psDetail = conn.prepareStatement(sqlUpdateDetail);

			psDetail.setDate(1, java.sql.Date.valueOf(returnDate));
			psDetail.setString(2, statusEnum);
			psDetail.setDouble(3, lateFee);
			psDetail.setDouble(4, compFee);
			psDetail.setInt(5, detailId);

			int rowsDetail = psDetail.executeUpdate();
			if (rowsDetail == 0) {
				throw new SQLException("Error: Loan slip details not found = " + detailId);
			}

			if ("Good".equals(statusEnum) || "Repaired".equals(statusEnum)) {
				String sqlUpdateBook = "UPDATE book SET available_quantity = available_quantity + 1 WHERE id = ?";
				psBook = conn.prepareStatement(sqlUpdateBook);
				psBook.setInt(1, bookId);

				int rowsBook = psBook.executeUpdate();
				if (rowsBook == 0) {
					System.out.println("Warning: No book ID = " + bookId + " was found to add to the inventory.");
				}
			} else {
				System.out.println("Lost/Damaged Books -> Inventory will not be added back.");
			}

			conn.commit();
			System.out.println("Check-in successful! (Detail ID: " + detailId + ", Fee: " + lateFee + ")");

		} catch (SQLException e) {
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
			throw new RuntimeException("Check-in error: " + e.getMessage());
		} finally {
			try {
				if (psDetail != null)
					psDetail.close();
				if (psBook != null)
					psBook.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}