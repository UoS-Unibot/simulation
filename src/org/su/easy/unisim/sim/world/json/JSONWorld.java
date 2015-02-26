/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.sim.world.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.su.easy.unisim.simulation.core.SimulationWorld;
import org.su.easy.unisim.util.Shape2D;

/**
 *
 * @author miles
 */
public class JSONWorld {

    private String filename;
    private double[] size;

    public void saveToFile(File file) throws IOException {
        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(file, this);
    }

    public JSONWorld() {
    }

    
    public static SimulationWorld fromFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JSONWorld jworld = mapper.readValue(file, JSONWorld.class);
        jworld.filename = file.getAbsolutePath();

        SimulationWorld world = new SimulationWorld(new Vector2D(jworld.size[0], jworld.size[1]));
        world.addWorldObjects(jworld.getWorldObjects());
        world.setFilename(file.getAbsolutePath());
        return world;
    }

    public Collection<Shape2D> getWorldObjects() {
        ArrayList<Shape2D> worldobjects = new ArrayList<>();
        for (JSONWorldObject obj : objects) {
            worldobjects.add(Shape2D.createRectangleFromCenter(new Vector2D(obj.getPosition()[0], obj.getPosition()[1]), new Vector2D(obj.getSize()[0], obj.getSize()[1]), 0));
        }
        return worldobjects;
    }

    private JSONWorldMetadata metadata;
    private JSONWorldObject[] objects;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public JSONWorldMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(JSONWorldMetadata metadata) {
        this.metadata = metadata;
    }

    public JSONWorldObject[] getObjects() {
        return objects;
    }

    public void setObjects(JSONWorldObject[] objects) {
        this.objects = objects;
    }

    @Override
    public String toString() {
        return "JSONWorld{" + "filename=" + filename + ", metadata=" + metadata + ", objects=" + Arrays.toString(objects) + '}';
    }

    public double[] getSize() {
        return size;
    }

    public void setSize(double[] size) {
        this.size = size;
    }

}
