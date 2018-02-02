package com.fwcd.amethyst.api;

import com.fwcd.amethyst.scene.Terrain;

@FunctionalInterface
public interface TerrainSpawner {
	Terrain spawn(int gridX, int gridZ);
}
