package org.unisim.io.world;

/**
 * POJO representation of an object in a world JSON file.
 *
 * @author Miles Bryant (mb459 at sussex.ac.uk)
 */
public class JSONWorldObject {

    private double[] size;
    private double[] position;

    public double[] getSize() {
        return size;
    }

    public void setSize(double[] size) {
        this.size = size;
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

}
