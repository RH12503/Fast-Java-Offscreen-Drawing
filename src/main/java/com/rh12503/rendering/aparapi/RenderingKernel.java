package com.rh12503.rendering.aparapi;

import com.aparapi.Kernel;
import com.aparapi.Range;
import com.rh12503.rendering.PointIntersects;
import com.rh12503.rendering.pixelbuffer.PixelBufferFloat;

import static com.rh12503.rendering.aparapi.ShapeType.*;

public class RenderingKernel extends Kernel {

    private static final float[] EMPTY_DATA = {-1};

    private float[] shapeData = null;
    private float[] backgroundData = null;

    private Range range;
    private float[] pixels = null;
    private final int width;
    private final int height;

    private final DataProvider dataProvider;

    public RenderingKernel(final DataProvider dataProvider, final int width, final int height) {
        this.dataProvider = dataProvider;
        range = Range.create(width * height);
        this.width = width;
        this.height = height;
    }

    public void execute(final PixelBufferFloat pixelBuffer) {
        this.pixels = pixelBuffer.getPixels();
        this.backgroundData = dataProvider.getBackgroundData();
        this.shapeData = dataProvider.getShapeData();
        if (shapeData.length == 0) {
            this.shapeData = EMPTY_DATA;
        }

        this.execute(range);
    }


    @Override
    public void run() {
        int i = getGlobalId();

        float x = (i % width) + 0.5f;
        float y = (i / width) + 0.5f;
        float r = backgroundData[0];
        float g = backgroundData[1];
        float b = backgroundData[2];


        int j = 0;
        boolean go = true;
        while (j < shapeData.length && go) {
            int shapeType = round(shapeData[j]);
            if (shapeType != -1) {
                j++;
                boolean intersects = false;

                float sR = shapeData[j];
                float sG = shapeData[j + 1];
                float sB = shapeData[j + 2];
                float sA = shapeData[j + 3];
                j += 4;

                if (shapeType == CIRCLE) {
                    intersects = PointIntersects.circle(x, y, shapeData[j], shapeData[j + 1], shapeData[j + 2]);
                    j += 3;
                } else if (shapeType == RECT) {
                    intersects = PointIntersects.rect(x, y, shapeData[j], shapeData[j + 1], shapeData[j + 2], shapeData[j + 3]);
                    j += 4;
                } else if (shapeType == ELLIPSE) {
                    intersects = PointIntersects.ellipse(x, y, shapeData[j], shapeData[j + 1], shapeData[j + 2], shapeData[j + 3]);
                    j += 4;
                } else if (shapeType == LINE) {
                    intersects = PointIntersects.line(x, y, shapeData[j], shapeData[j + 1], shapeData[j + 2], shapeData[j + 3], shapeData[j + 4] / 2f);
                    j += 5;
                } else if (shapeType == POLYGON) {
                    int vertexCount = round(shapeData[j]);
                    intersects = PointIntersects.polygon(x, y, shapeData, j + 1, vertexCount);
                    j += 1 + vertexCount * 2;
                }

                if (intersects) {
                    r = r * (1 - sA) + sR * sA;

                    g = g * (1 - sA) + sG * sA;
                    b = b * (1 - sA) + sB * sA;
                }
            } else {
                go = false;
            }
        }

        int pixelIndex = i * 3;

        pixels[pixelIndex] = r;
        pixels[pixelIndex + 1] = g;
        pixels[pixelIndex + 2] = b;
    }

    public interface DataProvider {
        float[] getBackgroundData();

        float[] getShapeData();
    }
}