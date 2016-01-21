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

	 String pp = "data/models/Player/player.g3db";

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
	private ModelInstance animInstance;
	public AnimationController controller;
	
//	UBJsonReader jsonReader = new UBJsonReader();
//	G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
	
	public Model getG3DBModel(String path){
//		return modelLoader.loadModel(Gdx.files.getFileHandle(path, FileType.Internal));
		return assets.get(path);
	}
	
	public void loadAnimation() {
		anim = getG3DBModel(pp);
		animInstance = new ModelInstance(anim);
		animInstance.transform.scl(0.01f);
		animInstance.transform.trn(0, 0, 5);
		controller = new AnimationController(animInstance);
		
		instances.add(animInstance);
	}

	public void doneLoading() {
		float test = 0f;
		int width = 3;
		int height = 3;

		float stepx = 10f + test;
		float maxx = width * (stepx + 2);

		float stepz = 10f + test;
		float maxz = height * (stepz + 2);
		
//		loadAnimation();

//		ModelInstance modelInstance = new ModelInstance(playerModel);

//		GameClass.getInstance().player = new Player(new ModelInstance(modelInstance));
		
		anim = getG3DBModel("data/models/Player/player.g3db");
		animInstance = new ModelInstance(anim);
		animInstance.transform.scl(0.01f);
		animInstance.transform.trn(0, 0, 5);
		
		GameClass.getInstance().player = new Player(animInstance);
		
		GameClass.getInstance().cameraController.setTrack(GameClass.getInstance().player.getModelInstance());
		
		instances.add(GameClass.getInstance().player.getModelInstance());

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
	}
}
