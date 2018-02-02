package com.fwcd.amethyst.api;

import com.fwcd.amethyst.scene.Entity;

@FunctionalInterface
public interface EntitySpawner {
	Entity spawn();
	
	default Entity spawn(float x, float y, float z) {
		Entity e = spawn();
		e.translate(x, y, z);
		return e;
	}
}
