package com.uos.mortaldestiny.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.bullet.collision.CollisionJNI;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.objects.GameObject;
import com.uos.mortaldestiny.objects.PlayerObject;

public class MyContactListener extends ContactListener {

//	public final static short GROUND_FLAG = 1 << 8;
//	public final static short OBJECT_FLAG = 1 << 9;
//	public final static short ALL_FLAG = -1;
	
	public final static int DEATH_ZONE = 0;
	public final static int GROUND_FLAG = 1;
	public final static int OBJECT_FLAG = 2;
	public final static int ALL_FLAG = -1;

	
	//when body still touches other
	@Override
	public boolean onContactAdded(int userValue0, int partId0, int index0, int userValue1, int partId1, int index1) {
		// if (userValue0 != 0) {
		// ((ColorAttribute)
		// GameClass.instances.get(userValue0).materials.get(0).get(ColorAttribute.Diffuse)).color
		// .set(Color.WHITE);
		// System.out.println("ContactAdded: u0");
		// }
		// if (userValue1 != 0) {
		// ((ColorAttribute)
		// GameClass.instances.get(userValue1).materials.get(0).get(ColorAttribute.Diffuse)).color
		// .set(Color.WHITE);
		// System.out.println("ContactAdded: u1");
		// }
		return true;
	}

	//on first contact
	@Override
	public void onContactStarted(int userValue0, int userValue1) {
		GameObject obj0 = GameClass.instances.get(userValue0);
		GameObject obj1 = GameClass.instances.get(userValue1);
		
		if(obj0.body.getCollisionFlags()==DEATH_ZONE){
			System.out.println("DeathZone0");
		}
		if(obj1.body.getCollisionFlags()==DEATH_ZONE){
			System.out.println("DeathZone1");
		}
		
		
		if (userValue0 != 0) {
			((ColorAttribute) GameClass.instances.get(userValue0).materials.get(0).get(ColorAttribute.Diffuse)).color
					.set(Color.WHITE);
			// System.out.println("ContactStarted: u0");
		}
		if (userValue1 != 0) {
			((ColorAttribute) GameClass.instances.get(userValue1).materials.get(0).get(ColorAttribute.Diffuse)).color
					.set(Color.WHITE);
			// System.out.println("ContactStarted: u1");
			GameObject o = GameClass.instances.get(userValue1);
			if (userValue0 == 0) { // this is the real ground
//				System.out.println("Hit ground");
				if (o instanceof PlayerObject) {
					PlayerObject p = (PlayerObject) o;
					p.onGround = true;
				}

			} else { // that is an other object
				if (o instanceof PlayerObject) {
					PlayerObject p = (PlayerObject) o;
					p.onGround = true;
				}
			}
		}
	}
}
