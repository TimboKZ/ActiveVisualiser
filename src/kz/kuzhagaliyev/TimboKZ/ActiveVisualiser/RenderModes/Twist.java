package kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.RenderModes;

import kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.Core;
import kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.RenderMode;
import kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.Util;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Timur Kuzhagaliyev
 * @since 06-12-2014
 */

public class Twist extends RenderMode {

    private float actualMean = 0.0f;
    private float actualBassMean = 0.0f;

    public Twist(String name, int key) {
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

        mean = values[0];

        actualMean = Util.smooth(actualMean, Math.abs(mean), 0.01f);
        actualBassMean = Util.smooth(actualBassMean, Math.abs(bassMean), 0.01f);

        mean = actualMean;
        bassMean = actualBassMean;

        float amplitude = 100 + 400 * bassMean;
        float sinMultiplier = 0.01f + bassMean * 0.01f;
        float indent = 10;
        float height = 5;
        float width = 10;
        int segments = 200;
        int turns = 24;
        float adjustment = -200 * mean;
        float rotation = Core.getTime() * 0.01f;

        glPushMatrix();
        {
            glTranslatef(Display.getWidth() / 2, Display.getHeight() / 2, 0);
            glRotatef(rotation, 0, 0, 1);

            for(int i = 0; i < turns; i++) {

                glPushMatrix();
                glRotatef(360.0f / (float) turns * (float) i, 0, 0, 1);
                glPushMatrix();
                glTranslatef(-width / 2, 0, 0);
                for(int k = 0; k < segments; k++) {
                    float progress = (float) k / (float) segments;
                    float nextStep = (float) (k + 1) / (float) segments;
                    glPushMatrix();
                    {
                        if(k % 2 == 0) {
                            glColor3f(bassValues[(int) (512.0f * progress)], bassValues[(int) (512.0f * progress)], bassValues[(int) (512.0f * progress)]);
                        } else {
                            glColor3f(0, 0, 0);
                        }
                        glTranslatef(0, indent + k * height, 0);
                        glBegin(GL_QUADS);
                        {
                            glVertex2f((float) Math.sin(k * height * sinMultiplier) * amplitude + adjustment * progress * progress, 0);
                            glVertex2f((float) Math.sin((k + 1) * height * sinMultiplier) * amplitude + adjustment * nextStep * nextStep, height);
                            glVertex2f((float) Math.sin((k + 1) * height * sinMultiplier) * amplitude + width + adjustment * nextStep * nextStep, height);
                            glVertex2f((float) Math.sin(k * height * sinMultiplier) * amplitude + width + adjustment * progress * progress, 0);
                        }
                        glEnd();
                    }
                    glPopMatrix();

                }
                glPopMatrix();
                glPopMatrix();

            }
        }
        glPopMatrix();

    }



}
