package ro.narc.liquiduu;

import net.minecraft.src.ItemStack;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.Slot;
import net.minecraft.src.StatCollector;

import org.lwjgl.opengl.GL11;

public class GUIAccelerator extends GuiContainer {
    public static final int BORDER = 4;
    public int main_width, main_height, left, top, center, middle, right, bottom;

    TileEntityAccelerator accelerator;

    public GUIAccelerator(InventoryPlayer inventory, TileEntityAccelerator accelerator) {
        super(new ContainerAccelerator(inventory, accelerator));
        this.accelerator = accelerator;

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

        Slot machineSlot = inventorySlots.getSlot(3);
        ItemStack machine = machineSlot.getStack();
        if(machine == null) { // "No Machine" icon
            drawTexturedModalRect(machineSlot.xDisplayPosition, machineSlot.yDisplayPosition,
                    176, 0, 16, 16);
        }

        // Restore previous coordinates.
        GL11.glPopMatrix();
    }

    protected void drawGuiContainerForegroundLayer() {
        fontRenderer.drawString("Accelerator", left + 4, top + 2, 0x404040);

        drawCenteredString("Connected Machine", 70, top + 12, 0x404040);

        String machineName = "None";
        ItemStack machine = inventorySlots.getSlot(3).getStack();
        if(machine != null) {
            machineName = StatCollector.translateToLocal(machine.getItemName() + ".name");
        }
        drawCenteredString(machineName, 70, top + 38, 0x404040);

        int operationCost = accelerator.getOperationCost();
        if(operationCost > 0) {
            drawOperationCosts(operationCost);
        }
        fontRenderer.drawString("Inventory", left + 4, bottom + 2, 0x404040);
    }

    public void drawCenteredString(String s, int x, int y, int color) {
        fontRenderer.drawString(s, x - fontRenderer.getStringWidth(s) / 2, y,
                                color);
    }

    public void drawRightAlignedString(String s, int x, int y, int color) {
        fontRenderer.drawString(s, x - fontRenderer.getStringWidth(s), y,
                                color);
    }

    public void drawOperationCosts(int cost) {
        String[] line1 = String.format("%.3f UUM/operation", (float)cost / 1000).split("\\.");
        String[] line2 = String.format("%.2f operations/UUM", 1000.0F / cost).split("\\.");

        int topWidth = fontRenderer.getStringWidth(line1[1]);
        int botWidth = fontRenderer.getStringWidth(line2[1]);
        int width = Math.max(topWidth, botWidth);

        fontRenderer.drawString("." + line1[1], (right - 24) - width, bottom - 17, 0x404040);
        fontRenderer.drawString("." + line2[1], (right - 24) - width, bottom - 8, 0x404040);

        drawRightAlignedString(line1[0], (right - 24) - width, bottom - 17, 0x404040);
        drawRightAlignedString(line2[0], (right - 24) - width, bottom - 8, 0x404040);
    }
}
