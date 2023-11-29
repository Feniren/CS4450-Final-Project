/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalprogram;

import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;

/**
 *
 * @author Aidan
 */
public class MapBase{
	protected ArrayList<Pair<Vector2f, Integer>> pairs;
	
	MapBase(){
		pairs = new ArrayList<Pair<Vector2f, Integer>>();
	}
	
	public void Add(Vector2f vector2f, int integer){
		Pair<Vector2f, Integer> pair = new Pair<Vector2f, Integer>(vector2f, integer);
		
		pairs.add(pair);
	}
	
	public int Find(Vector2f vector2f){
            int found = -1;
		
            for (int i = 0; i < pairs.size(); i++){
		if (pairs.get(i).GetFirst().equals(vector2f)){
                    found = pairs.get(i).GetSecond();
		}
            }
            
            return found;
	}
}
