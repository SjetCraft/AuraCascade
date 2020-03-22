package com.sjet.auracascade.client.particles.pumptransferparticle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PumpTransferParticle extends SpriteTexturedParticle {
    private double sourceX;
    private double sourceY;
    private double sourceZ;
    private double targetX;
    private double targetY;
    private double targetZ;
    private IAnimatedSprite spriteSet;

    public PumpTransferParticle(World world, double sourceX, double sourceY, double sourceZ, double targetX, double targetY, double targetZ, double xSpeed, double ySpeed, double zSpeed,
                                float size, float red, float green, float blue, float maxAge, IAnimatedSprite spriteSet) {
        super(world, sourceX, sourceY, sourceZ);
        motionX = xSpeed;
        motionY = ySpeed;
        motionZ = zSpeed;

        float randomColorOffset = (float) (Math.random() * 0.15D);
        red -= randomColorOffset;
        blue -= randomColorOffset;
        green -= randomColorOffset;

        setColor(red, green, blue);

        particleGravity = 0;
        this.setGravity(0f);

        this.maxAge = Math.round(maxAge);

        this.particleScale = size;
        setSize(size, size);

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.sourceZ = sourceZ;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.canCollide = false;

        this.spriteSet = spriteSet;

        this.motionX *= 0.1D;
        this.motionY *= 0.1D;
        this.motionZ *= 0.1D;
        this.motionX += motionX * 0.4D;
        this.motionY += motionY * 0.4D;
        this.motionZ += motionZ * 0.4D;

        //random movement
        motionX += 0.005 * rand.nextFloat() * (rand.nextFloat() > 0.5 ? 1 : -1);
        motionY += 0.003 * rand.nextFloat() * (rand.nextFloat() > 0.5 ? 1 : -1);
        motionZ += 0.005 * rand.nextFloat() * (rand.nextFloat() > 0.5 ? 1 : -1);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void setGravity(float value) {
        particleGravity = value;
    }

    @Override
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        //Get the current position and target of the particle
        Vec3d particlePosition = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d targetPosition = new Vec3d(targetX, targetY, targetZ);
        //The total distance between the particle and target
        double totalDistance = targetPosition.distanceTo(particlePosition);

        if (this.age++ >= this.maxAge) {
            this.setExpired();
        } else if (totalDistance < 0.2) {
            this.setExpired();
        } else {
            this.move(this.motionX, this.motionY, this.motionZ);

            //random movement
            motionX += 0.003 * rand.nextFloat() * (rand.nextFloat() > 0.5 ? 1 : -1);
            motionY += 0.001 * rand.nextFloat() * (rand.nextFloat() > 0.5 ? 1 : -1);
            motionZ += 0.003 * rand.nextFloat() * (rand.nextFloat() > 0.5 ? 1 : -1);

            //randomly age the particle
            this.age += (rand.nextFloat() > 0.5 ? 0 : 1);
        }
    }
}
