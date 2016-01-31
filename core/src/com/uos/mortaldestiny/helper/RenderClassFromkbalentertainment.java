package com.uos.mortaldestiny.helper;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.fotm.adventure.OverworldMap;
import com.fotm.adventure.OverworldMapNode;
import com.fotm.entity.EntitySystem;
import com.fotm.graphics.shader.FrontFaceDepthShaderProvider;

//import de.fruitfly.ovr.OculusRift;

public class RenderClassFromkbalentertainment {
	public static float fboscale = 1f;

	public static boolean or_initialized = false;
	public static FrameBuffer copyeyebuffer = new FrameBuffer(Pixmap.Format.RGBA8888,
			(int) (Gdx.graphics.getWidth() / 2f * fboscale), (int) (Gdx.graphics.getHeight() * fboscale), true);
	public static TextureRegion copyeyebuffertexreg = new TextureRegion(copyeyebuffer.getColorBufferTexture());
	public static boolean useglow = false;
	public static boolean useor = false;
	// public static OculusRift or = new OculusRift();

	private static Boolean staticinitialized = false;
	private static float[] distortionK;
	private static int glowfrac = 64;
	private static FrameBuffer finalbuffer1 = new FrameBuffer(Pixmap.Format.RGBA8888,
			(int) (Gdx.graphics.getWidth() * fboscale), (int) (Gdx.graphics.getHeight() * fboscale), true);
	private static FrameBuffer finalbuffer2 = new FrameBuffer(Pixmap.Format.RGBA8888,
			(int) (Gdx.graphics.getWidth() * fboscale), (int) (Gdx.graphics.getHeight() * fboscale), true);
	private static float lenscenteroffset;
	private static ShapeRenderer shapeRenderer = new ShapeRenderer();
	private static ModelBatch defaultBatch = new ModelBatch();
	// private static ModelBatch toonBatch = new ModelBatch(new
	// DefaultShaderProvider(Gdx.files.internal("shader/default_toon/default_toon.vertex.glsl"),
	// Gdx.files.internal("shader/default_toon/default_toon.fragment.glsl")));
	private static DecalBatch decalBatch;
	private static FrontFaceDepthShaderProvider depthshaderprovider = new FrontFaceDepthShaderProvider();
	private static ModelBatch depthModelBatch = new ModelBatch(depthshaderprovider);
	private static FrameBuffer helpbuffer1;
	private static FrameBuffer helpbuffer2;
	private static FrameBuffer glowbuffer1;
	private static FrameBuffer glowbuffer2;
	private static SpriteBatch spriteBatch = new SpriteBatch();;
	private static ShaderProgram edgeshader;
	private static ShaderProgram glowshader;
	private static ShaderProgram blurshader;
	private static ShaderProgram lensshader;
	private static Mesh fullScreenTriangle;

	public RenderClassFromkbalentertainment() {

	}

	public static void init(Camera cam) {
		if (cam != null) {
			CameraGroupStrategy strat = new CameraGroupStrategy(cam);
			decalBatch = new DecalBatch(strat);
		} else {
			decalBatch = new DecalBatch();
		}
		if (staticinitialized) {
			return;
		}
		edgeshader = new ShaderProgram(Gdx.files.internal("shader/edge/edge.vertex.glsl"),
				Gdx.files.internal("shader/edge/edge.fragment.glsl"));
		blurshader = new ShaderProgram(Gdx.files.internal("shader/blur/blur.vertex.glsl"),
				Gdx.files.internal("shader/blur/blur.fragment.glsl"));
		glowshader = new ShaderProgram(Gdx.files.internal("shader/glow/glow.vertex.glsl"),
				Gdx.files.internal("shader/glow/glow.fragment.glsl"));
		lensshader = new ShaderProgram(Gdx.files.internal("shader/oculusrift/lens.vertex.glsl"),
				Gdx.files.internal("shader/oculusrift/lens.fragment.glsl"));
		fullScreenTriangle = createFullScreenTriangle();
		// Oculus Rift
		// if(useor && !or_initialized){
		// or_initialized = or.init(new File("."));
		// Gdx.app.log("Oculus Rift", or.getInitializationStatus());
		// }
		int framebufferwidth = (int) (fboscale * Gdx.graphics.getWidth());
		int framebufferheight = (int) (fboscale * Gdx.graphics.getHeight());
		if (useor) {
			copyeyebuffertexreg.flip(false, true);
			// lenscenteroffset = (1.f - (or.getHMDInfo().LensSeparationDistance
			// / 2.f / (or.getHMDInfo().HScreenSize / 4.f))) / 4.f;
			// distortionK = or.getHMDInfo().DistortionK;
			// for(int i = 0; i < distortionK.length; ++i){
			// distortionK[i] *= 2.f;
			// }
			helpbuffer1 = new FrameBuffer(Pixmap.Format.RGBA8888, framebufferwidth / 2, framebufferheight, true);
			helpbuffer2 = new FrameBuffer(Pixmap.Format.RGBA8888, framebufferwidth / 2, framebufferheight, true);
		} else {
			helpbuffer1 = new FrameBuffer(Pixmap.Format.RGBA8888, framebufferwidth, framebufferheight, true);
			helpbuffer2 = new FrameBuffer(Pixmap.Format.RGBA8888, framebufferwidth, framebufferheight, true);
		}
		glowbuffer1 = new FrameBuffer(Pixmap.Format.RGBA8888, framebufferwidth / glowfrac, framebufferheight / glowfrac,
				true);
		glowbuffer2 = new FrameBuffer(Pixmap.Format.RGBA8888, framebufferwidth / glowfrac, framebufferheight / glowfrac,
				true);
		helpbuffer1.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		helpbuffer2.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		staticinitialized = true;
	}

