/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.ui.GASetup;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.evors.genesis.GAParameters;
import org.evors.genesis.GAParameters.GABuilder;

/**
 *
 * @author miles
 */
public final class GAParametersComp {
    
    private int population;
    
    private final GABuilder builder = new GAParameters.GABuilder();

    public GAParametersComp() {
        GAParameters initParam = builder.build();
        updateParams(initParam);
    }

    public void updateParams(GAParameters param) {
        setPopulation(param.getPopulationSize());
        setGenerations(param.getGenerations());
        setMutationRate(param.getMutrate());
        setCrossoverRate(param.getCrossrate());
        setDemeSize(param.getDemesize());
    }

    
    
    public GAParametersComp(int population, int generations, float mutationRate,
            float crossoverRate, int demeSize) {
        setPopulation(population);
        setGenerations(generations);
        setMutationRate(mutationRate);
        setCrossoverRate(crossoverRate);
        setDemeSize(demeSize);
    }
    
    

    /**
     * Get the value of parameters
     *
     * @return the value of parameters
     */
    public GAParameters getParameters() {
        return builder.build();
    }

    public static final String PROP_POPULATION = "population";

    /**
     * Get the value of population
     *
     * @return the value of population
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Set the value of population
     *
     * @param population new value of population
     */
    public void setPopulation(int population) {
        int oldPopulation = this.population;
        this.population = population;
        builder.setPopulationSize(population);
        propertyChangeSupport.firePropertyChange(PROP_POPULATION, oldPopulation,
                population);
    }

    private transient final PropertyChangeSupport propertyChangeSupport
            = new PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(
            PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(
            PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private int generations;

    public static final String PROP_GENERATIONS = "generations";

    /**
     * Get the value of generations
     *
     * @return the value of generations
     */
    public int getGenerations() {
        return generations;
    }

    /**
     * Set the value of generations
     *
     * @param generations new value of generations
     */
    public void setGenerations(int generations) {
        int oldGenerations = this.generations;
        this.generations = generations;
        builder.setGenerations(generations);
        propertyChangeSupport.firePropertyChange(PROP_GENERATIONS,
                oldGenerations, generations);
    }

    private float mutationRate;

    public static final String PROP_MUTATIONRATE = "mutationRate";

    /**
     * Get the value of mutationRate
     *
     * @return the value of mutationRate
     */
    public float getMutationRate() {
        return mutationRate;
    }

    /**
     * Set the value of mutationRate
     *
     * @param mutationRate new value of mutationRate
     */
    public void setMutationRate(float mutationRate) {
        float oldMutationRate = this.mutationRate;
        this.mutationRate = mutationRate;
        builder.setMutrate((float) mutationRate);
        propertyChangeSupport.firePropertyChange(PROP_MUTATIONRATE,
                oldMutationRate, mutationRate);
    }

    private float crossoverRate;

    public static final String PROP_CROSSOVERRATE = "crossoverRate";

    /**
     * Get the value of crossoverRate
     *
     * @return the value of crossoverRate
     */
    public float getCrossoverRate() {
        return crossoverRate;
    }

    /**
     * Set the value of crossoverRate
     *
     * @param crossoverRate new value of crossoverRate
     */
    public void setCrossoverRate(float crossoverRate) {
        float oldCrossoverRate = this.crossoverRate;
        this.crossoverRate = crossoverRate;
        builder.setCrossrate(crossoverRate);
        propertyChangeSupport.firePropertyChange(PROP_CROSSOVERRATE,
                oldCrossoverRate, crossoverRate);
    }

    private int demeSize;

    public static final String PROP_DEMESIZE = "demeSize";

    /**
     * Get the value of demeSize
     *
     * @return the value of demeSize
     */
    public int getDemeSize() {
        return demeSize;
    }

    /**
     * Set the value of demeSize
     *
     * @param demeSize new value of demeSize
     */
    public void setDemeSize(int demeSize) {
        int oldDemeSize = this.demeSize;
        this.demeSize = demeSize;
        builder.setDemesize(demeSize);
        propertyChangeSupport.firePropertyChange(PROP_DEMESIZE, oldDemeSize,
                demeSize);
    }

}
