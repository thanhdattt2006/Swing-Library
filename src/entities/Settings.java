package entities;

public class Settings {
	private int id;
	private int max_borrow_days;
	private double deposit_fee_per_loan;
	private double late_fee_per_day;
	private double lost_compensation_fee;
	private double damaged_compensation_fee;

	public Settings() {
		super();
	}

	public Settings(int id, int max_borrow_days, double deposit_fee_per_loan, double late_fee_per_day,
			double lost_compensation_fee, double damaged_compensation_fee) {
		super();
		this.id = id;
		this.max_borrow_days = max_borrow_days;
		this.deposit_fee_per_loan = deposit_fee_per_loan;
		this.late_fee_per_day = late_fee_per_day;
		this.lost_compensation_fee = lost_compensation_fee;
		this.damaged_compensation_fee = damaged_compensation_fee;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMax_borrow_days() {
		return max_borrow_days;
	}

	public void setMax_borrow_days(int max_borrow_days) {
		this.max_borrow_days = max_borrow_days;
	}

	public double getDeposit_fee_per_loan() {
		return deposit_fee_per_loan;
	}

	public void setDeposit_fee_per_loan(double deposit_fee_per_loan) {
		this.deposit_fee_per_loan = deposit_fee_per_loan;
	}

	public double getLate_fee_per_day() {
		return late_fee_per_day;
	}

	public void setLate_fee_per_day(double late_fee_per_day) {
		this.late_fee_per_day = late_fee_per_day;
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

	@Override
	public String toString() {
		return "Settings [id=" + id + ", max_borrow_days=" + max_borrow_days + ", deposit_fee_per_loan="
				+ deposit_fee_per_loan + ", late_fee_per_day=" + late_fee_per_day + ", lost_compensation_fee="
				+ lost_compensation_fee + ", damaged_compensation_fee=" + damaged_compensation_fee + "]";
	}

}