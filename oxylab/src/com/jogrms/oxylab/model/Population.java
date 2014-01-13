package com.jogrms.oxylab.model;

/**
 * Current state of a population.
 */
public class Population implements ValueModel {
    private Lab lab;
    private Species species;
    private double size;
    private double delta;
    
    private double maxSize;

    public Population(Lab lab, Species species) {
        this.lab = lab;
        this.species = species;
        this.size = species.getInitialSize();
        this.maxSize = lab.getArea() * species.getMaxDensity();
    }

    public void calculate(float dt) {
        species.calculate(this, dt);
    }

    public void update() {
        size += size * delta;
        if (size < 0f) {
        	size = 0f;
        }
        if (size > maxSize) {
        	size = maxSize;
        }
        delta = 0;
    }

    public Lab getLab() {
        return lab;
    }

    public Species getSpecies() {
        return species;
    }

    public double getSize() {
        return size;
    }

    public void addDelta(double delta) {
        this.delta = delta;
    }

	@Override
	public double getValue() {
		return size;
	}
}
