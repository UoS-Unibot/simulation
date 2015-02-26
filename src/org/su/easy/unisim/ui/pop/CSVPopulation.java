/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.ui.pop;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import org.su.easy.unisim.genesis.RobotGenotype;
import org.su.easy.unisim.sim.world.json.JSONWorld;
import org.su.easy.unisim.simulation.core.SimulationWorld;
import org.su.easy.unisim.simulation.robot.ctrnn.CTRNNLayout;
import org.su.easy.unisim.simulation.robot.ctrnn.jsonIO.JSONCTRNNLayout;

/**
 *
 * @author miles
 */
public class CSVPopulation {

    private final ArrayList<Map<String, String>> individuals = new ArrayList<>();
    private CTRNNLayout layout;
    String filename;
    

    public static CSVPopulation fromFile(File file) throws IOException {
        CSVPopulation pop = new CSVPopulation();
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader(); // use first row as header; otherwise defaults are fine
        MappingIterator<Map<String, String>> it = mapper.reader(Map.class)
                .with(schema)
                .readValues(file);
        while (it.hasNext()) {
            Map<String, String> rowAsMap = it.next();
            
            pop.individuals.add(rowAsMap);
        }
        
        pop.layout = JSONCTRNNLayout.fromFile(new File(file.getParent() + "/layout.json")).toCTRNNLayout();
        pop.filename = file.getAbsolutePath();
        return pop;
    }

    public int getIDAt(int id) {
        return id;
    }

    public double getFitnessAt(int id) {
        return Double.valueOf(individuals.get(id).get("Fitness"));
    }

    public RobotGenotype getGenotypeAt(int id) {
        float[] genes = new float[layout.genomeLength];
        if(layout == null)
            return null;
        for(int i = 0; i < layout.genomeLength; i++) {
            genes[i] = Float.valueOf(individuals.get(id).get("gene " + i));
        }
        return new RobotGenotype(layout, genes);
    }

    public int getSize() {
        return individuals.size();
    }

}
