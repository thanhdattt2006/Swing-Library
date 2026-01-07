package entities;

public enum LoanStatus {
    ACTIVE("Active"),       
    COMPLETED("Completed"), 
    LATED("Lated");

    private final String dbValue;

    LoanStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static LoanStatus fromString(String text) {
        for (LoanStatus s : LoanStatus.values()) {
            if (s.dbValue.equalsIgnoreCase(text)) {
                return s;
            }
        }
        return ACTIVE; 
    }
}
