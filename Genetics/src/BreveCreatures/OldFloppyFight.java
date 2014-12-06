/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BreveCreatures;

import BaseObjects.GeneticData;
import java.util.ArrayList;
import java.util.Random;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.joints.JointType;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.testbed.framework.TestbedTest;

/**
 *
 * @author Colton
 */
public class OldFloppyFight extends TestbedTest{
    private ArrayList<ArrayList<ArrayList<GeneticData>>> fighters = new ArrayList<>();
    private ArrayList<Blob> blobs1 = new ArrayList();
    private ArrayList<RevoluteJoint> floppies1 = new ArrayList();
    private ArrayList<Blob> blobs2 = new ArrayList();
    private ArrayList<RevoluteJoint> floppies2 = new ArrayList();
    public OldFloppyFight(ArrayList<ArrayList<ArrayList<GeneticData>>> warriors){
        fighters = warriors;
    }
    //copied from floppyblob 3
    private ArrayList[] decodeGene(ArrayList<ArrayList<GeneticData>> data) {
        System.out.println(data);
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
        System.out.println(bodyDefGeneticData);
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
            System.out.println("AMOUNT:" + amount);
            float[] temp2 = new float[amount];
            for (int j = 0; j < amount; j++) {
                try {
                    temp2[j] = (new Float(bodyDefGeneticData.get(index + j)).floatValue()/5) + 3f;
                } catch (Exception e) {
                    System.out.println("we ran out of verticies due to a rounding error, we dont care though");
                }
                System.out.println(temp2[j]);
            }
            for (int ffrf = 0; ffrf < temp2.length; ffrf++) {
                if (temp2[ffrf] <= 3) {
                    temp2[ffrf] = 3f;
                }
            }
            bodyDefValues.add(temp2);
            System.out.println(bodyDefValues);
            index += amount + 1;
            System.out.println(index);
        }

