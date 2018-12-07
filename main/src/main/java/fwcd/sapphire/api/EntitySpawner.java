package fwcd.sapphire.api;

import fwcd.sapphire.scene.Entity;

@FunctionalInterface
public interface EntitySpawner {
	Entity spawn();
	
	default Entity spawn(float x, float y, float z) {
		Entity e = spawn();
		e.translate(x, y, z);
		return e;
	}
}
