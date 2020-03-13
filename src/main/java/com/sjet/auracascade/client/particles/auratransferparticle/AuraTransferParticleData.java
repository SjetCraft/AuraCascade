package com.sjet.auracascade.client.particles.auratransferparticle;

import com.sjet.auracascade.client.particles.ModParticles;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

import javax.annotation.Nonnull;
import java.util.Locale;

public class AuraTransferParticleData implements IParticleData {
    public final float size;
    public final float r, g, b;
    public final float maxAge;
    public final double targetX;
    public final double targetY;
    public final double targetZ;

    public static AuraTransferParticleData size(double targetX, double targetY, double targetZ, float size) {
        return new AuraTransferParticleData(targetX, targetY, targetZ, size, 1F, 1F, 1F, 20);
    }

    public static AuraTransferParticleData sizeMaxAge(double targetX, double targetY, double targetZ, float size, float maxAge) {
        return new AuraTransferParticleData(targetX, targetY, targetZ, size, 1F, 1F, 1F, maxAge);
    }

    public static AuraTransferParticleData nodeconnectionparticle(double targetX, double targetY, double targetZ, float size, float r, float g, float b, float maxAge) {
        return new AuraTransferParticleData(targetX, targetY, targetZ, size, r, g, b, maxAge);
    }

    private AuraTransferParticleData(double targetX, double targetY, double targetZ, float size, float r, float g, float b, float maxAge) {
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
    public ParticleType<AuraTransferParticleData> getType() {
        return ModParticles.AURA_TRANSFER;
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

    public static final IParticleData.IDeserializer<AuraTransferParticleData> DESERIALIZER = new IParticleData.IDeserializer<AuraTransferParticleData>() {
        @Nonnull
        @Override
        public AuraTransferParticleData deserialize(@Nonnull ParticleType<AuraTransferParticleData> type, @Nonnull StringReader reader) throws CommandSyntaxException {
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

            return new AuraTransferParticleData(targetX, targetY, targetZ, size, r, g, b, maxAge);
        }

        @Override
        public AuraTransferParticleData read(@Nonnull ParticleType<AuraTransferParticleData> type, PacketBuffer buf) {
            return new AuraTransferParticleData(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
        }
    };
}
