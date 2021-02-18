package sk.durovic.model;

public enum Gear {
    Automatic ("Automatická"),
    Manual ("Manuálna");

    private String value;

    Gear(String value) {
        this.value = value;
    }
}
