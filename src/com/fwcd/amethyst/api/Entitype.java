package com.fwcd.amethyst.api;

import com.fwcd.amethyst.model.Material;
import com.fwcd.amethyst.scene.EntityBehavior;

/**
 * A convenient entity prototyper that
 * uses method chaining to achieve a concise
 * and expressive syntax.
 * 
 * @author Fredrik
 *
 */
public class Entitype implements EntityPrototype {
	private final EntityAsset asset;
	private EntityModifier[] modifiers = null;
	private EntityBehavior[] behaviors = null;
	private Material material = null;
	private boolean useFakeLighting = false;
	
	public Entitype(EntityAsset asset) {
		this.asset = asset;
	}
	
	public Entitype behaviors(EntityBehavior... behaviors) {
		this.behaviors = behaviors;
		return this;
	}
	
	public Entitype modifiers(EntityModifier... modifiers) {
		this.modifiers = modifiers;
		return this;
	}
	
	public Entitype material(Material material) {
		this.material = material;
		return this;
	}
	
	/**
	 * Sets whether this entity should pretend that all
	 * light is coming from above. (All normals will
	 * set to face up, regardless of model data)
	 * 
	 * <p>This is false by default.</p>
	 * 
	 * @param useFakeLighting
	 */
	public Entitype useFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
		return this;
	}
	
	@Override
	public EntityAsset getAsset() {
		return asset;
	}

	@Override
	public EntityBehavior[] getBehaviors() {
		if (behaviors == null) {
			return EntityPrototype.super.getBehaviors();
		} else {
			return behaviors;
		}
	}

	@Override
	public Material getMaterial() {
		if (material == null) {
			return EntityPrototype.super.getMaterial();
		} else {
			return material;
		}
	}

	@Override
	public boolean doesUseFakeLighting() {
		return useFakeLighting;
	}

	@Override
	public EntityModifier[] getModifiers() {
		if (modifiers == null) {
			return EntityPrototype.super.getModifiers();
		} else {
			return modifiers;
		}
	}
}
