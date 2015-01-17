Active Visualiser
=================

Active Visualiser by Timbo_KZ. [Demo on YouTube](https://www.youtube.com/watch?v=OUPmbOWXMtE).

Dependencies
============

This project uses Java SDK 1.7. You will need [Beads Project](http://www.beadsproject.net/), [LWJGL 2.9.1](http://legacy.lwjgl.org/) and [Slick](http://slick.ninjacave.com/).

Running and controlling the visualiser
======================================

First of all, make sure all dependencies are installed and the `Core` class is the main class.

For Active Visualiser to run correctly, you must add a path to LWJGL natives, which are usually found in LWJGL archive. This is done by specifying appropriate VM options before running the application. I'm using Windows and path to Windows natices on my system is `D:\Workspaces\Java\Libraries\lwjgl-2.9.1\native\windows`, so my VM options look like this:

`-Djava.library.path="D:\Workspaces\Java\Libraries\lwjgl-2.9.1\native\windows"`

At this point in time, if everything was done correctly, you should be able to run the visualiser without any issues. You can test that all default render modes work by pressing buttons `1-5` and `T` to run the test mode. Pressing `F` should display the name of the render mode and the current frame rate. Hover the bottom of the window to reveal the progress bar. Clicking at any point on the progress bar will play the audio from that point.

Additionally, you can configure the visualiser by specifying program arguments. Table of all possible program arguments can be found below, but keep in mind none of them are required for the app to run.

|Argument|Parameters|Description|
|--------|----------|------------|
|fullscreen|No parameters|Simply add this word to the program arguments to force th visualiser run in fullscreen mode.|
|width|Window width as an integer|If the visualiser is not in fullscreen mode, this argument will change the width of the visualiser window. Default width is 1280.|
|height|Window height as an integer|If the visualiser is not in fullscreen mode, this argument will change the height of the visualiser window. Default height is 720.|
|framerate|Maximum framerate as an integer|Adding this argument will limit the maximum framerate of the visualiser to the specified value. Default framerate is 60.|
|active|No parameters|Adding this word to the program arguments will attempt to run the visualiser in the active mode, i.e. using the sound from your microphone. This feature is experimental and has only been tested on Windows.|
|audio|Path to an audio file as a string|If this argument is added the visualiser will attempt to load an audio file specified instead of prompting a file chooser dialog.|
|fps|No parameters|Adding this word to the program arguments will display FPS counter and the name of the render mode by default. You can toggle it using `F` key on your keyboard.|

Examples of different configurations:

![Examples of different configurations](http://i.imgur.com/dWTTSBk.png)

How to add new render modes
===========================

1. Create a class in `RenderModes` package that extends `RenderMode`, example:

    ```java
    public class Your_Visualiser extends RenderMode {

        public Your_Visualiser(String name, int key) {
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

                // Any code for drawing here

        }

    }
    ```

2. Register your render mode inside `initRenderModes` method in the `Core` class. The method will look something like this:

    ```java

    // ....

    private static void initRenderModes() {
        renderModes = new ArrayList<RenderMode>();

        // Registering new render modes
        renderModes.add(new Pulse("Pulse", Keyboard.KEY_1));
        renderModes.add(new ShaderTest("Shader Test", Keyboard.KEY_5));
        renderModes.add(new Blur("Blur", Keyboard.KEY_4));
        renderModes.add(new Twist("Twist", Keyboard.KEY_3));
        renderModes.add(new Flow("Flow", Keyboard.KEY_2));
        renderModes.add(new Test("Test", Keyboard.KEY_T));

        if(renderModes.size() == 0) {
            System.out.println("No render modes available.");
            System.exit(1);
        }

        currentRenderMode = renderModes.get(0);
    }

    // ....

    ```

    You will need to add an object of your visualiser to the `renderModes` list. The first parameter is the name of the visualiser, the second parameter is the keyboard key assigned to your visualiser. The line you'd add should look something like this:

    ```java
    renderModes.add(new Your_Visualiser("Your Visualiser", Keyboard.KEY_6));
    ```

    Now you can see your render mode by launching the visualiser and pressing the key you assigned, in this case it's `6`.
