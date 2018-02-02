package com.fwcd.amethyst.api;

import java.util.concurrent.ExecutionException;

import com.fwcd.amethyst.core.Controller;
import com.fwcd.amethyst.core.SceneWindow;
import com.fwcd.amethyst.exception.AmethystException;
import com.fwcd.amethyst.model.ModelLoader;
import com.fwcd.amethyst.model.TerrainHeightMap;
import com.fwcd.amethyst.model.TerrainTexturePack;
import com.fwcd.amethyst.model.TexModel;
import com.fwcd.amethyst.parser.OBJLoader;
import com.fwcd.amethyst.render.MasterRenderer;
import com.fwcd.amethyst.scene.Entity;
import com.fwcd.amethyst.scene.EntityBehavior;
import com.fwcd.amethyst.scene.Scene;
import com.fwcd.amethyst.scene.Terrain;
import com.fwcd.amethyst.utils.AnyFile;

public class AmethystApp {
	private final SceneWindow window;
	private final ModelLoader modelLoader;
	private final OBJLoader objLoader;
	private final MasterRenderer renderer;
	
	public AmethystApp(String title, int width, int height) {
		window = new SceneWindow(title, width, height);
		modelLoader = window.getModelLoader();
		objLoader = window.getOBJLoader();
		renderer = window.getRenderer();
	}
	
	public EntitySpawner loadEntity(EntityAsset asset, EntityBehavior... behaviors) {
		return loadEntity(new Entitype(asset).behaviors(behaviors));
	}
	
	public EntitySpawner loadEntity(EntityPrototype prototype) {
		final TexModel model;
		try {
			EntityAsset asset = prototype.getAsset();
			model = renderer.scheduleRenderTask(() -> objLoader.loadOBJModel(
					asset.getOBJModel(),
					asset.getTexture(),
					modelLoader,
					prototype.getMaterial()
			)).get();
		} catch (InterruptedException | ExecutionException e) {
			throw new AmethystException(e);
		}
		model.getTexture().setFakeLighting(prototype.doesUseFakeLighting());
		return () -> {
			Entity e = new Entity(model, prototype.getBehaviors());
			
			for (EntityModifier modifier : prototype.getModifiers()) {
				modifier.modify(e);
			}
			
			return e;
		};
	}
	
	public TerrainSpawner loadTerrain(TerrainAsset asset) {
		return loadTerrain(new Terratype(asset));
	}
	
	public TerrainSpawner loadTerrain(TerrainPrototype prototype) {
		final TerrainTexturePack pack;
		final TerrainHeightMap heightMap;
		try {
			TerrainAsset asset = prototype.getAsset();
			pack = renderer.scheduleRenderTask(() -> new TerrainTexturePack(
					modelLoader.loadTexture(asset.getBGTexture()),
					modelLoader.loadTexture(asset.getRTexture()),
					modelLoader.loadTexture(asset.getGTexture()),
					modelLoader.loadTexture(asset.getBTexture()),
					modelLoader.loadTexture(asset.getBlendMap())
			)).get();
			
			AnyFile heightMapFile = asset.getHeightMap();
			if (heightMapFile != null) {
				heightMap = new TerrainHeightMap(
						heightMapFile,
						prototype.getMinHeight(),
						prototype.getMaxHeight()
				);
			} else {
				heightMap = null;
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new AmethystException(e);
		}
		return (gridX, gridZ) -> {
			try {
				return renderer.scheduleRenderTask(() -> new Terrain(
						gridX,
						gridZ,
						modelLoader,
						pack,
						heightMap
				)).get();
			} catch (InterruptedException | ExecutionException e) {
				throw new AmethystException(e);
			}
		};
	}
	
	public Controller getController() {
		return window.getController();
	}
	
	public Scene getScene() {
		return window.getScene();
	}
	
	public void setScene(Scene scene) {
		window.setScene(scene);
	}
	
	public void setLightEnabled(boolean lightEnabled) {
		window.setLightEnabled(lightEnabled);
	}
}
