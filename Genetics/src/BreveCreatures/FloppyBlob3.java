/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BreveCreatures;

import BaseObjects.GeneticData;
import BaseObjects.StopWatch;
import java.util.*;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.joints.JointType;
import org.jbox2d.dynamics.joints.RevoluteJoint;
//import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;

/**
 *
 * @author Colton
 */
public class FloppyBlob3 extends TestbedTest {

    private ArrayList<Blob> blobs = new ArrayList();
    private ArrayList<RevoluteJoint> floppies = new ArrayList();
    //private World blobSpace = new World(new Vec2(0, -10));//IT FINALLY FELL
    private ArrayList<ArrayList<GeneticData>> s;
    private float diffTotal = 0f;
    private float numDiff = 0f;
    private float lastTime = 0f;
    private float currTime = 0f;
    private float currDiff = 0.0166f;
    private double HzChange = 1d;
    private StopWatch timer = new StopWatch();

    public FloppyBlob3(ArrayList<ArrayList<GeneticData>> s) {
        this.s = s;
    }

    @Override
    public void initTest(boolean argDeserialized) {
        setTitle("TESTING FLOPPY BLOB " + s);
        timer.start();
        getWorld().setGravity(new Vec2(0, -10));
        BodyDef groundBodyDef = new BodyDef();
        Body gBody;
        PolygonShape groundBox;
        for (int i = -500; i < 500; i += 10) {
            groundBodyDef.position.set(i, -10);
            gBody = getWorld().createBody(groundBodyDef);
            groundBox = new PolygonShape();
            groundBox.setAsBox(5, 10);
            gBody.createFixture(groundBox, 0);
        }
        blobs = new ArrayList<>();
        ArrayList<Blob> tempBlobs = new ArrayList<>();
        //System.out.println("FLOPPY DERP 1");
        ArrayList[] mauss = decodeGene(s);
        ArrayList<Integer> blobValues = mauss[0];
            ArrayList<Integer> jointValues = mauss[1];
            ArrayList<float[]> blobVert = mauss[2];
            System.out.println("FLOPPY DERP 2");
            float x = (float) blobValues.remove(0);
            float y = (float) blobValues.remove(0) + 15;
            //float type = blobValues.remove(0);
            //float xWidth = (float) (blobValues.remove(0) / 3.0 + 2);
            float density = (float) blobValues.remove(0) + 1;
            float friction = (float) blobValues.remove(0) / 2;
            blobs.add(new Blob(blobVert.remove(0), x, y, density, friction, getWorld(), false));
            while (!blobValues.isEmpty()) {
                x = (float) blobValues.remove(0);
                y = (float) blobValues.remove(0) + 15;
                //type = blobValues.remove(0);
                //xWidth = (float) (blobValues.remove(0) / 3.0 + 2);
            /*if (type.equals("circle")) {
                 System.out.println(xWidth);
                 }*/
                density = (float) blobValues.remove(0) + 1;
                friction = (float) blobValues.remove(0) / 2;
                tempBlobs.add(new Blob(blobVert.remove(0), x, y, density, friction, getWorld(), false));
            }
        while (!blobValues.isEmpty()) {
                x = (float) blobValues.remove(0);
                y = (float) blobValues.remove(0) + 15;
                //type = blobValues.remove(0);
                //xWidth = (float) (blobValues.remove(0) / 3.0 + 2);
            /*if (type.equals("circle")) {
                 System.out.println(xWidth);
                 }*/
                density = (float) blobValues.remove(0) + 1;
                friction = (float) blobValues.remove(0) / 2;
                tempBlobs.add(new Blob(blobVert.remove(0), x, y, density, friction, getWorld(), false));
            }
        //System.out.println("FLOPPY DERP 3");
        while (!jointValues.isEmpty()) {
            int index = (int) (blobs.size() * (jointValues.remove(0) / 100.0));
            Blob blob1 = blobs.get(index);
            Blob blob2 = tempBlobs.remove(0);
            blobs.add(blob2);
            float xShift = jointValues.remove(0);
            float yShift = jointValues.remove(0);
            float torque = jointValues.remove(0);
            float maxAngle = jointValues.remove(0);
            float minAngle = jointValues.remove(0);
            float speed = jointValues.remove(0);
            float timing = jointValues.remove(0) / 10;
            float timing2 = jointValues.remove(0) / 10;
            float balance = jointValues.remove(0);
            Vec2 blob1Domain = new Vec2(blob1.getBody().getPosition().x - blob1.getHalfWidths().x, blob1.getBody().getPosition().x + blob1.getHalfWidths().x);
            Vec2 blob1Range = new Vec2(blob1.getBody().getPosition().y - blob1.getHalfWidths().y, blob1.getBody().getPosition().y + blob1.getHalfWidths().y);
            Vec2 blob2Domain = new Vec2(blob2.getBody().getPosition().x - blob2.getHalfWidths().x, blob2.getBody().getPosition().x + blob2.getHalfWidths().x);
            Vec2 blob2Range = new Vec2(blob2.getBody().getPosition().y - blob2.getHalfWidths().y, blob2.getBody().getPosition().y + blob2.getHalfWidths().y);
            Vec2 overallDomain = new Vec2();
            Vec2 overallRange = new Vec2();
            if (blob1Domain.x < blob2Domain.x) {
                overallDomain.x = blob1Domain.x;
            } else {
                overallDomain.x = blob2Domain.x;
            }
            if (blob1Domain.y < blob2Domain.y) {
                overallDomain.y = blob1Domain.y;
            } else {
                overallDomain.y = blob2Domain.y;
            }
            if (blob1Range.x < blob2Range.x) {
                overallRange.x = blob1Range.x;
            } else {
                overallRange.x = blob2Range.x;
            }
            if (blob1Range.y < blob2Range.y) {
                overallRange.y = blob1Range.y;
            } else {
                overallRange.y = blob2Range.y;
            }
            float domain = overallDomain.y - overallDomain.x;
            float range = overallRange.y - overallRange.x;
            Vec2 bottomLeft = new Vec2(overallDomain.x, overallRange.x);
            Vec2 shiftPoint = new Vec2(domain * (xShift / 100), range * (yShift / 100));
            Vec2 jointCenter = bottomLeft.addLocal(shiftPoint);
            RevoluteJointDef def = new RevoluteJointDef();
            def.type = JointType.REVOLUTE;
            def.initialize(blob1.getBody(), blob2.getBody(), jointCenter);
            def.upperAngle = (float) Math.toRadians(maxAngle);
            def.lowerAngle = -(float) Math.toRadians(minAngle);
            def.enableMotor = false;
            def.motorSpeed = speed / 30;
            def.collideConnected = false;
            def.enableLimit = true;
            def.maxMotorTorque = torque;
            float[] data = new float[5];
            data[0] = timing;
            data[1] = timing2 / 4;
            data[2] = balance / 10;
            data[3] = speed / 30;
            data[4] = 0;
            def.userData = data;
            RevoluteJoint temp = (RevoluteJoint) getWorld().createJoint(def);
            floppies.add(temp);
        }
    }

