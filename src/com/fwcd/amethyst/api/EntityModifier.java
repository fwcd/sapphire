package com.fwcd.amethyst.api;

import com.fwcd.amethyst.scene.Entity;

@FunctionalInterface
public interface EntityModifier {
	void modify(Entity entity);
}
