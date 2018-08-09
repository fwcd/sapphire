package com.fwcd.sapphire.api;

import com.fwcd.sapphire.scene.Terrain;

@FunctionalInterface
public interface TerrainSpawner {
	Terrain spawn(int gridX, int gridZ);
}
