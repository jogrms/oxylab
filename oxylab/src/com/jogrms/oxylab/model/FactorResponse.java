package com.jogrms.oxylab.model;

/**
 * Defines a population's response to a factor.
 */
public class FactorResponse {
    private Factor.Kind kind;
    private double idealValue;
    private double radius;

    /**
     *
     * @param kind
     * @param idealValue
     * @param radius
     */
    public FactorResponse(Factor.Kind kind, double idealValue, double radius) {
        this.kind = kind;
        this.idealValue = idealValue;
        this.radius = radius;
    }

    public Factor.Kind getKind() {
        return kind;
    }

    public double getIdealValue() {
        return idealValue;
    }

    public double getRadius() {
        return radius;
    }

}
