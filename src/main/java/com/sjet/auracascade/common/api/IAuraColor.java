package com.sjet.auracascade.common.api;

import net.minecraft.world.World;

/**
 * constructor
 */
public enum IAuraColor {
    WHITE(true, true, 1, 1, 1),
    YELLOW(true, true, 1, 1, .1F),
    ORANGE(true, false, 1, .5F, 0),
    RED(true, true, 1, .1F, .1F),
    GREEN(true, true, .1F, 1, .1F),
    BLUE(true, true, .1F, .1F, 1),
    VIOLET(true, true, 1, .1F, 1),
    BLACK(false, true, .1F, .1F, .1F);

    private boolean horizontalTransfer;
    private boolean verticalTransfer;
    private float red, green, blue;

    IAuraColor(boolean horizontalTransfer, boolean verticalTransfer, float r, float g, float b) {
        this.horizontalTransfer = horizontalTransfer;
        this.verticalTransfer = verticalTransfer;
        this.red = r;
        this.green = g;
        this.blue = b;
    }

    public String capitalizedName() {
        String name = super.toString().toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public boolean canTransferHorizontal() {
        return horizontalTransfer;
    }

    public boolean canTransferVertical() {
        return verticalTransfer;
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }
}
