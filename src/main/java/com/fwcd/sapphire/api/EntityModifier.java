package com.fwcd.sapphire.api;

import com.fwcd.sapphire.scene.Entity;

@FunctionalInterface
public interface EntityModifier {
	void modify(Entity entity);
}
