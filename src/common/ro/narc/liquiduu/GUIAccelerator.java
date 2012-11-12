package ro.narc.liquiduu;

import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.GuiContainer;

import org.lwjgl.opengl.GL11;

public class GUIAccelerator extends GuiContainer {
    public static final int BORDER = 4;
    public int main_width, main_height, left, top, center, middle, right, bottom;

    public GUIAccelerator(InventoryPlayer inventory, TileEntityAccelerator accelerator) {
        super(new ContainerAccelerator(inventory, accelerator));

        // These are such wonderful things to have, I've just stolen them from SeedManager.
        main_width = xSize - BORDER * 2;
        main_height = ySize - BORDER * 2 - 92;

        left = BORDER;
        top = BORDER;
        center = left + main_width / 2;
        middle = top + main_height / 2;
        right = left + main_width;
        bottom = top + main_height;
    }

    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        int textureID = mc.renderEngine.getTexture("/liquiduu-gfx/gui-accelerator.png");
        mc.renderEngine.bindTexture(textureID);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        // Transfer the coordinate space to within the GUI screen.
        GL11.glPushMatrix();
        GL11.glTranslatef(guiLeft, guiTop, 0.0F);

        // Draw the background.
        drawTexturedModalRect(0, 0, 0, 0, xSize, ySize);

        // Nothing else to do right now...

        // Restore previous coordinates.
        GL11.glPopMatrix();
    }

    protected void drawGuiContainerForegroundLayer() {
        drawCenteredString("Accelerator", center, top + 2, 0x404040);

        fontRenderer.drawString("Inventory", left + 4, bottom + 2, 0x404040);
    }

    public void drawCenteredString(String s, int x, int y, int color) {
        fontRenderer.drawString(s, x - fontRenderer.getStringWidth(s) / 2, y,
                                color);
    }
}
