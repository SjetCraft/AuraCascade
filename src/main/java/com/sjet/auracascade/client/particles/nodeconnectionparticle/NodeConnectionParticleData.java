package com.sjet.auracascade.client.particles.nodeconnectionparticle;

import com.sjet.auracascade.client.particles.ModParticles;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

import javax.annotation.Nonnull;
import java.util.Locale;

public class NodeConnectionParticleData implements IParticleData {
    public final float size;
    public final float maxAge;
    public final double targetX;
    public final double targetY;
    public final double targetZ;

    public static NodeConnectionParticleData nodeConnectionParticle(double targetX, double targetY, double targetZ, float size, float maxAge) {
        return new NodeConnectionParticleData(targetX, targetY, targetZ, size, maxAge);
    }

    private NodeConnectionParticleData(double targetX, double targetY, double targetZ, float size, float maxAge) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.size = size;
        this.maxAge = maxAge;
    }

    @Nonnull
    @Override
    public ParticleType<NodeConnectionParticleData> getType() {
        return ModParticles.NODE_CONNECT;
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeDouble(targetX);
        buf.writeDouble(targetY);
        buf.writeDouble(targetZ);
        buf.writeFloat(size);
        buf.writeFloat(maxAge);
    }

    @Nonnull
    @Override
    public String getParameters() {
        return String.format(Locale.ROOT, "%s %.2f %.2f",
                this.getType().getRegistryName(), this.size, this.maxAge);
    }

    public static final IParticleData.IDeserializer<NodeConnectionParticleData> DESERIALIZER = new IParticleData.IDeserializer<NodeConnectionParticleData>() {
        @Nonnull
        @Override
        public NodeConnectionParticleData deserialize(@Nonnull ParticleType<NodeConnectionParticleData> type, @Nonnull StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            double targetX = reader.readDouble();
            reader.expect(' ');
            double targetY = reader.readDouble();
            reader.expect(' ');
            double targetZ = reader.readDouble();
            reader.expect(' ');
            float size = reader.readFloat();
            reader.expect(' ');
            float maxAge = reader.readFloat();

            return new NodeConnectionParticleData(targetX, targetY, targetZ, size, maxAge);
        }

        @Override
        public NodeConnectionParticleData read(@Nonnull ParticleType<NodeConnectionParticleData> type, PacketBuffer buf) {
            return new NodeConnectionParticleData(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readFloat(), buf.readFloat());
        }
    };
}
