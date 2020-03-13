package com.sjet.auracascade.common.api;

/**
 * constructor
 */
public enum IAuraColor {
    WHITE(true, true),
    YELLOW(true, true),
    ORANGE(true, false),
    RED(true, true),
    GREEN(true, true),
    BLUE(true, true),
    VIOLET(true, true),
    BLACK(false, true);

    private boolean horizontalTransfer;
    private boolean verticalTransfer;

    IAuraColor(boolean horizontalTransfer, boolean verticalTransfer) {
        this.horizontalTransfer = horizontalTransfer;
        this.verticalTransfer = verticalTransfer;
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
}
