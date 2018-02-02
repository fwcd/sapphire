package com.fwcd.amethyst.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.fwcd.amethyst.exception.FileException;
import com.fwcd.amethyst.exception.ParseException;
import com.fwcd.amethyst.model.Material;
import com.fwcd.amethyst.model.ModelLoader;
import com.fwcd.amethyst.model.RawModel;
import com.fwcd.amethyst.model.TexModel;
import com.fwcd.amethyst.utils.AnyFile;
import com.fwcd.amethyst.utils.IntList;

/**
 * <p>Loads Blender ".obj"-models. Model should preferably only have a single texture file
 * that is used as a spritesheet.</p>
 * 
 * <p>Check the following flags while exporting in Blender:
 * <ul>
 * <li>Write Normals</li>
 * <li>Include UVs</li>
 * <li>Triangulate Faces</li>
 * </ul>
 * </p>
 */
public class OBJLoader {
	public TexModel loadOBJModel(AnyFile objFile, AnyFile textureFile, ModelLoader loader, Material material) {
		return new TexModel(loadOBJModel(objFile, loader), loader.loadTexture(textureFile), material);
	}
	
	public TexModel loadOBJModel(AnyFile objFile, AnyFile textureFile, ModelLoader loader) {
		return new TexModel(loadOBJModel(objFile, loader), loader.loadTexture(textureFile));
	}
	
	public RawModel loadOBJModel(AnyFile objFile, ModelLoader loader) {
		try {
			return objFile.mapStream(in -> loadOBJModel(in, loader));
		} catch (FileException e) {
			throw new ParseException("While opening OBJ file: " + e.getMessage());
		}
	}
	
	private RawModel loadOBJModel(InputStream in, ModelLoader loader) {
		List<Vertex> vertices = new ArrayList<>();
		List<Vector2f> textures = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		IntList indices = new IntList();
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			String line = "";
			
			boolean doneParsing = false;
			while (!doneParsing) {
				line = reader.readLine();
				
				if (line == null) {
					throw new ParseException("OBJ file ended too early.");
				}
				
				String[] current = line.split(" ");
				
				switch (current[0]) {
				
				case "v":
					int index = vertices.size();
					vertices.add(new Vertex(
							Float.parseFloat(current[1]),
							Float.parseFloat(current[2]),
							Float.parseFloat(current[3]),
							index
					));
					break;
				case "vn":
					normals.add(new Vector3f(
							Float.parseFloat(current[1]),
							Float.parseFloat(current[2]),
							Float.parseFloat(current[3])
					));
					break;
				case "vt":
					textures.add(new Vector2f(
							Float.parseFloat(current[1]),
							Float.parseFloat(current[2])
					));
					break;
				case "f":
					doneParsing = true;
					break;
					
				}
			}
			
			while (line != null && line.startsWith("f ")) {
				String[] current = line.split(" ");
				String[] vertex1 = current[1].split("/");
				String[] vertex2 = current[2].split("/");
				String[] vertex3 = current[3].split("/");
				
				processVertex(vertex1, vertices, indices);
				processVertex(vertex2, vertices, indices);
				processVertex(vertex3, vertices, indices);
				line = reader.readLine();
			}
		} catch (IOException e) {
			throw new ParseException("Could not read/find OBJ file: " + e.getMessage());
		}
		
		removeUnusedVertices(vertices);
		// TODO: The scene loading API needs a rewrite [too inconvenient] (see AmethystMain)
		return toModelData(vertices, textures, normals, indices).loadToVAO(loader);
	}
	
	private void processVertex(
			String[] vertex,
			List<Vertex> vertices,
			IntList indices
	) {
		int index = Integer.parseInt(vertex[0]) - 1;
		Vertex currentVertex = vertices.get(index);
		int textureIndex = Integer.parseInt(vertex[1]) - 1;
		int normalIndex = Integer.parseInt(vertex[2]) - 1;
		if (!currentVertex.isSet()) {
			currentVertex.setTextureIndex(textureIndex);
			currentVertex.setNormalIndex(normalIndex);
			indices.add(index);
		} else {
			dealWithAlreadyProcessedVertex(
					currentVertex,
					textureIndex,
					normalIndex,
					indices,
					vertices
			);
		}
	}


	private ModelData toModelData(
			List<Vertex> vertices,
			List<Vector2f> textures,
			List<Vector3f> normals,
			IntList indices
	) {
		float[] verticesArray = new float[vertices.size() * 3];
		float[] texturesArray = new float[vertices.size() * 2];
		float[] normalsArray = new float[vertices.size() * 3];
		int[] indicesArray = indices.toArray();
		float furthestPoint = 0;
		for (int i=0; i<vertices.size(); i++) {
			Vertex currentVertex = vertices.get(i);
			if (currentVertex.getLength() > furthestPoint) {
				furthestPoint = currentVertex.getLength();
			}
			Vector2f textureCoord = textures.get(currentVertex.getTextureIndex());
			Vector3f normalVector = normals.get(currentVertex.getNormalIndex());
			verticesArray[i * 3] = currentVertex.getX();
			verticesArray[(i * 3) + 1] = currentVertex.getY();
			verticesArray[(i * 3) + 2] = currentVertex.getZ();
			texturesArray[i * 2] = textureCoord.x;
			texturesArray[(i * 2) + 1] = 1 - textureCoord.y;
			normalsArray[i * 3] = normalVector.x;
			normalsArray[(i * 3) + 1] = normalVector.y;
			normalsArray[(i * 3) + 2] = normalVector.z;
		}
		return new ModelData(verticesArray, texturesArray, normalsArray, indicesArray, furthestPoint);
	}

	private void dealWithAlreadyProcessedVertex(
			Vertex previousVertex,
			int newTextureIndex,
			int newNormalIndex,
			IntList indices,
			List<Vertex> vertices
	) {
		if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
			indices.add(previousVertex.getIndex());
		} else {
			Vertex anotherVertex = previousVertex.getDuplicate();
			if (anotherVertex != null) {
				dealWithAlreadyProcessedVertex(
						anotherVertex,
						newTextureIndex,
						newNormalIndex,
						indices,
						vertices
				);
			} else {
				int index = vertices.size();
				Vertex duplicateVertex = new Vertex(
						previousVertex.getX(),
						previousVertex.getY(),
						previousVertex.getZ(),
						index
				);
				duplicateVertex.setTextureIndex(newTextureIndex);
				duplicateVertex.setNormalIndex(newNormalIndex);
				previousVertex.setDuplicate(duplicateVertex);
				vertices.add(duplicateVertex);
				indices.add(duplicateVertex.getIndex());
			}

		}
	}
	
	private void removeUnusedVertices(List<Vertex> vertices) {
		for (Vertex vertex : vertices) {
			if (!vertex.isSet()) {
				vertex.setTextureIndex(0);
				vertex.setNormalIndex(0);
			}
		}
	}
}
