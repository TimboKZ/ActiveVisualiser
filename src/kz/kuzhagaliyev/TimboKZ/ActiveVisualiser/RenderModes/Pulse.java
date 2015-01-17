package kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.RenderModes;

import kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.Core;
import kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.RenderMode;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Timur Kuzhagaliyev
 * @since 05-12-2014
 */

public class Pulse extends RenderMode {

    public Pulse(String name, int key) {
        super(name, key);
    }

    public void render(float delta) {

        Core.initGL();

        float[] values = Core.getValues(0);
        float[] bassValues = Core.getValues(20);

        float mean = 0;
        float bassMean = 0;
        for (float i = 0.0f; i < 512.0f; i++) {
            mean += values[(int) i];
            bassMean += bassValues[(int) i];
        }
        mean /= 512.0f;
        bassMean /= 512.0f;

        float barHeight = Display.getHeight() / 2 * 0.8f * 0.4f;
        float indent = Display.getHeight() / 2 * 0.8f * 0.4f;
        float multiplier = Display.getHeight() / 2 * 0.8f * 0.6f;
        int barsNum = 8;

        float value = 0;
        float bassValue = 0;

        float barWidth = Display.getWidth() / 512.0f;
        for (float i = 0.0f; i < 512.0f; i++) {
            value += values[(int) i];
            bassValue += bassValues[(int) i];
            if((i + 1) % (float) barsNum == 0) {
                value /= barsNum;
                bassValue /= barsNum;
                glPushMatrix();
                {
                    glTranslatef(Display.getWidth() / 2, Display.getHeight() / 2, 0.0f);
                    glRotatef(i * 360.0f / 512.0f + System.nanoTime() / 100000000.0f, 0, 0, 1.0f);
                    glColor3f(Math.abs(bassMean), Math.abs(bassMean), Math.abs(bassMean));
                    glBegin(GL_QUADS);
                    {
                        glVertex2f(-barWidth, indent + barHeight);
                        glVertex2f(-barWidth, indent + multiplier * Math.abs(bassMean));
                        glVertex2f(barWidth, indent + multiplier * Math.abs(bassMean));
                        glVertex2f(barWidth, indent + barHeight);
                    }
                    glEnd();
                }
                glPopMatrix();
                glPushMatrix();
                {
                    glTranslatef(Display.getWidth() / 2, Display.getHeight() / 2, 0.0f);
                    glRotatef(i * 360.0f / 512.0f - System.nanoTime() / 100000000.0f, 0, 0, 1.0f);
                    glBegin(GL_QUADS);
                    {
                        glColor3f(Math.abs(value), 1 - Math.abs(value), Math.abs(mean));
                        glVertex2f(-barWidth, indent * 2 + multiplier + multiplier * Math.abs(value) / 2 + multiplier * Math.abs(mean) * Math.abs(value) / 2);
                        glColor3f(Math.abs(value) * Math.abs(mean), (1 - Math.abs(value)) * Math.abs(mean), Math.abs(mean));
                        glVertex2f(-barWidth, indent + multiplier);
                        glVertex2f(barWidth, indent + multiplier);
                        glColor3f(Math.abs(value), 1 - Math.abs(value), Math.abs(mean));
                        glVertex2f(barWidth, indent * 2 + multiplier + multiplier * Math.abs(value) / 2 + multiplier * Math.abs(mean) * Math.abs(value) / 2);
                    }
                    glEnd();
                }
                glPopMatrix();
            }
        }

    }

}
