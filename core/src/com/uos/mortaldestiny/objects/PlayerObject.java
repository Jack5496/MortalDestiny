package com.uos.mortaldestiny.objects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationDesc;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationListener;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.MyMotionState;

public class PlayerObject extends GameObject implements Disposable {
	public Player player;
	public boolean onGround = true;
	public boolean ended = false;
	public boolean running = false;
	
	public static final String WALK = "Armature|walk";

	public AnimationController animationController;

	public PlayerObject(GameObject obj, Player player) {
		super(obj.model, obj.constructionInfo);
		init(player);
	}
	
	private void init(Player player){
		animationController = new AnimationController(this);
		this.player = player;		
//		obj.body.setLinearFactor(new Vector3(1,0,1));	//hold object in XZ-Layer
		
		body.setAngularFactor(new Vector3(0,1,0));	//wont let object rotate around given axe
	}
	
	private double shotLast = 0;
	private double shotPerSecond = 5;
	private double shotWait = (1000/shotPerSecond);
	
	public void shoot(){
		double now = System.currentTimeMillis();
		if(shotLast+shotWait<now){	//if enough time elapsed
			shotLast = now;
			GameObject ball = GameClass.getInstance().physics.spawn("sphere");
//			BulletObject ball = GameClass.getInstance().physics.spawnSack(20);
//			ball.body.setMassProps(10, new Vector3(1,1,1));
			Vector3 lookDir = myGetYawVector();
			Vector3 pos = myGetTranslation();
			pos.add(lookDir.scl(2f));
			ball.mySetTranslation(pos);
			
			//Add ball same Velocity Horizontally
//			Vector3 linV = ball.body.getLinearVelocity();
//			linV.y = body.getLinearVelocity().y;
//			ball.body.setLinearVelocity(linV);
			//end
			
			ball.mySetScale(1);
			
			ball.body.applyCentralImpulse(lookDir.scl(20/ball.body.getInvMass()));
		}
	}
	
	public void moveHorizontal(Vector3 dir){
		Vector3 linV = body.getLinearVelocity();
		dir.y = linV.y;
		
		body.setLinearVelocity(dir);
	}

	public void jump() {
		if (onGround) {
			System.out.println("Lets Jump");
			onGround = false;
			body.applyCentralImpulse(new Vector3(0, 10, 0));
		}
	}

	public void walkAnimation() {
		if (ended) {
			animationController.setAnimation(null);
			ended = false;
		} else {
			if (!running) {
				running = true;
				String ani = animations.get(0).id;
				animationController.setAnimation(ani, 0, 3.25f, 1, 10 * 0.5f, new AnimationListener() {

					@Override
					public void onEnd(AnimationDesc animation) {
						// this will be called when the current animation is
						// done.
						// queue up another animation called "balloon".
						// Passing a negative to loop count loops forever. 1f
						// for
						// speed is normal speed.
						// controller.setAnimation(null);
						running = false;
						ended = true;
					}

					@Override
					public void onLoop(AnimationDesc animation) {
						// TODO Auto-generated method stub

					}

				});
			}
		}

	}

	@Override
	public void update(float delta) {
		animationController.update(delta);
	}

	public void stop() {
		animationController.setAnimation(null);
		ended = false;
	}

	public static boolean isPlayerObject(GameObject obj) {
		return (obj instanceof PlayerObject);
	}
}