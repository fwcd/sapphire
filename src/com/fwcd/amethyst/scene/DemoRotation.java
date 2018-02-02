package com.fwcd.amethyst.scene;

public class DemoRotation implements EntityBehavior {
	@Override
	public void onTick(Entity entity, Scene scene) {
		entity.rotate(0, 1, 0);
	}
}
