package entities;

public class Settings {
	int id;
	int max_borrow_date;
	double borrow_fee_per_loan;
	double late_fee_per_day;
	double deposit_per_loan;
	double lost_compensation_fee;
	double damaged_compensation_fee;

	
	public Settings() {
		super();
	}
	
	public Settings(int id, int max_borrow_date, double borrow_fee_per_loan, double late_fee_per_day,
			double deposit_per_loan, double damaged_compensation_fee, double lost_compensation_fee) {
		super();
		this.id = id;
		this.max_borrow_date = max_borrow_date;
		this.borrow_fee_per_loan = borrow_fee_per_loan;
		this.late_fee_per_day = late_fee_per_day;
		this.deposit_per_loan = deposit_per_loan;
		this.lost_compensation_fee = lost_compensation_fee;
		this.damaged_compensation_fee = damaged_compensation_fee;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getMax_borrow_date() {
		return max_borrow_date;
	}
	
	public void setMax_borrow_date(int max_borrow_date) {
		this.max_borrow_date = max_borrow_date;
	}
	
	public double getBorrow_fee_per_loan() {
		return borrow_fee_per_loan;
	}
	
	public void setBorrow_fee_per_loan(double borrow_fee_per_loan) {
		this.borrow_fee_per_loan = borrow_fee_per_loan;
	}
	
	public double getLate_fee_per_day() {
		return late_fee_per_day;
	}
	
	public void setLate_fee_per_day(double late_fee_per_day) {
		this.late_fee_per_day = late_fee_per_day;
	}
	
	public double getDeposit_per_loan() {
		return deposit_per_loan;
	}
	
	public void setDeposit_per_loan(double deposit_per_loan) {
		this.deposit_per_loan = deposit_per_loan;
	}

	public double getLost_compensation_fee() {
		return lost_compensation_fee;
	}

	public void setLost_compensation_fee(double lost_compensation_fee) {
		this.lost_compensation_fee = lost_compensation_fee;
	}

	public double getDamaged_compensation_fee() {
		return damaged_compensation_fee;
	}

	public void setDamaged_compensation_fee(double damaged_compensation_fee) {
		this.damaged_compensation_fee = damaged_compensation_fee;
	}
	
	
}
