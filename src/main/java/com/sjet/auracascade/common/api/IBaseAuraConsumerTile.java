package com.sjet.auracascade.common.api;

import net.minecraft.client.Minecraft;

public interface IBaseAuraConsumerTile {

    public int getMaxProgress();

    public int getPowerPerProgress();

    public void onUsePower();

    public boolean validItemsNearby();

    /**
     * Used for rendering information about the IBaseAuraNodeTile to the screen on mouse-over
     *
     * @param minecraft
     */
    void renderHUD(Minecraft minecraft);
}
