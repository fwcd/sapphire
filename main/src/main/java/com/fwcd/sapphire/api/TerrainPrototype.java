package com.fwcd.sapphire.api;

public interface TerrainPrototype {
	TerrainAsset getAsset();
	
	default float getMaxHeight() {
		return 20;
	}
	
	default float getMinHeight() {
		return -20;
	}
}
