package sk.durovic.model;

public enum Category {
    HATCHBACK ("Hatchback"),
    SEDAN ("Sedan"),
    COMBI ("Combi"),
    CROSSOVER ("Crossover"),
    SUV ("SUV"),
    WORK ("Pracovné");

    private String value;

    Category(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
