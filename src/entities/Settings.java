package entities;

public class Settings {
	private String id;
	private int max_borrow_days;
	private double borrow_fee_per_loan;
	private double late_fee_per_day;
	private double deposit_per_load;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getMax_borrow_days() {
		return max_borrow_days;
	}

	public void setMax_borrow_days(int max_borrow_days) {
		this.max_borrow_days = max_borrow_days;
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

	public double getDeposit_per_load() {
		return deposit_per_load;
	}

	public void setDeposit_per_load(double deposit_per_load) {
		this.deposit_per_load = deposit_per_load;
	}

	public Settings(String id, int max_borrow_days, double borrow_fee_per_loan, double late_fee_per_day,
			double deposit_per_load) {
		super();
		this.id = id;
		this.max_borrow_days = max_borrow_days;
		this.borrow_fee_per_loan = borrow_fee_per_loan;
		this.late_fee_per_day = late_fee_per_day;
		this.deposit_per_load = deposit_per_load;
	}

	public Settings() {
		super();
	}

	@Override
	public String toString() {
		return "Settings [id=" + id + ", max_borrow_days=" + max_borrow_days + ", borrow_fee_per_loan="
				+ borrow_fee_per_loan + ", late_fee_per_day=" + late_fee_per_day + ", deposit_per_load="
				+ deposit_per_load + "]";
	}

}
