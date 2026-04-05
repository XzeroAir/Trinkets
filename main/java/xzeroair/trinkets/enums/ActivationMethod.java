package xzeroair.trinkets.enums;

public enum ActivationMethod {

    //@formatter:off
    SNEAK("Sneak", 0),
    STAND("Stand", 1),
    ALWAYS("Always", 2),
    NEVER("Never", 99);
    //@formatter:on

    private String name;
    private int id;

    ActivationMethod(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.getName();
    }

}
