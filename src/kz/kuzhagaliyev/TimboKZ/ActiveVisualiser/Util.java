package kz.kuzhagaliyev.TimboKZ.ActiveVisualiser;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Timur Kuzhagaliyev
 * @since 12-12-2014
 */

public class Util {

    public static float smooth(float value, float target, float rate) {

        if(value < target) {
            value += rate;
        }
        if(value > target) {
            value -= rate;
        }

        return value;

    }

    public static void drawRect(float x, float y, float width, float height, float colour) {

        drawRect(x, y, width, height, colour, colour, colour);

    }

    public static void drawRect(float x, float y, float width, float height, float r, float g, float b) {

        glPushMatrix();
        {
            glColor3f(r, g, b);
            glBegin(GL_QUADS);
            {
                glVertex2f(x, y);
                glVertex2f(x, y + height);
                glVertex2f(x + width, y + height);
                glVertex2f(x + width, y);
            }
            glEnd();
        }
        glPopMatrix();

    }

    public static void drawCircle(float x, float y, float radius, float colour) {

        drawCircle(x, y, radius, colour, colour, colour);

    }

    public static void drawCircle(float x, float y, float radius, float r, float g, float b) {
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

    public static Integer parseFrequency(Integer[] values, Integer value) {


        int difference = Math.abs(values[0] - value);
        int counter = 0;

        for(int i = 1; i < values.length; i++) {

            int currentDifference = Math.abs(values[i] - value);

            if(currentDifference < difference) {
                difference = currentDifference;
                counter = i;
            }

        }

        return values[counter];

    }

    public static float[] parseFloatArray(Float[] values) {
        float[] newValues = new float[values.length];
        for(int i = 0; i < values.length; i++) {
            newValues[i] = values[i];
        }
        return newValues;
    }

    public static int createShader(String filename, int shaderType) throws Exception {
        int shader = 0;
        try {
            shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

            if(shader == 0)
                return 0;

            ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
            ARBShaderObjects.glCompileShaderARB(shader);

            if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
                throw new RuntimeException("Error creating shader: " + getLogInfo(shader));

            return shader;
        }
        catch(Exception exc) {
            ARBShaderObjects.glDeleteObjectARB(shader);
            throw exc;
        }
    }

    public static String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }

    public static String readFileAsString(String filename) throws Exception {
        StringBuilder source = new StringBuilder();

        FileInputStream in = new FileInputStream(filename);

        Exception exception = null;

        BufferedReader reader;
        try{
            reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));

            Exception innerExc= null;
            try {
                String line;
                while((line = reader.readLine()) != null)
                    source.append(line).append('\n');
            }
            catch(Exception exc) {
                exception = exc;
            }
            finally {
                try {
                    reader.close();
                }
                catch(Exception exc) {
                    if(innerExc == null)
                        innerExc = exc;
                    else
                        exc.printStackTrace();
                }
            }

            if(innerExc != null)
                throw innerExc;
        }
        catch(Exception exc) {
            exception = exc;
        }
        finally {
            try {
                in.close();
            }
            catch(Exception exc) {
                if(exception == null)
                    exception = exc;
                else
                    exc.printStackTrace();
            }

            if(exception != null)
                throw exception;
        }

        return source.toString();
    }

    public static boolean isInRange(float value, float lowerLimit, float upperLimit) {

        return (value >= lowerLimit && value <= upperLimit);

    }

}
