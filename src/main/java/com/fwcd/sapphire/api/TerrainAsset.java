package com.fwcd.sapphire.api;

import com.fwcd.sapphire.utils.AnyFile;

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
