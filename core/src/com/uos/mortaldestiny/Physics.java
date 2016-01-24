package com.uos.mortaldestiny;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btConeShape;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;
import com.uos.mortaldestiny.objects.GameObject;

public class Physics implements Disposable {

	ModelBatch modelBatch;

	Model model;
	ModelBuilder mb;

	ArrayMap<String, GameObject.Constructor> constructors;

	btCollisionConfiguration collisionConfig;
	btDispatcher dispatcher;
	MyContactListener contactListener;
	btBroadphaseInterface broadphase;
	btDynamicsWorld dynamicsWorld;
	btConstraintSolver constraintSolver;

	float spawnTimer;
	
	float size = 10;

	public Physics() {
		Bullet.init();

		modelBatch = new ModelBatch();

		ModelBuilder mb = new ModelBuilder();
		mb.begin();
		mb.node().id = "ground";
		mb.part("ground", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.RED))).box(10f, 1f, 10f);
		mb.node().id = "sphere";
		mb.part("sphere", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.GREEN))).sphere(1f, 1f, 1f, 10, 10);
		mb.node().id = "box";
		mb.part("box", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.BLUE))).box(1f, 1f, 1f);
		mb.node().id = "cone";
		mb.part("cone", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.YELLOW))).cone(1f, 2f, 1f, 10);
		mb.node().id = "capsule";
		mb.part("capsule", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.CYAN))).capsule(0.5f, 2f, 10);
		mb.node().id = "cylinder";
		mb.part("cylinder", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.MAGENTA))).cylinder(1f, 2f, 1f, 10);

		Model anim = GameClass.getInstance().resourceManager.getPlayer();
		Model field = ResourceManager.getInstance()
				.getG3DBModel(ResourceManager.getInstance().pathGrounds + "spielfeld.g3db");
		Model box = ResourceManager.getInstance()
				.getG3DBModel(ResourceManager.getInstance().pathModels + "1x1.g3db");

		model = mb.end();

		constructors = new ArrayMap<String, GameObject.Constructor>(String.class, GameObject.Constructor.class);
		constructors.put("ground", new GameObject.Constructor(model, "ground",
				new btBoxShape(new Vector3(5f, 0.5f, 5f)), 0f));
		constructors.put("field", new GameObject.Constructor(field, new btBoxShape(new Vector3(size*6, size*1, size*3)), 0f));
		constructors.put("player", new GameObject.Constructor(anim, new btCylinderShape(new Vector3(2f, 4f, 2f)), 1f));

		constructors.put("sphere", new GameObject.Constructor(model, "sphere", new btSphereShape(0.5f), 1f));
		
//		constructors.put("box",
//				new GameObject.Constructor(model, "box", new btBoxShape(new Vector3(0.5f, 0.5f, 0.5f)), 1f));
		constructors.put("box",
				new GameObject.Constructor(box, new btBoxShape(new Vector3(1, 1, 1)), 1f));
		
		
		constructors.put("cone", new GameObject.Constructor(model, "cone", new btConeShape(0.5f, 2f), 1f));
		constructors.put("capsule", new GameObject.Constructor(model, "capsule", new btCapsuleShape(.5f, 1f), 1f));
		constructors.put("cylinder",
				new GameObject.Constructor(model, "cylinder", new btCylinderShape(new Vector3(.5f, 1f, .5f)), 1f));

		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);
		broadphase = new btDbvtBroadphase();
		constraintSolver = new btSequentialImpulseConstraintSolver();
		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
		dynamicsWorld.setGravity(new Vector3(0, -10f, 0));
		contactListener = new MyContactListener();

		GameClass.instances = new Array<ModelInstance>();
		
		
