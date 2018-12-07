package fwcd.sapphire.api;

import fwcd.sapphire.scene.Entity;

@FunctionalInterface
public interface EntityModifier {
	void modify(Entity entity);
}
