package fwcd.sapphire.api;

import fwcd.sapphire.utils.AnyFile;

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