        parts[0] = bodyValues;
        parts[1] = jointValues;
        parts[2] = bodyDefValues;
        //System.out.println(bodyValues);
        //System.out.println(jointValues);
        return parts;
    }
    
    private ArrayList[] decodeGeneFlip(ArrayList<ArrayList<GeneticData>> data){
        System.out.println("Who really knows if this works?");
        System.out.println(data);
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
        System.out.println(bodyDefGeneticData);
        String temp;
        while (!bodyjointGeneticData.isEmpty()) {
            //xCoord
            temp = bodyjointGeneticData.remove(0);
            temp = setSign(temp);
            temp += bodyjointGeneticData.remove(0);
            bodyValues.add(new Integer(temp) * (-1));
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
            jointValues.add(100 - new Integer(temp));
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
            System.out.println("AMOUNT:" + amount);
            float[] temp2 = new float[amount];
            for (int j = 0; j < amount; j++) {
                try {
                    temp2[j] = (new Float(bodyDefGeneticData.get(index + j)).floatValue()/5) + 3f;
                } catch (Exception e) {
                    System.out.println("we ran out of verticies due to a rounding error, we dont care though");
                }
                System.out.println(temp2[j]);
            }
            for (int ffrf = 0; ffrf < temp2.length; ffrf++) {
                if (temp2[ffrf] <= 3) {
                    temp2[ffrf] = 3f;
                }
            }
            bodyDefValues.add(temp2);
            System.out.println(bodyDefValues);
            index += amount + 1;
            System.out.println(index);
        }

        parts[0] = bodyValues;
        parts[1] = jointValues;
        parts[2] = bodyDefValues;
        //System.out.println(bodyValues);
        //System.out.println(jointValues);
        return parts;
    }
    //copied from floppyblob 3
    private String geneticDataDecoder(GeneticData g) {
        String result = "";
        for (int i = 0; i < g.getNumInstances(); i++) {
            result += g.getGenericDataList2().get(i) + g.getSpecificData2();
        }
        return result;
    }
    //copied from floppyblob 3
    private String setSign(String in) {
        int temp = new Integer(in).intValue();
        if (temp >= 5) {
            return "-";
        }
        return "";
    }

    @Override
    public void initTest(boolean argDeserialized) {
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
        Random r = new Random();
        int arenaMaster = r.nextInt(2); //randomly position on left or right side
        ArrayList<Blob> tempBlobs1 = new ArrayList<>();
        ArrayList[] mauss1 = decodeGene(fighters.get(arenaMaster));
        ArrayList<Integer> blobValues1 = mauss1[0];
        ArrayList<Integer> jointValues1 = mauss1[1];
        ArrayList<float[]> blobVert1 = mauss1[2];
        ArrayList<Blob> tempBlobs2 = new ArrayList<>();
        ArrayList[] mauss2 = decodeGeneFlip(fighters.get(1-arenaMaster));
        ArrayList<Integer> blobValues2 = mauss2[0];
        ArrayList<Integer> jointValues2 = mauss2[1];
        ArrayList<float[]> blobVert2 = mauss2[2];
        
        //FIRST FIGHTER START
        float x1 = (float) blobValues1.remove(0);
        float y1 = (float) blobValues1.remove(0) + 15;
        //float type = blobValues.remove(0);
        //float xWidth = (float) (blobValues.remove(0) / 3.0 + 2);
        float density1 = (float) blobValues1.remove(0) + 1;
        float friction1 = (float) blobValues1.remove(0) / 2;
        blobs1.add(new Blob(blobVert1.remove(0), x1, y1, density1, friction1, getWorld(), false));
        while (!blobValues1.isEmpty()) {
            x1 = (float) blobValues1.remove(0);
            y1 = (float) blobValues1.remove(0) + 15;
            //type = blobValues.remove(0);
            //xWidth = (float) (blobValues.remove(0) / 3.0 + 2);
            /*if (type.equals("circle")) {
             System.out.println(xWidth);
             }*/
            density1 = (float) blobValues1.remove(0) + 1;
            friction1 = (float) blobValues1.remove(0) / 2;
            tempBlobs1.add(new Blob(blobVert1.remove(0), x1, y1, density1, friction1, getWorld(), false));
        }
        //System.out.println("FLOPPY DERP 3");
        while (!jointValues1.isEmpty()) {
            int index = (int) (blobs1.size() * (jointValues1.remove(0) / 100.0));
            Blob blob1 = blobs1.get(index);
            Blob blob2 = tempBlobs1.remove(0);
            blobs1.add(blob2);
            float xShift = jointValues1.remove(0);
            float yShift = jointValues1.remove(0);
            float torque = jointValues1.remove(0);
            float maxAngle = jointValues1.remove(0);
            float minAngle = jointValues1.remove(0);
            float speed = jointValues1.remove(0);
            float timing = jointValues1.remove(0) / 10;
            float timing2 = jointValues1.remove(0) / 10;
            float balance = jointValues1.remove(0);
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
            floppies1.add(temp);
        }
        //FIRST FIGHTER END
        
        //SECOND FIGHTER START
        float x2 = (float) blobValues2.remove(0);
        float y2 = (float) blobValues2.remove(0) + 15;
        //float type = blobValues.remove(0);
        //float xWidth = (float) (blobValues.remove(0) / 3.0 + 2);
        float density2 = (float) blobValues2.remove(0) + 1;
        float friction2 = (float) blobValues2.remove(0) / 2;
        blobs1.add(new Blob(blobVert2.remove(0), x2, y2, density2, friction2, getWorld(), true));
        while (!blobValues2.isEmpty()) {
            x2 = (float) blobValues2.remove(0);
            y2 = (float) blobValues2.remove(0) + 15;
            //type = blobValues.remove(0);
            //xWidth = (float) (blobValues.remove(0) / 3.0 + 2);
            /*if (type.equals("circle")) {
             System.out.println(xWidth);
             }*/
            density2 = (float) blobValues2.remove(0) + 1;
            friction2 = (float) blobValues2.remove(0) / 2;
            tempBlobs2.add(new Blob(blobVert2.remove(0), x2, y2, density2, friction2, getWorld(), true));
        }
        //System.out.println("FLOPPY DERP 3");
        while (!jointValues2.isEmpty()) {
            int index = (int) (blobs2.size() * (jointValues2.remove(0) / 100.0));
            Blob blob1 = blobs2.get(index);
            Blob blob2 = tempBlobs2.remove(0);
            blobs2.add(blob2);
            float xShift = jointValues2.remove(0);
            float yShift = jointValues2.remove(0);
            float torque = jointValues2.remove(0);
            float maxAngle = jointValues2.remove(0);
            float minAngle = jointValues2.remove(0);
            float speed = jointValues2.remove(0);
            float timing = jointValues2.remove(0) / 10;
            float timing2 = jointValues2.remove(0) / 10;
            float balance = jointValues2.remove(0);
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
            floppies2.add(temp);
        }
        //SECOND FIGHTER END
    }

    @Override
    public String getTestName() {
        return "Floppy Fight";
    }
}
