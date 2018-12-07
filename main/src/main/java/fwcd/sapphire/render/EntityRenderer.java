package fwcd.sapphire.render;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import fwcd.sapphire.model.Material;
import fwcd.sapphire.model.RawModel;
import fwcd.sapphire.model.TexModel;
import fwcd.sapphire.model.Texture;
import fwcd.sapphire.scene.Entity;

public class EntityRenderer {
	private final MasterRenderer master;
	private final StaticShader shader;
	
	public EntityRenderer(MasterRenderer master, StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		this.master = master;
	}
	
	public void render(Map<TexModel, List<Entity>> entities) {
		for (TexModel model : entities.keySet()) {
			prepareTexModel(model);
			List<Entity> batch = entities.get(model);
			
			for (Entity entity : batch) {
				loadModelMatrix(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			
			unbindTexModel();
		}
	}
	
	private void prepareTexModel(TexModel model) {
		Texture texture = model.getTexture();
		Material material = model.getMaterial();
		RawModel rawModel = model.getRawModel();
		
		shader.loadMaterial(material);
		GL30.glBindVertexArray(rawModel.getVaoID());
		
		if (texture.hasTransparency()) {
			master.disableCulling();
		}
		shader.loadFakeLightingVariable(texture.usesFakeLighting());
		
		int[] boundAttributes = shader.getBoundAttributes();
		for (int attribute : boundAttributes) {
			GL20.glEnableVertexAttribArray(attribute);
		}
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
	}
	
	private void unbindTexModel() {
		master.enableCulling();
		
		int[] boundAttributes = shader.getBoundAttributes();
		for (int attribute : boundAttributes) {
			GL20.glDisableVertexAttribArray(attribute);
		}
		
		GL30.glBindVertexArray(0);
	}
	
	private void loadModelMatrix(Entity entity) {
		shader.loadTransformationMatrix(entity.getTransformationMatrix());
	}
}
