package ro.narc.liquiduu;

import net.minecraft.item.Item;

public class ItemGeneric extends Item {
    public ItemGeneric(String name, int iconIndex, int itemID) {
        super(itemID);

        this.setItemName(name);
        this.setIconIndex(iconIndex);
        this.setTextureFile("/liquiduu-gfx/items.png");
    }
}
