package com.fwcd.sapphire.scene;

import org.lwjgl.util.vector.Vector3f;

public class TerrainCollider implements EntityBehavior {
	@Override
	public Vector3f interceptMotion(Entity entity, Scene scene, float x, float y, float z) {
		for (Terrain terrain : scene.getTerrains()) {
			if (terrain.contains(x, z)) {
				float height = terrain.getHeightAt(x, z);
				if (y <= height) {
					return new Vector3f(x, height, z);
				}
			}
		}
		
		return null;
	}
}
