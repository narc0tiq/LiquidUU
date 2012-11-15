package ro.narc.liquiduu;

import buildcraft.api.liquids.LiquidStack;
import buildcraft.api.liquids.LiquidTank;


public class LiquidUUTank extends LiquidTank {
    public LiquidUUTank(int capacity) {
        super(capacity);
    }

    public int getLiquidAmount() {
        LiquidStack liquid = this.getLiquid();

        if(liquid == null) {
            return 0;
        }

        return liquid.amount;
    }
}
