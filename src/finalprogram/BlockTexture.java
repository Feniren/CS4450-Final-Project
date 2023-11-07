/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalprogram;

import java.util.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author Aidan
 */
public class BlockTexture{
    //private Pair<BlockType, Texture> textures = new Pair<>();
    
    private static Map<BlockType, Texture> textures = new HashMap<BlockType, Texture>();
    
    private BlockTexture(){
        try{
            textures.put(BlockType.Bedrock, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("terrain.png")));
            textures.put(BlockType.Dirt, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("terrain.png")));
            textures.put(BlockType.Grass, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("terrain.png")));
            textures.put(BlockType.Sand, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("terrain.png")));
            textures.put(BlockType.Stone, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("terrain.png")));
            textures.put(BlockType.Water, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("terrain.png")));
        }
        catch(Exception e){
            System.out.print("Texture Load Failed");
        }
    }
    
    public static Texture getTexture(BlockType blockType){
        return textures.get(blockType);
    }
}
