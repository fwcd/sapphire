package fwcd.sapphire.render;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import fwcd.sapphire.model.Material;
import fwcd.sapphire.model.RawModel;
import fwcd.sapphire.model.TerrainTexturePack;
import fwcd.sapphire.model.Texture;
import fwcd.sapphire.scene.Terrain;

public class TerrainRenderer {
	private TerrainShader shader;
	
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}
	
	public void render(List<Terrain> terrains) {
		for (Terrain terrain : new ArrayList<>(terrains)) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			
			unbindTexModel();
		}
	}
	
	private void prepareTerrain(Terrain terrain) {
		RawModel rawModel = terrain.getModel();
		
		shader.loadMaterial(Material.DEFAULT); // TODO
		GL30.glBindVertexArray(rawModel.getVaoID());
		
		int[] boundAttributes = shader.getBoundAttributes();
		for (int attribute : boundAttributes) {
			GL20.glEnableVertexAttribArray(attribute);
		}
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		bindTextures(terrain.getTextures());
	}
	
	private void bindTextures(TerrainTexturePack textures) {
		bindTexture(GL13.GL_TEXTURE0, textures.getBackgroundTexture());
		bindTexture(GL13.GL_TEXTURE1, textures.getRTexture());
		bindTexture(GL13.GL_TEXTURE2, textures.getGTexture());
		bindTexture(GL13.GL_TEXTURE3, textures.getBTexture());
		bindTexture(GL13.GL_TEXTURE4, textures.getBlendMap());
	}
	
	private void bindTexture(int textureSlot, Texture texture) {
		GL13.glActiveTexture(textureSlot);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
	}
	
	private void unbindTexModel() {
		int[] boundAttributes = shader.getBoundAttributes();
		for (int attribute : boundAttributes) {
			GL20.glDisableVertexAttribArray(attribute);
		}
		
		GL30.glBindVertexArray(0);
	}
	
	private void loadModelMatrix(Terrain terrain) {
		shader.loadTransformationMatrix(terrain.getTransformationMatrix());
	}
}