	public static void renderCopyEyeBuffer() {
		finalbuffer1.begin();
		resetViewport(finalbuffer1);
		clearScreen(0, 0, 0, 0);
		spriteBatch.begin();
		spriteBatch.draw(copyeyebuffertexreg, 0f, 0f, 0f, 0f, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight(),
				1.f, 1.f, 0f);
		spriteBatch.draw(copyeyebuffertexreg, Gdx.graphics.getWidth() / 2f, 0f, 0f, 0f, Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight(), 1.f, 1.f, 0f);
		spriteBatch.end();
		finalbuffer1.end();
		resetViewport();
		// spriteBatch.begin();
		// spriteBatch.draw(finalbuffer.getColorBufferTexture()
		// , 0, 0
		// , Gdx.graphics.getWidth(), Gdx.graphics.getHeight()
		// , 0, 0
		// , finalbuffer.getWidth(), finalbuffer.getHeight()
		// , false, true);
		// spriteBatch.end();
		// Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		finalbuffer1.getColorBufferTexture().bind();
		lensshader.begin();
		{
			lensshader.setUniformf("u_hmdwarpparam", distortionK[0], distortionK[1], distortionK[2], distortionK[3]);
			lensshader.setUniformf("u_lenscenteroffset", lenscenteroffset);
			fullScreenTriangle.render(lensshader, GL20.GL_TRIANGLES, 0, 3);
		}
		lensshader.end();
	}

	public static void renderOR(EntitySystem es, Camera cam, Environment env) {
		// if(!or_initialized){return;}
		Rectangle viewport = new Rectangle(0, 0, finalbuffer2.getWidth() / 2, finalbuffer2.getHeight());
		// or.poll();
		clearScreen();
		Vector3 ipd = new Vector3(-1.f, 0.f, 0.f);
		// ipd.scl(or.getIPD()/2.f);
		ipd.scl(1.f);
		Vector3 leye = ipd.cpy().rot(cam.view);
		cam.translate(leye);
		cam.update();
		render(es, cam, env, viewport, finalbuffer2);
		//
		cam.translate(leye.scl(-1.f));
		viewport.setX(finalbuffer2.getWidth() / 2);
		ipd.scl(-1.f);
		Vector3 reye = ipd.cpy().rot(cam.view);
		cam.translate(reye);
		cam.update();
		render(es, cam, env, viewport, finalbuffer2);
		cam.translate(reye.scl(-1.f));
		cam.update();

		// spriteBatch.begin();
		// spriteBatch.draw(finalbuffer.getColorBufferTexture(), 0, 0
		// , Gdx.graphics.getWidth(), Gdx.graphics.getHeight()
		// , 0, 0
		// , finalbuffer.getWidth(), finalbuffer.getHeight()
		// , false, true);
		// spriteBatch.end();

		finalbuffer2.getColorBufferTexture().bind();
		lensshader.begin();
		{
			lensshader.setUniformf("u_hmdwarpparam", distortionK[0], distortionK[1], distortionK[2], distortionK[3]);
			lensshader.setUniformf("u_lenscenteroffset", lenscenteroffset);
			fullScreenTriangle.render(lensshader, GL20.GL_TRIANGLES, 0, 3);
		}
		lensshader.end();
	}

