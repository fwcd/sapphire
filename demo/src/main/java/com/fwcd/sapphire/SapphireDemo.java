package com.fwcd.sapphire;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import com.fwcd.sapphire.api.SapphireApp;
import com.fwcd.sapphire.api.EntitySpawner;
import com.fwcd.sapphire.api.Entitype;
import com.fwcd.sapphire.api.REA;
import com.fwcd.sapphire.api.RTA;
import com.fwcd.sapphire.api.TerrainSpawner;
import com.fwcd.sapphire.core.Controller;
import com.fwcd.sapphire.render.Light;
import com.fwcd.sapphire.scene.Entity;
import com.fwcd.sapphire.scene.Scene;
import com.fwcd.sapphire.scene.TerrainCollider;
import com.fwcd.sapphire.utils.RGBColor;

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
