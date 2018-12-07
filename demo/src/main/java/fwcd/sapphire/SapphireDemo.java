package fwcd.sapphire;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import fwcd.sapphire.api.SapphireApp;
import fwcd.sapphire.api.EntitySpawner;
import fwcd.sapphire.api.Entitype;
import fwcd.sapphire.api.REA;
import fwcd.sapphire.api.RTA;
import fwcd.sapphire.api.TerrainSpawner;
import fwcd.sapphire.core.Controller;
import fwcd.sapphire.render.Light;
import fwcd.sapphire.scene.Entity;
import fwcd.sapphire.scene.Scene;
import fwcd.sapphire.scene.TerrainCollider;
import fwcd.sapphire.utils.RGBColor;

public class SapphireDemo {
	// TODO: Implement terrain normals calculation
	
	public static void main(String[] args) {
		SapphireApp app = new SapphireApp("Sapphire", 1280, 720);
		
		TerrainSpawner terrain = app.loadTerrain(new RTA(
				"textures/grass.png",
				"textures/mud.png",
				"textures/grassy2.png",
				"textures/path.png",
				"textures/blendMap.png",
				"textures/terrainHeightMap.png"
		));
		
		EntitySpawner stall = app.loadEntity(new REA("models/stall.obj", "textures/stallTexture.png"));
		EntitySpawner table = app.loadEntity(new REA("models/woodenTable.obj", "textures/woodTexture.jpg"));
		EntitySpawner grass = app.loadEntity(new Entitype(new REA("models/lowGrass.obj", "textures/lowGrassTexture.png"))
				.useFakeLighting(true)
		);
		
		// TODO: Create an "actual" player API
		
		Entity player = app.loadEntity(new Entitype(new REA("models/person.obj", "textures/playerTexture.png"))
				.useFakeLighting(true)
				.behaviors(new TerrainCollider())
				.modifiers(e -> e.enableGravity(-0.08F))
		).spawn();
		player.scale(0.3F);
		app.getController().bindEntity(player);
		
		Controller controller = app.getController();
		
		controller.unmapKey(Keyboard.KEY_SPACE);
		controller.mapKeyHold(Keyboard.KEY_SPACE, () -> {
			if (!player.isInMotion()) {
				player.setVelocity(new Vector3f(0, 1F, 0));
			}
		});
		
		Scene scene = app.getScene();
		scene.add(player);
		scene.add(new Light(0, 10, 0, RGBColor.WHITE));
		scene.add(terrain.spawn(0, 0));
		scene.add(terrain.spawn(-1, 0));
		scene.add(terrain.spawn(-1, -1));
		scene.add(terrain.spawn(0, -1));
		scene.add(stall.spawn(15, 0, 5));
		scene.add(table.spawn(0, 0, 10));
		scene.add(grass.spawn(0, 0, 10));
		scene.add(grass.spawn(10, 0, 0));
	}
}
