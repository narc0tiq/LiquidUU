package ro.narc.liquiduu;

import java.text.DecimalFormatSymbols;

import net.minecraftforge.client.ForgeHooksClient;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import ro.narc.util.GUITooltip;
import ro.narc.util.GUITank;

public class GUIAccelerator extends GuiContainer {
    public static final int BORDER = 4;
    public int main_width, main_height, left, top, center, middle, right, bottom;
    public GUITooltip tooltip = new GUITooltip();
    public GUITank guiTank = new GUITank("/liquiduu-gfx/gui-accelerator.png", 47, 176, 16);

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

    @Override
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

        guiTank.tank = accelerator.tank;
        guiTank.draw(152, 29);

        // Restore previous coordinates.
        GL11.glPopMatrix();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        // Easier to work with these when localized to the GUI proper.
        mouseX -= guiLeft;
        mouseY -= guiTop;

        String machineName = "None";
        ItemStack machine = inventorySlots.getSlot(3).getStack();
        if(machine != null) {
            machineName = StatCollector.translateToLocal(machine.getItemName() + ".name");
        }

        drawCenteredString("Accelerator",       70, top +  2, 0x404040);
        drawCenteredString("Connected Machine", 70, top + 12, 0x404040);
        drawCenteredString(machineName,         70, top + 38, 0x404040);

        ((ContainerAccelerator)inventorySlots).updateOutputSlot();
        Slot outputSlot = inventorySlots.getSlot(1);
        ItemStack output = outputSlot.getStack();
        if(output == null) { // "Unknown or no output" icon
            drawCenteredString("??", outputSlot.xDisplayPosition + 8,
                                     outputSlot.yDisplayPosition + 4, 0x404040);
        }

        int uumAmount = accelerator.tank.getLiquidAmount();
        int operationCost = accelerator.getOperationCost();
        if(operationCost > 0) {
            drawOperationCosts(operationCost);
        }
        fontRenderer.drawString("Inventory", left + 4, bottom + 2, 0x404040);

        if((mouseX >= 152) && (mouseX < 168) &&
           (mouseY >=  29) && (mouseY <  76)) {
            tooltip.lines.clear();
            tooltip.lines.add(String.format("%.3f UUM\u00a77 stored", (float) uumAmount / 1000.0F));

            if(operationCost > 0) {
                tooltip.lines.add(String.format("%d operations\u00a77 remaining",
                    MathHelper.floor_float((float)uumAmount / (float)operationCost)));
            }

            tooltip.draw(mouseX, mouseY);
        }
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
        String decimalSeparator = DecimalFormatSymbols.getInstance().getDecimalSeparator() + "";

        String splitStr = "[ " + decimalSeparator + "]";
        String[] line1 = String.format("%.3f UUM/operation",  cost / 1000.0F).split(splitStr);
        String[] line2 = String.format("%.3f operations/UUM", 1000.0F / cost).split(splitStr);

        int topWidths[] = new int[3];
        int botWidths[] = new int[3];

        for (int i=0; i<3; i++) {
            topWidths[i] = fontRenderer.getStringWidth(line1[i]);
            botWidths[i] = fontRenderer.getStringWidth(line2[i]);
        }

        int dotWidth = fontRenderer.getStringWidth(decimalSeparator);
        int spaceWidth = fontRenderer.getStringWidth(" ");

        int offsets[] = new int[3];
        offsets[2] = Math.max(topWidths[2], botWidths[2]);
        offsets[1] = Math.max(topWidths[1], botWidths[1]) + dotWidth + spaceWidth;
        // Keep offsets[0], just for aligning the arrays.

        int rightAlign = right - 24 - offsets[2];

        fontRenderer.drawString(line1[2], rightAlign, bottom-17, 0x404040);
        fontRenderer.drawString(line2[2], rightAlign, bottom-8,  0x404040);

        rightAlign -= offsets[1];

        fontRenderer.drawString(decimalSeparator + line1[1], rightAlign, bottom-17, 0x404040);
        fontRenderer.drawString(decimalSeparator + line2[1], rightAlign, bottom-8,  0x404040);

        fontRenderer.drawString(line1[0], rightAlign - topWidths[0], bottom-17, 0x404040);
        fontRenderer.drawString(line2[0], rightAlign - botWidths[0], bottom-8,  0x404040);
    }
}
