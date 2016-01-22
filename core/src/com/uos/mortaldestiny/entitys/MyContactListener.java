package com.uos.mortaldestiny.entitys;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;

public class MyContactListener extends ContactListener {
	
	public static int PLAYER_FLAG = 2; // second bit
	public static int COIN_FLAG = 4; // third bit
	
    @Override
    public void onContactStarted (btCollisionObject colObj0, btCollisionObject colObj1) {
        // implementation
    }
    @Override
    public void onContactProcessed (int userValue0, int userValue1) {
        // implementation
    }
    
    @Override
    public void onContactEnded (int userValue0, boolean match0, int userValue1, boolean match1) {
        if (match0) {
            // collision object 0 (userValue0) matches
        }
        if (match1) {
            // collision object 1 (userValue1) matches
        }
    }
}