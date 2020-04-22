# Fast-Java-Offscreen-Drawing
A small library which provides fast 2d offscreen rendering for Java. Up to 50x faster than a BufferedImage. This library is experimental and is intended for personal use. 

## Simple usage
Every rendering operation goes through the `DrawingCanvas` interface, which is defined as follows:

```java
public interface DrawingCanvas {
    void begin();
    void end();
    void background(SimpleColor color);
    void rect(float x, float y, float w, float h);
    void ellipse(float x, float y, float w, float h);
    void circle(float x, float y, float r);
    void polygon(float[] xVertices, float[] yVertices);
    void line(float x1, float y1, float x2, float y2, float strokeWeight);
    void fill(SimpleColor color);
    PixelBuffer getPixels();
    BufferedImage toImage();
    int getWidth();
    int getHeight();
}
```


An example program to render an circle, save it as an image, and then exit could look something like this:
```java
public class SimpleExample {
    public static void main(String[] args) throws IOException {
        // Create a new canvas with a width and height of 500 pixels
        DrawingCanvas canvas = new AparapiCanvas(500, 500);

        // When rendering it is important to call begin() and end() methods
        canvas.begin();
        // Color the background white
        canvas.background(new SimpleColor(1, 1, 1, 1));

        // Set the color of the circle to red with an opacity of 50%
        canvas.fill(new SimpleColor(1,0,0,0.5f));

        // Draw a circle in the center of the canvas, with a radius of 100 pixels
        canvas.circle(250, 250, 100);
        // After end() is called, the shape data is sent to the GPU and processed
        canvas.end();

        // Write the image to a file
        ImageIO.write(canvas.toImage(), "png", new File("result.png"));
        
        // Exit
        System.exit(1);
    }
}
```
## Reasons to use over OpenGL and limitations
OpenGL is undoubtedly the fastest way to render both 2D and 3D on the GPU. However, one disadvantage is that due to the pipeline, obtaining pixels is very slow. I made this library for a genetic algorithm project which needed to constantly access pixels, so use a Pixel Buffer Object wasn't an option. 

Of course, the most obvious limitation is that it renders offscreen. This narrows down this libraries use cases greatly, which was made specifically for personal use. Another limitation is that strokes and anti-aliasing are not supported, but that may change the future. 
