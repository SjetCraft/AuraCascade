package com.sjet.auracascade.client.particles.pumptransferparticle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sjet.auracascade.client.particles.ParticleRegistry;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

import javax.annotation.Nonnull;
import java.util.Locale;

public class PumpTransferParticleData implements IParticleData {
    public final float size;
    public final float r, g, b;
    public final float maxAge;
    public final double targetX;
    public final double targetY;
    public final double targetZ;

    public static PumpTransferParticleData pumpTransferParticle(double targetX, double targetY, double targetZ, float size, float r, float g, float b, float maxAge) {
        return new PumpTransferParticleData(targetX, targetY, targetZ, size, r, g, b, maxAge);
    }

    private PumpTransferParticleData(double targetX, double targetY, double targetZ, float size, float r, float g, float b, float maxAge) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.size = size;
        this.r = r;
        this.g = g;
        this.b = b;
        this.maxAge = maxAge;
    }

    @Nonnull
    @Override
    public ParticleType<PumpTransferParticleData> getType() {
        return ParticleRegistry.PUMP_TRANSFER;
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeDouble(targetX);
        buf.writeDouble(targetY);
        buf.writeDouble(targetZ);
        buf.writeFloat(size);
        buf.writeFloat(r);
        buf.writeFloat(g);
        buf.writeFloat(b);
        buf.writeFloat(maxAge);
    }

    @Nonnull
    @Override
    public String getParameters() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f",
                this.getType().getRegistryName(), this.size, this.r, this.g, this.b, this.maxAge);
    }

    public static final IDeserializer<PumpTransferParticleData> DESERIALIZER = new IDeserializer<PumpTransferParticleData>() {
        @Nonnull
        @Override
        public PumpTransferParticleData deserialize(@Nonnull ParticleType<PumpTransferParticleData> type, @Nonnull StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            double targetX = reader.readDouble();
            reader.expect(' ');
            double targetY = reader.readDouble();
            reader.expect(' ');
            double targetZ = reader.readDouble();
            reader.expect(' ');
            float size = reader.readFloat();
            reader.expect(' ');
            float r = reader.readFloat();
            reader.expect(' ');
            float g = reader.readFloat();
            reader.expect(' ');
            float b = reader.readFloat();
            reader.expect(' ');
            float maxAge = reader.readFloat();

            return new PumpTransferParticleData(targetX, targetY, targetZ, size, r, g, b, maxAge);
        }

        @Override
        public PumpTransferParticleData read(@Nonnull ParticleType<PumpTransferParticleData> type, PacketBuffer buf) {
            return new PumpTransferParticleData(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
        }
    };
}
