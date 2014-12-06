/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BreveCreatures;

import BaseObjects.GeneticData;
import BaseObjects.StopWatch;
import java.util.*;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
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
public class FloppyElephant extends TestbedTest {

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

    public FloppyElephant(ArrayList<ArrayList<GeneticData>> s) {
        this.s = s;
    }

    @Override
    public void initTest(boolean argDeserialized) {
        setTitle("TESTING FLOPPY ELEPHANT " + s.toString());
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
        float[] ghettoFloat = new float[4];
        //body
        ghettoFloat[0] = 3f;
        ghettoFloat[1] = 3f;
        ghettoFloat[2] = 3f;
        ghettoFloat[3] = 3f;
        blobs.add(new Blob(ghettoFloat, 0f, 10f, 10f, 3f, getWorld(), false));
        //4 upper legs
        ghettoFloat[0] = 1f;
        ghettoFloat[1] = 2f;
        blobs.add(new Blob(ghettoFloat, -4f, 7f, 10f, 3f, getWorld(), false));
        blobs.add(new Blob(ghettoFloat, -4f, 7f, 10f, 3f, getWorld(), false));
        blobs.add(new Blob(ghettoFloat, 4f, 7f, 10f, 3f, getWorld(), false));
        blobs.add(new Blob(ghettoFloat, 4f, 7f, 10f, 3f, getWorld(), false));
        //4 lower legs
        //blobs.add(new Blob(ghettoFloat, -4f, 3f, 10f, 3f, getWorld()));
        //blobs.add(new Blob(ghettoFloat, -4f, 3f, 10f, 3f, getWorld()));
        //blobs.add(new Blob(ghettoFloat, 4f, 3f, 10f, 3f, getWorld()));
        //blobs.add(new Blob(ghettoFloat, 4f, 3f, 10f, 3f, getWorld()));
        //Body body;
        //BodyDef bodyDef = new BodyDef();
        //bodyDef.type = BodyType.DYNAMIC;
        //bodyDef.position.set(5, 5);
        //body = getWorld().createBody(bodyDef);
        Shape shape;
        shape = new PolygonShape();
        Vec2[] currTriangle = new Vec2[3];
        currTriangle[0] = new Vec2(0.75f, 5.75f);
        currTriangle[0] = new Vec2(0.75f, 2.75f);
        currTriangle[1] = new Vec2(-1.75f, 6.75f);
        currTriangle[1] = new Vec2(-0.75f, -2.75f);
        currTriangle[2] = new Vec2(0, 1.5f); //WHY ISNT THIS WORK
        ((PolygonShape) shape).set(currTriangle, 3);
        //((CircleShape) shape).m_radius = 4;
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 10f; //1 is hella good
        fixtureDef.friction = 3f; //0.3 is hella good
        fixtureDef.filter.groupIndex = -1;
        //System.out.print
        blobs.get(1).getBody().createFixture(fixtureDef);
        blobs.get(2).getBody().createFixture(fixtureDef);
        blobs.get(3).getBody().createFixture(fixtureDef);
        blobs.get(4).getBody().createFixture(fixtureDef);

        //ArrayList<Blob> tempBlobs = new ArrayList<>();
        //System.out.println("FLOPPY DERP 1");
        //ArrayList[] mauss = decodeGene(s);
        //ArrayList<Integer> blobValues = mauss[0];

        ArrayList<Integer> jointValues = decodeGene(s);
        //<editor-fold desc="Back Leg 1">
        {
            float torque = jointValues.remove(0);
            float maxAngle = jointValues.remove(0);
            float minAngle = jointValues.remove(0);
            float speed = jointValues.remove(0);
            float balance = jointValues.remove(0);
            int timing = jointValues.remove(0) / 10;
            int timing2 = jointValues.remove(0) / 10;
            RevoluteJointDef def = new RevoluteJointDef();
            def.type = JointType.REVOLUTE;
            def.initialize(blobs.get(0).getBody(), blobs.get(1).getBody(), new Vec2(-4, 8));
            def.upperAngle = (float) Math.toRadians(maxAngle);
            def.lowerAngle = -(float) Math.toRadians(minAngle);
            def.enableMotor = false;
            def.motorSpeed = speed / 30;
            def.collideConnected = false;
            def.enableLimit = true;
            def.maxMotorTorque = 80000 + torque * 100;
            float[] data = new float[4];
            data[0] = timing;
            data[1] = timing2;
            data[2] = balance / 10;
            data[3] = speed / 30;
            def.userData = data;
            RevoluteJoint temp = (RevoluteJoint) getWorld().createJoint(def);
            floppies.add(temp);
        }
        //</editor-fold>
        //<editor-fold desc="Back Leg 2">
        {
            float torque = jointValues.remove(0);
            float maxAngle = jointValues.remove(0);
            float minAngle = jointValues.remove(0);
            float speed = jointValues.remove(0);
            float balance = jointValues.remove(0);
            int timing = jointValues.remove(0) / 10;
            int timing2 = jointValues.remove(0) / 10;
            RevoluteJointDef def = new RevoluteJointDef();
            def.type = JointType.REVOLUTE;
            def.initialize(blobs.get(0).getBody(), blobs.get(2).getBody(), new Vec2(-4, 8));
            def.upperAngle = (float) Math.toRadians(maxAngle);
            def.lowerAngle = -(float) Math.toRadians(minAngle);
            def.enableMotor = false;
            def.motorSpeed = speed / 30;
            def.collideConnected = false;
            def.enableLimit = true;
            def.maxMotorTorque = 80000 + torque * 100;
            float[] data = new float[4];
            data[0] = timing;
            data[1] = timing2;
            data[2] = balance / 10;
            data[3] = speed / 30;
            def.userData = data;
            RevoluteJoint temp = (RevoluteJoint) getWorld().createJoint(def);
            floppies.add(temp);
        }
        //</editor-fold>
        //<editor-fold desc="Front Leg 1">
        {
            float torque = jointValues.remove(0);
            float maxAngle = jointValues.remove(0);
            float minAngle = jointValues.remove(0);
            float speed = jointValues.remove(0);
            float balance = jointValues.remove(0);
            int timing = jointValues.remove(0) / 10;
            int timing2 = jointValues.remove(0) / 10;
            RevoluteJointDef def = new RevoluteJointDef();
            def.type = JointType.REVOLUTE;
            def.initialize(blobs.get(0).getBody(), blobs.get(3).getBody(), new Vec2(4, 8));
            def.upperAngle = (float) Math.toRadians(maxAngle);
            def.lowerAngle = -(float) Math.toRadians(minAngle);
            def.enableMotor = false;
            def.motorSpeed = speed / 30;
            def.collideConnected = false;
            def.enableLimit = true;
            def.maxMotorTorque = 80000 + torque * 100;
            float[] data = new float[4];
            data[0] = timing;
            data[1] = timing2;
            data[2] = balance / 10;
            data[3] = speed / 30;
            def.userData = data;
            RevoluteJoint temp = (RevoluteJoint) getWorld().createJoint(def);
            floppies.add(temp);
        }
        //</editor-fold>
        //<editor-fold desc="Front Leg 2">
        {
            float torque = jointValues.remove(0);
            float maxAngle = jointValues.remove(0);
            float minAngle = jointValues.remove(0);
            float speed = jointValues.remove(0);
            float balance = jointValues.remove(0);
            int timing = jointValues.remove(0) / 10;
            int timing2 = jointValues.remove(0) / 10;
            RevoluteJointDef def = new RevoluteJointDef();
            def.type = JointType.REVOLUTE;
            def.initialize(blobs.get(0).getBody(), blobs.get(4).getBody(), new Vec2(4, 8));
            def.upperAngle = (float) Math.toRadians(maxAngle);
            def.lowerAngle = -(float) Math.toRadians(minAngle);
            def.enableMotor = false;
            def.motorSpeed = speed / 30;
            def.collideConnected = false;
            def.enableLimit = true;
            def.maxMotorTorque = 80000 + torque * 100;
            float[] data = new float[4];
            data[0] = timing;
            data[1] = timing2;
            data[2] = balance / 10;
            data[3] = speed / 30;
            def.userData = data;
            RevoluteJoint temp = (RevoluteJoint) getWorld().createJoint(def);
            floppies.add(temp);
        }

        //</editor-fold>
        //<editor-fold desc="Back Bottom Leg 1">
        {
            float torque = jointValues.remove(0);
            float maxAngle = jointValues.remove(0);
            float minAngle = jointValues.remove(0);
            float speed = jointValues.remove(0);
            float balance = jointValues.remove(0);
            int timing = jointValues.remove(0) / 10;
            int timing2 = jointValues.remove(0) / 10;
            RevoluteJointDef def = new RevoluteJointDef();
            def.type = JointType.REVOLUTE;
            def.initialize(blobs.get(1).getBody(), blobs.get(5).getBody(), new Vec2(-4, 5));
            def.upperAngle = (float) Math.toRadians(maxAngle);
            def.lowerAngle = -(float) Math.toRadians(minAngle);
            def.enableMotor = false;
            def.motorSpeed = speed / 30;
            def.collideConnected = false;
            def.enableLimit = true;
            def.maxMotorTorque = 80000 + torque * 100;
            float[] data = new float[4];
            data[0] = timing;
            data[1] = timing2;
            data[2] = balance / 10;
            data[3] = speed / 30;
            def.userData = data;
            RevoluteJoint temp = (RevoluteJoint) getWorld().createJoint(def);
            floppies.add(temp);
        }
        //</editor-fold>
        //<editor-fold desc="Back Bottom Leg 2">
        {
            float torque = jointValues.remove(0);
            float maxAngle = jointValues.remove(0);
            float minAngle = jointValues.remove(0);
            float speed = jointValues.remove(0);
            float balance = jointValues.remove(0);
            int timing = jointValues.remove(0) / 10;
            int timing2 = jointValues.remove(0) / 10;
            RevoluteJointDef def = new RevoluteJointDef();
            def.type = JointType.REVOLUTE;
            def.initialize(blobs.get(2).getBody(), blobs.get(6).getBody(), new Vec2(-4, 5));
            def.upperAngle = (float) Math.toRadians(maxAngle);
            def.lowerAngle = -(float) Math.toRadians(minAngle);
            def.enableMotor = false;
            def.motorSpeed = speed / 30;
            def.collideConnected = false;
            def.enableLimit = true;
            def.maxMotorTorque = 80000 + torque * 100;
            float[] data = new float[4];
            data[0] = timing;
            data[1] = timing2;
            data[2] = balance / 10;
            data[3] = speed / 30;
            def.userData = data;
            RevoluteJoint temp = (RevoluteJoint) getWorld().createJoint(def);
            floppies.add(temp);
        }
        //</editor-fold>
        //<editor-fold desc="Front Bottom Leg 1">
        {
            float torque = jointValues.remove(0);
            float maxAngle = jointValues.remove(0);
            float minAngle = jointValues.remove(0);
            float speed = jointValues.remove(0);
            float balance = jointValues.remove(0);
            int timing = jointValues.remove(0) / 10;
            int timing2 = jointValues.remove(0) / 10;
            RevoluteJointDef def = new RevoluteJointDef();
            def.type = JointType.REVOLUTE;
            def.initialize(blobs.get(3).getBody(), blobs.get(7).getBody(), new Vec2(4, 5));
            def.upperAngle = (float) Math.toRadians(maxAngle);
            def.lowerAngle = -(float) Math.toRadians(minAngle);
            def.enableMotor = false;
            def.motorSpeed = speed / 30;
            def.collideConnected = false;
            def.enableLimit = true;
            def.maxMotorTorque = 80000 + torque * 100;
            float[] data = new float[4];
            data[0] = timing;
            data[1] = timing2;
            data[2] = balance / 10;
            data[3] = speed / 30;
            def.userData = data;
            RevoluteJoint temp = (RevoluteJoint) getWorld().createJoint(def);
            floppies.add(temp);
        }
        //</editor-fold>
        //<editor-fold desc="Front Bottom Leg 2">
        {
            float torque = jointValues.remove(0);
            float maxAngle = jointValues.remove(0);
            float minAngle = jointValues.remove(0);
            float speed = jointValues.remove(0);
            float balance = jointValues.remove(0);
            int timing = jointValues.remove(0) / 10;
            int timing2 = jointValues.remove(0) / 10;
            RevoluteJointDef def = new RevoluteJointDef();
            def.type = JointType.REVOLUTE;
            def.initialize(blobs.get(4).getBody(), blobs.get(8).getBody(), new Vec2(4, 5));
            def.upperAngle = (float) Math.toRadians(maxAngle);
            def.lowerAngle = -(float) Math.toRadians(minAngle);
            def.enableMotor = false;
            def.motorSpeed = speed / 30;
            def.collideConnected = false;
            def.enableLimit = true;
            def.maxMotorTorque = 80000 + torque * 100;
            float[] data = new float[4];
            data[0] = timing;
            data[1] = timing2;
            data[2] = balance / 10;
            data[3] = speed / 30;
            def.userData = data;
            RevoluteJoint temp = (RevoluteJoint) getWorld().createJoint(def);
            floppies.add(temp);
        }
        //</editor-fold>
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
            float waitTime = data[0];
            if (waitTime > 0) {
                waitTime--;
                data[0] = waitTime;
                j.setUserData(data);
            } else {
                j.enableMotor(true);
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

    private ArrayList<Integer> decodeGene(ArrayList<ArrayList<GeneticData>> data) {
        System.out.println("SUPPLIES");
        System.out.println(data);
        ArrayList<Integer> tempJointValues = new ArrayList<>();
        ArrayList<Integer> jointValues = new ArrayList<>();
        ArrayList<int[]> order = new ArrayList<>();
        ArrayList<String> geneticData = new ArrayList<>();
        for (ArrayList<GeneticData> temp : data) {
            for (GeneticData swag : temp) {
                for (char curr : geneticDataDecoder(swag).toCharArray()) {
                    geneticData.add(curr + "");
                }
            }
        }
        //joint defined with 3+19
        //each additional blob requires 3+19 more values
        //
        String temp;
        int gI = 0;
        while (!geneticData.isEmpty()) {
            //Index 3
            temp = geneticData.remove(0);
            temp += geneticData.remove(0);
            temp += geneticData.remove(0);
            int[] derp = new int[2];
            derp[0] = gI;
            derp[1] = new Integer(temp).intValue();
            order.add(derp);
            gI += 7;
            //Torque 3
            temp = geneticData.remove(0);
            temp = setSign(temp);
            temp += geneticData.remove(0);
            temp += geneticData.remove(0);
            tempJointValues.add(new Integer(temp));
            //MaxAngle 2
            temp = geneticData.remove(0);
            temp += geneticData.remove(0);
            tempJointValues.add(new Integer(temp));
            //MinAngle 2
            temp = geneticData.remove(0);
            temp += geneticData.remove(0);
            tempJointValues.add(new Integer(temp));
            //Speed 3
            temp = geneticData.remove(0);
            temp = setSign(temp);
            temp += geneticData.remove(0);
            temp += geneticData.remove(0);
            tempJointValues.add(new Integer(temp));
            //Balance 3
            temp = geneticData.remove(0);
            temp = setSign(temp);
            temp += geneticData.remove(0);
            temp += geneticData.remove(0);
            System.out.println("Balance:" + temp);
            tempJointValues.add(new Integer(temp));
            //Timing 1 3
            temp = geneticData.remove(0);
            temp += geneticData.remove(0);
            temp += geneticData.remove(0);
            tempJointValues.add(new Integer(temp));
            //Timing 2 3
            temp = geneticData.remove(0);
            temp += geneticData.remove(0);
            temp += geneticData.remove(0);
            tempJointValues.add(new Integer(temp));
        }
        ArrayList<int[]> newOrder = new ArrayList<>();
        while (!order.isEmpty()) {
            int minIndex = 0;
            int minValue = 1000;
            for (int i = 0; i < order.size(); i++) {
                if (order.get(i)[1] < minValue) {
                    minValue = order.get(i)[1];
                    minIndex = i;
                }
            }
            System.out.println(order.get(minIndex)[0] + "," + order.get(minIndex)[1]);
            newOrder.add(order.remove(minIndex));
        }
        for (int[] currOrderPair : newOrder) {
            jointValues.add(tempJointValues.get(currOrderPair[0]));
            jointValues.add(tempJointValues.get(currOrderPair[0] + 1));
            jointValues.add(tempJointValues.get(currOrderPair[0] + 2));
            jointValues.add(tempJointValues.get(currOrderPair[0] + 3));
            jointValues.add(tempJointValues.get(currOrderPair[0] + 4));
            jointValues.add(tempJointValues.get(currOrderPair[0] + 5));
            jointValues.add(tempJointValues.get(currOrderPair[0] + 6));
            System.out.println(jointValues.subList(jointValues.size() - 7, jointValues.size()));
        }
        //System.out.println(jointValues);
        return jointValues;
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