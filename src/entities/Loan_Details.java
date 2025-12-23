package entities;

import java.sql.Date;

public class Loan_Details {
	int id;
	int loan_master_id;
	double deposit_fee;
	double late_fee;
	Date return_date;
	boolean status;

	public Loan_Details() {
		super();
	}

	public Loan_Details(int id, int loan_master_id, double deposit_fee, double late_fee, Date return_date,
			boolean status) {
		super();
		this.id = id;
		this.loan_master_id = loan_master_id;
		this.deposit_fee = deposit_fee;
		this.late_fee = late_fee;
		this.return_date = return_date;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLoan_master_id() {
		return loan_master_id;
	}

	public void setLoan_master_id(int loan_master_id) {
		this.loan_master_id = loan_master_id;
	}

	public double getDeposit_fee() {
		return deposit_fee;
	}

	public void setDeposit_fee(double deposit_fee) {
		this.deposit_fee = deposit_fee;
	}

	public double getLate_fee() {
		return late_fee;
	}

	public void setLate_fee(double late_fee) {
		this.late_fee = late_fee;
	}

	public Date getReturn_date() {
		return return_date;
	}

	public void setReturn_date(Date return_date) {
		this.return_date = return_date;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Loan_Details [id=" + id + ", loan_master_id=" + loan_master_id + ", deposit_fee=" + deposit_fee
				+ ", late_fee=" + late_fee + ", return_date=" + return_date + ", status=" + status + "]";
	}
}