    @Override
    public void step(TestbedSettings settings) {
        super.step(settings);
        currTime = (float) timer.getTime();
        if (Math.random() >= 0) {
            currDiff = currTime - lastTime;
            diffTotal += currDiff;
            //numDiff++;
            HzChange = Math.pow(currDiff, -1);
            this.getModel().getSettings().getSetting("Hz").value = (int) (HzChange);
            //System.out.println((int) (HzChange));
            //System.out.println(currDiff);

        }
        lastTime = currTime;
        //TestbedModel model = getModel();
        /*if (model.getKeys()['a']) { // model also contains the coded key values
         for (RevoluteJoint j : floppies) {
         j.setMotorSpeed(-j.getMotorSpeed());
         }
         }*/

        //Vec2 screenMouse = model.getMouse();
        // ^^ this is in screen coordinates, so we'd rather grab:
        //Vec2 worldMouse = super.getWorldMouse(); // which is in world coordinates

        // etc
        //this.setCamera(new Vec2((float) Math.random()*10, (float) Math.random()*10));
        this.setCamera(this.getCenterOfMass());

        for (RevoluteJoint j : floppies) {
            float[] data = (float[]) j.getUserData();
            boolean moving = j.isMotorEnabled();
            float waitTime;
            if (moving) {
                waitTime = data[4];
                if (waitTime == -1) {
                    waitTime = data[0];
                }
                //System.out.println("1 wait time: " + waitTime);
                if (waitTime > 0) {
                    waitTime--;
                    data[4] = waitTime;
                    //j.setUserData(data);
                } else {
                    data[4] = -1;
                    //j.setUserData(data);
                    j.enableMotor(false);
                }
            } else {
                waitTime = data[4];
                if (waitTime == -1) {
                    waitTime = data[1];
                }
                //System.out.println("2 wait time: " + waitTime);
                if (waitTime > 0) {
                    waitTime--;
                    data[4] = waitTime;
                    //j.setUserData(data);
                } else {
                    data[4] = -1;
                    //j.setUserData(data);
                    j.enableMotor(true);
                }
            }

            float angle = 0;
            angle += Math.abs(j.getBodyA().getAngle());
            angle += Math.abs(j.getBodyB().getAngle());
            angle /= 2;
            float a = angle * data[2];
            float speed = data[3];
            if (j.getJointAngle() < j.getLowerLimit()) {
                j.setMotorSpeed(Math.abs(speed + a));
                //j.setMotorSpeed(Math.abs(speed));
            }
            if (j.getJointAngle() > j.getUpperLimit()) {
                j.setMotorSpeed(-Math.abs(speed + a));
                //j.setMotorSpeed(-Math.abs(speed));
            }
        }
    }

