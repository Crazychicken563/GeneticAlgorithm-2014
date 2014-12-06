
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.JointType;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.j2d.DebugDrawJ2D;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;
import org.jbox2d.testbed.tests.Car;
import org.jbox2d.testbed.tests.CircleStress;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Seva
 */
public class Java2dSandbox {

    public static void main(String[] args) {
        //DebugDrawJ2D temp;
        TestbedModel testB = new TestbedModel();
        testB.setRunningTest(new CircleStress());
        TestPanelJ2D testP = new TestPanelJ2D(testB);
        testP.render();
        //temp = new DebugDrawJ2D(new TestPanelJ2D(new TestbedModel()));
        // Static Body
        Vec2 gravity = new Vec2(0, -10);
        World world = new World(gravity);
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(0, -10);
        Body groundBody = world.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(50, 10);
        groundBody.createFixture(groundBox, 0);
        //world.drawDebugData();

        // Dynamic Body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(0, 4);
        Body body = world.createBody(bodyDef);
        PolygonShape dynamicBox = new PolygonShape();
        dynamicBox.setAsBox(1, 1);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = dynamicBox;
        fixtureDef.density = 1;
        fixtureDef.friction = 0.3f;
        body.createFixture(fixtureDef);

        BodyDef bodyDef2 = new BodyDef();
        bodyDef2.type = BodyType.DYNAMIC;
        bodyDef2.position.set(1, 4);
        Body body2 = world.createBody(bodyDef2);
        PolygonShape dynamicBox2 = new PolygonShape();
        dynamicBox2.setAsBox(1, 1);
        FixtureDef fixtureDef2 = new FixtureDef();
        fixtureDef2.shape = dynamicBox2;
        fixtureDef2.density = 1;
        fixtureDef2.friction = 0.3f;
        body2.createFixture(fixtureDef2);

        //JOINT
        RevoluteJointDef def = new RevoluteJointDef();
        def.type = JointType.REVOLUTE;
        def.bodyA = body;
        def.bodyB = body2;
        def.enableMotor = true;
        def.motorSpeed = 1000;
        def.collideConnected = false;
        def.enableLimit = false;
        def.maxMotorTorque = 1000;
        Joint temp = world.createJoint(def);

        // Setup world
        float timeStep = 1.0f / 60.0f;
        int velocityIterations = 6;
        int positionIterations = 2;

        // Run loop
        for (int i = 0; i < 120; ++i) {
            world.step(timeStep, velocityIterations, positionIterations);
            Vec2 position = body.getPosition();
            float angle = body.getAngle();
            Vec2 position2 = body2.getPosition();
            float angle2 = body2.getAngle();
            System.out.println("B1 " + position.x + " " + position.y + " " + angle + " B2 " + position2.x + " " + position2.y + " " + angle2);
        }
    }
}
