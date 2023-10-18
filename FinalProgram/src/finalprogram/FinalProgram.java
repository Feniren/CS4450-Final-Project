/** *************************************************************
 * file: FinalProgram.java
 * author: J. Areff and A. Sanders
 * class: CS 4450-01 – Computer Graphics
 *
 * assignment: FinalProgram
 * date last modified: 10/17/2023
 *
 * purpose:
 *   This program draws a 640x480 window, displays a cube
 *   with first person controls
 *
 * controls:
 *   WASD or arrow keys for movement
 *   mouse for camera manipulation
 *   space bar for up movement
 *   left shift for down movement
 *************************************************************** */

package finalprogram;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;

public class FinalProgram {
    private FPCameraController fp = new FPCameraController(0f, 0f, 0f);
    private DisplayMode displayMode;

    public void start() {
        try {
            createWindow();
            initGL();
            fp.gameLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void createWindow() throws Exception {
        Display.setFullscreen(false);
        DisplayMode d[]
                = Display.getAvailableDisplayModes();
        for (int i = 0; i < d.length; i++) {
            if (d[i].getWidth() == 640
                    && d[i].getHeight() == 480
                    && d[i].getBitsPerPixel() == 32) {
                displayMode = d[i];
                break;
            }
        }
        Display.setDisplayMode(displayMode);
        Display.setTitle("Voxel World");
        Display.create();
    }
    
    private void initGL() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(90.0f, (float) displayMode.getWidth() / (float) displayMode.getHeight(), 0.1f, 300.0f);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        
        glEnable(GL_DEPTH_TEST);
    }
    
    public static void main(String[] args) {
        FinalProgram basic = new FinalProgram();
        basic.start();
    }
    
}
