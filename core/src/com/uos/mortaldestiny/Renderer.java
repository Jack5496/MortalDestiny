package com.uos.mortaldestiny;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.player.Player;

public class Renderer {

	public SpriteBatch batch;
	public BitmapFont font;

	public Renderer() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.RED);
	}

	int width = GameClass.getInstance().getWidth();
	int height = GameClass.getInstance().getHeight();

	public void renderForPlayers() {
		int amountPlayers = GameClass.getInstance().playerHandler.getPlayerAmount();

		// frameBuffer.begin();

		Gdx.gl.glClearColor(0, 0, 0, 1.f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		// switch (amountPlayers) {
		// case 1:
		// renderForOnePlayer(GameClass.getInstance().physics.modelBatch);
		// break;
		// case 2:
		// renderForTwoPlayer(GameClass.getInstance().physics.modelBatch);
		// break;
		// case 3:
		// renderForThreePlayer(GameClass.getInstance().physics.modelBatch);
		// break;
		// case 4:
		// renderForFourPlayer(GameClass.getInstance().physics.modelBatch);
		// break;
		// }
		//
		// frameBuffer.end();

		ShaderProgram outlineShader = loadShader();

		FrontFaceDepthShaderProvider depthshaderprovider = new FrontFaceDepthShaderProvider();
		ModelBatch depthModelBatch = new ModelBatch(depthshaderprovider);

		FrameBuffer frameBuffer1 = new FrameBuffer(Format.RGB888, width, height, true);
		FrameBuffer frameBuffer2 = new FrameBuffer(Format.RGB888, width, height, true);

		FrameBuffer dest = frameBuffer1;
		dest.begin();
		{
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
			switch (amountPlayers) {
			case 1:
				renderForOnePlayer(depthModelBatch);
				break;
			case 2:
				renderForTwoPlayer(depthModelBatch);
				break;
			case 3:
				renderForThreePlayer(depthModelBatch);
				break;
			case 4:
				renderForFourPlayer(depthModelBatch);
				break;
			}
		}
		dest.end();

		Mesh fullScreenQuad = createFullScreenQuad();

		FrameBuffer src = dest;
		dest = frameBuffer2;
		dest.begin();
		src.getColorBufferTexture().bind();
		{
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
			outlineShader.begin();
			{
				System.out.println(outlineShader.isCompiled());
				
				outlineShader.setUniformf("u_size", (float)src.getColorBufferTexture().getWidth(), (float)src.getColorBufferTexture().getHeight());
				fullScreenQuad.render(outlineShader, GL20.GL_TRIANGLE_STRIP, 0, 4);
			}
			outlineShader.end();
		}
		dest.end();
		SpriteBatch batch = GameClass.getInstance().renderer.batch;
		batch.begin();
		TextureRegion fboRegion = new TextureRegion(dest.getColorBufferTexture());
		fboRegion.flip(false, true);

		batch.draw(fboRegion, 0, 0, width, height);
		batch.end();
	}
	
	public ShaderProgram loadShader(){
		String efs = Gdx.files.internal("data/shaders/edgeFs.glsl").readString();
		String evs = Gdx.files.internal("data/shaders/edgeVs.glsl").readString();
		
		ShaderProgram outlineShader = new ShaderProgram(evs, efs);
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
		verts[i++] = 1.f; // x3
		verts[i++] = 1.f; // y2
		verts[i++] = 1.f; // u3
		verts[i++] = 1.f; // v3
		verts[i++] = -1.f; // x4
		verts[i++] = 1.f; // y4
		verts[i++] = 0.f; // u4
		verts[i++] = 1.f; // v4
		Mesh tmpMesh = new Mesh(true, 4, 0, new VertexAttribute(Usage.Position, 2, "a_position"),
				new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0"));
		tmpMesh.setVertices(verts);
		return tmpMesh;
	}

	int border = 1;

	private void renderForPlayer(Player p, int x, int y, int width, int height, ModelBatch batch) {
		p.cameraController.updateViewPort(width - border, height - border);
		p.cameraController.update(); // Update Camera Position

		Gdx.gl.glViewport(x, y, width - border, height - border);

		p.menuHandler.renderActivMenu(batch);
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
