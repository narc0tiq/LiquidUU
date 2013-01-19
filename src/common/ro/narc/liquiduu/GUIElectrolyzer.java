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

public class GUIElectrolyzer extends GuiContainer {
    public static final String TEXTURE_FILE = "/liquiduu-gfx/gui-electrolyzer.png";
    public static final int BORDER = 4;
    public int main_width, main_height, left, top, center, middle, right, bottom;

    public GUITank guiTank = new GUITank(TEXTURE_FILE, 64, 176, 0);

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

    // Temporary test tanks.
    public static ILiquidTank waterTank = new LiquidTank(new LiquidStack(Block.waterStill, 7000), 16000);
    public static ILiquidTank electricWaterTank = new LiquidTank(CommonProxy.electricWaterLiquidStack.itemID, 9000, 16000);

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        ForgeHooksClient.bindTexture("/liquiduu-gfx/gui-electrolyzer.png", 0);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        // Transfer the coordinate space to within the GUI screen.
        GL11.glPushMatrix();
        GL11.glTranslatef(guiLeft, guiTop, 0.0F);

        // Draw the background.
        drawTexturedModalRect(0, 0, 0, 0, xSize, ySize);

        guiTank.tank = waterTank;
        guiTank.draw(8, 6);
        guiTank.tank = electricWaterTank;
        guiTank.draw(132, 6);

        // Restore previous coordinates.
        GL11.glPopMatrix();
    }
}
