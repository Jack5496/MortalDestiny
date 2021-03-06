package com.uos.mortaldestiny;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Array;
import com.uos.mortaldestiny.Inputs.InputHandler;
import com.uos.mortaldestiny.objects.GameObject;
import com.uos.mortaldestiny.player.AIHandler;
import com.uos.mortaldestiny.player.PlayerHandler;
import com.uos.mortaldestiny.rendering.Renderer;
import com.uos.mortaldestiny.world.Physics;
import com.uos.mortaldestiny.world.WorldManager;

public class GameClass implements ApplicationListener {

	public ResourceManager resourceManager;
	public InputHandler inputs;
	public PlayerHandler playerHandler;
	public AIHandler aiHandler;
	public Physics physics;
	public WorldManager worldManager;
	public Renderer renderer;
	
	public static int totalObjectsSpawned = 0;

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
		initAIHandler();
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
	
	public void initAIHandler() {
		aiHandler = new AIHandler();
	}

	public void initRenderer() {
		renderer = new Renderer();
	}

	int amount = 0;

	@Override
	public void render() {
		updateInputsAndGameWorld();
		renderer.renderForPlayers();
	}

	private void updateInputsAndGameWorld() {
		final float delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());

		inputs.updateInputLogic(); // Update Inputs which effect Players
		
		playerHandler.updateEntityHealth();
		aiHandler.updateEntityHealth();
		
		playerHandler.updatePlayers(); // Players set PlayerObjects animations
		aiHandler.updateAIs();
		
		updateGameObjectsInformations(delta); // PlayerObjects calc animation

		physics.dynamicsWorld.stepSimulation(delta, 5, 1f / 60f); // Calcs
																	// Physics
	}
	
	
	private void updateGameObjectsInformations(float delta) {
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
	
	public static void log(Class<?> c, String msg){
		System.out.println(c.getSimpleName()+": "+msg);
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
