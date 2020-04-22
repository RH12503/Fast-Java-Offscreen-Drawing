package com.rh12503.rendering.pixelbuffer;


public class PixelBufferByte implements PixelBuffer<PixelBufferByte> {

    private byte[] pixels;

    public byte[] getPixels() {
        return pixels;
    }

    public void setPixels(byte[] pixels) {
        this.pixels = pixels;
    }

    @Override
    public int size() {
        return pixels.length;
    }
}
