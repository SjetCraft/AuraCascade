package com.sjet.auracascade.common.tiles;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sjet.auracascade.common.api.IBaseAuraCrystalItem;
import com.sjet.auracascade.common.api.IBaseAuraNode;
import com.sjet.auracascade.common.api.IAuraColor;
import com.sjet.auracascade.common.items.BaseAuraCrystalItem;
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
import java.util.Map;

import static com.sjet.auracascade.AuraCascade.MAX_DISTANCE;

public abstract class BaseAuraTile extends TileEntity implements IBaseAuraNode, ITickableTileEntity {

    /**
     * Using a HashMap instead of EnumMap as Gson doesn't like to serialize/deserialize the EnumMap :(
     */
    HashMap<IAuraColor, Integer> auraMap = new HashMap<IAuraColor, Integer>() {
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

    public BaseAuraTile(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        Gson gson = new Gson();
        String jsonString;

        auraEnergy = tag.getInt("energy");
        jsonString = tag.getString("auraMap");

        Type type = new TypeToken<HashMap<IAuraColor, Integer>>(){}.getType();
        HashMap<IAuraColor, Integer> clonedMap = gson.fromJson(jsonString, type);
        auraMap = clonedMap;
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

    public void findNodes() {
        if (!world.isRemote) {
            connectedNodes = new LinkedList<BlockPos>();

            //search for connected nodes in each direction
            for (Direction direction : Direction.values()) {
                boolean blocked = false; //allows for early exit of search
                for (int i = 1; i <= MAX_DISTANCE && !blocked; i++) {
                    BlockPos targetBlock = getPos().offset(direction, i);
                    //if block is not transparent/air stop checking in this direction
                    if (isBlocked(targetBlock)) {
                        blocked = true;
                    }
                    //if block is an AuraTile, connect and stop checking in this direction
                    if (isAuraTile(targetBlock)) {
                        connectNode(targetBlock);
                        blocked = true;
                    }
                }
            }
        }
        this.markDirty();
    }

    public boolean isBlocked(BlockPos target) {
        Block block = world.getBlockState(target).getBlock();

        //false if block is not air and if block is not transparent or an aura node
        return !block.isAir(block.getDefaultState(), world, target) &&
                !(isAuraTile(target) || !block.isSolid(block.getDefaultState()));
    }

    public boolean isAuraTile(BlockPos pos) {
        return world.getTileEntity(pos) instanceof IBaseAuraNode;
    }

    public void connectNode(BlockPos pos) {
        if (isAuraTile(pos) && world.getTileEntity(pos) != this) {
            AuraNodeTile otherNode = (AuraNodeTile) world.getTileEntity(pos);

            //add the found node this this node's connected Nodes list
            this.connectedNodes.add(otherNode.getPos());
            //add this node the found node's connected Nodes list
            otherNode.connectedNodes.add(getPos());
        }
    }

    public void addAura(BlockPos sourcePos, IAuraColor color, int aura) {
        //add the aura type to the auraMap
        int newAura = auraMap.get(color);
        newAura += aura;
        auraMap.replace(color, newAura);

        this.markDirty();
    }

    public boolean playerAddAura(@Nullable PlayerEntity player, ItemStack stack) {
        if (stack.getItem() instanceof IBaseAuraCrystalItem) {
            addAura(this.pos, ((BaseAuraCrystalItem) stack.getItem()).getColor(), ((BaseAuraCrystalItem) stack.getItem()).getAura());
            //Check to not remove BaseAuraCrystalItem if player is in Creative
            if (!player.abilities.isCreativeMode) {
                stack.shrink(1);
            }
            return true;
        }
        return false;
    }
    /**
     * Used to render the amount of Aura on the screen
     *
     * @param mc
     */
    @OnlyIn(Dist.CLIENT)
    public void renderHUD(Minecraft mc) {
        String output = "";

        for (Map.Entry<IAuraColor, Integer> entry : auraMap.entrySet()) {
            if(entry.getValue() != 0) {
                if (output.length() != 0) {
                    output += "\n";
                }
                output += entry.getKey().capitalizedName() + ": " + entry.getValue();
            }
        }
        if ( output.length() == 0 ) {
            output = "No Aura";
        }

        int width = mc.fontRenderer.getStringWidth(output) / 2;
        int x = (mc.getMainWindow().getScaledWidth() / 2) - width;
        int y = mc.getMainWindow().getScaledHeight() / 2;

        mc.fontRenderer.drawStringWithShadow(output, x, y + 5, 0xFFFFFF);
    }
}
