package com.uos.mortaldestiny.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.player.AIHandler;
import com.uos.mortaldestiny.player.Player;

public class Renderer {

	public SpriteBatch batch;
	public BitmapFont font;

	public Renderer() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(1, 0, 0, 1);
		resizeFrameBuffers();
	}

	int width = GameClass.getInstance().getWidth();
	int height = GameClass.getInstance().getHeight();

	public void resizeFrameBuffers() {
		frameBuffer = new FrameBuffer(Format.RGBA8888, width, height, true);
		frameBuffer1 = new FrameBuffer(Format.RGBA8888, width, height, true);
		frameBuffer2 = new FrameBuffer(Format.RGBA8888, width, height, true);
	}

	// public void resizeOutline(){
	// float ratioX = (float)width/Gdx.graphics.getWidth();
	// float ratioY = (float)height/Gdx.graphics.getHeight();
	//
	// frameBuffer2 = new FrameBuffer(Format.RGBA8888, (int) (width*ratioX),
	// (int) (height*ratioY), true);
	// }

	ShaderProgram outlineShader = loadShader();

	public static FrameBuffer frameBuffer;
	public static FrameBuffer frameBuffer1;
	public static FrameBuffer frameBuffer2;

	ModelBatch depthModelBatch = new ModelBatch(new FrontFaceDepthShaderProvider());

	public static boolean renderNormal = true;
	public static boolean renderOutlines = true;
	public static boolean renderHUD = true;

	public void renderForPlayers() {
		Gdx.gl.glClearColor(1, 1, 1, 1f);
		Gdx.gl.glViewport(0, 0, GameClass.getInstance().getWidth(), GameClass.getInstance().getHeight());
		if (renderNormal) {
			renderWithBatch(GameClass.getInstance().physics.modelBatch);
		}
		if (renderOutlines) {
			renderOutlines();
			renderFrameBuffer(frameBuffer2);
		}
		if (renderHUD) {
			renderHUDForPlayers();
		}
	}

	private void renderHUD(Player p) {
		int height = (int) p.cameraController.camera.viewportHeight;
		// int width = (int) p.cameraController.camera.viewportWidth;
		SpriteBatch batch = GameClass.getInstance().renderer.batch;
		BitmapFont font = GameClass.getInstance().renderer.font;

		batch.enableBlending();
		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, height - 5);
		font.draw(batch, "Objects: " + GameClass.instances.size, 80, height - 5);
		Vector3 pos = p.getObjPos();
		font.draw(batch, "X: " + (int) pos.x + "    | Y: " + (int) pos.y + "    | Z: " + (int) pos.z, 5, height - 20);
		font.draw(batch, "Player Health: " + p.health, 5, height - 60);
		font.draw(batch, "Points: " + p.points, 5, height - 75);

		String totalObjectsSpawned = "" + GameClass.totalObjectsSpawned;
		if (GameClass.totalObjectsSpawned > 10000) {
			totalObjectsSpawned = "" + (GameClass.totalObjectsSpawned / 1000) + " K";
		}

		font.draw(batch, "Spawned Object Total: " + totalObjectsSpawned, 5, height - 90);
		batch.end();

	}

	public void renderHUDForPlayers() {
		for (Player p : GameClass.getInstance().playerHandler.getPlayers()) {
			renderHUD(p);
		}
	}

	public void renderWithBatch(ModelBatch batch) {
		int amountPlayers = GameClass.getInstance().playerHandler.getPlayerAmount();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		switch (amountPlayers) {
		case 1:
			renderForOnePlayer(batch);
			break;
		case 2:
			renderForTwoPlayer(batch);
			break;
		case 3:
			renderForThreePlayer(batch);
			break;
		case 4:
			renderForFourPlayer(batch);
			break;
		}
	}

	public void renderDepthMap(FrameBuffer frame) {
		frame.begin();
		{
			renderWithBatch(depthModelBatch);
		}
		frame.end();
	}

	public void renderOutlines() {
		renderDepthMap(frameBuffer1);

		// resizeOutline();
		renderOutlines(frameBuffer2, frameBuffer1);
	}

	public void renderOutlines(FrameBuffer frame, FrameBuffer source) {
		Mesh fullScreenQuad = createFullScreenQuad();
		source.getColorBufferTexture().bind();
		frame.begin();
		{
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
			outlineShader.begin();
			{
				outlineShader.setUniformf("u_size", (float) source.getColorBufferTexture().getWidth(),
						(float) source.getColorBufferTexture().getHeight());
				fullScreenQuad.render(outlineShader, GL20.GL_TRIANGLE_STRIP, 0, 4);
			}
			outlineShader.end();
		}
		frame.end();
	}

	public void renderFrameBuffer(FrameBuffer frame) {
		SpriteBatch batch = GameClass.getInstance().renderer.batch;

		TextureRegion fboRegion = new TextureRegion(frame.getColorBufferTexture());
		fboRegion.flip(false, true);

		batch.enableBlending();
		batch.begin();
		batch.draw(fboRegion, 0, 0, fboRegion.getRegionWidth(), fboRegion.getRegionHeight());
		batch.disableBlending();
		batch.end();
	}

	public ShaderProgram loadShader() {
		String efs = Gdx.files.internal("data/shaders/edgeFs.glsl").readString();
		String evs = Gdx.files.internal("data/shaders/edgeVs.glsl").readString();

		ShaderProgram outlineShader = new ShaderProgram(evs, efs);
		// System.out.println(outlineShader.getLog());
		return outlineShader;
	}

	public Mesh createFullScreenQuad() {
		float[] verts = new float[16];
		int i = 0;
		verts[i++] = -1.f; // x1
		verts[i++] = -1.f; // y1
		verts[i++] = 0.f; // u1
		verts[i++] = 0.f; // v1
		verts[i++] = 1.f; // x2
		verts[i++] = -1.f; // y2
		verts[i++] = 1.f; // u2
		verts[i++] = 0.f; // v2
		verts[i++] = -1.f; // x4
		verts[i++] = 1.f; // y4
		verts[i++] = 0.f; // u4
		verts[i++] = 1.f; // v4
		verts[i++] = 1.f; // x3
		verts[i++] = 1.f; // y2
		verts[i++] = 1.f; // u3
		verts[i++] = 1.f; // v3
		Mesh tmpMesh = new Mesh(true, 4, 0, new VertexAttribute(Usage.Position, 2, "a_position"),
				new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0"));
		tmpMesh.setVertices(verts);
		return tmpMesh;
	}

	int border = 1;
	
	boolean test = true;

	private void renderForPlayer(Player p, int x, int y, int width, int height, ModelBatch batch) {
		p.cameraController.updateViewPort(width - border, height - border);
		p.cameraController.update(); // Update Camera Position

		Gdx.gl.glViewport(x, y, width - border, height - border);
		
		if(test){
			test = false;
		}
		else{
			Gdx.gl.glViewport(x, y, this.width - border, this.height - border);
			test = true;
		}

		p.menuHandler.renderActivMenu(batch);
		// Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(),
		// Gdx.graphics.getHeight());
	}

	private void renderForOnePlayer(ModelBatch batch) {
		Player p = GameClass.getInstance().playerHandler.getPlayer(0);
		renderForPlayer(p, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), batch);
	}

	public void renderForTwoPlayer(ModelBatch batch) {
		Player p0 = GameClass.getInstance().playerHandler.getPlayer(0);
		renderForPlayer(p0, 0, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight(), batch);

		Player p1 = GameClass.getInstance().playerHandler.getPlayer(1);
		renderForPlayer(p1, Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight(),
				batch);
	}

	public void renderForThreePlayer(ModelBatch batch) {
		Player p0 = GameClass.getInstance().playerHandler.getPlayer(0);
		renderForPlayer(p0, 0, Gdx.graphics.getHeight() / 2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2,
				batch);

		Player p1 = GameClass.getInstance().playerHandler.getPlayer(1);
		renderForPlayer(p1, 0, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, batch);

		Player p2 = GameClass.getInstance().playerHandler.getPlayer(2);
		renderForPlayer(p2, Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2,
				batch);
	}

	public void renderForFourPlayer(ModelBatch batch) {
		Player p0 = GameClass.getInstance().playerHandler.getPlayer(0);
		renderForPlayer(p0, 0, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, batch);

		Player p1 = GameClass.getInstance().playerHandler.getPlayer(1);
		renderForPlayer(p1, 0, Gdx.graphics.getHeight() / 2, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2,
				batch);

		Player p2 = GameClass.getInstance().playerHandler.getPlayer(2);
		renderForPlayer(p2, Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2,
				batch);

		Player p3 = GameClass.getInstance().playerHandler.getPlayer(3);
		renderForPlayer(p3, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2, batch);
	}

}
