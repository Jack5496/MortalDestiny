package com.uos.mortaldestiny;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.UBJsonReader;
import com.uos.mortaldestiny.Inputs.InputHandler;
import com.uos.mortaldestiny.entitys.CameraController;
import com.uos.mortaldestiny.entitys.Player;

public class ResourceManager {

	public AssetManager assets;
	private boolean loaded;
	private boolean init;

	public String pathModels = "data/models/";
	public String pathGrounds = pathModels + "MapParts/Ground/";

	public Array<ModelInstance> instances = new Array<ModelInstance>();

	public Model model;
	public Model playerModel;
	public Model obstacle;
	public ModelBatch modelBatch;

	public ResourceManager() {
		loaded = false;
		assets = new AssetManager();
		load();
		update();
	}

	public void update() {
		boolean doneLoading = !loaded && assets.update() && init;

		if (doneLoading) {
			doneLoading();
		}
	}

	public void dispose() {
		modelBatch.dispose();
	}

	 String pp = "data/models/Player/player.obj";
	// pp = "data/models/Player/player.dae";
//	String pp = "data/models/Player/player.g3db";

	public void load() {

		loadAssets();

		float progress = 0;
		System.out.println("Loaded: " + assets.getProgress() * 100 + "%");

		while (!assets.update()) {
			if (progress != assets.getProgress()) {
				progress = assets.getProgress();
				System.out.println("Loaded: " + assets.getProgress() * 100 + "%");
			}
		}
		progress = assets.getProgress();
		System.out.println("Loaded: " + assets.getProgress() * 100 + "%");

		initModels();
	}

	public void loadAssets() {
		assets.load(pathGrounds + "GroundTile5x5.obj", Model.class);
		assets.load(pp, Model.class);
	}

	public void initModels() {
		init = false;

		modelBatch = new ModelBatch();

		ModelLoader loader = new ObjLoader();

		model = assets.get(pathGrounds + "GroundTile5x5.obj", Model.class);
		// model = loader.loadModel(Gdx.files.internal(pathGrounds +
		// "GroundTile5x5.obj"));

		 playerModel = loader.loadModel(Gdx.files.internal(pathModels +
		 "Player/player.obj"));

		

		obstacle = loader.loadModel(Gdx.files.internal(pathModels + "MapParts/Construction/constructionObstacle.obj"));

		init = true;
	}

	private Model anim; 
	private ModelBatch animBatch;
	private ModelInstance animInstance;
	public AnimationController controller;
	
	public void loadAnimation() {
		animBatch = new ModelBatch();

		// Model loader needs a binary json reader to decode
		UBJsonReader jsonReader = new UBJsonReader();
		// Create a model loader passing in our json reader
		G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
		// Now load the model by name
		// Note, the model (g3db file ) and textures need to be added to the
		// assets folder of the Android proj
		anim = modelLoader.loadModel(Gdx.files.getFileHandle("data/blob.g3db", FileType.Internal));
		// Now create an instance. Instance holds the positioning data, etc of
		// an instance of your model
		animInstance = new ModelInstance(anim);

		// fbx-conv is supposed to perform this rotation for you... it doesnt
		// seem to
		
		animInstance.transform.rotate(0, 0, 0, 0);
		// move the model down a bit on the screen ( in a z-up world, down is -z
		// ).
		animInstance.transform.setToTranslation(0, 1, 0);
		animInstance.transform.scl(0.01f);
		animInstance.transform.trn(0, 0, 5);
//		animInstance.transform.translate(0, 30, 0);

		controller = new AnimationController(animInstance);
		// Pick the current animation by name
		
		instances.add(animInstance);
	}
	
	public void walk(){
		controller.setAnimation("Armature|walk", 0, 3.25f, 10, 4, null);
	}

	public void doneLoading() {
		float test = 0f;
		int width = 3;
		int height = 3;

		float stepx = 10f + test;
		float maxx = width * (stepx + 2);

		float stepz = 10f + test;
		float maxz = height * (stepz + 2);

		ModelInstance modelInstance = new ModelInstance(playerModel);

		// fbx-conv is supposed to perform this rotation for you... it doesnt
		// seem to
		modelInstance.transform.rotate(1, 0, 0, -90);
		// move the model down a bit on the screen ( in a z-up world, down is -z
		// ).
		modelInstance.transform.translate(0, 0, -2);

		GameClass.getInstance().player = new Player(new ModelInstance(modelInstance));
		GameClass.getInstance().cameraController.setTrack(GameClass.getInstance().player.getModelInstance());
		instances.add(GameClass.getInstance().player.getModelInstance());

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
		loaded = true;
		
		loadAnimation();
	}
}
