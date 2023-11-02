/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalprogram;

/**
 *
 * @author Aidan
 */
public class Block{
    private int blockID;
    private boolean isActive;
    private BlockType type;
    private float x;
    private float y;
    private float z;
    
    public Block(BlockType type){
        this.type = type;
    }
    
    public void SetCoordinates(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public int GetBlockID(){
        return blockID;
    }
    
    public boolean GetIsActive(){
        return isActive;
    }
    
    public void SetIsAvtive(boolean active){
        isActive = active;
    }
}
