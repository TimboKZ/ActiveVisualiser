package kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.RenderModes;

import kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.Util;

/**
 * @author Timur Kuzhagaliyev
 * @since 06-12-2014
 */

public class FlowParticle {

    float posX;
    float posY;
    float displacementY;
    float radius;

    float speed;
    float curvature;
    float phase;

    float r;
    float g;
    float b;

    public FlowParticle(float posX, float posY, float radius, float speed, float curvature, float phase, float r, float g, float b) {
        this.posX = posX;
        this.posY = posY;
        this.displacementY = 0;
        this.radius = radius;
        this.speed = speed;
        this.curvature = curvature;
        this.phase = phase;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public FlowParticle(float posX, float posY, float radius, float speed, float curvature, float phase, float colour) {
        this.posX = posX;
        this.posY = posY;
        this.radius = radius;
        this.speed = speed;
        this.curvature = curvature;
        this.phase = phase;
        this.r = colour;
        this.g = colour;
        this.b = colour;
    }

    public FlowParticle(float posX, float posY, float radius, float speed, float curvature, float phase) {
        this.posX = posX;
        this.posY = posY;
        this.radius = radius;
        this.speed = speed;
        this.curvature = curvature;
        this.phase = phase;
        this.r = 1;
        this.g = 1;
        this.b = 1;
    }

    public void render(float delta, float mean, float value) {

        posX += speed * 0.25 + speed * 0.75 * Math.abs(mean) - speed * 5 * Math.abs(value);

        displacementY = (float) Math.sin(posX / 100.0f + phase) * curvature;

        Util.drawCircle(posX, posY + displacementY, radius + radius * 2 * Math.abs(value), r + 0.5f * Math.abs(value), g + 0.5f * Math.abs(value), b + 0.5f * Math.abs(value));

    }

    public float getPosX() {
        return posX;
    }

    public float getRadius() {
        return radius;
    }
}
