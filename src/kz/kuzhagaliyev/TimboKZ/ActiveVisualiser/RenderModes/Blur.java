package kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.RenderModes;

import kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.Core;
import kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.RenderMode;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Timur Kuzhagaliyev
 * @since 08-12-2014
 */

public class Blur extends RenderMode {

    private float[] actualValues;
    private float actualMean;
    private float actualBassMean;

    private float valueSpeed;
    private float meanSpeed;

    public Blur(String name, int key) {
        super(name, key);
        actualValues = new float[512];
        actualMean = 0.0f;
        actualBassMean = 0.0f;
        for(int i = 0; i < actualValues.length; i++)
            actualValues[i] = 0.0f;

        valueSpeed = 0.01f;
        meanSpeed = 0.01f;
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

        if(Math.abs(mean) >= actualMean)
            actualMean += meanSpeed * 2;
        else if(actualMean > 0)
            actualMean -= meanSpeed;

        float displacementX = Display.getWidth() / 20 * bassMean;
        float displacementY = Display.getWidth() / 20 * bassMean;

        float rotationZ = Core.getTime();

        if(Math.abs(actualMean) >= 0.1f && actualValues[0] < 2)
            actualValues[0] += valueSpeed * 10;
        else if(actualValues[0] > 0)
            actualValues[0] -= valueSpeed / 2;
        if(Math.abs(actualMean) >= 0.2f && actualValues[1] < 2)
            actualValues[1] += valueSpeed * 10;
        else if(actualValues[1] > 0)
            actualValues[1] -= valueSpeed / 2;
        if(Math.abs(actualMean) >= 0.25f && actualValues[2] < 2)
            actualValues[2] += valueSpeed * 10;
        else if(actualValues[2] > 0)
            actualValues[2] -= valueSpeed / 2;

        float colour = 2.0f * mean * (2 - actualValues[0]);

        glClearColor(colour, colour, colour, 0);

        glPushMatrix();
        {
            glTranslatef(Display.getWidth() / 2, Display.getHeight() / 2, 0);
            glRotatef(rotationZ, 0, 0, 1);


            glPushMatrix();
            glRotatef(-rotationZ * 0.99f, 0, 0, 1);
            for(int i = 0; i < 32; i++) {
                glPushMatrix();
                {
                    glRotatef(i * 11.25f, 0, 0, 1);
                    drawCircle(0, actualValues[2] * Display.getHeight() / 4 + actualMean * Display.getHeight() / 20 + displacementX * 10 * bassMean, 10, 0, 0, 1);
                    drawCircle(0, actualValues[2] * Display.getHeight() / 4 + actualMean * Display.getHeight() / 20 + displacementX * 10 * bassMean * bassMean, 10, 1, 0, 0);
                    drawCircle(0, actualValues[2] * Display.getHeight() / 4 + actualMean * Display.getHeight() / 20, 10, 1, 1, 1);
                }
                glPopMatrix();
            }
            glPopMatrix();

            glPushMatrix();
            glRotatef(-rotationZ * 1.01f, 0, 0, 1);
            for(int i = 0; i < 16; i++) {
                glPushMatrix();
                {
                    glRotatef(i * 22.5f, 0, 0, 1);
                    drawCircle(0, actualValues[1] * Display.getHeight() / 6 + actualMean * Display.getHeight() / 20 + displacementX * 10 * bassMean, 15, 0, 0, 1);
                    drawCircle(0, actualValues[1] * Display.getHeight() / 6 + actualMean * Display.getHeight() / 20 + displacementX * 10 * bassMean * bassMean, 15, 1, 0, 0);
                    drawCircle(0, actualValues[1] * Display.getHeight() / 6 + actualMean * Display.getHeight() / 20, 15, 1, 1, 1);
                }
                glPopMatrix();
            }
            glPopMatrix();

            glPushMatrix();
            glRotatef(-rotationZ * 0.99f, 0, 0, 1);
            for(int i = 0; i < 8; i++) {
                glPushMatrix();
                {
                    glRotatef(i * 45, 0, 0, 1);
                    drawCircle(0, actualValues[0] * Display.getHeight() / 10 + actualMean * Display.getHeight() / 20 + displacementX * 10 * bassMean, 20, 0, 0, 1);
                    drawCircle(0, actualValues[0] * Display.getHeight() / 10 + actualMean * Display.getHeight() / 20 + displacementX * 10 * bassMean * bassMean, 20, 1, 0, 0);
                    drawCircle(0, actualValues[0] * Display.getHeight() / 10 + actualMean * Display.getHeight() / 20, 20, 1, 1, 1);
                }
                glPopMatrix();
            }
            glPopMatrix();

            glPushMatrix();
            {
                glTranslatef(displacementX, displacementY, 0);
                drawCircle(0, 0, Display.getHeight() / 12 * (1 + actualMean), 0, 0, 1);
            }
            glPopMatrix();
            glPushMatrix();
            {
                glTranslatef(- displacementX, - displacementY, 0);
                drawCircle(0, 0, Display.getHeight() / 12 * (1 + actualMean), 1, 0, 0);
            }
            glPopMatrix();
            glPushMatrix();
            {
                drawCircle(0, 0, Display.getHeight() / 12 * (1 + actualMean), 1f, 1f, 1f);
            }
            glPopMatrix();
        }
        glPopMatrix();

    }

    public void drawCircle(float x, float y, float radius, float r, float g, float b) {
        glPushMatrix();
        {
            glColor3f(r, g, b);
            glTranslatef(x, y, 0);
            glScalef(radius, radius, 0);
            glBegin(GL_TRIANGLE_FAN);
            {
                glVertex2f(0, 0);
                for (int i = 0; i <= 80; i++) { //NUM_PIZZA_SLICES decides how round the circle looks.
                    double angle = Math.PI * 2 * i / 80;
                    glVertex2f((float) Math.cos(angle), (float) Math.sin(angle));
                }
            }
            glEnd();
        }
        glPopMatrix();
    }

}
