package com.jogrms.oxylab.model;

/**
 * Environmental factor model.
 */
public class Factor implements ValueModel {
    public static enum Kind {
        TEMPERATURE,
        HUMIDITY
    }

    private Kind kind;
    private double value;
    private double delta;

    public Factor(Kind kind, double value) {
        this.value = value;
        this.kind = kind;
    }

    public Kind getKind() {
        return kind;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void addDelta(double delta) {
        this.delta += delta;
    }

    public void update() {
        value += delta;
        delta = 0;
    }
}
