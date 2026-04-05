package xzeroair.trinkets.enums;

public enum BowScalingMode {


    //@formatter:off
    LINEAR("linear", 0),
    SOFT("soft", 1),
    KINETIC("kinetic", 2);
    //@formatter:on

    private String name;
    private int id;

    BowScalingMode(String name, int id) {
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
