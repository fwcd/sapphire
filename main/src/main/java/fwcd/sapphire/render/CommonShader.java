package fwcd.sapphire.render;

import org.lwjgl.util.vector.Matrix4f;

import fwcd.sapphire.model.Material;
import fwcd.sapphire.model.ModelLoader;
import fwcd.sapphire.utils.AnyFile;
import fwcd.sapphire.utils.RGBColor;

public class CommonShader extends ShaderProgram {
	private Uniform transformationMatrix;
	private Uniform projectionMatrix;
	private Uniform viewMatrix;
	private Uniform lightPos;
	private Uniform lightColor;
	private Uniform lightEnabled;
	private Uniform shineDamper;
	private Uniform reflectivity;
	private Uniform useFakeLighting;
	private Uniform skyColor;
	
	public CommonShader(AnyFile vertexFile, AnyFile fragmentFile) {
		super(vertexFile, fragmentFile);
	}

	@Override
	protected void registerUniforms() {
		transformationMatrix = createUniform("transformationMatrix");
		projectionMatrix = createUniform("projectionMatrix");
		viewMatrix = createUniform("viewMatrix");
		lightPos = createUniform("lightPosition");
		lightColor = createUniform("lightColor");
		lightEnabled = createUniform("lightEnabled");
		shineDamper = createUniform("shineDamper");
		reflectivity = createUniform("reflectivity");
		useFakeLighting = createUniform("useFakeLighting");
		skyColor = createUniform("skyColor");
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(ModelLoader.VAO_VERTICES_INDEX, "position");
		bindAttribute(ModelLoader.VAO_TEXTURES_INDEX, "textureCoords");
		bindAttribute(ModelLoader.VAO_NORMALS_INDEX, "normal");
	}
	
	public void loadSkyColor(RGBColor color) {
		skyColor.setVector(color.toVector());
	}
	
	public void setLightEnabled(boolean enabled) {
		lightEnabled.setBool(enabled);
	}
	
	public void loadMaterial(Material material) {
		shineDamper.setFloat(material.getShineDamper());
		reflectivity.setFloat(material.getReflectivity());
	}
	
	public void loadLight(Light light) {
		lightPos.setVector(light.getPosition());
		lightColor.setVector(light.getColor().toVector());
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		transformationMatrix.setMatrix(matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection) {
		projectionMatrix.setMatrix(projection);
	}
	
	public void loadFakeLightingVariable(boolean useFakeLight) {
		useFakeLighting.setBool(useFakeLight);
	}
	
	public void loadViewMatrix(Matrix4f view) {
		viewMatrix.setMatrix(view);
	}
}
