package com.jogrms.oxylab.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes species and its interactions with other species and environmental factors.
 */
public class Species {
    private String name;
    private List<FactorResponse> factorResponses = new ArrayList<FactorResponse>(5);
    private double initialSize;
    private double maxDensity;
    private double productionRate;

    public Species(String name, double initialSize, double maxDensity, double productionRate) {
        this.name = name;
        this.initialSize = initialSize;
        this.maxDensity = maxDensity;
        this.productionRate = productionRate;
    }

    public void calculate(Population p, float dt) {
        double squaresSum = 0;
        for (FactorResponse fr : factorResponses) {
            double rate = (p.getLab().getFactor(fr.getKind()).getValue() - fr.getIdealValue()) / fr.getRadius();
            squaresSum += rate * rate;
        }
        p.addDelta((1 - squaresSum) * p.getSize() * productionRate * dt);
    }

    public String getName() {
        return name;
    }

    public double getInitialSize() {
        return initialSize;
    }

    public double getMaxDensity() {
        return maxDensity;
    }

    public double getProductionRate() {
        return productionRate;
    }

    public void addFactorResponse(FactorResponse factorResponse) {
        factorResponses.add(factorResponse);
    }
}
