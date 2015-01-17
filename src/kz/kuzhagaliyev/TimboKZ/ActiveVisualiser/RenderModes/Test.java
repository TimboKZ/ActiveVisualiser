package kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.RenderModes;

import kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.RenderMode;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Timur Kuzhagaliyev
 * @since 05-12-2014
 */

public class Test extends RenderMode {

    public Test(String name, int key) {
        super(name, key);
    }

    public void render(float delta) {

        glPushMatrix();
        {
            glColor3f(1.0f, 1.0f, 1.0f);
            glBegin(GL_QUADS);
            {
                glVertex2f(0, Display.getHeight() / 2 - 1);
                glVertex2f(0, Display.getHeight() / 2 + 1);
                glVertex2f(Display.getWidth(), Display.getHeight() / 2 + 1);
                glVertex2f(Display.getWidth(), Display.getHeight() / 2 - 1);
            }
            glEnd();
            glBegin(GL_QUADS);
            {
                glVertex2f(Display.getWidth() / 2 - 1, 0);
                glVertex2f(Display.getWidth() / 2 - 1, Display.getHeight());
                glVertex2f(Display.getWidth() / 2 + 1, Display.getHeight());
                glVertex2f(Display.getWidth() / 2 + 1, 0);
            }
            glEnd();
        }
        glPopMatrix();

    }

}
