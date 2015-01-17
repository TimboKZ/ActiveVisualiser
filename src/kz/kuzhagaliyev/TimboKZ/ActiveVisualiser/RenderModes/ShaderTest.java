package kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.RenderModes;

import kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.Core;
import kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.RenderMode;
import kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.Util;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Timur Kuzhagaliyev
 * @since 13-12-2014
 */

public class ShaderTest extends RenderMode {

    int program;
    boolean useShader;

    public ShaderTest(String name, int key) {
        super(name, key);

        int vertShader = 0, fragShader = 0;

        try {
            vertShader = Util.createShader("D:/Workspaces/Java/Workspace/Active Visualiser/res/ShaderTest_Vertex.glsl", ARBVertexShader.GL_VERTEX_SHADER_ARB);
            fragShader = Util.createShader("D:/Workspaces/Java/Workspace/Active Visualiser/res/ShaderTest_Fragment.glsl", ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
        }
        catch(Exception exc) {
            exc.printStackTrace();
            return;
        }
        finally {
            if(vertShader == 0 || fragShader == 0)
                return;
        }
        program = ARBShaderObjects.glCreateProgramObjectARB();
        if(program == 0)
            return;
        ARBShaderObjects.glAttachObjectARB(program, vertShader);
        ARBShaderObjects.glAttachObjectARB(program, fragShader);
        ARBShaderObjects.glLinkProgramARB(program);
        if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
            System.err.println(Util.getLogInfo(program));
            return;
        }
        ARBShaderObjects.glValidateProgramARB(program);
        if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
            System.err.println(Util.getLogInfo(program));
            return;
        }

        useShader = true;

    }

    public void render(float delta) {

        glViewport(0, 0, Display.getWidth(), Display.getHeight());
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(45.0f, ((float) Display.getWidth() / (float) Display.getHeight()), 0.1f, 100.0f);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glShadeModel(GL_SMOOTH);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClearDepth(1.0f);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT,
                GL_NICEST);

        if(useShader)
            ARBShaderObjects.glUseProgramObjectARB(program);

        glLoadIdentity();
        glTranslatef(0.0f, 0.0f, -10.0f);
        glColor3f(1.0f, 1.0f, 1.0f);//white

        int location = GL20.glGetUniformLocation(program, "mean");
        GL20.glUniform1f(location, Math.abs(Core.getMean(0)));

        glPushMatrix();
        Util.drawCircle(0, 0,1, 1);
        glPopMatrix();

        glPushMatrix();
        glTranslatef(-3.0f, 0, 0);
        glBegin(GL_QUADS);
        glVertex3f(-1.0f, 1.0f + Math.abs(Core.getMean(0)), 0.0f);
        glVertex3f(1.0f, 1.0f + Math.abs(Core.getMean(0)), 0.0f);
        glVertex3f(1.0f + Math.abs(Core.getMean(0)), -1.0f, 0.0f);
        glVertex3f(-1.0f - Math.abs(Core.getMean(0)), -1.0f, 0.0f);
        glEnd();
        glPopMatrix();

        glPushMatrix();
        glTranslatef(3.0f, 0, 0);
        glBegin(GL_QUADS);
        glVertex3f(-1.0f, 1.0f + Math.abs(Core.getMean(0)), 0.0f);
        glVertex3f(1.0f, 1.0f + Math.abs(Core.getMean(0)), 0.0f);
        glVertex3f(1.0f + Math.abs(Core.getMean(0)), -1.0f, 0.0f);
        glVertex3f(-1.0f - Math.abs(Core.getMean(0)), -1.0f, 0.0f);
        glEnd();
        glPopMatrix();

        //release the shader
        if(useShader)
            ARBShaderObjects.glUseProgramObjectARB(0);

    }

}