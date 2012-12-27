package ro.narc.liquiduu;

import buildcraft.core.render.TextureLiquidsFX;

import java.util.Random;

public class TextureFXElectrolyzed extends TextureLiquidsFX {
    public static Random rand = new Random();
    public static final byte MAX_SPARK_STEPS = 4;

    public int sparkX = 0;
    public int sparkY = 0;
    public boolean sparkLeft = true;
    public byte sparkStep = MAX_SPARK_STEPS;

    public TextureFXElectrolyzed(int redMin, int redMax, int greenMin, int greenMax, int blueMin, int blueMax, int spriteIndex, String texture) {
        super(redMin, redMax, greenMin, greenMax, blueMin, blueMax, spriteIndex, texture);
    }

    @Override
    public void onTick() {
        super.onTick();

        if(rand.nextInt(20) < 4) {
            sparkX = rand.nextInt(tileSizeBase);
            sparkY = rand.nextInt(tileSizeBase) / 2;
            sparkLeft = rand.nextBoolean();
            sparkStep = 0;
        }

        if(sparkStep < MAX_SPARK_STEPS) {
            sparkStep++;
            sparkLeft = !sparkLeft;
            drawSpark();
        }
    }

    public void drawSpark() {
        int x = sparkX;
        int y = sparkY;
        boolean zig = sparkLeft;

        // A "step" is a diagonal line in the spark zig-zag shape
        for(int step = 0; step < (2 + rand.nextInt(3)); step++) {
            zig = !zig;
            for(int i = 0; i < (3 + rand.nextInt(2)); i++) {
                if(zig) { x += 1; }
                else    { x -= 1; }

                if((i % 2) == 1) {
                    y += 1;
                }

                // No drawing outside the image, but keep trying in case we return inside the image later
                if((x < 0) || (x >= tileSizeBase) || (y >= tileSizeBase)) {
                    continue;
                }

                int pixelIndex = (x + (y * tileSizeBase)) * 4;
                imageData[pixelIndex + 0] = (byte)255;
                imageData[pixelIndex + 1] = (byte)255;
                imageData[pixelIndex + 2] = (byte)255;
                imageData[pixelIndex + 3] = (byte)255;
            }
        }

    }
}
