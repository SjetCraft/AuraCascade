package com.sjet.auracascade.client.particles.auratransferparticle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AuraTransferParticle extends SpriteTexturedParticle {
    private double sourceX;
    private double sourceY;
    private double sourceZ;
    private double targetX;
    private double targetY;
    private double targetZ;
    private IAnimatedSprite spriteSet;

    public AuraTransferParticle(World world, double sourceX, double sourceY, double sourceZ, double targetX, double targetY, double targetZ, double xSpeed, double ySpeed, double zSpeed,
                                  float size, float red, float green, float blue, float maxAge) {
        super(world, sourceX, sourceY, sourceZ);
        motionX = xSpeed;
        motionY = ySpeed;
        motionZ = zSpeed;

        particleRed = red;
        particleGreen = green;
        particleBlue = blue;
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

        selectSpriteRandomly(spriteSet);
    }

    @Override
    public void renderParticle(IVertexBuilder p_225606_1_, ActiveRenderInfo p_225606_2_, float p_225606_3_) {
        super.renderParticle(p_225606_1_, p_225606_2_, p_225606_3_);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void setGravity(float value) {
        particleGravity = value;
    }

    // [VanillaCopy] of super, without drag when onGround is true
    @Override
    public void tick() {
        //Just in case something goes weird, we remove the particle if its been around too long.
        if (this.age++ >= this.maxAge) {
            this.setExpired();
            return;
        }

        //prevPos is used in the render. if you don't do this your particle rubber bands (Like lag in an MMO).
        //This is used because ticks are 20 per second, and FPS is usually 60 or higher.
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        //Get the current position and target of the particle
        Vec3d particlePosition = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d targetPosition = new Vec3d(targetX, targetY, targetZ);

        //The total distance between the particle and target
        double totalDistance = targetPosition.distanceTo(particlePosition);
        if (totalDistance < 0.1) {
            this.setExpired();
        }

        double speedAdjust = 4;
        double moveX = (targetX - this.posX) / speedAdjust;
        double moveY = (targetY - this.posY) / speedAdjust;
        double moveZ = (targetZ - this.posZ) / speedAdjust;

        particleAlpha -= 0.01;
        //selectSpriteWithAge(spriteSet);

        //Perform the ACTUAL move of the particle.
        this.move(moveX, moveY, moveZ);
    }
}
