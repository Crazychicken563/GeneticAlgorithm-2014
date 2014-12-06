package BreveCreatures;


import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.testbed.framework.TestbedTest;

public class LoadingTest extends TestbedTest {

    @Override
    public void initTest(boolean argDeserialized) {
        setTitle("Press a Command...");

        getWorld().setGravity(new Vec2());

            PolygonShape polygonShape = new PolygonShape();
            polygonShape.setAsBox(5, 5);

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyType.DYNAMIC;
            bodyDef.position.set(0, 10);
            Body body = getWorld().createBody(bodyDef);
            body.createFixture(polygonShape, 0.01f);
            body.applyTorque(5000);
        
    }

    @Override
    public String getTestName() {
        return "Waiting for Creature";
    }
}