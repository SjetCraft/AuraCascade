package com.sjet.auracascade.common.api;

import net.minecraft.world.World;

/**
 * constructor
 */
public enum IAuraColor {
    WHITE(1, 1, 1, true, true),
    YELLOW(1, 1, .1F, true, true) {
        @Override
        public float getAscentBoost(World world) {
            return 0.5F;
        }
    },
    ORANGE(1, .5F, 0, false, true) {
        @Override
        public float getRelativeMass(World world) {
            return 0;
        }
    },
    RED(1, .1F, .1F, true, true),
    GREEN(.1F, 1, .1F, true, true) {
        @Override
        public float getRelativeMass(World world) {
            return world.isDaytime() ? 2 : 0.5F;
        }
    },
    BLUE(.1F, .1F, 1, true, true) {
        @Override
        public float getAscentBoost(World world) {
            return world.isRaining() ? 4 : 0.5F;
        }
    },
    VIOLET(1, .1F, 1, true, true),
    BLACK(.1F, .1F, .1F, true, false) {
        @Override
        public float getRelativeMass(World world) {
            return 0;
        }
    };

    private float red, green, blue;
    private boolean vertical, horizontal;

    IAuraColor(float r, float g, float b, boolean vertical, boolean horizontal) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.vertical = vertical;
        this.horizontal = horizontal;
    }

    public String capitalizedName() {
        String name = super.toString().toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
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

    public boolean getVerticalTransfer() {
        return vertical;
    }

    public boolean getHorizontalTransfer() {
        return horizontal;
    }

    public float getRelativeMass(World world) {
        return 1;
    }

    public float getAscentBoost(World world) {
        return 1;
    }
}
