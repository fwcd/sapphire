package com.fwcd.sapphire.api;

import com.fwcd.sapphire.model.Material;
import com.fwcd.sapphire.utils.AnyFile;

public interface EntityAsset {
	AnyFile getOBJModel();
	
	AnyFile getTexture();
	
	default Material getMaterial() {
		return Material.DEFAULT;
	}
}
