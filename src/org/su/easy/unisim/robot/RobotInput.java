package org.su.easy.unisim.robot;

/**
 * The RobotInput class specifies input data to be passed to a robot controller.
 * This may be generated from a real world robot interface, or calculated in
 * simulation. Currently includes Unibot-relevant input data, including
 * rangefinder value and the four wheel sonar values.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class RobotInput {

    private final float range;
    private final float sonar1;
    private final float sonar2;
    private final float sonar3;
    private final float sonar4;

    /**
     * Creates a new RobotInput to pass to a controller.
     * @param range Value of rangefinder.
     * @param sonar1 Value of sonar 1.
     * @param sonar2 Value of sonar 2.
     * @param sonar3 Value of sonar 3.
     * @param sonar4 Value of sonar 4.
     */
    public RobotInput(float range, float sonar1, float sonar2, float sonar3, float sonar4) {
        this.range = range;
        this.sonar1 = sonar1;
        this.sonar2 = sonar2;
        this.sonar3 = sonar3;
        this.sonar4 = sonar4;
    }

    /**
     * @return the value of sonar4
     */
    public float getSonar4() {
        return sonar4;
    }

    /**
     * @return the value of sonar3
     */
    public float getSonar3() {
        return sonar3;
    }

    /**
     * @return the value of sonar2
     */
    public float getSonar2() {
        return sonar2;
    }

    /**
     * @return the value of sonar1
     */
    public float getSonar1() {
        return sonar1;
    }

    /**
     * @return the rangefinder value.
     */
    public float getRange() {
        return range;
    }

}
