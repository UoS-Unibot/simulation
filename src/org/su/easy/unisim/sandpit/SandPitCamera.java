/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.sandpit;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * An orthogonal camera viewing the simulation.
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class SandPitCamera {

    public SandPitCamera(Vector2D windowSize, Vector2D initCenterPos, double initScale) {
        scale = initScale;
        currentPosWorldCoord = new Vector2D(1,initCenterPos);
        setWindowSize(windowSize);
    }
    
    private double scale = 0;
    private Vector2D currentPosWorldCoord;
    private Vector2D windowSize;

    /**
     * Get the value of transform
     *
     * @return the value of transform
     */
    public AffineTransform getTransform() {
        AffineTransform returnTransform = new AffineTransform();
        Vector2D currentPosScreenCoord = new Vector2D(getScale(), getCurrentPosWorldCoord());
        Vector2D halfWindow = getHalfWindowSize();
        returnTransform.translate(currentPosScreenCoord.getX() + halfWindow.getX(), currentPosScreenCoord.getY() + halfWindow.getY());
        returnTransform.scale(getScale(), -getScale());
        
        return returnTransform;
    }

    public void move(Vector2D changeVector) {
        currentPosWorldCoord = currentPosWorldCoord.add(changeVector);
        System.out.println(currentPosWorldCoord);
    }

    public void changeScale(double scaleDiff) {
        scale = Math.max(scale + scaleDiff, 0.1);
    }
    
    public final void setWindowSize(Vector2D windowSize) {
        this.windowSize = new Vector2D(1, windowSize);
    }
    
    public Vector2D convertWorldToScreenCoords(Vector2D worldCoords) {
        Vector2D halfWindow = getHalfWindowSize();
        return (new Vector2D(getScale(), worldCoords)).add(halfWindow);
    }
    
    public Vector2D convertScreenToWorldCoords(Vector2D screen) {
        Vector2D halfWindow = getHalfWindowSize();
        return new Vector2D(1/getScale(), screen.subtract(halfWindow)).subtract(currentPosWorldCoord);
    }
    
    public Vector2D getHalfWindowSize() {
        return new Vector2D(0.5, getWindowSize());
    }
    
    
    public Rectangle2D getViewPortInWorldCoords() {
        Vector2D screenZeroZeroInWorldCoords = convertScreenToWorldCoords(Vector2D.ZERO);
        Vector2D screenWidthHeightInWorldCoords = convertScreenToWorldCoords(new Vector2D(2, getWindowSize()));
        
        Rectangle2D rect = new Rectangle2D.Double();
        rect.setFrame(screenZeroZeroInWorldCoords.getX(), screenZeroZeroInWorldCoords.getY(), screenWidthHeightInWorldCoords.getX(), screenWidthHeightInWorldCoords.getY());
        
        return rect;
    }

    /**
     * @return the scale
     */
    public double getScale() {
        return scale;
    }

    /**
     * @return the currentPosWorldCoord
     */
    public Vector2D getCurrentPosWorldCoord() {
        return currentPosWorldCoord;
    }

    /**
     * @return the windowSize
     */
    public Vector2D getWindowSize() {
        return windowSize;
    }

}
