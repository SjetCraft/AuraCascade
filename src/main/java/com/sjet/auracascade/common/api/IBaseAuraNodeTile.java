package com.sjet.auracascade.common.api;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public interface IBaseAuraNodeTile {

    /**
     * Algorithm to find aura nodes to be connected in each Direction
     * Should iterate through each Direction to find an IBaseAuraNodeTile
     * Should call isBlocked(BlockPos) to allow for early exit if the Direction is blocked
     * Should call isAuraTile(BlockPos) to check for an IBaseAuraNodeTile and if one is found,
     * should call connectNode(BlockPos) to connect the target node to this node
     */
    void findNodes();

    /**
     * Verifies that the target BlockPos allows aura to pass through it
     * Should allow aura to pass through transparent blocks
     * !!NOTE: Should allow IBaseAuraNodeTile TileEntities to 'pass' aura through it
     * Should call isAuraTile(BlockPos) to verify if target is IBaseAuraNodeTile
     *
     * @param target BlockPos of the target block
     * @return false if the target cannot have aura passed through it
     */
    boolean canAuraFlow(BlockPos target);

    /**
     * @param pos BlockPos of the node to verify if it is an IBaseAuraNodeTile
     * @return true if the TileEntity at the BlockPos position implements IBaseAuraNodeTile
     */
    boolean isAuraTile(BlockPos pos);

    /**
     * Connects the aura node in BlockPos to the current node
     * Should verify the validity of the node using isAuraTile(BlockPos)
     *
     * @param pos BlockPos position of the node to connect
     */
    void connectNode(BlockPos pos);

    /**
     * Removes aura from the current node.
     * If there is less aura than the amount to be removed, the aura will be set to 0
     *
     * @param color the IAuraColor of aura to be removed
     * @param auraRemove the amount of aura to be removed
     */
    void removeAura(IAuraColor color, int auraRemove);

    /**
     * To be used to add aura to a node from another node
     *
     * @param sourcePos the source of the aura to be added
     * @param color     the IAuraColor enum of the aura to be added
     * @param auraInput the amount of aura to be added
     */
    void addAura(BlockPos sourcePos, IAuraColor color, int auraInput);

    /**
     * To be used when a player adds aura to the node
     * Should call addAura(BlockPos, IAuraColor, int) to add the aura to the node
     *
     * @param player The player that added the aura
     * @param stack The aura ItemStack
     * @return true if the player is able to add the Aura to the node
     */
    boolean playerAddAura(@Nullable PlayerEntity player, ItemStack stack);

    /**
     * Used to distribute aura from one Aura Node to another
     */
    void distributeAura();

    /**
     * @param power The amount of power to be converted to auraEnergy
     */
    void receivePower(int power);

    /**
     * @param target
     * @return
     */
    boolean canTransfer(BlockPos target);

    /**
     * @param source the source which is sending aura
     * @return whether the IBaseAuraNodeTile can receive aura
     */
    boolean canReceive(BlockPos source);

    /**
     * Used for rendering information about the IBaseAuraNodeTile to the screen on mouse-over
     *
     * @param minecraft
     */
    void renderHUD(Minecraft minecraft);
}
