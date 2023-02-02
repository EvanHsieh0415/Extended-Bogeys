package com.rabbitminers.extendedbogeys.bogey.styles;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rabbitminers.extendedbogeys.bogey.util.LanguageKey;
import com.rabbitminers.extendedbogeys.index.BogeyPartials;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.content.logistics.trains.entity.BogeyInstance;
import com.simibubi.create.content.logistics.trains.track.StandardBogeyBlock;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class FourWheelBogey implements IBogeyStyle {
    @Override
    public String getStyleName() {
        return LanguageKey.translateDirect("bogeys.styles.fourwheel").getString();
    }

    private ModelData frame;
    private ModelData[] wheels;
    private ModelData drivePin;
    private ModelData driveRod;
    private ModelData connectingRod;

    @Override
    public List<ModelData> getAllCustomModelComponents() {
        List<ModelData> modelData = new ArrayList<>();
        modelData.add(frame);
        modelData.add(drivePin);
        modelData.add(driveRod);
        modelData.addAll(List.of(wheels));
        modelData.add(connectingRod);
        return modelData;
    }

    @Override
    public void registerSmallBogeyModelData(MaterialManager materialManager) {
        frame = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.FOUR_WHEEL_DRIVE_FRAME)
                .createInstance();

        drivePin = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.FOUR_WHEEL_DRIVE_PIN)
                .createInstance();

        driveRod = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.FOUR_WHEEL_DRIVE_ROD)
                .createInstance();

        wheels = new ModelData[2];

        materialManager.defaultSolid()
                .material(Materials.TRANSFORMED)
                .getModel(AllBlockPartials.LARGE_BOGEY_WHEELS)
                .createInstances(wheels);

        connectingRod = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.FOUR_WHEEL_CONNECTING_ROD)
                .createInstance();
    }

    @Override
    public void registerLargeBogeyModelData(MaterialManager materialManager) {
        frame = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.FOUR_WHEEL_DRIVE_FRAME)
                .createInstance();

        drivePin = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.FOUR_WHEEL_DRIVE_PIN)
                .createInstance();

        driveRod = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.FOUR_WHEEL_DRIVE_ROD)
                .createInstance();

        wheels = new ModelData[2];

        materialManager.defaultSolid()
                .material(Materials.TRANSFORMED)
                .getModel(AllBlockPartials.LARGE_BOGEY_WHEELS)
                .createInstances(wheels);

        connectingRod = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.FOUR_WHEEL_CONNECTING_ROD)
                .createInstance();
    }

    @Override
    public void renderLargeInContraption(float wheelAngle, boolean isFacingForward, PoseStack ms) {
        float offSetScaleFactor =  Math.max(0f, (1f - Math.abs(Math.abs(wheelAngle) - 180f) / 180f));

        for (int side : Iterate.positiveAndNegative) {
            wheels[(side+1) / 2].setTransform(ms)
                .translate(0, 1, side)
                .rotateX(-wheelAngle);
        }

        connectingRod.setTransform(ms)
                .rotateX(wheelAngle)
                .translate(0, 1 / 4f, 0)
                .rotateX(-wheelAngle)
                .rotateY(isFacingForward ? 0 : 180);

        drivePin.setTransform(ms)
                .translateZ(1/4f * Math.sin(Math.toRadians(wheelAngle)))
                .rotateY(isFacingForward ? 0 : 180);

        driveRod.setTransform(ms)
                .translateZ(-0.6)
                .translateY(0.85)
                .rotateX(offSetScaleFactor*20-10)
                .translateZ(1/4f * Math.sin(Math.toRadians(wheelAngle)))
                .rotateY(isFacingForward ? 0 : 180);


        frame.setTransform(ms);

        IBogeyStyle.super.renderLargeInContraption(wheelAngle, isFacingForward, ms);
    }

    @Override
    public void renderLargeInWorld(float wheelAngle, boolean isFacingForward, PoseStack ms, int light, VertexConsumer vb, BlockState air) {
        CachedBufferer.partial(BogeyPartials.FOUR_WHEEL_DRIVE_FRAME, air)
                .rotateY(isFacingForward ? 180 : 0)
                .scale(1 - 1/512f)
                .light(light)
                .renderInto(ms, vb);

        CachedBufferer.partial(BogeyPartials.FOUR_WHEEL_CONNECTING_ROD, air)
                .rotateY(isFacingForward ? 180 : 0)
                .rotateX(wheelAngle)
                .translate(0, 1 / 4f, 0)
                .rotateX(-wheelAngle)
                .scale(1 - 1/512f)
                .light(light)
                .renderInto(ms, vb);

        float offSetScaleFactor =  Math.max(0f, (1f - Math.abs(Math.abs(wheelAngle) - 180f) / 180f));

        CachedBufferer.partial(BogeyPartials.FOUR_WHEEL_DRIVE_PIN, air)
                .rotateY(isFacingForward ? 180 : 0)
                .translateZ(1/4f * Math.sin(Math.toRadians(wheelAngle)))
                .scale(1 - 1/512f)
                .light(light)
                .renderInto(ms, vb);

        CachedBufferer.partial(BogeyPartials.FOUR_WHEEL_DRIVE_ROD, air)
                .translateZ(0.6)
                .translateY(0.85)
                .rotateY(isFacingForward ? 180 : 0)
                .rotateX(offSetScaleFactor*20-10)
                .translateZ(1/4f * Math.sin(Math.toRadians(wheelAngle)))
                .scale(1 - 1/512f)
                .light(light)
                .renderInto(ms, vb);

        for (int side : Iterate.positiveAndNegative) {
            ms.pushPose();
            CachedBufferer.partial(AllBlockPartials.LARGE_BOGEY_WHEELS, air)
                    .translate(0, 1, side)
                    .rotateX(wheelAngle)
                    .light(light)
                    .renderInto(ms, vb);
            ms.popPose();
        }

        IBogeyStyle.super.renderLargeInWorld(wheelAngle, isFacingForward, ms, light, vb, air);
    }

    @Override
    public void renderSmallInWorld(float wheelAngle, boolean isFacingForward, PoseStack ms, int light, VertexConsumer vb, BlockState air) {
        IBogeyStyle.super.renderSmallInWorld(wheelAngle, isFacingForward, ms, light, vb, air);
    }

    @Override
    public boolean shouldRenderInnerShaft() {
        return false;
    }
}