package com.uos.mortaldestiny;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.uos.mortaldestiny.Inputs.InputHandler;
import com.uos.mortaldestiny.Inputs.PlayerHandler;
import com.uos.mortaldestiny.objects.CameraController;
import com.uos.mortaldestiny.objects.GameObject;
import com.uos.mortaldestiny.objects.Player;
import com.uos.mortaldestiny.world.WorldManager;

public class GameClass implements ApplicationListener {

	// PerspectiveCamera cam;
	// CameraInputController camController;

	public ResourceManager resourceManager;
	public InputHandler inputs;
	public PlayerHandler playerHandler;
	public Physics physics;
	public WorldManager worldManager;
	public Renderer renderer;

	public static Array<GameObject> instances;
	// public static Array<ModelInstance> instances;

	private static GameClass application;



	public static GameClass getInstance() {
		return application;
	}

	@Override
	public void create() {
		application = this;

		initResourceManager();
		initPhysics();
		initWorldManager();
		initInputHandler();
		initPlayerHandler();
		initRenderer();
	}

	public static Array<GameObject> getInstances() {
		return instances;
	}

	public void initResourceManager() {
		resourceManager = new ResourceManager();
	}

	public void initPhysics() {
		physics = new Physics();
	}
	
	public void initWorldManager() {
		worldManager = new WorldManager();
	}

	public void initInputHandler() {
		inputs = new InputHandler();
	}

	public void initPlayerHandler() {
		playerHandler = new PlayerHandler();
	}
	
	public void initRenderer(){
		renderer = new Renderer();
	}

	float angle = 90f;
	float speed = 40f;

	int amount = 0;

	@Override
	public void render() {
		updateInputsAndGameWorld();		
		renderer.renderForPlayers();
	}

	private void updateInputsAndGameWorld() {
		final float delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());

		inputs.updateInputLogic(); // Update Inputs which effect Players
		playerHandler.updatePlayers(); // Players set PlayerObjects animations
		updateGameObjectsAnimations(delta); // PlayerObjects calc animation

		physics.dynamicsWorld.stepSimulation(delta, 5, 1f / 60f); // Calcs
																	// Physics

		if ((physics.spawnTimer -= delta) < 0 && amount < 10) { // spawn 10
																// random
																// GameObjects
			// physics.spawn();
			physics.spawnTimer = 1.5f;
			amount++;
		}
	}

	private void updateGameObjectsAnimations(float delta) {
		for (GameObject o : instances) {
			o.update(delta);
		}
	}

	public int getWidth() {
		return Gdx.graphics.getWidth();
	}

	public int getHeight() {
		return Gdx.graphics.getHeight();
	}

	@Override
	public void dispose() {
		for (ModelInstance obj : instances)
			if (obj instanceof GameObject)
				((GameObject) obj).dispose();
		instances.clear();

		physics.dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void resize(int width, int height) {
	}
}
