package fwcd.sapphire.api;

import fwcd.sapphire.scene.Terrain;

@FunctionalInterface
public interface TerrainSpawner {
	Terrain spawn(int gridX, int gridZ);
}
