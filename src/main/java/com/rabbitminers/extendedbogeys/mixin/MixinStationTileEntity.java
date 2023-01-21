package com.rabbitminers.extendedbogeys.mixin;

import com.rabbitminers.extendedbogeys.mixin_interface.BlockStates;
import com.rabbitminers.extendedbogeys.mixin_interface.ICarriageBogeyStyle;
import com.simibubi.create.content.logistics.trains.*;
import com.simibubi.create.content.logistics.trains.entity.Carriage;
import com.simibubi.create.content.logistics.trains.entity.CarriageBogey;
import com.simibubi.create.content.logistics.trains.entity.CarriageContraption;
import com.simibubi.create.content.logistics.trains.entity.Train;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.StationTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Mixin(StationTileEntity.class)
public class MixinStationTileEntity {
    private CarriageContraption contraption;

    @ModifyVariable(method = "assemble", at = @At("STORE"), name = "contraption", remap = false)
    public CarriageContraption captureCarriageContraptionOnInit(CarriageContraption carriageContraption) {
        this.contraption = carriageContraption;
        return carriageContraption;
    }

    @ModifyVariable(method = "assemble", at = @At("STORE"), name = "firstBogey", remap = false)
    public CarriageBogey onFirstBogeyInit(CarriageBogey firstBogey) {
        Level level = Minecraft.getInstance().level;
        if (contraption == null || level == null)
            return firstBogey;

        if (firstBogey instanceof ICarriageBogeyStyle styledCustomBogey) {
            BlockPos firstBogeyBlockPos = contraption.anchor;
            BlockState state = level.getBlockState(firstBogeyBlockPos);
            int firstBogeyStyle = state.getValue(BlockStates.STYLE);
            styledCustomBogey.setStyle(firstBogeyStyle);
            return (CarriageBogey) styledCustomBogey;
        }

        return firstBogey;
    }

    @ModifyVariable(method = "assemble", at = @At("STORE"), name = "secondBogey", remap = false)
    public CarriageBogey onSecondBogeyInit(CarriageBogey secondBogey) {
        Level level = Minecraft.getInstance().level; // Only needs to read data so client side is safe
        if (contraption == null || level == null)
            return secondBogey;

        if (secondBogey != null) {
            BlockPos secondBogeyPos = contraption.getSecondBogeyPos();
            BlockState secondBogeyState = level.getBlockState(secondBogeyPos);
            int secondBogeyStyle = secondBogeyState.getValue(BlockStates.STYLE);
            if (secondBogey instanceof ICarriageBogeyStyle styledCustomBogey) {
                styledCustomBogey.setStyle(secondBogeyStyle);
                return (CarriageBogey) styledCustomBogey;
            }
        }

        return secondBogey;
    }
}
