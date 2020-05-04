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
    public final float maxAge;
    public final double targetX;
    public final double targetY;
    public final double targetZ;

    public static PumpTransferParticleData pumpTransferParticle(double targetX, double targetY, double targetZ, float size, float maxAge) {
        return new PumpTransferParticleData(targetX, targetY, targetZ, size, maxAge);
    }

    private PumpTransferParticleData(double targetX, double targetY, double targetZ, float size, float maxAge) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.size = size;
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
        buf.writeFloat(maxAge);
    }

    @Nonnull
    @Override
    public String getParameters() {
        return String.format(Locale.ROOT, "%s %.2f %.2f",
                this.getType().getRegistryName(), this.size, this.maxAge);
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
            reader.expect(' ');
            float maxAge = reader.readFloat();

            return new PumpTransferParticleData(targetX, targetY, targetZ, size, maxAge);
        }

        @Override
        public PumpTransferParticleData read(@Nonnull ParticleType<PumpTransferParticleData> type, PacketBuffer buf) {
            return new PumpTransferParticleData(buf.readDouble(), buf.readDouble(), buf.readDouble(),buf.readFloat(), buf.readFloat());
        }
    };
}
