package fwcd.sapphire.scene;

import org.lwjgl.util.vector.Vector3f;

/**
 * Encapsulates a piece of behavior for entities.
 * 
 * @author fwcd
 *
 */
public interface EntityBehavior {
	default void onTick(Entity entity, Scene scene) {}
	
	/**
	 * Checks whether a progressing move/translate operation should
	 * be intercepted and returns a vector if so (otherwise just null).
	 * 
	 * @param entity - This entity
	 * @param newX - The position x after the proposed operation
	 * @param newY - The position y after the proposed operation
	 * @param newZ - The position z after the proposed operation
	 * @return Null (if no intercept is desired) or otherwise a new entity position
	 */
	default Vector3f interceptMotion(Entity entity, Scene scene, float newX, float newY, float newZ) {
		return null;
	}
	
	/**
	 * Checks whether a progressing rotation operation should
	 * be intercepted and returns a vector if so (otherwise just null).
	 * 
	 * @param entity - This entity
	 * @param newRotX - The rotation x after the proposed operation
	 * @param newRotY - The rotation y after the proposed operation
	 * @param newRotZ - The rotation z after the proposed operation
	 * @return Null (if no intercept is desired) or otherwise a new entity rotation state
	 */
	default Vector3f interceptRotation(Entity entity, Scene scene, float newRotX, float newRotY, float newRotZ) {
		return null;
	}
	
	/**
	 * Checks whether a progressing scaling operation should
	 * be intercepted and returns a different scale if so (otherwise just the same scale).
	 * 
	 * @param entity - This entity
	 * @param newScale - The entity scale after the proposed operation
	 * @return The scale after the interception
	 */
	default float interceptScaling(Entity entity, Scene scene, float newScale) {
		return newScale;
	}
}
