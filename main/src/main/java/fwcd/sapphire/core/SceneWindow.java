package fwcd.sapphire.core;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.PixelFormat;

import fwcd.sapphire.model.ModelLoader;
import fwcd.sapphire.parser.OBJLoader;
import fwcd.sapphire.render.Camera;
import fwcd.sapphire.render.MasterRenderer;
import fwcd.sapphire.render.StaticShader;
import fwcd.sapphire.scene.Entity;
import fwcd.sapphire.scene.Scene;
import fwcd.sapphire.utils.AnyFile;
import fwcd.sapphire.utils.ResourceFile;

/**
 * The "core class" in the object graph of the Sapphire API.
 */
public class SceneWindow {
	private final ExecutorService threadPool = Executors.newCachedThreadPool();
	private final GameLoop gameloop;
	private final Camera camera = new Camera();
	private final Controller controller = new Controller(camera);
	private final MasterRenderer renderer = new MasterRenderer(camera);
	private final ModelLoader modelLoader = new ModelLoader();
	private final OBJLoader objLoader = new OBJLoader();
	
	private StaticShader shader;
	private Scene scene = new Scene();

	public SceneWindow(String title, int width, int height) {
		gameloop = new GameLoop(this);
		
		// Spawn thread with OpenGL context
		threadPool.execute(() -> runOpenGLThread(title, width, height));
		
		// Load default config
		setLightEnabled(true);
	}
	
	private void runOpenGLThread(String title, int width, int height) {
		try {
			ContextAttribs attribs = new ContextAttribs(3, 2)
				.withProfileCore(true)
				.withForwardCompatible(true);
			
			Display.setTitle(title);
			Display.setIcon(new ByteBuffer[] {loadImage(new ResourceFile("/icons/gemIcon.png"))});
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create(new PixelFormat(), attribs);
			
			System.out.println("GLSL Shading Version: " + GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));
			
			shader = new StaticShader();
			
			renderer.init();
			
			GL11.glViewport(0, 0, width, height);
			
			threadPool.execute(gameloop::runInputDispatchLoop); // Runs the input dispatch loop asynchronously
			threadPool.execute(gameloop::runTickLoop); // Runs the tick loop asynchronously
			gameloop.runRenderLoop(); // Runs the render loop on the (current) OpenGL-context thread
			
			cleanup();
			System.exit(0);
		} catch (LWJGLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Asynchronously sets the light to be enabled.
	 * (Thus it is not guaranteed that it also has happened when
	 * the method returns).
	 * 
	 * @param lightEnabled - Whether scene lighting should be enabled or every color simply to be rendered
	 */
	public void setLightEnabled(boolean lightEnabled) {
		renderer.scheduleShaderTask(() -> shader.setLightEnabled(lightEnabled));
	}
	
	private ByteBuffer loadImage(AnyFile file) {
		BufferedImage img = file.mapStream(ImageIO::read);
		
		int[] rgbArray = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
		ByteBuffer buffer = BufferUtils.createByteBuffer(rgbArray.length * 4);
		for (int argb : rgbArray) {
			byte r = (byte) ((argb >> 16) & 0xFF);
			byte g = (byte) ((argb >> 8) & 0xFF);
			byte b = (byte) (argb & 0xFF);
			byte a = (byte) ((argb >> 24) & 0xFF);
			
			buffer.put(r).put(g).put(b).put(a);
		}
		buffer.flip();
		return buffer;
	}
	
	/**
	 * Fetches the current scene. <b>This method should be used
	 * cautiously and preferably just in a READ-ONLY matter! If
	 * you need to modify the scene, use withScene(...) instead.</b>
	 * 
	 * @return The scene
	 */
	public Scene getScene() {
		return scene;
	}
	
	/**
	 * Performs some action with the scene on the OpenGL-context/render
	 * thread.
	 * 
	 * @param action - The action
	 */
	public void withScene(Consumer<Scene> action) {
		renderer.scheduleRenderTask(() -> action.accept(scene));
	}
	
	private void cleanup() {
		threadPool.shutdown();
		if (shader != null) { shader.close(); }
		if (modelLoader != null) { modelLoader.close(); }
		System.out.println("Cleaned up resources...");
	}
	
	/**
	 * <p>Loads the scene asynchronously though the
	 * provided supplier.</p>
	 * 
	 * <p>The scene isn't passed directly, because some
	 * methods (like texture loading) might require an OpenGL-context upon construction.</p>
	 * 
	 * @param sceneLoader - Fetches/generates the scene
	 */
	public void loadScene(Supplier<Scene> sceneLoader) {
		renderer.scheduleRenderTask(() -> {
			setScene(sceneLoader.get());
		});
	}
	
	public void setScene(Scene scene) {
		this.scene = Objects.requireNonNull(scene);
		camera.setPos(scene.getStartPos());
	}
	
	public Controller getController() {
		return controller;
	}
	
	protected void render() {
		renderer.render(scene, camera);
		Display.update();
	}

	protected void tick() {
		for (Entity entity : scene.getEntities()) {
			entity.onTick();
		}
	}
	
	public MasterRenderer getRenderer() {
		return renderer;
	}
	
	public ModelLoader getModelLoader() {
		return modelLoader;
	}

	public OBJLoader getOBJLoader() {
		return objLoader;
	}
}
