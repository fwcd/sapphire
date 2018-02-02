package com.fwcd.amethyst.api;

import com.fwcd.amethyst.model.Material;
import com.fwcd.amethyst.scene.EntityBehavior;

public interface EntityPrototype {
	EntityAsset getAsset();
	
	default EntityBehavior[] getBehaviors() {
		return new EntityBehavior[0];
	}
	
	default Material getMaterial() {
		return Material.DEFAULT;
	}
	
	default boolean doesUseFakeLighting() {
		return false;
	}
	
	default EntityModifier[] getModifiers() {
		return new EntityModifier[0];
	}
}
