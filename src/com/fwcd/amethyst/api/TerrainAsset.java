package com.fwcd.amethyst.api;

import com.fwcd.amethyst.utils.AnyFile;

public interface TerrainAsset {
	AnyFile getBGTexture();
	
	AnyFile getRTexture();
	
	AnyFile getGTexture();
	
	AnyFile getBTexture();
	
	AnyFile getBlendMap();
	
	default AnyFile getHeightMap() {
		return null;
	}
}
