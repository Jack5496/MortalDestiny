package com.uos.mortaldestiny;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.utils.Array;
import com.uos.mortaldestiny.Inputs.InputHandler;
import com.uos.mortaldestiny.entitys.CameraController;
import com.uos.mortaldestiny.entitys.Player;

public class GameClass extends ApplicationAdapter {

	public static boolean debug = true;

	// private SpriteBatch batch;
	// private BitmapFont font;
	// private Texture img;
	private Graphics grafics;

	public static GameClass application;

	final Matrix4 matrix = new Matrix4();

	public Model model;
	public Model playerModel;
	public Model obstacle;
	public ModelBatch modelBatch;
	public Player player;

	public Environment environment;
	public boolean loading;

	public ResourceManager resourceManager;

	// public CameraInputController camController;

	public InputHandler inputs;
	public CameraController cameraController;
	public MyContactListener contactListener;

	public static GameClass getInstance() {
		return application;
	}

	@Override
	public void create() {
		application = this;

		Bullet.init(); // Loads Collision Physics; important; realy important;
		contactListener = new MyContactListener();
		contactListener.enable();

		grafics = Gdx.app.getGraphics();

		initEnvironment();
		initCamera();
		initResourceManager();
		initInputHandler();

		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.RED);
	}

	private SpriteBatch batch;
	private BitmapFont font;

	public void initResourceManager() {
		resourceManager = new ResourceManager();
	}

	public void initInputHandler() {
		inputs = new InputHandler();
	}

	public void initEnvironment() {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
	}

	public void initCamera() {
		cameraController = new CameraController();
	}

	@Override
	public void resize(int width, int height) {

	}

	public int getWidth() {
		return grafics.getWidth();
	}

	public int getHeight() {
		return grafics.getHeight();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		// batch.dispose();
		// font.dispose();
		// model.dispose();
		resourceManager.modelBatch.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		resourceManager.update();

		inputs.updateInputLogic();
		cameraController.update();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		player.controller.update(Gdx.graphics.getDeltaTime());

		resourceManager.modelBatch.begin(cameraController.getCamera());
		resourceManager.modelBatch.render(resourceManager.instances, environment);
		resourceManager.modelBatch.end();

		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, getHeight() - 5);
		batch.end();
	}
}
