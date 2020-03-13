package com.sjet.auracascade.common.tiles;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sjet.auracascade.client.particles.ParticleHelper;
import com.sjet.auracascade.common.util.Common;
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
import java.util.ArrayList;
import java.util.Map;

import static com.sjet.auracascade.AuraCascade.MAX_DISTANCE;

public abstract class BaseAuraTile extends TileEntity implements IBaseAuraNode, ITickableTileEntity {

    /**
     * Using a HashMap instead of EnumMap as Gson doesn't like to serialize/deserialize EnumMap :(
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

    protected ArrayList<BlockPos> connectedNodesList = new ArrayList<BlockPos>();
    protected ArrayList<BlockPos> sentNodesList = new ArrayList<BlockPos>();
    public int auraEnergy;

    public BaseAuraTile(TileEntityType<?> type) {
        super(type);
    }

    public void findNodes() {
        connectedNodesList = new ArrayList<BlockPos>();
        //search for connected nodes in each direction
        for (Direction direction : Direction.values()) {
            boolean blocked = false; //allows for early exit of search
            for (int i = 1; i <= MAX_DISTANCE && !blocked; i++) {
                BlockPos targetBlock = getPos().offset(direction, i);
                //if block is not transparent/air stop checking in this direction
                if (canAuraFlow(targetBlock)) {
                    blocked = true;
                }
                //if block is an AuraTile, connect and stop checking in this direction
                if (isAuraTile(targetBlock)) {
                    connectNode(targetBlock);
                    blocked = true; //aura can no longer pass through this block as it is a terminating node
                }
            }
        }
        this.markDirty();
    }

    /**
     * Will not check for target being null
     *
     * @param target BlockPos of the target block
     * @return true if the current node con connect
     */
    public boolean canAuraFlow(BlockPos target) {
        Block block = world.getBlockState(target).getBlock();

        //false if block is not air and if block is not transparent or an aura node
        return !block.isAir(block.getDefaultState(), world, target) &&
                !(isAuraTile(target) || !block.isSolid(block.getDefaultState()));
    }

    public boolean isAuraTile(BlockPos target) {
        return world.getTileEntity(target) instanceof IBaseAuraNode;
    }

    public void connectNode(BlockPos target) {
        if (isAuraTile(target) && world.getTileEntity(target) != this) {
            AuraNodeTile otherNode = (AuraNodeTile) world.getTileEntity(target);

            //add the found node this this node's connected Nodes list
            this.connectedNodesList.add(otherNode.getPos());
            //add this node the found node's connected Nodes list
            otherNode.connectedNodesList.add(getPos());
        }
    }

    private void removeAura(IAuraColor color, int auraRemove) {
        int aura = auraMap.get(color);

        if ((aura - auraRemove) >= 0) {
            aura -= auraRemove;
        } else {
            aura = 0;
        }
        auraMap.replace(color, aura);
        this.markDirty();
    }

