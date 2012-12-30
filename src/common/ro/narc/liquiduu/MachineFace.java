package ro.narc.liquiduu;

import net.minecraftforge.common.ForgeDirection;

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

    // We use this because Enums don't seem to natively have a getValueFromOrdinal(int).
    public static final MachineFace[] VALID_FACES = MachineFace.values();

    private MachineFace(int textureIndex) {
        this.textureIndex = textureIndex;
    }

    public static MachineFace translateOrdinal(int ordinal) {
        if(ordinal >= 0 && ordinal < VALID_FACES.length) {
            return VALID_FACES[ordinal];
        }
        return None;
    }

    public static final int[][] rotationMatrices = {
        { 0, 1, 2, 3, 4, 5 },
        { 0, 1, 5, 4, 2, 3 },
        { 0, 1, 3, 2, 5, 4 },
        { 0, 1, 4, 5, 3, 2 },
    };

    public static int[] getRotationMatrix(int oldFace, int newFace) {
        ForgeDirection oldFront = ForgeDirection.getOrientation(oldFace);
        ForgeDirection newFront = ForgeDirection.getOrientation(newFace);

        if(newFront == oldFront.getRotation(ForgeDirection.DOWN)) {
            return rotationMatrices[1];
        }
        else if(newFront == oldFront.getOpposite()) {
            return rotationMatrices[2];
        }
        else if(newFront == oldFront.getRotation(ForgeDirection.UP)) {
            return rotationMatrices[3];
        }
        else {
            return rotationMatrices[0];
        }
    }
}

