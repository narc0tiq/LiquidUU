package ro.narc.liquiduu;

import net.minecraftforge.common.ForgeDirection;

public enum MachineFace {
    AcceleratorFront          (0),
    AcceleratorSide           (1),
    ElectrolyzerIdle          (2),
    ElectrolyzerElectrolyzing (3),
    ElectrolyzerDelectrolyzing(4),
    None                      (16),
    InputEU                   (17),
    OutputEU                  (18),
    InputGeneric              (19),
    OutputGeneric             (20),
    Water                     (19),
    ElectricWater             (21),
    Broken                    (255);
    // As of v0.8 this is how far we've gotten. Please don't reorder the entries above until v0.9.

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

