package ro.narc.liquiduu;

public enum MachineFace {
    AcceleratorFront (BlockGeneric.TI_ACCELERATOR_FRONT),
    AcceleratorSide  (BlockGeneric.TI_ACCELERATOR_SIDE),
    None             (BlockGeneric.TI_MACHINEFACE_NONE),
    InputEU          (BlockGeneric.TI_MACHINEFACE_EU_IN),
    OutputEU         (BlockGeneric.TI_MACHINEFACE_EU_OUT),
    Water            (BlockGeneric.TI_MACHINEFACE_WATER),
    ElectricWater    (BlockGeneric.TI_MACHINEFACE_EWATER),
    ElectrolyzerFront(BlockGeneric.TI_ELECTROLYZER_FRONT);

    public final int textureIndex;

    private MachineFace(int textureIndex) {
        this.textureIndex = textureIndex;
    }
}