    /*public void step(float flotWOTWOTWOTWOT) {
     //good values
     //timeStep = 1.0f / 60.0f;
     int velocityIterations = 6;
     int positionIterations = 2;
     getWorld().step(flotWOTWOTWOTWOT, velocityIterations, positionIterations);
     }*/
    private void noCollide(ArrayList<Blob> blobs) {
        Body curr;
        Body temp;
        for (int i = 0; i < blobs.size(); i++) {
            curr = blobs.get(i).getBody();
            for (int j = 0; j < blobs.size(); j++) {
                if (i != j) {
                    temp = blobs.get(j).getBody();
                    System.out.println(curr.shouldCollide(temp));
                }
            }
        }
    }

    public ArrayList<Blob> getBlobs() {
        return blobs;
    }

    private String geneticDataDecoder(GeneticData g) {
        String result = "";
        for (int i = 0; i < g.getNumInstances(); i++) {
            result += g.getGenericDataList2().get(i) + g.getSpecificData2();
        }
        return result;
    }

    private ArrayList[] decodeGene(ArrayList<ArrayList<GeneticData>> data) {
        //System.out.println(data);
        ArrayList<Integer> bodyValues = new ArrayList<>();
        ArrayList<Integer> jointValues = new ArrayList<>();
        ArrayList<String> bodyjointGeneticData = new ArrayList<>();
        ArrayList<String> jointGeneticData = new ArrayList<>();
        ArrayList<String> bodyDefGeneticData = new ArrayList<>();
        ArrayList<float[]> bodyDefValues = new ArrayList<>();
        ArrayList<Double> bodyDefAssignments = new ArrayList<>();
        ArrayList[] parts = new ArrayList[3];
        for (GeneticData swag : data.get(0)) {
            for (char curr : geneticDataDecoder(swag).toCharArray()) {
                bodyjointGeneticData.add(curr + "");
            }
        }
        for (GeneticData swag : data.get(1)) {
            for (char curr : geneticDataDecoder(swag).toCharArray()) {
                jointGeneticData.add(curr + "");
            }
        }
        for (GeneticData swag : data.get(2)) {
            for (char curr : geneticDataDecoder(swag).toCharArray()) {
                bodyDefGeneticData.add(curr + "");
            }
        }

        //blob defined with 9
        //joint defined with 21
        //Length is minimum of 9 + 9 + 21 = 39
        //each additional blob requires 30 more values
        //
        //System.out.println(bodyDefGeneticData);
        String temp;
        while (!bodyjointGeneticData.isEmpty()) {
            //xCoord
            temp = bodyjointGeneticData.remove(0);
            temp = setSign(temp);
            temp += bodyjointGeneticData.remove(0);
            bodyValues.add(new Integer(temp));
            //yCoord
            temp = bodyjointGeneticData.remove(0);
            temp = setSign(temp);
            temp += bodyjointGeneticData.remove(0);
            bodyValues.add(new Integer(temp));
            //Box or Oval
            //bodyValues.add(new Integer(bodyjointGeneticData.remove(0)));
            //Width
            //bodyValues.add(new Integer(bodyjointGeneticData.remove(0)));
            //Height
            //bodyValues.add(new Integer(bodyjointGeneticData.remove(0)));
            //Density
            bodyValues.add(new Integer(bodyjointGeneticData.remove(0)));
            //Friction
            bodyValues.add(new Integer(bodyjointGeneticData.remove(0)));
            //THIS IS THE INDEX PROPORTION
            temp = bodyjointGeneticData.remove(0);
            temp += bodyjointGeneticData.remove(0);
            bodyDefAssignments.add(new Double(temp));
        }
        double total = 0;
        for (Double d : bodyDefAssignments) {
            total += d;
        }
        ArrayList<Double> proportions = new ArrayList<>();
        for (Double d : bodyDefAssignments) {
            proportions.add(d / total);
        }
        while (!jointGeneticData.isEmpty()) {
            //Body1Index
            temp = jointGeneticData.remove(0);
            temp += jointGeneticData.remove(0);
            jointValues.add(new Integer(temp));
            //xShift
            temp = jointGeneticData.remove(0);
            temp += jointGeneticData.remove(0);
            jointValues.add(new Integer(temp));
            //yShift
            temp = jointGeneticData.remove(0);
            temp += jointGeneticData.remove(0);
            jointValues.add(new Integer(temp));
            //Torque
            temp = jointGeneticData.remove(0);
            temp += jointGeneticData.remove(0);
            temp += jointGeneticData.remove(0);
            temp += jointGeneticData.remove(0);
            temp += jointGeneticData.remove(0);
            jointValues.add(new Integer(temp));
            //MaxAngle
            temp = jointGeneticData.remove(0);
            temp += jointGeneticData.remove(0);
            jointValues.add(new Integer(temp));
            //MinAngle
            temp = jointGeneticData.remove(0);
            temp += jointGeneticData.remove(0);
            jointValues.add(new Integer(temp));
            //Speed
            temp = jointGeneticData.remove(0);
            temp = setSign(temp);
            temp += jointGeneticData.remove(0);
            temp += jointGeneticData.remove(0);
            jointValues.add(new Integer(temp));
            //Timing
            temp = jointGeneticData.remove(0);
            temp += jointGeneticData.remove(0);
            temp += jointGeneticData.remove(0);
            jointValues.add(new Integer(temp));
            //Timing2
            temp = jointGeneticData.remove(0);
            temp += jointGeneticData.remove(0);
            temp += jointGeneticData.remove(0);
            jointValues.add(new Integer(temp));
            //Balance
            temp = jointGeneticData.remove(0);
            temp += jointGeneticData.remove(0);
            temp += jointGeneticData.remove(0);
            jointValues.add(new Integer(temp));
        }
        int index = 0;
        int timeShift = 0;
        for (int i = 0; i < proportions.size(); i++) {
            int amount = (int) (proportions.get(i).doubleValue() * bodyDefGeneticData.size());
            if (amount == 0) {
                timeShift--;
                amount++;
            } else if (timeShift < 0 && amount - 1 > -timeShift) {
                amount += timeShift;
                timeShift = 0;
            }
            //System.out.println("AMOUNT:" + amount);
            float[] temp2 = new float[amount];
            for (int j = 0; j < amount; j++) {
                try {
                    temp2[j] = (new Float(bodyDefGeneticData.get(index + j)).floatValue() / 5) + 3f;
                } catch (Exception e) {
                    System.out.println("we ran out of verticies due to a rounding error, we dont care though");
                }
                //System.out.println(temp2[j]);
            }
            for (int ffrf = 0; ffrf < temp2.length; ffrf++) {
                if (temp2[ffrf] <= 3) {
                    temp2[ffrf] = 3f;
                }
            }
            bodyDefValues.add(temp2);
            //System.out.println(bodyDefValues);
            index += amount + 1;
            //System.out.println(index);
        }

        parts[0] = bodyValues;
        parts[1] = jointValues;
        parts[2] = bodyDefValues;
        //System.out.println(bodyValues);
        //System.out.println(jointValues);
        return parts;
    }

    private String setSign(String in) {
        int temp = new Integer(in).intValue();
        if (temp >= 5) {
            return "-";
        }
        return "";
    }

    @Override
    public String getTestName() {
        return "A Floppy Blob";
    }

    public Vec2 getCenterOfMass() {
        //return (float) (Math.random()*100);
        Vec2 center = new Vec2();
        if (!blobs.isEmpty()) {
            Body curr = getWorld().getBodyList();
            float totalMass = 0;
            while (curr.getNext() != null) {
                if (!curr.m_type.equals(BodyType.STATIC)) {
                    center.addLocal(curr.getPosition().x * curr.getMass(), curr.getPosition().y * curr.getMass());
                    totalMass += curr.getMass();
                }
                curr = curr.getNext();
            }
            center.mulLocal(1 / totalMass);
        }
        return center;
    }
}