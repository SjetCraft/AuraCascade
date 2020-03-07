package com.sjet.auracascade.common.api;

import org.apache.commons.lang3.StringUtils;

public enum IAuraColor {
    WHITE,
    YELLOW,
    ORANGE,
    RED,
    GREEN,
    BLUE,
    VIOLET,
    BLACK;

    public String capitalizedName() {
        return StringUtils.capitalize(super.toString().toLowerCase());
    }
}
