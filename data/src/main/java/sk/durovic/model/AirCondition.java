package sk.durovic.model;

public enum AirCondition {
    MANUAL ("manuálna"),
    AUTOMATIC ("automatická"),
    NO ("bez klimatizácie");

    private String value;

    AirCondition(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
