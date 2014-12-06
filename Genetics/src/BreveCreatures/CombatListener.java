/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BreveCreatures;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author Colton
 */
public class CombatListener implements ContactListener {

    public float score1 = 0;
    public float score2 = 0;
    public float fightRatio = 0;

    @Override
    public void beginContact(Contact contact) {
        if ((contact.m_fixtureA.m_filter.groupIndex == -1
            && contact.m_fixtureB.m_filter.groupIndex == -2)
            || (contact.m_fixtureA.m_filter.groupIndex == -1
            && contact.m_fixtureB.m_filter.groupIndex == -2)) {
            //System.out.println("THAT'S A BIG HIT");
            Vec2 linVel1 = contact.m_fixtureA.m_body.getLinearVelocity();
            float m1 = contact.getFixtureA().m_body.m_mass;
            Vec2 linVel2 = contact.m_fixtureB.m_body.getLinearVelocity();
            float m2 = contact.getFixtureA().m_body.m_mass;
            
            score1 += linVel1.length() * linVel1.length() * m1;
            //System.out.println("Floppy has " + score1);
            score2 += linVel2.length() * linVel2.length() * m2;
            //System.out.println("Blob has " + score2);
        }
    }

    @Override
    public void endContact(Contact contact) {
        fightRatio = score1 / score2;
        if(fightRatio == 0) fightRatio = 0.5f;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
