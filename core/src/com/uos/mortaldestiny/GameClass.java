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
import com.badlogic.gdx.utils.Array;
import com.uos.mortaldestiny.Inputs.InputHandler;

public class GameClass extends ApplicationAdapter {

	// private SpriteBatch batch;
	// private BitmapFont font;
	// private Texture img;
	private Graphics grafics;

	public static GameClass application;

	public PerspectiveCamera cam;
	// public OrthographicCamera cam;
	final Matrix4 matrix = new Matrix4();

	public Model model;
	public Model player;
	public Model obstacle;
	public ModelInstance playerInstance;
	public ModelBatch modelBatch;

	public Environment environment;
	public boolean loading;

	public String pathModels = "data/models/";
	public String pathGrounds = pathModels + "MapParts/Ground/";

	public AssetManager assets;
	public Array<ModelInstance> instances = new Array<ModelInstance>();

	public CameraInputController camController;

	public InputHandler inputs;
	
	public static GameClass getInstance(){
		return application;
	}

	@Override
	public void create() {
		application = this;
		// batch = new SpriteBatch();
		// font = new BitmapFont();
		// font.setColor(Color.RED);
		// img = new Texture(Gdx.files.internal("data/badlogic.jpg"));
		grafics = Gdx.app.getGraphics();

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(10f, 10f, 10f);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 300f;

		int zoom = 10;

		// cam = new OrthographicCamera(10, 10 * (Gdx.graphics.getHeight() /
		// (float)Gdx.graphics.getWidth()));
		// cam.position.set(10, 15, 10);
		// cam.direction.set(-1, -1, -1);
		// cam.near = 1;
		// cam.far = 100;
		// matrix.setToRotation(new Vector3(1, 0, 0), 90);

		cam.update();

		// ModelBuilder modelBuilder = new ModelBuilder();
		// model = modelBuilder.createBox(5f, 5f, 5f, new
		// Material(ColorAttribute.createDiffuse(Color.GREEN)),
		// Usage.Position | Usage.Normal);
		// instance = new ModelInstance(model);

		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		inputs = new InputHandler();
		
		camController = new CameraInputController(cam);
//		 Gdx.input.setInputProcessor(camController);



		assets = new AssetManager();

		loading = true;

		ModelLoader loader = new ObjLoader();
		model = loader.loadModel(Gdx.files.internal(pathGrounds + "GroundTile5x5.obj"));

		player = loader.loadModel(Gdx.files.internal(pathModels + "Player/player.obj"));
		obstacle = loader.loadModel(Gdx.files.internal(pathModels + "MapParts/Construction/constructionObstacle.obj"));

		assets.load("data/models/ship/ship.obj", Model.class);
	}

	private void doneLoading() {

		float stepx = 11f;
		float maxx = 3 * (stepx + 2);

		float stepz = 11f;
		float maxz = 3 * (stepz + 2);

		playerInstance = new ModelInstance(player);
		playerInstance.transform.setToTranslation(3, 2, 3);
		instances.add(playerInstance);

		ModelInstance obstacleInstance = new ModelInstance(obstacle);
		obstacleInstance.transform.setToTranslation(-maxx + 3 * stepx, 0, -maxz + stepz);
		instances.add(obstacleInstance);

		for (float x = -maxx; x <= maxx; x += stepx) {
			for (float z = -maxz; z <= maxz; z += stepz) {
				ModelInstance shipInstance = new ModelInstance(model);
				shipInstance.transform.setToTranslation(x, 0, z);
				instances.add(shipInstance);
			}
		}
		loading = false;
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
		modelBatch.dispose();
	}

	public void move() {
		float dx = 0;
		float dz = 0;

		float speed = 0.5f;

		if (inputs.downLeft()) {
			dz += speed;
		}
		if (inputs.upRight()) {
			dz -= speed;
		}
		if (inputs.upLeft()) {
			dx -= speed;
		}
		if (inputs.downRight()) {
			dx += speed;
		}

		if (playerInstance != null) {
			cam.translate(dx, 0, dz);
			
			playerInstance.transform.trn(dx, 0, dz);
			
			
			cam.update();
		}
	}

	@Override
	public void render() {
		// Gdx.gl.glClearColor(1, 1, 1, 1);
		// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//
		//
		// batch.begin();
		//// batch.draw(img, 0, 0);
		// font.draw(batch, getWidth()+" x "+getHeight(), 250, getHeight()/2);
		// batch.end();

		if (loading && assets.update()) {
			doneLoading();
		}

		move();
		camController.update();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		modelBatch.end();
	}
}
