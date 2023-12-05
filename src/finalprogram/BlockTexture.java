/***************************************************************
* file: BlockTexture.java
* author: Aidan, Jace
* class: CS 4450 Computer Graphics
*
* assignment: final program
* date last modified: 11/7/2023
*
* purpose: This determines block texture from block type
*
****************************************************************/
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
    
    private Map<BlockType, Texture> textures = new HashMap<BlockType, Texture>();
    private Map<BlockType, String> textureString = new HashMap<BlockType, String>();
    
    //constructor
    BlockTexture(){
        try{
            textures.put(BlockType.Bedrock, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("BlockGrass.png")));
            textures.put(BlockType.Dirt, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("BlockDirt.png")));
            textures.put(BlockType.Grass, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("BlockSand.png")));
            textures.put(BlockType.Sand, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("BlockGrass.png")));
            textures.put(BlockType.Stone, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("BlockStone.png")));
            textures.put(BlockType.Water, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("BlockWater.png")));
            
            textureString.put(BlockType.Bedrock, "BlockWater.png");
            textureString.put(BlockType.Dirt, "BlockDirt.png");
            textureString.put(BlockType.Grass, "BlockSand.png");
            textureString.put(BlockType.Sand, "BlockGrass.png");
            textureString.put(BlockType.Stone, "BlockStone.png");
            textureString.put(BlockType.Water, "BlockWater.png");
        }
        catch(Exception e){
            System.out.print("Texture Load Failed");
        }
    }
    
    //returns texture based on block type
    public Texture getTexture(BlockType blockType){
        return textures.get(blockType);
    }
    
    public String getTextureString(BlockType blockType){
        return textureString.get(blockType);
    }
}
