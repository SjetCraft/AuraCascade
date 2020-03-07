package com.sjet.auracascade.common.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public interface IBaseAuraNode {

    /**
     * Algorithm to find aura nodes to be connected in each Direction
     * Should iterate through each Direction to find an IBaseAuraNode
     * Should call isBlocked(BlockPos) to allow for early exit if the Direction is blocked
     * Should call isAuraTile(BlockPos) to check for an IBaseAuraNode and if one is found,
     * should call connectNode(BlockPos) to connect the target node to this node
     */
    void findNodes();

    /**
     * Verifies that the target BlockPos allows aura to pass through it
     * Should allow aura to pass through transparent blocks
     * !!NOTE: Should allow IBaseAuraNode TileEntities to 'pass' aura through it
     * Should call isAuraTile(BlockPos) to verify if target is IBaseAuraNode
     *
     * @param target BlockPos of the target block
     * @return false if the target cannot have aura passed through it
     */
    boolean isBlocked(BlockPos target);

    /**
     * @param pos BlockPos of the node to verify if it is an IBaseAuraNode
     * @return true if the TileEntity at the BlockPos position implements IBaseAuraNode
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
     * To be used to add aura to a node from another node
     *
     * @param sourcePos the source of the aura to be added
     * @param color     the IAuraColor enum of the aura to be added
     * @param aura      the amount of aura to be added
     */
    void addAura(BlockPos sourcePos, IAuraColor color, int aura);

    /**
     * To be used when a player adds aura to the node
     * Should call addAura(BlockPos, IAuraColor, int) to add the aura to the node
     *
     * @param player The player that added the aura
     * @param stack The aura ItemStack
     * @return true if the player is able to add the Aura to the node
     */
    boolean playerAddAura(@Nullable PlayerEntity player, ItemStack stack);
}
