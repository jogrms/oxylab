package com.jogrms.oxylab.model;

import java.util.*;

/**
 * Game lab model. Units:
 * - All factor velocity values are represented in "floating point units per second".
 * - All metric values are represented in meters.
 */
public class Lab {
    private Map<Factor.Kind, Factor> factors = new EnumMap<Factor.Kind, Factor>(Factor.Kind.class);
    private List<Population> populations = new LinkedList<Population>();
    private Map<String, Species> species = new HashMap<String, Species>(10);

    // Default lab area: 100 meters squared.
    private double area = 100;

    public Lab() {
        addFactor(Factor.Kind.HUMIDITY, 1);
        addFactor(Factor.Kind.TEMPERATURE, 25);

        // production rate = 0.005 population sizes per second.
        Species moss = addSpecies("moss", 10, 10, 0.005);
        moss.addFactorResponse(new FactorResponse(Factor.Kind.HUMIDITY, 1, 3));
        moss.addFactorResponse(new FactorResponse(Factor.Kind.TEMPERATURE, 25, 10));

        addPopulation("moss");

    }

    /**
     * Update the model.
     * @param dt seconds from the last update.
     */
    public void update(float dt) {
        for (Population p : populations) {
            p.calculate(dt);
        }
        for (Population p : populations) {
            p.update();
        }
        for (Factor f : factors.values()) {
            f.update();
        }
    }

    public double getArea() {
        return area;
    }

    public Factor getFactor(Factor.Kind kind) {
    	return factors.get(kind);
    }
    
    public Map<Factor.Kind, Factor> getFactors() {
        return factors;
    }

    public List<Population> getPopulations() {
        return populations;
    }

    public void addPopulation(String name) {
        Species s = species.get(name);
        if (s == null) {
            return;
        }
        populations.add(new Population(this, s));
    }

    private void addFactor(Factor.Kind kind, double value) {
        Factor f = new Factor(kind, value);
        factors.put(kind, f);
    }

    private Species addSpecies(String name, double size, double density, double birthRate) {
        Species s = new Species(name, size, density, birthRate);
        species.put(name, s);
        return s;
    }
}
