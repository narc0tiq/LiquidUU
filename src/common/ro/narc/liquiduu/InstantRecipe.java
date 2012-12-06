package ro.narc.liquiduu;

import net.minecraft.src.ItemStack;

public class InstantRecipe {
    public ItemStack input;
    public ItemStack output;
    public ItemStack display;
    public int uuCost;
    public IAcceleratorFriend machine;
    // Special notes about the display stack, above: this will be shown in the output slot and
    // ItemStack merging *will* try to apply to it. Use *ONLY* items that do not stack here. This
    // also means that if your output stack is something that would be merge-able, it will be
    // broken for the case where the player clicks with a stack of [output stack item].
    // In other words: use only in case of dire necessity. Like hiding seed bags' scan values.

    // Maximal constructor: an IAcceleratorFriend to be notified, and a special ItemStack to display
    // in place of the output.
    public InstantRecipe(ItemStack input, ItemStack output, ItemStack display, int uuCost, IAcceleratorFriend machine) {
        this.input = input;
        this.output = output;
        this.display = display;
        this.uuCost = uuCost;
        this.machine = machine;
    }

    // Display the output ItemStack, but notify the IAcceleratorFriend's .instantCapacity and
    // .instantProcess
    public InstantRecipe(ItemStack input, ItemStack output, int uuCost, IAcceleratorFriend machine) {
        this(input, output, null, uuCost, machine);
    }

    // Display a custom ItemStack, but don't notify anyone.
    public InstantRecipe(ItemStack input, ItemStack output, ItemStack display, int uuCost) {
        this(input, output, display, uuCost, null);
    }

    // Simplest version: Display the output ItemStack and don't notify anyone.
    public InstantRecipe(ItemStack input, ItemStack output, int uuCost) {
        this(input, output, null, uuCost, null);
    }

    public boolean validate() {
        if (input == null || input.stackSize <= 0) {
            return false;
        }

        if (output == null || output.stackSize <= 0) {
            return false;
        }

        if (uuCost < 0) {
            return false;
        }

        return true;
    }

    public int getInputSize() {
        return input.stackSize;
    }

    public int getOutputSize() {
        return output.stackSize;
    }
}
