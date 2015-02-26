package org.unisim.exp.params;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

/**
 * Stores GA and experimental parameters.
 *
 * @author Miles Bryant (mb459 at sussex.ac.uk)
 */
public class Parameters {

    public static Parameters fromJSONFile(File file) throws IOException {
        return new ObjectMapper().readValue(file, Parameters.class);
    }

    public void saveToFile(File file) throws IOException {
        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(file, this);
    }

    private double controller_timestep = 0.1;
    private int fitness_n_trials = 1;
    private boolean fitness_terminate_trial_on_collision = true;
    private double ga_mutrate = 0.4;
    private double ga_crossrate = 0.05;
    private int ga_population = 100;
    private int ga_generations = 100;
    private int ga_demesize = 5;

    public double getController_timestep() {
        return controller_timestep;
    }

    public void setController_timestep(double controller_timestep) {
        this.controller_timestep = controller_timestep;
    }

    public int getFitness_n_trials() {
        return fitness_n_trials;
    }

    public void setFitness_n_trials(int fitness_n_trials) {
        this.fitness_n_trials = fitness_n_trials;
    }

    public boolean isFitness_terminate_trial_on_collision() {
        return fitness_terminate_trial_on_collision;
    }

    public void setFitness_terminate_trial_on_collision(boolean fitness_terminate_trial_on_collision) {
        this.fitness_terminate_trial_on_collision = fitness_terminate_trial_on_collision;
    }

    public double getGa_mutrate() {
        return ga_mutrate;
    }

    public void setGa_mutrate(double ga_mutrate) {
        this.ga_mutrate = ga_mutrate;
    }

    public double getGa_crossrate() {
        return ga_crossrate;
    }

    public void setGa_crossrate(double ga_crossrate) {
        this.ga_crossrate = ga_crossrate;
    }

    public int getGa_population() {
        return ga_population;
    }

    public void setGa_population(int ga_population) {
        this.ga_population = ga_population;
    }

    public int getGa_generations() {
        return ga_generations;
    }

    public void setGa_generations(int ga_generations) {
        this.ga_generations = ga_generations;
    }

    public int getGa_demesize() {
        return ga_demesize;
    }

    public void setGa_demesize(int ga_demesize) {
        this.ga_demesize = ga_demesize;
    }

}
