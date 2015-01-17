package kz.kuzhagaliyev.TimboKZ.ActiveVisualiser;

/**
 * @author Timur Kuzhagaliyev
 * @since 05-12-2014
 */

public abstract class RenderMode {

    private String name;
    private int key;

    public RenderMode(String name, int key) {
        this.name = name;
        this.key = key;
    }

    public abstract void render(float delta);

    public String getName() {
        return name;
    }
    public int getKey() {
        return key;
    }

}
