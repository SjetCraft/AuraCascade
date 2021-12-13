package com.sjet.auracascade.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.sjet.auracascade.common.tiles.node.AuraNodePedestalTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.MathHelper;
import net.minecraft.item.ItemStack;

public class AuraNodePedestalRenderer extends TileEntityRenderer<AuraNodePedestalTile> {

    public AuraNodePedestalRenderer(TileEntityRendererDispatcher manager) {
        super(manager);
    }

    @Override
    public void render(AuraNodePedestalTile pedestal, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
        ItemStack stack = pedestal.getItem();

        if (!stack.isEmpty()) {
            matrixStack.push();
            float yDiff = MathHelper.sin((System.currentTimeMillis() % 86400000) / 1000F) * 0.1F + 0.1F;
            matrixStack.translate(0.5D, 0.9D + yDiff, 0.5D);
            float f3 = ((System.currentTimeMillis() % 86400000) / 2000F) * (180F / (float) Math.PI);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(f3));
            matrixStack.scale(0.75F, 0.75F, 0.75F);
            Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND, light, OverlayTexture.NO_OVERLAY, matrixStack, buffer);
            matrixStack.pop();
        }
    }
}
