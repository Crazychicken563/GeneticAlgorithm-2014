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
import org.jbox2d.dynamics.joints.JointType;
import org.jbox2d.dynamics.joints.RevoluteJoint;
//import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;

/**
 *
 * @author Colton
 */
public class FloppyBlob extends TestbedTest {

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

    public FloppyBlob(ArrayList<ArrayList<GeneticData>> s) {
        this.s = s;
    }

    @Override
    public void initTest(boolean argDeserialized) {
        setTitle("TESTING FLOPPY BLOB " + s);
        timer.start();
        getWorld().setGravity(new Vec2(0, -50));
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(0, -10);
        Body gBody = getWorld().createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(500, 10);
        gBody.createFixture(groundBox, 0);
        blobs = new ArrayList();
        ArrayList<Integer> jointValues = decodeGene(s);
        float[] flotflotflot = new float[2];
        flotflotflot[0] = new Float(4);
        flotflotflot[1] = new Float(2);
        blobs.add(new Blob(flotflotflot, -28f, 20f, 50f, 3f, getWorld(),false));
        blobs.add(new Blob(flotflotflot, -14f, 20f, 50f, 3f, getWorld(),false));
        blobs.add(new Blob(flotflotflot, 0f, 20f, 50f, 3f, getWorld(),false));
        blobs.add(new Blob(flotflotflot, 14f, 20f, 50f, 3f, getWorld(),false));
        blobs.add(new Blob(flotflotflot, 28f, 20f, 50f, 3f, getWorld(),false));
        for (int i = 0; i < blobs.size() - 1; i++) {
            //System.out.println(jointValues.get(i));
            RevoluteJointDef def = new RevoluteJointDef();
            def.type = JointType.REVOLUTE;
            //def.initialize(blobs.get(i).getBody(), blobs.get(i + 1).getBody(), new Vec2(blobs.get(i).getBody().getPosition().x+7,blobs.get(i).getBody().getPosition().y));  //GOOD
            def.initialize(blobs.get(i).getBody(), blobs.get(i + 1).getBody(),
                    new Vec2(blobs.get(i).getBody().getPosition().x + ((blobs.get(i).getHalfWidths().x + blobs.get(i + 1).getHalfWidths().x) / 2 - 1), blobs.get(i).getBody().getPosition().y));
            //def.initialize(blobs.get(i).getBody(), blobs.get(i + 1).getBody(), new Vec2(0, 0));
            //def.bodyA = blobs.get(i).getBody();
            //def.bodyB = blobs.get(i + 1).getBody();
            def.enableMotor = true;
            if (jointValues.get(i) != 0) {
                def.motorSpeed = 2 * jointValues.get(i) / Math.abs(jointValues.get(i));
            } else {
                def.motorSpeed = 0;
            }
            def.collideConnected = false;
            def.enableLimit = false;
            def.maxMotorTorque = 9000000;
            //def.localAnchorA.x = blobs.get(i).getBody().getPosition().addLocal((blobs.get(i).getHalfWidths().x + blobs.get(i + 1).getHalfWidths().x) / 2,0).x;
            //def.localAnchorA.y = blobs.get(i).getBody().getPosition().addLocal(blobs.get(i).getHalfWidths().y - blobs.get(i + 1).getHalfWidths().y,0).y;
            //def.localAnchorA.x = blobs.get(i).getBody().getPosition().x + 7;
            def.lowerAngle = -(float) Math.toRadians(Math.abs(jointValues.get(i)));
            def.upperAngle = (float) Math.toRadians(Math.abs(jointValues.get(i)));
            //def.localAnchorA.y = 0;
            //System.out.println("THESE ARE LOCAL ANCHORS");
            //System.out.println(def.localAnchorA);
            //System.out.println(def.localAnchorB);
            //def.upperAngle = (float) Math.toRadians(jointValues.get(i));
            //getWorld().createJoint(def);
            RevoluteJoint temp = (RevoluteJoint) getWorld().createJoint(def);
            //temp.setLimits(-jointValues.get(i), jointValues.get(i));
            floppies.add(temp);
        }
        //noCollide(blobs);
        /* EXAMPLE CODE
         for (int i = 0; i < 2; i++) {
         PolygonShape polygonShape = new PolygonShape();
         polygonShape.setAsBox(1, 1);

         BodyDef bodyDef = new BodyDef();
         bodyDef.type = BodyType.DYNAMIC;
         bodyDef.position.set(5 * i, 0);
         bodyDef.angle = (float) (Math.PI / 4 * i);
         bodyDef.allowSleep = false;
         Body body = getWorld().createBody(bodyDef);
         body.createFixture(polygonShape, 5.0f);

         body.applyForce(new Vec2(-10000 * (i - 1), 0), new Vec2());
         }
         */
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

        for (RevoluteJoint j : floppies) {
            if (j.getJointAngle() < j.getLowerLimit()) {
                j.setMotorSpeed(Math.abs(j.getMotorSpeed()));
            }
            if (j.getJointAngle() > j.getUpperLimit()) {
                j.setMotorSpeed(-Math.abs(j.getMotorSpeed()));
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
        ArrayList<Integer> jointValues = new ArrayList<>();
        ArrayList<Character> geneticData = new ArrayList<>();
        int looper = 3;
        String currNum = "";
        for (ArrayList<GeneticData> temp : data) {
            for (GeneticData swag : temp) {
                for (char curr : geneticDataDecoder(swag).toCharArray()) {
                    geneticData.add(curr);
                }
            }
        }
        //System.out.println(geneticData);
        for (char curr : geneticData) {
            //System.out.println(currNum);
            if (looper % 3 == 0) {
                if (!currNum.equals("")) {
                    jointValues.add(new Integer(currNum));
                    currNum = "";
                    //System.out.println("RESET");
                }
                if (curr < '5') {
                    currNum += "-";
                }
            } else {
                currNum += curr;
            }
            looper++;
        }
        //currNum += geneticData.charAt(geneticData.length() - 1);
        jointValues.add(new Integer(currNum));
        return jointValues;
    }

    @Override
    public String getTestName() {
        return "A Floppy Blob";
    }

    public float getPosition() {
        //return (float) (Math.random()*100);
        if (blobs.isEmpty()) {
            return 0;
        }
        return blobs.get(blobs.size() / 2).getBody().getPosition().x;
    }
}