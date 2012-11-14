package ro.narc.liquiduu;

import net.minecraft.src.ItemStack;

public class InstantRecipe {
    public ItemStack input;
    public ItemStack output;
    public int uuCost;
    public IAcceleratorFriend machine;

    // Slightly advanced version: The machine's .instantCapacity and .instantProcess methods will
    //                            be called, to enable any custom behaviour the machine may
    //                            require.
    public InstantRecipe(ItemStack input, ItemStack output, int uuCost, IAcceleratorFriend machine) {
        this.input = input;
        this.output = output;
        this.uuCost = uuCost;
        this.machine = machine;
    }

    // Simple version: The machine will not receive .instantCapacity or .instantProcess calls.
    public InstantRecipe(ItemStack input, ItemStack output, int uuCost) {
        this(input, output, uuCost, null);
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
