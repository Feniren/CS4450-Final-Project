/** *************************************************************
 * file: FinalProgram.java
 * author: J. Areff and A. Sanders
 * class: CS 4450-01 – Computer Graphics
 *
 * assignment: FinalProgram
 * date last modified: 10/19/2023
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

// package
package finalprogram;

// imports
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;

public class FinalProgram {
    
    private FPCameraController fp;
    private DisplayMode displayMode;

    // method: start
    // purpose: open the window, start up openGL, and start the game loop
    public void start() {
        try {
            createWindow();
            initGL();
            fp = new FPCameraController(0f, 0f, 0f);
            fp.gameLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // method: createWindow
    // purpose: open a new window
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
    
    // method: initGL
    // purpose: start up openGL
    private void initGL() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(90.0f, (float) displayMode.getWidth() / (float) displayMode.getHeight(), 0.1f, 300.0f);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnable(GL_DEPTH_TEST);
        glEnable (GL_CULL_FACE);
        glFrontFace(GL_CW);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable( GL_BLEND );
        
        glEnable(GL_TEXTURE_2D);
        glEnableClientState (GL_TEXTURE_COORD_ARRAY);

    }
    
    // method: main
    // purpose: call for start
    public static void main(String[] args) {
        FinalProgram basic = new FinalProgram();
        basic.start();
    }
    
}
