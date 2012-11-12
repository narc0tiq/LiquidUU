package ro.narc.liquiduu;

import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class ItemGenericBlock extends ItemBlock {
    public ItemGenericBlock(int i) {
        super(i);
        setHasSubtypes(true);
    }

    public int getMetadata(int damage) {
        return damage;
    }

    public String getItemNameIS(ItemStack itemstack)
    {
        int damage = itemstack.getItemDamage();

        if(damage == BlockGeneric.DATA_ACCELERATOR) {
            return "tile.UUMAccelerator";
        }

        return "tile.UUMBlock";
    }
}