    public void addAura(BlockPos sourcePos, IAuraColor color, int auraInput) {
        //add the aura type to the auraMap
        int newAura = auraMap.get(color);
        newAura += auraInput;
        auraMap.replace(color, newAura);

        if (color.canTransferVertical() && this.pos.getY() < sourcePos.getY()) {
            switch (color) {
                default:
                    auraEnergy += auraInput;
            }
        }
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

    public void distributeAura() {
        //early exit if there are no nodes to distribute Aura & if redstone is blocking the node from distributing
        if (connectedNodesList.isEmpty() && !world.isRemote && !world.isBlockPowered(this.pos)) {
            return;
        }

        int totalWeight = 0;

        for(BlockPos nodes : connectedNodesList) {
            if( canTransfer(nodes) ) {
                totalWeight += getWeight(nodes);
            }
        }

        //Add a balance so the node doesn't completely discharge
        totalWeight += (20 * 20);

        //iterate over the aura
        for (Map.Entry<IAuraColor, Integer> colorList : auraMap.entrySet()) {
            IAuraColor color = colorList.getKey();
            if (colorList.getValue() > 0) {
                //iterate over each connected node
                for (BlockPos target : connectedNodesList) {
                    BaseAuraTile targetNode = (BaseAuraTile) world.getTileEntity(target);
                    double factor = getWeight(target) / totalWeight;

                    if (canTransfer(target)) {
                        int auraHere = colorList.getValue();
                        int auraThere = targetNode.auraMap.get(color);
                        int diff = Math.abs(auraHere - auraThere);

                        if (diff > 25) {
                            int auraToSend = (int) (auraHere * factor);
                            transferAura(target, color, auraToSend);
                            // add node to the sentNodesList to use for rendering
                            sentNodesList.add(target);
                        }
                    }
                }
            }
        }

        this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
        this.markDirty();
    }

    public void transferAura(BlockPos target, IAuraColor color, int aura) {
        //only transfer aura if this node has enough to send
        if (aura <= this.auraMap.get(color)) {
            BaseAuraTile targetNode = (BaseAuraTile) world.getTileEntity(target);
            targetNode.addAura(this.pos, color, aura);
            this.removeAura(color, aura);
            sentNodesList.add(target);
        }
    }

    public boolean canTransfer(BlockPos target) {
        return pos.getY() > target.getY() || pos.getY() == target.getY();
    }

    /**
     * @param target
     * @return
     */
    public double getWeight(BlockPos target) {
        return Math.pow(10 - Common.getDistance(this.pos, target), 2);
    }

    /**
     * Intended to be used as an indicator to what other nodes this BauseAuraTile is connected to.
     */
    @OnlyIn(Dist.CLIENT)
    public void connectParticles() {
        for(BlockPos target: connectedNodesList) {
            ParticleHelper.sendConnectionParticlesToTarget(world, this.pos, target);
        }
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
            if (entry.getValue() != 0) {
                if (output.length() != 0) {
                    output += "\n";
                }
                output += entry.getKey().capitalizedName() + ": " + entry.getValue();
            }
        }
        if (output.length() == 0) {
            output = "No Aura";
        }

        int width = mc.fontRenderer.getStringWidth(output) / 2;
        int x = (mc.getMainWindow().getScaledWidth() / 2) - width;
        int y = mc.getMainWindow().getScaledHeight() / 2;

        mc.fontRenderer.drawStringWithShadow(output, x, y + 5, 0xFFFFFF);
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        auraEnergy = tag.getInt("energy");

        //deserialize auraMap
        Gson auraNBT = new Gson();
        String auraJSON;
        auraJSON = tag.getString("auraMap");

        Type auraType = new TypeToken<HashMap<IAuraColor, Integer>>() {}.getType();
        HashMap<IAuraColor, Integer> auraClonedMap = auraNBT.fromJson(auraJSON, auraType);
        auraMap = auraClonedMap;

        //deserialize connectedNodesList
        Gson nodeNBT = new Gson();
        String nodeJSON;
        nodeJSON = tag.getString("connectedNodes");

        Type nodeType = new TypeToken<ArrayList<BlockPos>>() {}.getType();
        ArrayList<BlockPos> nodeClonedList = nodeNBT.fromJson(nodeJSON, nodeType);
        connectedNodesList = nodeClonedList;

        //deserialize connectedNodesList
        Gson sNodeNBT = new Gson();
        String sNodeJSON;
        sNodeJSON = tag.getString("sentNodes");

        Type sNodeType = new TypeToken<ArrayList<BlockPos>>() {}.getType();
        ArrayList<BlockPos> sNodeClonedList = sNodeNBT.fromJson(sNodeJSON, sNodeType);
        sentNodesList = sNodeClonedList;

    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        //serialize auraMap
        Gson auraNBT = new Gson();
        String auraJSON = auraNBT.toJson(new HashMap(auraMap));
        tag.putString("auraMap", auraJSON);

        //serialize connectedNodesList
        Gson nodeNBT = new Gson();
        String nodeJSON = nodeNBT.toJson(new ArrayList(connectedNodesList));
        tag.putString("connectedNodes", nodeJSON);

        //serialize connectedNodesList
        Gson sNodeNBT = new Gson();
        String sNodeJSON = sNodeNBT.toJson(new ArrayList(sentNodesList));
        tag.putString("sentNodes", sNodeJSON);


        tag.putInt("energy", auraEnergy);

        return super.write(tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = new CompoundNBT();
        write(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundNBT nbt) {
        read(nbt);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        write(nbt);
        return new SUpdateTileEntityPacket(pos, -999, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
        handleUpdateTag(packet.getNbtCompound());

        BlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
    }
}
