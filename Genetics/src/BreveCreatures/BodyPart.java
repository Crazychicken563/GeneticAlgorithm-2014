/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BreveCreatures;

/**
 *
 * @author Seva
 */
public class BodyPart {

    private BodyPart connectedRear = null;
    private BodyPart connectedFront = null;
    private double jointValue;
    private double currPosition = 0;
    private int moveDirection;

    public BodyPart(double jointValue) {
        this.jointValue = jointValue;
        if (jointValue > 0) {
            moveDirection = 1;
        } else {
            moveDirection = -1;
        }
    }

    public void setFront(BodyPart front) {
        connectedFront = front;
    }

    public void setRear(BodyPart rear) {
        connectedRear = rear;
    }

    public BodyPart getFront() {
        return connectedFront;
    }

    public BodyPart getRear() {
        return connectedRear;
    }

    public void setJointValue(double value) {
        jointValue = value;
    }

    public double getJointValue() {
        return jointValue;
    }

    public void move(double increment) {
        if (Math.abs(currPosition) > Math.abs(jointValue)) {
            moveDirection = -moveDirection;
        }
        currPosition += increment * moveDirection;
    }
}
