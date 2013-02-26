package ro.narc.liquiduu;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

import org.lwjgl.opengl.GL11;

import ro.narc.util.GUITank;
import ro.narc.util.GUITooltip;

public class GUIElectrolyzer extends GuiContainer {
    public static final String TEXTURE_FILE = "/liquiduu-gfx/gui-electrolyzer.png";
    public static final int BORDER = 4;
    public int main_width, main_height, left, top, center, middle, right, bottom;

    public GUITank guiTank = new GUITank(TEXTURE_FILE, 64, 176, 0);
    public GUITooltip tooltip = new GUITooltip();

    TileEntityElectrolyzer electrolyzer;

    public GUIElectrolyzer(InventoryPlayer inventory, TileEntityElectrolyzer electrolyzer) {
        super(new ContainerElectrolyzer(inventory, electrolyzer));
        this.electrolyzer = electrolyzer;

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

    public static LiquidTank waterTank = new LiquidTank(new LiquidStack(Block.waterStill, 0), 0);
    public static LiquidTank electricWaterTank = new LiquidTank(CommonProxy.electricWaterLiquidStack.itemID, 0, 0);

    public void updateTanks() {
        waterTank.setCapacity(electrolyzer.getConnectedLiquidCapacity(waterTank.getLiquid(), MachineFace.Water));
        waterTank.setLiquid(electrolyzer.getConnectedLiquid(waterTank.getLiquid(), MachineFace.Water));

        electricWaterTank.setCapacity(electrolyzer.getConnectedLiquidCapacity(electricWaterTank.getLiquid(), MachineFace.ElectricWater));
        electricWaterTank.setLiquid(electrolyzer.getConnectedLiquid(electricWaterTank.getLiquid(), MachineFace.ElectricWater));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        ForgeHooksClient.bindTexture("/liquiduu-gfx/gui-electrolyzer.png", 0);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        // Transfer the coordinate space to within the GUI screen.
        GL11.glPushMatrix();
        GL11.glTranslatef(guiLeft, guiTop, 0.0F);

        // Draw the background.
        drawTexturedModalRect(0, 0, 0, 0, xSize, ySize);

        updateTanks();

        guiTank.tank = waterTank;
        guiTank.draw(8, 6);
        guiTank.tank = electricWaterTank;
        guiTank.draw(132, 6);

        // Restore previous coordinates.
        GL11.glPopMatrix();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        // Easier to work with these when localized to the GUI proper.
        mouseX -= guiLeft;
        mouseY -= guiTop;

        drawCenteredString("Liquid Electrolyzer", 76, top +  2, 0x404040);
        fontRenderer.drawString("Inventory", left + 4, bottom + 2, 0x404040);

        if((mouseX >= 8) && (mouseX < 24) &&
           (mouseY >= 6) && (mouseY < 70)) {
            tooltip.lines.clear();
            tooltip.lines.add(String.format("%.3f/%.3f water\u00a77 stored",
                (float) waterTank.getLiquid().amount / 1000.0F, (float) waterTank.getCapacity() / 1000.00F));

            tooltip.draw(mouseX, mouseY);
        }
        else if((mouseX >= 132) && (mouseX < 148) &&
           (mouseY >= 6) && (mouseY < 70)) {
            tooltip.lines.clear();
            tooltip.lines.add(String.format("%.3f/%.3f electrolyzed water\u00a77 stored",
                (float) electricWaterTank.getLiquid().amount / 1000.0F, (float) electricWaterTank.getCapacity() / 1000.00F));

            tooltip.draw(mouseX, mouseY);
        }
    }

    public void drawCenteredString(String s, int x, int y, int color) {
        fontRenderer.drawString(s, x - fontRenderer.getStringWidth(s) / 2, y,
                                color);
    }
}
