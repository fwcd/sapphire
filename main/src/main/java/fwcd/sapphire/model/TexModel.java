package fwcd.sapphire.model;

import fwcd.sapphire.scene.Entity;

/**
 * A textured model.
 * 
 * @author fwcd
 *
 */
public class TexModel {
	private RawModel model;
	private Texture texture;
	private Material material;
	
	public TexModel(RawModel model, Texture texture) {
		this(model, texture, Material.DEFAULT);
	}
	
	public TexModel(RawModel model, Texture texture, Material material) {
		this.model = model;
		this.texture = texture;
		this.material = material;
	}

	public RawModel getRawModel() {
		return model;
	}

	public Texture getTexture() {
		return texture;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public Entity spawn() {
		return new Entity(this);
	}
}
