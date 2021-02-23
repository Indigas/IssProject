package sk.durovic.model;

public enum Fuel {
    Diesel ("Diesel"),
    Gasoline ("Benzín"),
    Electric ("Elektrické");

    private String value;

    Fuel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
