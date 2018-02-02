package com.fwcd.amethyst.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import com.fwcd.amethyst.model.TexModel;
import com.fwcd.amethyst.scene.Entity;
import com.fwcd.amethyst.scene.Scene;
import com.fwcd.amethyst.utils.RGBColor;
import com.fwcd.amethyst.utils.TaskQueue;

public class MasterRenderer implements AutoCloseable {
	private final Camera camera;
	private final TaskQueue renderTaskQueue = new TaskQueue();
	private final TaskQueue shaderTaskQueue = new TaskQueue();
	private final Map<TexModel, List<Entity>> entityCache = new HashMap<>();
	
	private float fovDeg = 89F;
	private float nearPlane = 0.1F;
	private float farPlane = 1000F;
	private RGBColor skyColor = RGBColor.WHITE;
	private Matrix4f projectionMatrix;
	
	private StaticShader staticShader;
	private EntityRenderer entityRenderer;
	
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader;
	
	public MasterRenderer(Camera camera) {
		this.camera = camera;
	}
	
	public void init() {
		projectionMatrix = createProjectionMatrix();
		staticShader = new StaticShader();
		terrainShader = new TerrainShader();
		terrainShader.setLightEnabled(true); // TODO
		
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		entityRenderer = new EntityRenderer(this, staticShader, projectionMatrix);
		updateProjectionMatrix();
	}
	
	/**
	 * Will be called repeatedly (pre-rendering) by the OpenGL-thread.
	 */
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(skyColor.getFloatR(), skyColor.getFloatG(), skyColor.getFloatB(), 0);
	}
	
	public void render(Scene scene, Camera camera) {
		prepare();
		renderTaskQueue.pollAndRunAll();
		staticShader.start();
		shaderTaskQueue.pollAndRunAll();
		staticShader.loadSkyColor(skyColor);
		
		for (Light light : scene.getLights()) {
			staticShader.loadLight(light);
		}
		
		staticShader.loadViewMatrix(camera.getViewMatrix());
		
		cacheEntities(scene.getEntities());
		
		entityRenderer.render(entityCache);
		staticShader.stop();
		
		terrainShader.start();
		terrainShader.loadSkyColor(skyColor);
		
		for (Light light : scene.getLights()) {
			terrainShader.loadLight(light);
		}
		
		terrainShader.loadViewMatrix(camera.getViewMatrix());
		terrainRenderer.render(scene.getTerrains()); // TODO: Might implement caching as with the entities
		terrainShader.stop();
		
		entityCache.clear();
	}
	
	private void cacheEntities(List<Entity> entities) {
		boolean isCamFirstPerson = camera.isFirstPerson();
		Entity cameraEntity = camera.getNullableTrackedEntity();
		
		for (Entity entity : new ArrayList<>(entities)) {
			if (cameraEntity == null || !isCamFirstPerson || !entity.equals(cameraEntity)) {
				TexModel model = entity.getModel();
				List<Entity> batch = entityCache.get(model);
				
				if (batch == null) {
					batch = new ArrayList<>();
					entityCache.put(model, batch);
				}
				
				batch.add(entity);
			}
		}
	}
	
	@Override
	public void close() {
		staticShader.close();
	}
	
	private Matrix4f createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float yScale = (float) ((1F / Math.tan(Math.toRadians(fovDeg / 2F))) * aspectRatio);
		float xScale = yScale / aspectRatio;
		float frustrumLength = farPlane - nearPlane;
		
		Matrix4f matrix = new Matrix4f();
		matrix.m00 = xScale;
		matrix.m11 = yScale;
		matrix.m22 = -((farPlane + nearPlane) / frustrumLength);
		matrix.m23 = -1;
		matrix.m32 = -((2 * nearPlane * farPlane) / frustrumLength);
		matrix.m33 = 0;
		
		return matrix;
	}
	
	/**
	 * Disable rendering of triangles that are
	 * facing away from the camera.
	 */
	public void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	/**
	 * Enable rendering of triangles that are
	 * facing away from the camera.
	 */
	public void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void setFOV(float degrees) {
		fovDeg = degrees;
		updateProjectionMatrix();
	}

	public void updateProjectionMatrix() {
		projectionMatrix = createProjectionMatrix();
		staticShader.start();
		staticShader.loadProjectionMatrix(projectionMatrix);
		staticShader.stop();
	}
	
	/**
	 * Enqueues a render task for execution on the render thread
	 * which owns an OpenGL-context.
	 * 
	 * @param task - A task that might require an OpenGL-context
	 */
	public void scheduleRenderTask(Runnable task) {
		renderTaskQueue.offer(task);
	}
	
	/**
	 * Enqueues a shader task for execution on the render thread
	 * which owns an OpenGL-context.
	 * 
	 * @param task - A task that might require an OpenGL-context and a shader context
	 */
	public void scheduleShaderTask(Runnable task) {
		shaderTaskQueue.offer(task);
	}
	
	public <T> Future<T> scheduleRenderTask(Callable<T> task) {
		return renderTaskQueue.offer(task);
	}
	
	public <T> Future<T> scheduleShaderTask(Callable<T> task) {
		return shaderTaskQueue.offer(task);
	}
}
