package fwcd.sapphire.scene;

import org.lwjgl.util.vector.Vector3f;

public class XZPlaneCollider implements EntityBehavior {
	@Override
	public Vector3f interceptMotion(Entity entity, Scene scene, float x, float y, float z) {
		if (y <= 0) {
			return new Vector3f(x, 0, z);
		} else {
			return null;
		}
	}
}