//		GameObject object = constructors.get("ground").construct();
//		object.body.setCollisionFlags(
//				object.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
//		GameClass.instances.add(object);
//		dynamicsWorld.addRigidBody(object.body);
//		object.body.setContactCallbackFlag(MyContactListener.GROUND_FLAG);
//		object.body.setContactCallbackFilter(0);
//		object.body.setActivationState(Collision.DISABLE_DEACTIVATION);
		
		GameObject fo = constructors.get("field").construct();
		fo.mySetScale(size*1f);
		fo.transform.trn(new Vector3(0,-1,0));
		fo.calculateTransforms();
		fo.body.setCollisionFlags(
				fo.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
		GameClass.instances.add(fo);
		dynamicsWorld.addRigidBody(fo.body);
		fo.body.setContactCallbackFlag(MyContactListener.GROUND_FLAG);
		fo.body.setContactCallbackFilter(0);
		fo.body.setActivationState(Collision.DISABLE_DEACTIVATION);
//		fo.body.setActivationState(Collision.WANTS_DEACTIVATION);
		
		
		spawnBall();
	}

	public Model playerModel;

	boolean done = true;

	public GameObject spawnPlayer() {
		GameObject obj = constructors.get("box").construct();

		obj.transform.trn(MathUtils.random(size*-2.5f, size*2.5f), 15f, MathUtils.random(size*-2.5f, size*2.5f));
		obj.body.proceedToTransform(obj.transform);
		obj.mySetScale(1f);
		obj.calculateTransforms();
		obj.body.setUserValue(GameClass.instances.size);
		obj.body.setCollisionFlags(
				obj.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		GameClass.instances.add(obj);
		dynamicsWorld.addRigidBody(obj.body);
		obj.body.setContactCallbackFlag(MyContactListener.OBJECT_FLAG);
//		obj.body.setContactCallbackFilter(0);
		obj.body.setContactCallbackFilter(MyContactListener.GROUND_FLAG);
		System.out.println("Obj: "+obj.toString());

		return obj;
	}
	
//	size*6, size*1, size*3

	public void spawn() {
		GameObject obj = constructors.values[2 + MathUtils.random(constructors.size - 3)].construct();
		obj.transform.setFromEulerAngles(MathUtils.random(360f), MathUtils.random(360f), MathUtils.random(360f));
		obj.transform.trn(MathUtils.random(-2.5f,2.5f), 15f, MathUtils.random(-2.5f, 2.5f));
		obj.body.proceedToTransform(obj.transform);
		obj.body.setUserValue(GameClass.instances.size);
		obj.body.setCollisionFlags(
				obj.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		GameClass.instances.add(obj);
		dynamicsWorld.addRigidBody(obj.body);
		obj.body.setContactCallbackFlag(MyContactListener.OBJECT_FLAG);
		obj.body.setContactCallbackFilter(MyContactListener.GROUND_FLAG);
	}
	
	public void spawnBall(){
		GameObject obj = constructors.get("sphere").construct();
		obj.transform.setFromEulerAngles(MathUtils.random(360f), MathUtils.random(360f), MathUtils.random(360f));
		obj.transform.trn(MathUtils.random(-2.5f,2.5f), 15f, MathUtils.random(-2.5f, 2.5f));
		obj.body.proceedToTransform(obj.transform);
		obj.body.setUserValue(GameClass.instances.size);
		obj.body.setCollisionFlags(
				obj.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		GameClass.instances.add(obj);
		dynamicsWorld.addRigidBody(obj.body);
		obj.body.setContactCallbackFlag(MyContactListener.OBJECT_FLAG);
		obj.body.setContactCallbackFilter(MyContactListener.GROUND_FLAG);
	}

	@Override
	public void dispose() {
		for (GameObject.Constructor ctor : constructors.values())
			ctor.dispose();
		constructors.clear();

		dynamicsWorld.dispose();
		constraintSolver.dispose();
		broadphase.dispose();
		dispatcher.dispose();
		collisionConfig.dispose();

		contactListener.dispose();

		modelBatch.dispose();
		model.dispose();
	}

}