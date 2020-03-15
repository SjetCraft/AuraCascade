package com.sjet.auracascade.client.particles;

import com.sjet.auracascade.client.particles.auratransferparticle.AuraTransferParticleData;
import com.sjet.auracascade.client.particles.nodeconnectionparticle.NodeConnectionParticleData;
import com.sjet.auracascade.common.api.IAuraColor;
import com.sjet.auracascade.common.util.Common;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class ParticleHelper {

    /**
     * This helper function will spawn particles from the source position to the target position.
     * Intended to be used as an indicator to what other nodes the Aura Node is connected to.
     *
     * @param world
     * @param source source block from where the particles will spawn
     * @param target target block where the particles will travel to
     */
    public static void sendConnectionParticlesToTarget(World world, BlockPos source, BlockPos target) {
        Random rand = new Random();
        //delta is the area where the particles will travel through
        double delta = Common.getDistance(source, target) * 4;
        //the deltaX,Y,Z is the control to extract the direction
        double deltaX = (target.getX() - source.getX() ) / delta;
        double deltaY = (target.getY() - source.getY() ) / delta;
        double deltaZ = (target.getZ() - source.getZ() ) / delta;
        //0.5 is the center of the block
        double sourceX = source.getX() + 0.5;
        double sourceY = source.getY() + 0.5;
        double sourceZ = source.getZ() + 0.5;
        //the first target is 'delta' distance away from the start
        double targetX = sourceX + deltaX;
        double targetY = sourceY + deltaY;
        double targetZ = sourceZ + deltaZ;

        //spawns particles through delta-1 areas
        for (int i = 0; i < delta; i++) {
            float randomSize = 0.05f + (0.125f - 0.05f) * rand.nextFloat();
            //random distribution - also includes negative numbers
            float randomDistribution = 0.08f * rand.nextFloat() * (rand.nextFloat() > 0.5 ? 0 : -1);

            NodeConnectionParticleData particle = NodeConnectionParticleData.nodeConnectionParticle(targetX + randomDistribution, targetY + randomDistribution, targetZ + randomDistribution, randomSize, 40);
            world.addParticle(particle, sourceX + randomDistribution, sourceY + randomDistribution, sourceZ + randomDistribution, deltaX * Math.random(), deltaY * Math.random(), deltaZ * Math.random());

            //moves the source and target of the particles by delta distance
            if (deltaX != 0) {
                sourceX += deltaX;
                targetX += deltaX;
            } else if (deltaY != 0) {
                sourceY += deltaY;
                targetY += deltaY;
            } else if (deltaZ != 0) {
                sourceZ += deltaZ;
                targetZ +=deltaZ;
            }
        }
    }

    public static void transferAura(World world, BlockPos source, BlockPos target, IAuraColor color, int auraAmount) {
        Random rand = new Random();
        //delta is the area where the particles will travel through
        double delta = Common.getDistance(source, target) * 6;
        //the deltaX,Y,Z is the control to extract the direction
        double deltaX = (target.getX() - source.getX() ) / delta;
        double deltaY = (target.getY() - source.getY() ) / delta;
        double deltaZ = (target.getZ() - source.getZ() ) / delta;
        //0.5 is the center of the block
        double sourceX = source.getX() + 0.5;
        double sourceY = source.getY() + 0.5;
        double sourceZ = source.getZ() + 0.5;
        //the first target is 'delta' distance away from the start
        double targetX = sourceX + deltaX;
        double targetY = sourceY + deltaY;
        double targetZ = sourceZ + deltaZ;

        //Aura Color
        float r = color.getRed();
        float g = color.getGreen();
        float b = color.getBlue();

        int density = 1 + (auraAmount / 300);

        //spawns particles through delta-1 areas
        for (int i = 0; i < delta; i++) {

            //density
            for (int j = 0; j < density; j++) {
                float randomSize = 0.04f + (0.085f - 0.15f) * rand.nextFloat();
                //random distribution - also includes negative numbers
                float randomDistribution = 0.08f * rand.nextFloat() * (rand.nextFloat() > 0.5 ? 1 : -1);

                AuraTransferParticleData particle = AuraTransferParticleData.auraTransferParticle(targetX + randomDistribution, targetY + randomDistribution, targetZ + randomDistribution, randomSize, r, g, b,30);
                world.addParticle(particle, sourceX + randomDistribution, sourceY + randomDistribution, sourceZ + randomDistribution, deltaX * Math.random(), deltaY * Math.random(), deltaZ * Math.random());
            }

            //moves the source and target of the particles by delta distance
            if (deltaX != 0) {
                sourceX += deltaX;
                targetX += deltaX;
            } else if (deltaY != 0) {
                sourceY += deltaY;
                targetY += deltaY;
            } else if (deltaZ != 0) {
                sourceZ += deltaZ;
                targetZ +=deltaZ;
            }
        }
    }
}