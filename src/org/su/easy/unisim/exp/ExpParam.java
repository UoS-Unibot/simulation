package org.su.easy.unisim.exp;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Stores all experimental parameters for easy editing.
 *
 * @author Miles
 */
public class ExpParam extends HashMap {

    public ArrayList<String> keys = new ArrayList<>();


    public ExpParam() {
        put("AGENT_EYE_WIDTH_RAD", 1.0f / 4.0f);
        keys.add("AGENT_EYE_WIDTH_RAD");
        put("AGENT_RADIUS", 15);
        keys.add("AGENT_RADIUS");
        put("AGENT_SENSOR_LENGTH", 220);
        keys.add("AGENT_SENSOR_LENGTH");
        put("AGENT_START_Y", 240);
        keys.add("AGENT_START_Y");

        put("CIRCLE_RADIUS", 13);
        keys.add("CIRCLE_RADIUS");
        put("CIRCLE_START_Y", 0);
        keys.add("CIRCLE_START_Y");

        put("GRID_WIDTH", 400);
        keys.add("GRID_WIDTH");
        put("GRID_HEIGHT", 275);
        keys.add("GRID_HEIGHT");
        put("GRID_CENTER", 200);
        keys.add("GRID_CENTER");

        put("EULER_TIMESTEP", 0.1);
        keys.add("EULER_TIMESTEP");

        put("FITNESS_EVAL_CIRC_DISP", 60f);
        keys.add("FITNESS_EVAL_CIRC_DISP");
        put("FITNESS_EVAL_H_VELOCITY", 6f);
        keys.add("FITNESS_EVAL_H_VELOCITY");
        put("FITNESS_EVAL_V_VELOCITY", 5f);
        keys.add("FITNESS_EVAL_V_VELOCITY");
        put("FITNESS_EVAL_NTRIALS", 12);
        keys.add("FITNESS_EVAL_NTRIALS");

        put("FITNESS_RAND_CIRC_DISP", 60f);
        keys.add("FITNESS_RAND_CIRC_DISP");
        put("FITNESS_RAND_H_VELOCITY", 6f);
        keys.add("FITNESS_RAND_H_VELOCITY");
        put("FITNESS_RAND_V_VELOCITY", 5f);
        keys.add("FITNESS_RAND_V_VELOCITY");
        put("FITNESS_RAND_NTRIALS", 24);
        keys.add("FITNESS_RAND_NTRIALS");

        put("GA_CROSSOVER_P_CROSS", 0.075);
        keys.add("GA_CROSSOVER_P_CROSS");
        put("GA_P_MUT", 0.4);
        keys.add("GA_P_MUT");
        put("GA_MAXFIT", 0.9);
        keys.add("GA_MAXFIT");
        put("GA_MAXGEN", 200);
        keys.add("GA_MAXGEN");
        put("GA_NPOP", 100);
        keys.add("GA_NPOP");
        put("GA_DEME", 5);
        keys.add("GA_DEME");

        put("UPDATE_SUMMARY", true);
        keys.add("UPDATE_SUMMARY");
        put("UPDATE_PLOT", true);
        keys.add("UPDATE_PLOT");

        put("GA_RUNTOMAXGEN", true);
        keys.add("GA_RUNTOMAXGEN");

        put("UPDATE_PLOT_FREQ", 1);
        keys.add("UPDATE_PLOT_FREQ");
        put("UPDATE_SUMMARY_FREQ", 1);
        keys.add("UPDATE_SUMMARY_FREQ");

        put("N_SCRIPTED_RUNS", 100);
        keys.add("N_SCRIPTED_RUNS");

    }

    public String getTrialsStr() {
        StringBuilder r = new StringBuilder("Trial\tStartingX\tDX\n");
        float dispRange = (float) get("FITNESS_EVAL_CIRC_DISP");
        float angRange = -(float) get("FITNESS_EVAL_H_VELOCITY");
        int nTrials = (int) get("FITNESS_EVAL_NTRIALS");
        float dHVelo = ((float) get("FITNESS_EVAL_H_VELOCITY") * 2.0f) / (nTrials - 1);
        float distClip = dispRange;
        for (int i = 0; i < nTrials; i++) {
            float disp = (i * (2 * dispRange / (nTrials - 1)) - dispRange);
            float ang = (int) Math.round(angRange + (double) (i * dHVelo));
            r.append(String.format("%d\t%f\t%f\n", i + 1, disp, ang));
        }
        return r.toString();
    }

    @Override
    public String toString() {
        StringBuilder r = new StringBuilder();
        for (String key : keys) {
            r.append(String.format("%s\t%s\n", key, this.get(key)));
        }
        return r.toString();
    }

}
