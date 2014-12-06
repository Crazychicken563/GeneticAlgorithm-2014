/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BreveCreatures;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 *
 * @author Colton
 */
public class Blob {

    private Body body;
    private World world;
    private Shape dynamicSwag;
    private float[] lengths;

    public Blob(float[] lengths, float x, float y, float dense, float fric, World w, boolean reverse) {
        world = w;
        //body.
        this.lengths = lengths;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);
        //System.out.println("LENGTHS IS: " + lengths.length);
        if (lengths.length == 1) {
            dynamicSwag = new CircleShape();
            ((CircleShape) dynamicSwag).m_radius = lengths[0];
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = dynamicSwag;
            fixtureDef.density = dense; //1 is hella good
            fixtureDef.friction = fric; //0.3 is hella good
            if (!reverse) {
                fixtureDef.filter.groupIndex = -1;
            } else {
                fixtureDef.filter.groupIndex = -2;
            }
            body.createFixture(fixtureDef);
        } else if (lengths.length == 2) {
            dynamicSwag = new PolygonShape();
            ((PolygonShape) dynamicSwag).setAsBox(lengths[0], lengths[1]);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = dynamicSwag;
            fixtureDef.density = dense; //1 is hella good
            fixtureDef.friction = fric; //0.3 is hella good
            if (!reverse) {
                fixtureDef.filter.groupIndex = -1;
            } else {
                fixtureDef.filter.groupIndex = -2;
            }
            body.createFixture(fixtureDef);
        } else if (lengths.length >= 3) {
            float coreAngle = 2 * (float) Math.PI / lengths.length;
            Vec2 center = new Vec2(0f, 0f);
            Vec2[] vertices = new Vec2[lengths.length];
            //vertices = new Vec2[lengths.length];
            if (!reverse) {
                float currAngle = 0;
                for (int i = 0; i < lengths.length; i++) {
                    Vec2 currVert = new Vec2();
                    currVert.x = (float) (lengths[i] * Math.cos(currAngle));
                    currVert.y = (float) (lengths[i] * Math.sin(currAngle));
                    vertices[i] = currVert;
                    currAngle += coreAngle;
                }
            } else {
                float currAngle = (float) Math.PI;
                for (int i = 0; i < lengths.length; i++) {
                    Vec2 currVert = new Vec2();
                    currVert.x = (float) (lengths[i] * Math.cos(currAngle));
                    currVert.y = (float) (lengths[i] * Math.sin(currAngle));
                    vertices[i] = currVert;
                    currAngle -= coreAngle;
                }
            }
            for (int i = 0; i < lengths.length; i++) {
                dynamicSwag = new PolygonShape();
                Vec2[] currTriangle = new Vec2[3];
                currTriangle[0] = vertices[i];
                if (i + 1 < lengths.length) { //not last case
                    currTriangle[1] = vertices[i + 1];
                } else { //last case
                    currTriangle[1] = vertices[0];
                }
                currTriangle[2] = center;
                ((PolygonShape) dynamicSwag).set(currTriangle, 3);
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = dynamicSwag;
                fixtureDef.density = dense; //1 is hella good
                fixtureDef.friction = fric; //0.3 is hella good
                if (!reverse) {
                    fixtureDef.filter.groupIndex = -1;
                } else {
                    fixtureDef.filter.groupIndex = -2;
                }
                body.createFixture(fixtureDef);

            }
        } else {
            System.out.println("OPEN THE DOOR GET ON THE FLOOR");
            System.out.println("EVERYBODY WALK THE DINOSAUR");
            System.out.println("lengths.length = 0 should never happen");
        }

    }

    public Body getBody() {
        return body;
    }

    public Shape getPS() {
        return dynamicSwag;
    }

    public Vec2 getHalfWidths() {
        Vec2 lowestBound = new Vec2(0f, 0f);
        Vec2 highestBound = new Vec2(0f, 0f);
        AABB currAABB;
        //Fixture f = body.getFixtureList();
        Fixture f2 = body.getFixtureList(); //WHY IS CHILD INDEX A THING?
        int index = 0;
        while (f2.getNext() != null) {

            currAABB = f2.getAABB(index); //why did they make it this way?
            if (currAABB.lowerBound.x < lowestBound.x) {
                lowestBound.x = currAABB.lowerBound.x;
            }
            if (currAABB.lowerBound.y < lowestBound.y) {
                lowestBound.y = currAABB.lowerBound.y;
            }
            if (currAABB.upperBound.x > highestBound.x) {
                highestBound.x = currAABB.upperBound.x;
            }
            if (currAABB.upperBound.y > highestBound.x) {
                highestBound.y = currAABB.upperBound.y;
            }
            //index++;
            f2 = f2.getNext();
        }
        return new Vec2((highestBound.x - lowestBound.x) / 2, (highestBound.y - lowestBound.y) / 2);
    }

    public Vec2 getBadHalfWidths() {
        return new Vec2(0f, 0f);
    }
    
    public String toString(){
        return "blobby " + lengths[0] + " " + lengths.length;
    }
}