	public static void clearScreen() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}

	public static void clearScreen(Color color) {
		Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}

	public static void clearScreen(float r, float g, float b, float a) {
		Gdx.gl.glClearColor(r, g, b, a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}

	public static void clearViewport(Rectangle viewport) {
		Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
		Gdx.gl.glScissor((int) viewport.getX(), (int) viewport.getY(), (int) viewport.getWidth(),
				(int) viewport.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
	}

	private static void setViewport(Rectangle viewport) {
		Gdx.gl.glViewport((int) viewport.getX(), (int) viewport.getY(), (int) viewport.getWidth(),
				(int) viewport.getHeight());
	}

	private static void resetViewport() {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	private static void resetViewport(FrameBuffer target) {
		Gdx.gl.glViewport(0, 0, target.getWidth(), target.getHeight());
	}

	public static void render(EntitySystem es, Camera cam, Environment env) {
		render(es, cam, env, new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), null);
	}

	public static void render(EntitySystem es, Camera cam, Environment env, Rectangle viewport) {
		render(es, cam, env, viewport, null);
	}

	public static void render(EntitySystem es, Camera cam, Environment env, Rectangle viewport, FrameBuffer target) {
		if (target != null) {
			if (useglow) {
				helpbuffer1.begin();
				setViewport(viewport);
				clearViewport(viewport);
				renderShades(es, cam, env);
				resetViewport(helpbuffer1);
				helpbuffer1.end();
				spriteBatch.begin();
				spriteBatch.draw(helpbuffer1.getColorBufferTexture(), viewport.getX() / fboscale,
						viewport.getY() / fboscale, viewport.getWidth() / fboscale, viewport.getHeight() / fboscale, 0,
						0, helpbuffer1.getWidth(), helpbuffer1.getHeight(), false, true);
				spriteBatch.end();
				glowify(helpbuffer1, target);
			} else {
				target.begin();
				setViewport(viewport);
				clearViewport(viewport);
				renderShades(es, cam, env);
				resetViewport(target);
				target.end();
			}
			renderOutlines(es, cam, env, viewport, target);
		} else {
			if (useglow) {
				helpbuffer1.begin();
				setViewport(viewport);
				clearViewport(viewport);
				renderShades(es, cam, env);
				resetViewport(helpbuffer1);
				helpbuffer1.end();
				spriteBatch.begin();
				spriteBatch.draw(helpbuffer1.getColorBufferTexture(), viewport.getX(), viewport.getY(),
						viewport.getWidth(), viewport.getHeight(), 0, 0, helpbuffer1.getWidth(),
						helpbuffer1.getHeight(), false, true);
				spriteBatch.end();
				glowify(helpbuffer1, null);
			} else {
				setViewport(viewport);
				clearViewport(viewport);
				renderShades(es, cam, env);
				resetViewport();
			}
			renderOutlines(es, cam, env, viewport, target);
		}
	}

	private static void glowify(FrameBuffer input, FrameBuffer output) {
		input.getColorBufferTexture().bind();
		glowbuffer1.begin();
		{
			clearScreen();
			blurshader.begin();
			{
				blurshader.setUniformf("dir", 0.f, 1.f);
				blurshader.setUniformf("size", (float) input.getColorBufferTexture().getWidth(),
						(float) input.getColorBufferTexture().getHeight());
				fullScreenTriangle.render(blurshader, GL20.GL_TRIANGLES, 0, 3);
			}
			blurshader.end();
		}
		glowbuffer1.end();

		glowbuffer1.getColorBufferTexture().bind();
		glowbuffer2.begin();
		{
			clearScreen();
			blurshader.begin();
			{
				blurshader.setUniformf("dir", 1.f, 0.f);
				blurshader.setUniformf("size", (float) glowbuffer1.getColorBufferTexture().getWidth(),
						(float) glowbuffer1.getColorBufferTexture().getHeight());
				fullScreenTriangle.render(blurshader, GL20.GL_TRIANGLES, 0, 3);
			}
			blurshader.end();
		}
		glowbuffer2.end();

		glowbuffer2.getColorBufferTexture().bind();
		if (output != null) {
			output.begin();
		}
		{
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
			Gdx.gl.glEnable(GL20.GL_BLEND);
			glowshader.begin();
			{
				fullScreenTriangle.render(glowshader, GL20.GL_TRIANGLES, 0, 3);
			}
			glowshader.end();
		}
		if (output != null) {
			output.end();
		}
	}

	private static void renderOutlines(EntitySystem es, Camera cam, Environment env, Rectangle viewport,
			FrameBuffer target) {
		FrameBuffer src = null;
		FrameBuffer dest = helpbuffer1;
		dest.begin();
		{
			clearScreen();
			resetViewport(dest);
			es.render(depthModelBatch, cam, env);
		}
		dest.end();
		src = dest;
		dest = helpbuffer2;
		src.getColorBufferTexture().bind();
		dest.begin();
		{
			clearScreen();
			resetViewport(dest);
			edgeshader.begin();
			{

				edgeshader.setUniformf("u_size", (float) src.getColorBufferTexture().getWidth(),
						(float) src.getColorBufferTexture().getHeight());
				fullScreenTriangle.render(edgeshader, GL20.GL_TRIANGLES, 0, 3);
			}
			edgeshader.end();
			Gdx.gl20.glColorMask(true, true, true, false);
			es.renderDecals(decalBatch, cam);
			Gdx.gl20.glColorMask(true, true, true, true);
		}
		dest.end();

		if (target != null) {
			target.begin();
		}
		spriteBatch.begin();
		if (target != null) {
			spriteBatch.draw(dest.getColorBufferTexture(), viewport.getX() / fboscale, viewport.getY() / fboscale,
					viewport.getWidth() / fboscale, viewport.getHeight() / fboscale, 0, 0, (int) (dest.getWidth()),
					(int) (dest.getHeight()), false, true);
			spriteBatch.end();
		} else {
			spriteBatch.draw(dest.getColorBufferTexture(), viewport.getX(), viewport.getY(), viewport.getWidth(),
					viewport.getHeight(), 0, 0, (int) (dest.getWidth()), (int) (dest.getHeight()), false, true);
			spriteBatch.end();
		}
		if (target != null) {
			target.end();
		}
	}

	private static void renderShades(EntitySystem es, Camera cam, Environment env) {
		Gdx.gl.glCullFace(GL20.GL_BACK);
		es.renderSkybox(defaultBatch, cam, env);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		es.render(defaultBatch, cam, env);
		// Gdx.gl20.glDisable(GL20.GL_DEPTH_TEST);
		es.renderDecals(decalBatch, cam);
		// Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);
	}

	public static void renderMap(Camera cam, OverworldMap map, int currentNode, int selected) {
		shapeRenderer.setProjectionMatrix(cam.combined);
		Vector2 offset = new Vector2((cam.viewportWidth - map.mapsize.x) / 2.f,
				(cam.viewportHeight - map.mapsize.y) / 2.f);
		// Vector2 offset = new Vector2(0,0);
		clearScreen();
		shapeRenderer.begin(ShapeType.Line);
		for (int i = 0; i < map.nodes.size(); ++i) {
			for (int j = 0; j < map.nodes.size(); ++j) {
				if (i == currentNode || j == currentNode) {
					shapeRenderer.setColor(Color.RED);
				} else {
					shapeRenderer.setColor(Color.DARK_GRAY);
				}
				if (map.adjmat.get(i).get(j)) {
					shapeRenderer.line(map.nodes.get(i).position.cpy().add(offset),
							map.nodes.get(j).position.cpy().add(offset));
				}
			}
		}
		shapeRenderer.end();
		shapeRenderer.begin(ShapeType.Filled);
		for (int i = 0; i < map.nodes.size(); ++i) {
			OverworldMapNode n = map.nodes.get(i);
			if (n == map.nodes.get(currentNode)) {
				shapeRenderer.setColor(Color.RED);
			} else if (map.adjmat.get(i).get(currentNode)) {
				shapeRenderer.setColor(Color.WHITE);
			} else {
				shapeRenderer.setColor(Color.DARK_GRAY);
			}
			shapeRenderer.circle(n.position.x + offset.x, n.position.y + offset.y, 5.f);
		}
		LinkedList<Integer> neighbors = map.getNeighbors(currentNode);
		shapeRenderer.setColor(Color.ORANGE);
		OverworldMapNode n = map.nodes.get(neighbors.get(selected));
		shapeRenderer.circle(n.position.x + offset.x, n.position.y + offset.y, 5.f);
		shapeRenderer.end();
	}

	public static void renderMouse(Camera cam) {
		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Filled);
		Vector3 pos = new Vector3(Gdx.input.getX() - (Gdx.graphics.getWidth() - cam.viewportWidth) * 0.5f,
				Gdx.graphics.getHeight() - Gdx.input.getY() - (Gdx.graphics.getHeight() - cam.viewportHeight) * 0.5f,
				0);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.circle(pos.x, pos.y, 5.f);
		shapeRenderer.end();
	}

	private static Mesh createFullScreenTriangle() {
		float[] verts = new float[12];// VERT_SIZE
		int i = 0;
		verts[i++] = -1; // x1
		verts[i++] = -1; // y1

		verts[i++] = 0f; // u1
		verts[i++] = 0f; // v1

		verts[i++] = 3f; // x2
		verts[i++] = -1; // y2

		verts[i++] = 2f; // u2
		verts[i++] = 0f; // v2

		verts[i++] = -1f; // x3
		verts[i++] = 3f; // y2

		verts[i++] = 0f; // u3
		verts[i++] = 2f; // v3

		Mesh tmpMesh = new Mesh(true, 3, 0, new VertexAttribute(Usage.Position, 2, "a_position"),
				new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0"));

		tmpMesh.setVertices(verts);
		return tmpMesh;
	}
}