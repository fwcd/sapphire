package fwcd.sapphire.api;

import fwcd.sapphire.model.Material;
import fwcd.sapphire.scene.EntityBehavior;

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
