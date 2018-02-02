package com.fwcd.amethyst.api;

import com.fwcd.amethyst.model.Material;
import com.fwcd.amethyst.utils.AnyFile;

public interface EntityAsset {
	AnyFile getOBJModel();
	
	AnyFile getTexture();
	
	default Material getMaterial() {
		return Material.DEFAULT;
	}
}
