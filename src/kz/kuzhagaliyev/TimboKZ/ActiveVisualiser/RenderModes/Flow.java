package kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.RenderModes;

import kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.Core;
import kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.RenderMode;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Timur Kuzhagaliyev
 * @since 06-12-2014
 */

public class Flow extends RenderMode {

    private ArrayList<FlowParticle> flowParticles;

    private float[] savedValues;

    public Flow(String name, int key) {
        super(name, key);

        flowParticles = new ArrayList<FlowParticle>();
        savedValues = new float[512];

        for(int i = 0; i < 512; i++)
            savedValues[i] = 0.0f;

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

        Random random = new Random();

        while(flowParticles.size() < 400) {
            float randomiser = random.nextFloat();
            float randomiser2 = random.nextFloat();
            float multiplier = (random.nextFloat() + 1.0f) * 0.5f;
            flowParticles.add(new FlowParticle(-20.0f * (1 - multiplier) * 2, Display.getHeight() * randomiser, 20.0f * (1 - multiplier), 20.0f * multiplier, 50.0f * multiplier, 2 * (float) Math.PI * randomiser2, 0.05f));
        }

        ArrayList<FlowParticle> deathRow = new ArrayList<FlowParticle>();
        for(FlowParticle flowParticle : flowParticles) {
            if(flowParticle.getPosX() > Display.getWidth() + flowParticle.getRadius() * 2) {
                deathRow.add(flowParticle);
            }
            flowParticle.render(delta, mean, bassMean);
        }
        for(FlowParticle flowParticle : deathRow)
            flowParticles.remove(flowParticle);

        glPushMatrix();
        {
            glColor3f(1, 1, 1);
            float step = Display.getWidth() / 512.0f;
            glBegin(GL_LINE_LOOP);
            {
                glVertex2f(-100, -100);
                glVertex2f(-100, Display.getHeight() / 2);

                for(int i = 0; i < 512; i++) {
                    float actualValue = savedValues[i] + (- savedValues[i] + values[i]) * 0.01f;
                    savedValues[i] = actualValue;
                    glVertex2f(step * i + step, - 100 + Display.getHeight() / 2 + Display.getHeight() * actualValue);
                }

                glVertex2f(Display.getWidth(), Display.getHeight() / 2);
                glVertex2f(Display.getWidth() + 100, -100);
            }
            glEnd();
            glBegin(GL_LINE_LOOP);
            {
                glVertex2f(-100, -100);
                glVertex2f(-100, Display.getHeight() / 2);

                for(int i = 0; i < 512; i++) {
                    float actualValue = savedValues[i] + (- savedValues[i] + values[i]) * 0.01f;
                    savedValues[i] = actualValue;
                    glVertex2f(step * i + step, 100 + Display.getHeight() / 2 - Display.getHeight() * actualValue);
                }

                glVertex2f(Display.getWidth(), Display.getHeight() / 2);
                glVertex2f(Display.getWidth() + 100, -100);
            }
            glEnd();
        }
        glPopMatrix();


    }

    public static int randInt(int min, int max) {
        Random rand = new Random();

        return rand.nextInt((max - min) + 1) + min;
    }

}
