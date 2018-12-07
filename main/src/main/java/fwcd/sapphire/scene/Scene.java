package fwcd.sapphire.scene;

import java.util.ArrayList;
import java.util.List;

import fwcd.sapphire.render.Light;
import fwcd.sapphire.utils.Vector3i;

public class Scene {
	private final List<Light> lights = new ArrayList<>();
	private final List<Entity> entities = new ArrayList<>();
	private final List<Terrain> terrains = new ArrayList<>();
	
	private Vector3i startPos = new Vector3i(0, 0, 0);
	
	public void add(Light light) {
		lights.add(light);
	}
	
	public void add(Entity entity) {
		entity.setScene(this);
		entities.add(entity);
	}
	
	public void add(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public Vector3i getStartPos() {
		return startPos;
	}
	
	public List<Entity> getEntities() {
		return entities;
	}
	
	public List<Light> getLights() {
		return lights;
	}

	public List<Terrain> getTerrains() {
		return terrains;
	}
	
	protected void setStartPos(Vector3i startPos) {
		this.startPos = startPos;
	}
}
