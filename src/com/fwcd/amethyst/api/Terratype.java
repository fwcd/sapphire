package com.fwcd.amethyst.api;

public class Terratype implements TerrainPrototype {
	private final TerrainAsset asset;
	private float maxHeight = Float.NaN;
	private float minHeight = Float.NaN;

	public Terratype(TerrainAsset asset) {
		this.asset = asset;
	}
	
	public Terratype maxHeight(float height) {
		maxHeight = height;
		return this;
	}
	
	public Terratype minHeight(float height) {
		minHeight = height;
		return this;
	}
	
	@Override
	public TerrainAsset getAsset() {
		return asset;
	}

	@Override
	public float getMaxHeight() {
		if (Float.isNaN(maxHeight)) {
			return TerrainPrototype.super.getMaxHeight();
		} else {
			return maxHeight;
		}
	}

	@Override
	public float getMinHeight() {
		if (Float.isNaN(minHeight)) {
			return TerrainPrototype.super.getMinHeight();
		} else {
			return minHeight;
		}
	}
}
