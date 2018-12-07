package fwcd.sapphire.api;

import fwcd.sapphire.model.Material;
import fwcd.sapphire.utils.AnyFile;

public interface EntityAsset {
	AnyFile getOBJModel();
	
	AnyFile getTexture();
	
	default Material getMaterial() {
		return Material.DEFAULT;
	}
}
