package com.sjet.auracascade.client;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class HUDHelper {

    public static void printTextOnScreen(Minecraft minecraft, ArrayList<String> list) {
        int x = minecraft.getMainWindow().getScaledWidth() / 2;
        int y = minecraft.getMainWindow().getScaledHeight() / 2;
        int lineSpacing = 13;

        //centers the text vertically
        if(list.size() > 1) {
            y -= (list.size() * lineSpacing) / 2;
        }

        //prints the list to the screen
        for(String print: list){
            minecraft.fontRenderer.drawStringWithShadow(print, x + 13, y, 0xFFFFFF);
            y += lineSpacing;
        }
    }
}
