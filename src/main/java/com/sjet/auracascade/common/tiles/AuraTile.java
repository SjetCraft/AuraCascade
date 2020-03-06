package com.sjet.auracascade.common.tiles;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sjet.auracascade.common.api.IAuraColor;
import com.sjet.auracascade.common.items.AuraCrystalItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;

public abstract class AuraTile extends TileEntity implements ITickableTileEntity {

    protected HashMap<IAuraColor, Integer> auraMap = new HashMap<IAuraColor, Integer>() {
        {
            put(IAuraColor.WHITE, 0);
            put(IAuraColor.YELLOW, 0);
            put(IAuraColor.ORANGE, 0);
            put(IAuraColor.RED, 0);
            put(IAuraColor.GREEN, 0);
            put(IAuraColor.BLUE, 0);
            put(IAuraColor.VIOLET, 0);
            put(IAuraColor.BLACK, 0);
        }
    };

    protected LinkedList<BlockPos> connectedNodes = new LinkedList<BlockPos>();
    protected int auraEnergy;

    public AuraTile(TileEntityType<?> type) {
        super(type);
    }

    public void findNodes() {
        if (!world.isRemote) {
            connectedNodes = new LinkedList<BlockPos>();

            //search for connected nodes in each direction
            for (Direction direction : Direction.values()) {
                boolean blocked = false; //allows for early exit of search
                for (int i = 1; i <= 15 && !blocked; i++) {
                    BlockPos targetBlock = getPos().offset(direction, i);
                    //if block is not transparent/air stop checking in this direction
                    if (isBlocked(targetBlock)) {
                        blocked = true;
                    }
                    //if block is an AuraTile, connect and stop checking in this direction
                    if (isAuraTile(targetBlock)) {
                        connect(targetBlock);
                        blocked = true;
                    }
                }
            }
        }
        this.markDirty();
    }

    public void connect(BlockPos pos) {
        if (isAuraTile(pos) && world.getTileEntity(pos) != this) {
            AuraNodeTile otherNode = (AuraNodeTile) world.getTileEntity(pos);

            //add the found node this this node's connected Nodes list
            this.connectedNodes.add(otherNode.getPos());
            //add this node the found node's connected Nodes list
            otherNode.connectedNodes.add(getPos());
        }
    }

    public boolean isAuraTile(BlockPos pos) {
        return world.getTileEntity(pos) instanceof AuraNodeTile;
    }

    /**
     * @param target
     * @return false if the target cannot have aura passed through it
     */
    public boolean isBlocked(BlockPos target) {
        Block block = world.getBlockState(target).getBlock();

        //false if block is not air and if block is not transparent or an aura node
        return !block.isAir(block.getDefaultState(), world, target) &&
                !(isAuraTile(target) || !block.isSolid(block.getDefaultState()));
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        Gson gson = new Gson();
        String jsonString;

        auraEnergy = tag.getInt("energy");
        jsonString = tag.getString("auraMap");

        Type type = new TypeToken<HashMap<IAuraColor, Integer>>() {
        }.getType();
        auraMap = gson.fromJson(jsonString, type);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(new HashMap(auraMap));

        tag.putInt("energy", auraEnergy);
        tag.putString("auraMap", jsonString);

        return super.write(tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        read(tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
        handleUpdateTag(packet.getNbtCompound());

        BlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
    }

    /**
     * Used to render the amount of Aura on the screen
     *
     * @param mc
     */
    @OnlyIn(Dist.CLIENT)
    public void renderHUD(Minecraft mc) {
        String auraWhiteString = "Aura: " + auraMap.get(IAuraColor.WHITE);
        int width = 16 + mc.fontRenderer.getStringWidth(auraWhiteString) / 2;
        int x = mc.getMainWindow().getScaledWidth() / 2 - width;
        int y = mc.getMainWindow().getScaledHeight() / 2 + 50;

        mc.fontRenderer.drawStringWithShadow(auraWhiteString, x + 20, y + 5, 0xFFFFFF);
    }

    /**
     * To be used to add aura to a node from another node
     *
     * @param sourcePos the source of the aura to be added
     * @param color     the IAuraColor enum of the aura to be added
     * @param aura      the amount of aura to be added
     */
    public void addAura(BlockPos sourcePos, IAuraColor color, int aura) {
        //add the aura type to the auraMap
        int newAura = auraMap.get(color);
        newAura += aura;
        auraMap.replace(color, newAura);

        this.markDirty();
    }

    /**
     * To be used when a player adds aura to the node
     *
     * @param player
     * @param stack
     * @return
     */
    public boolean playerAddAura(@Nullable PlayerEntity player, ItemStack stack) {
        if (stack.getItem() instanceof AuraCrystalItem) {
            addAura(this.pos, ((AuraCrystalItem) stack.getItem()).getColor(), ((AuraCrystalItem) stack.getItem()).getAura());
            if (!player.abilities.isCreativeMode) {
                stack.shrink(1);
            }
            return true;
        }
        return false;
    }
}
