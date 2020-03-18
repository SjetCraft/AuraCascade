package com.sjet.auracascade.common.tiles;

import com.sjet.auracascade.client.HUDHandler;
import com.sjet.auracascade.client.particles.ParticleHelper;
import com.sjet.auracascade.common.api.IBaseAuraNodeTile;
import com.sjet.auracascade.common.util.Common;
import com.sjet.auracascade.common.api.IBaseAuraCrystalItem;
import com.sjet.auracascade.common.api.IAuraColor;
import com.sjet.auracascade.common.items.BaseAuraCrystalItem;
import com.sjet.auracascade.common.util.NBTListHelper;
import com.sjet.auracascade.common.util.NBTMapHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
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
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

import static com.sjet.auracascade.AuraCascade.MAX_DISTANCE;

public abstract class BaseAuraTile extends TileEntity implements IBaseAuraNodeTile, ITickableTileEntity {

    public static final String AURA_MAP = "aura_map";
    public static final String CONNECTED_LIST = "connected_list";
    public static final String SENT_AURA = "sent_aura";

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

    protected ArrayList<BlockPos> connectedNodesList = new ArrayList<>();
    //Using a String for the IAuraColor and Integer because Java doesn't have Tuples or Triples and saving custom data structures to NBT is a pain
    protected HashMap<BlockPos, String> sentNodesMap = new HashMap<>();

    public int auraEnergy;

    public BaseAuraTile(TileEntityType<?> type) {
        super(type);
    }

    public void findNodes() {
        connectedNodesList = new ArrayList<>();
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
        this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
    }

    @SuppressWarnings("deprecation")
    public boolean canAuraFlow(BlockPos target) {
        Block block = world.getBlockState(target).getBlock();

        //false if block is not air and if block is not transparent or an aura node
        return !block.isAir(block.getDefaultState(), world, target) &&
                !(isAuraTile(target) || !block.isSolid(block.getDefaultState()));
    }

    public boolean isAuraTile(BlockPos target) {
        return world.getTileEntity(target) instanceof IBaseAuraNodeTile;
    }

    public void connectNode(BlockPos target) {
        if (isAuraTile(target) && world.getTileEntity(target) != this) {
            BaseAuraTile otherNode = (BaseAuraTile) world.getTileEntity(target);

            //add the found node this this node's connected Nodes list
            this.connectedNodesList.add(otherNode.getPos());
            //add this node the found node's connected Nodes list
            otherNode.connectedNodesList.add(getPos());
        }
    }

    public void removeAura(IAuraColor color, int auraRemove) {
        int aura = auraMap.get(color);

        if ((aura - auraRemove) >= 0) {
            aura -= auraRemove;
        } else {
            aura = 0;
        }
        auraMap.replace(color, aura);
    }

    public void addAura(BlockPos sourcePos, IAuraColor color, int auraInput) {
        //add the aura type to the auraMap
        int newAura = auraMap.get(color);
        newAura += auraInput;
        auraMap.replace(color, newAura);

        if (this.pos.getY() < sourcePos.getY()) {
            int power = (sourcePos.getY() - this.pos.getY()) * auraInput;

            if (power > 0) {
                receivePower(power);
            }
        }
        this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
    }

    public boolean playerAddAura(@Nullable PlayerEntity player, ItemStack stack) {
        if (stack.getItem() instanceof IBaseAuraCrystalItem) {
            addAura(this.pos, ((BaseAuraCrystalItem) stack.getItem()).getColor(), ((BaseAuraCrystalItem) stack.getItem()).getAura());
            this.markDirty();
            //Check to not remove BaseAuraCrystalItem if player is in Creative
            if (!player.abilities.isCreativeMode) {
                stack.shrink(1);
            }
            return true;
        }
        return false;
    }

