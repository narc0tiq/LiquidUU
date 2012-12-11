package ro.narc.liquiduu;

import buildcraft.core.render.TextureLiquidsFX;

import net.minecraft.src.GLAllocation;
import net.minecraft.src.MathHelper;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import net.minecraft.src.World;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.*;

public class RenderAccelerator extends TileEntitySpecialRenderer {
    /**
     * The TileEntityRenderer instance associated with this TileEntitySpecialRenderer
     */
    //protected TileEntityRenderer tileEntityRenderer;

    // The golden ratio.
    public static final float PHI = (float) (1 + Math.sqrt(5))/2;


    public static final int VERTEX_BUFFER_SIZE = (7+7+12)*3;
    public static final int COLOR_BUFFER_SIZE = 1024;
    public static final int TOTAL_BYTES = 4*VERTEX_BUFFER_SIZE + COLOR_BUFFER_SIZE;

    public long last_tick=0;

    public final FloatBuffer vertexes;
    public final ByteBuffer colors;
    public final int bufferID;
    public final float[] vertexArray;

    public RenderAccelerator() {
        vertexes = GLAllocation.createDirectFloatBuffer(VERTEX_BUFFER_SIZE);
        colors   = GLAllocation.createDirectByteBuffer(COLOR_BUFFER_SIZE);

        vertexArray = new float[3*4*3];

        // Get the vertices by using the three internal golden rectangles
        // of an icosahedron.
        for (int rect=0; rect<3; rect++) {
            // Our three rectangles are:
            // (+-PHI,   0  , +-1  )
            // (+-1  , +-PHI,   0  )
            // (  0  , +-1  , +-PHI)
            int phi_coord  = rect;
            int zero_coord = (rect + 1) % 3;
            int one_coord  = (rect + 2) % 3;

            for (int vertex=0; vertex<4; vertex++) {
                int vertex_offset = (rect*4 + vertex)*3;
                vertexArray[vertex_offset+zero_coord] = 0F;

                if (vertex < 2) {
                    vertexArray[vertex_offset + one_coord] =  1F;
                } else {
                    vertexArray[vertex_offset + one_coord] = -1F;
                }

                if (vertex == 1 || vertex == 2) {
                    vertexArray[vertex_offset + phi_coord] =  PHI;
                } else {
                    vertexArray[vertex_offset + phi_coord] = -PHI;
                }
            }
        }

        // Starting point.
        addVertex(0, 0);

        // And the five points on its fan.
        addVertex(1, 2);
        addVertex(0, 3);
        addVertex(1, 3);
        addVertex(2, 2);
        addVertex(2, 1);
        // Overlap 1.
        addVertex(1, 2);

        // Opposite starting point.
        addVertex(0, 2);

        // And the five points on its fan.
        addVertex(1, 1);
        addVertex(0, 1);
        addVertex(1, 0);
        addVertex(2, 3);
        addVertex(2, 0);
        // Overlap 1.
        addVertex(1, 1);

        // The strip connecting the two fans.
        addVertex(0, 3);
        addVertex(2, 3);
        addVertex(1, 3);
        addVertex(1, 0);
        addVertex(2, 2);
        addVertex(0, 1);
        addVertex(2, 1);
        addVertex(1, 1);
        addVertex(1, 2);
        addVertex(2, 0);
        // Overlap 2.
        addVertex(0, 3);
        addVertex(2, 3);
        vertexes.rewind();

        bufferID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, TOTAL_BYTES,
                          GL15.GL_DYNAMIC_DRAW);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vertexes);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void addVertex(int rectangle, int vertex) {
        int offset = (rectangle * 4 + vertex) * 3;
        vertexes.put(vertexArray, offset, 3);
    }

    @SuppressWarnings("unchecked")
    public void updateColors(long tick) {
        if (tick == last_tick) {
            return;
        }
        last_tick = tick;

        ClientProxy proxy = (ClientProxy) LiquidUU.proxy;
        TextureLiquidsFX fx = proxy.liquidUUFX;

        /*Random rand = new Random(3595L);
        for (int i=0; i<28; i++) {
            colors.put(rand.nextFloat());
            colors.put(rand.nextFloat());
            colors.put(rand.nextFloat());
        }*/

        colors.rewind();
        colors.put(fx.imageData);
        colors.rewind();

        //byte[] arr = new byte[1024];
        //colors.get(arr);
        //colors.rewind();
        //
        //System.out.println(Arrays.toString(arr));

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, VERTEX_BUFFER_SIZE*4, colors);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
        if (!(te instanceof TileEntityAccelerator)) {
            return;
        }

        TileEntityAccelerator accelerator = (TileEntityAccelerator) te;
        float fill = accelerator.tank.getLiquidAmount() / 2000.0F;

        if (fill == 0) {
            // No UU to render.
            return;
        }

        // Update the color buffer from the TextureFX, but only once per tick.
        updateColors(te.worldObj.getWorldInfo().getWorldTime());

        int phase_len    = accelerator.character[0];
        int phase_offset = accelerator.character[1];
        int spin_axis    = accelerator.character[2];
        int colorOffset  = accelerator.character[3];

        if (spin_axis < 0 || spin_axis > 5) {
            spin_axis = 0;
        }

        // Store previous modelview matrix.
        GL11.glPushMatrix();

        // Translate so the origin is at the center of the block.
        GL11.glTranslatef((float)(x + 0.5), (float)(y + 0.5),
                          (float)(z + 0.5));

        // Does anybody really know what time it is?
        long effective_time = System.currentTimeMillis() + phase_offset;
        float phase = (effective_time % phase_len) / (float) phase_len;

        float base_size = 0.2F;
        double breath;
        float progress;
        if (phase < 0.25) {
            // Inhaling.
            progress = (phase - 0.0F) / 0.25F;
            breath = 0.9 + (1 + Math.sin((progress - 0.5) * Math.PI)) * 0.05;
        } else if (phase < 0.75) {
            // Exhaling.
            progress = (phase - 0.25F) / 0.5F;
            breath = 0.9 + (1 + Math.sin((0.5 - progress) * Math.PI)) * 0.05;
        } else {
            // Minimum size.
            breath = 0.9F;
        }

        float size = base_size * fill * (float) breath;
        GL11.glScalef(size, size, size);

        // Current spin angle.
        float spin_angle = phase*360;

        // This line was a bug, but the results are better than without it.
        // A very happy accident.
        GL11.glRotatef(spin_angle, 0F,1F,0F);
        switch (spin_axis) {
            case 0:
                GL11.glRotatef(spin_angle,  0F,0F,1F);
                break;
            case 1:
                GL11.glRotatef(spin_angle,  0F,0F,-1F);
                break;
            case 2:
                GL11.glRotatef(spin_angle,  0F,1F,0F);
                break;
            case 3:
                GL11.glRotatef(spin_angle,  0F,-1F,0F);
                break;
            case 4:
                GL11.glRotatef(spin_angle,  1F,0F,0F);
                break;
            case 5:
                GL11.glRotatef(spin_angle,  -1F,0F,0F);
                break;
        }

        // Angle in vertical plain to orient point-up.
        float vert_angle = (float) Math.toDegrees(Math.atan2(1, PHI));
        GL11.glRotatef(vert_angle,  0F,0F,1F);

        // Enable array modes.
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);

        // Bind the vertices.
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);
        // And the colors.
        // (I continue to hate Java's % operator. -FM)
        GL11.glColorPointer( 4, GL11.GL_UNSIGNED_BYTE, 0, VERTEX_BUFFER_SIZE*4
                                                          + colorOffset*4);

        // Stop using textures
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        // Select full-bright in texture-based lighting
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,
                (float)0xF0 / 1.0F, (float)0xF0 / 1.0F);

        // Stop using lighting
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_LIGHT0);
        GL11.glDisable(GL11.GL_LIGHT1);

        // Ensure the baseline color is set properly.
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        // Draw the icosahedron itself.
        GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN,    0,  7);
        GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN,    7,  7);
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 14, 12);

        // Resume using lighting
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_LIGHT0);
        GL11.glEnable(GL11.GL_LIGHT1);

        // Resume using textures
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        // Unbind the buffer.
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        // Disable array modes.
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);

        // Restore previous modelview matrix.
        GL11.glPopMatrix();
    }

    /**
     * Binds a texture to the renderEngine given a filename from the JAR.
     */
    //protected void bindTextureByName(String par1Str)

    // bindTextureByURL
    //protected void func_82392_a(String par1Str, String par2Str)

    /**
     * Associate a TileEntityRenderer with this TileEntitySpecialRenderer
     */
    //public void setTileEntityRenderer(TileEntityRenderer par1TileEntityRenderer)

    /**
     * Called when the ingame world being rendered changes (e.g. on world -> nether travel) due to using one renderer
     * per tile entity type, rather than instance
     */
    public void onWorldChange(World par1World) {}

    //public FontRenderer getFontRenderer()
}
