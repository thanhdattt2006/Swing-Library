package entities;

import java.io.ObjectInputFilter.Status;
import java.sql.Date;

public class Loan_Master {
	int id;
	int account_id;
	Date borrow_date;
	Date due_date;
	double total_compensation_fee;
	double total_deposit_fee;
	double total_late_fee;
	int total_quantity;
	private Status status;

	public enum Status {
		Borrowing, Finished, Lated
	}

	public Loan_Master() {
		super();
	}

	public Loan_Master(int id, int account_id, Date borrow_date, Date due_date, double total_compensation_fee,
			double total_deposit_fee, double total_late_fee, int total_quantity) {
		super();
		this.id = id;
		this.account_id = account_id;
		this.borrow_date = borrow_date;
		this.due_date = due_date;
		this.total_compensation_fee = total_compensation_fee;
		this.total_deposit_fee = total_deposit_fee;
		this.total_late_fee = total_late_fee;
		this.total_quantity = total_quantity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAccount_id() {
		return account_id;
	}

	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}

	public Date getBorrow_date() {
		return borrow_date;
	}

	public void setBorrow_date(Date borrow_date) {
		this.borrow_date = borrow_date;
	}

	public Date getDue_date() {
		return due_date;
	}

	public void setDue_date(Date due_date) {
		this.due_date = due_date;
	}

	public double getTotal_compensation_fee() {
		return total_compensation_fee;
	}

	public void setTotal_compensation_fee(double total_compensation_fee) {
		this.total_compensation_fee = total_compensation_fee;
	}

	public double getTotal_deposit_fee() {
		return total_deposit_fee;
	}

	public void setTotal_deposit_fee(double total_deposit_fee) {
		this.total_deposit_fee = total_deposit_fee;
	}

	public double getTotal_late_fee() {
		return total_late_fee;
	}

	public void setTotal_late_fee(double total_late_fee) {
		this.total_late_fee = total_late_fee;
	}

	public int getTotal_quantity() {
		return total_quantity;
	}

	public void setTotal_quantity(int total_quantity) {
		this.total_quantity = total_quantity;
	};

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Loan_Master [id=" + id + ", account_id=" + account_id + ", borrow_date=" + borrow_date + ", due_date="
				+ due_date + ", total_compensation_fee=" + total_compensation_fee + ", total_deposit_fee="
				+ total_deposit_fee + ", total_late_fee=" + total_late_fee + ", total_quantity=" + total_quantity
				+ ", status=" + status + "]";
	}

}
