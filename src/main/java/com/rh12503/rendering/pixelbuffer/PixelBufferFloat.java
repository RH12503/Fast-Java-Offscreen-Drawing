package com.rh12503.rendering.pixelbuffer;

public class PixelBufferFloat implements PixelBuffer<PixelBufferFloat> {
    private float[] pixels;

    public float[] getPixels() {
        return pixels;
    }

    public void setPixels(float[] pixels) {
        this.pixels = pixels;
    }

    @Override
    public int size() {
        return pixels.length;
    }
}