    /**
     * Used to distribute aura from one Aura Node to another
     */
    public void distributeAura() {
        //empty sentNodesMap
        this.sentNodesMap.clear();

        //early exit if there are no nodes to distribute Aura & if redstone is blocking the node from distributing
        if (connectedNodesList.isEmpty() || this.world.isBlockPowered(this.pos)) {
            return;
        }

        int totalWeight = 0;

        for (BlockPos nodes : connectedNodesList) {
            if (canTransfer(nodes)) {
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

                        int auraToSend = (int) (auraHere * factor);
                        if (diff > 25 && auraToSend > 0) {
                            transferAura(target, color, auraToSend);
                        }
                    }
                }
            }
        }
        this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
    }

    public void transferAura(BlockPos target, IAuraColor color, int aura) {
        //only transfer aura if this node has enough to send
        if (aura <= this.auraMap.get(color)) {
            BaseAuraTile targetNode = (BaseAuraTile) world.getTileEntity(target);
            targetNode.addAura(this.pos, color, aura);
            this.removeAura(color, aura);
            // add node to the sentNodesMap to use for rendering, ";" for split
            sentNodesMap.put(target, "" + color.name() + ";" + aura);
        }
    }

    public void receivePower(int power) {
        auraEnergy += power;
    }

    /**
     * @param target
     * @return true if the target node is below or on the same Y level as this current node
     */
    public boolean canTransfer(BlockPos target) {
        boolean isLower = target.getY() < this.pos.getY();
        boolean isSame = target.getY() == this.pos.getY();

        return world.getTileEntity(target) instanceof BaseAuraTile && (isSame || isLower) && ((BaseAuraTile) world.getTileEntity(target)).canReceive(this.pos);
    }

    public boolean canReceive(BlockPos source) {
        return true;
    }

    /**
     * @param target
     * @return
     */
    public double getWeight(BlockPos target) {
        return Math.pow(20 - Common.getDistance(this.pos, target), 2);
    }

    /**
     * Intended to be used as an indicator to what other nodes this BauseAuraTile is connected to.
     */
    @OnlyIn(Dist.CLIENT)
    public void connectParticles() {
        for (BlockPos target : connectedNodesList) {
            ParticleHelper.sendConnectionParticlesToTarget(world, this.pos, target);
        }
    }

    /**
     * Intended to be used as an indicator to when and how much aura is being transferred from this node
     */
    @OnlyIn(Dist.CLIENT)
    public void transferAuraParticles() {
        for (Map.Entry<BlockPos, String> target : sentNodesMap.entrySet()) {
            String array[] = target.getValue().split(";");
            ParticleHelper.transferAuraParticles(this.world, this.pos, target.getKey(), IAuraColor.valueOf(array[0]), Integer.parseInt(array[1]));
        }
    }

    /**
     * Used to render the amount of Aura on the screen when the cursor is on this node
     *
     * @param minecraft
     */
    @OnlyIn(Dist.CLIENT)
    public void renderHUD(Minecraft minecraft) {
        ArrayList<String> list = new ArrayList<>();

        for (IAuraColor color : IAuraColor.values()) {
            int auraAmount = auraMap.get(color);
            if (auraAmount > 0) {
                list.add(color.capitalizedName() + ": " + auraAmount);
            }
        }
        if (list.size() == 0) {
            list.add("No Aura");
        }

        HUDHandler.printAuraOnScreen(minecraft, list);
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        auraEnergy = tag.getInt("auraEnergy");

        auraMap.clear();
        this.auraMap = (HashMap<IAuraColor, Integer>) AURA_MAP_NBT.read(tag);
        connectedNodesList.clear();
        this.connectedNodesList = (ArrayList<BlockPos>) CONNECTED_LIST_NBT.read(tag);
        sentNodesMap.clear();
        this.sentNodesMap = (HashMap<BlockPos, String>) SENT_AURA_NBT.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        AURA_MAP_NBT.write(auraMap, tag);
        CONNECTED_LIST_NBT.write(connectedNodesList, tag);
        SENT_AURA_NBT.write(sentNodesMap, tag);
        tag.putInt("auraEnergy", auraEnergy);
        return super.write(tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
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
        return new SUpdateTileEntityPacket(pos, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        this.read(packet.getNbtCompound());
    }

    private NBTListHelper<BlockPos> CONNECTED_LIST_NBT = new NBTListHelper<BlockPos>(
            CONNECTED_LIST,
            (nbt, pos) -> nbt.put("connected_node", NBTUtil.writeBlockPos(pos)),
            nbt -> NBTUtil.readBlockPos(nbt.getCompound("connected_node"))
    );

    private NBTMapHelper<IAuraColor, Integer> AURA_MAP_NBT = new NBTMapHelper<IAuraColor, Integer>(
            AURA_MAP,
            (nbt, auraColor) -> nbt.putString("aura_color", auraColor.name()),
            nbt -> IAuraColor.valueOf(nbt.getString("aura_color")),
            (nbt, auraAmount) -> nbt.putInt("aura_amount_now", (int) auraAmount),
            nbt -> nbt.getInt("aura_amount_now")
    );

    private NBTMapHelper<BlockPos, String> SENT_AURA_NBT = new NBTMapHelper<BlockPos, String>(
            SENT_AURA,
            (nbt, pos) -> nbt.put("position", NBTUtil.writeBlockPos(pos)),
            nbt -> NBTUtil.readBlockPos(nbt.getCompound("position")),
            (nbt, colorAndAmount) -> nbt.putString("color_and_amount", colorAndAmount),
            nbt -> nbt.getString("color_and_amount")
    );
}
