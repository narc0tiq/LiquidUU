package ro.narc.liquiduu;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemGenericBlock extends ItemBlock {
    public ItemGenericBlock(int i) {
        super(i);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getItemNameIS(ItemStack itemstack)
    {
        int damage = itemstack.getItemDamage();

        if(damage == BlockGeneric.DATA_ACCELERATOR) {
            return "tile.UUMAccelerator";
        }

        return "tile.UUMBlock";
    }
}
