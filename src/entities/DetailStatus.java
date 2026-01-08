package entities;

public enum DetailStatus {
    GOOD("Good"),    
    DAMAGED("Damaged"), 
    LOST("Lost");       

    private final String dbValue;

    DetailStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static DetailStatus fromString(String text) {
        for (DetailStatus s : DetailStatus.values()) {
            if (s.dbValue.equalsIgnoreCase(text)) {
                return s;
            }
        }
        return GOOD; 
    }
}