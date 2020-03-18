package com.sjet.auracascade.client.particles;

import com.sjet.auracascade.client.particles.auratransferparticle.AuraTransferParticleData;
import com.sjet.auracascade.client.particles.nodeconnectionparticle.NodeConnectionParticleData;
import com.sjet.auracascade.common.api.IAuraColor;
import com.sjet.auracascade.common.util.Common;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class ParticleHelper {

    /**
     * This helper function will spawn particles from the source position to the target position.
     * Intended to be used as an indicator to what other nodes the placed node connected to.
     *
     * @param world
     * @param source source block from where the particles will spawn
     * @param target target block where the particles will travel to
     */
    public static void sendConnectionParticlesToTarget(World world, BlockPos source, BlockPos target) {
        Random rand = new Random();
        //delta is the area where the particles will travel through
        double delta = Common.getDistance(source, target) * 3;
        //the deltaX,Y,Z is the control to extract the direction
        double deltaX = (target.getX() - source.getX()) / delta;
        double deltaY = (target.getY() - source.getY()) / delta;
        double deltaZ = (target.getZ() - source.getZ()) / delta;
        //0.5 is the center of the block
        double sourceX = source.getX() + 0.5;
        double sourceY = source.getY() + 0.5;
        double sourceZ = source.getZ() + 0.5;

        int density = 1;// + (auraAmount / 500);

        //spawns particles through delta-1 areas
        for (int i = 0; i < delta; i++) {

            float randomSize = 0.1f + (0.105f - 0.1f) * rand.nextFloat();

            int randomAge = 20 + (rand.nextInt(6));

            NodeConnectionParticleData particle = NodeConnectionParticleData.nodeConnectionParticle(0, 0, 0, randomSize, randomAge);
            world.addParticle(particle, sourceX, sourceY, sourceZ, deltaX * rand.nextDouble(), deltaY * rand.nextDouble(), deltaZ * rand.nextDouble());

            //moves the particle generation by delta distance
            if (deltaX != 0) {
                sourceX += deltaX;
            } else if (deltaY != 0) {
                sourceY += deltaY;
            } else if (deltaZ != 0) {
                sourceZ += deltaZ;
            }
        }
    }

    public static void transferAuraParticles(World world, BlockPos source, BlockPos target, IAuraColor color, int auraAmount) {
        Random rand = new Random();
        //delta is the area where the particles will travel through
        double delta = Common.getDistance(source, target) * 6;
        //the deltaX,Y,Z is the control to extract the direction
        double deltaX = (target.getX() - source.getX()) / delta;
        double deltaY = (target.getY() - source.getY()) / delta;
        double deltaZ = (target.getZ() - source.getZ()) / delta;
        //0.5 is the center of the block
        double sourceX = source.getX() + 0.5;
        double sourceY = source.getY() + 0.5;
        double sourceZ = source.getZ() + 0.5;

        //Aura Color
        float r = color.getRed();
        float g = color.getGreen();
        float b = color.getBlue();

        int density = 1 + (auraAmount / 1000);
        double deltaYspeed = 1.3;

        //spawns particles through delta-1 areas
        for (int i = 0; i < delta; i++) {

            for (int j = 0; j < density; j++) {
                float randomSize = 0.05f + (0.075f - 0.15f) * rand.nextFloat();
                int randomAge = 14 + (rand.nextInt(5));
                //spreads the particle source across the range
                double range = rand.nextDouble() * (0.02) * (rand.nextFloat() > 0.5 ? 1 : -1);

                //makes aura 'fall' faster
                if (deltaY < 0) {
                    deltaYspeed = 2;
                }

                AuraTransferParticleData particle = AuraTransferParticleData.auraTransferParticle(target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5, randomSize, r, g, b, randomAge);
                //addParticle(particle, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed)
                world.addParticle(particle, sourceX + range, sourceY + range, sourceZ + range, deltaX * rand.nextDouble() * 1.1, deltaY * rand.nextDouble() * deltaYspeed, deltaZ * rand.nextDouble() * 1.1);
            }

            //moves the particle generation by delta distance
            if (deltaX != 0) {
                sourceX += deltaX;
            } else if (deltaY != 0) {
                sourceY += deltaY;
            } else if (deltaZ != 0) {
                sourceZ += deltaZ;
            }
        }
    }

    public static void pumpTransferParticles(World world, BlockPos source, BlockPos target, IAuraColor color, int auraAmount) {

    }

    public static void itemBurningParticles(World world, Vec3d source) {
        for (int i = 0; i < 50; i++) {
            Random rand = new Random();
            //addParticle(particle, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed)
            world.addParticle(ParticleTypes.FLAME, source.x, source.y, source.z, (rand.nextDouble() - .5D) / 16, rand.nextDouble() / 16, (rand.nextDouble() - .5) / 16);
        }
    }

    public static void burstParticles(World world, BlockPos source) {
        for (int i = 0; i < 50; i++) {
            Random rand = new Random();
            world.addParticle(ParticleTypes.POOF, source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5, (rand.nextDouble() - .5) / 4, rand.nextDouble() / 4, (rand.nextDouble() - .5) / 4);
        }
    }
}