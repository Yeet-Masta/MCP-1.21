package net.minecraft.world.level.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class StandingSignBlock extends SignBlock {
    public static final MapCodec<StandingSignBlock> CODEC = RecordCodecBuilder.mapCodec(
        p_311594_ -> p_311594_.group(WoodType.CODEC.fieldOf("wood_type").forGetter(SignBlock::type), propertiesCodec())
                .apply(p_311594_, StandingSignBlock::new)
    );
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;

    @Override
    public MapCodec<StandingSignBlock> codec() {
        return CODEC;
    }

    public StandingSignBlock(WoodType p_56991_, BlockBehaviour.Properties p_56990_) {
        super(p_56991_, p_56990_.sound(p_56991_.soundType()));
        this.registerDefaultState(this.stateDefinition.any().setValue(ROTATION, Integer.valueOf(0)).setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    @Override
    protected boolean canSurvive(BlockState p_56995_, LevelReader p_56996_, BlockPos p_56997_) {
        return p_56996_.getBlockState(p_56997_.below()).isSolid();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_56993_) {
        FluidState fluidstate = p_56993_.getLevel().getFluidState(p_56993_.getClickedPos());
        return this.defaultBlockState()
            .setValue(ROTATION, Integer.valueOf(RotationSegment.convertToSegment(p_56993_.getRotation() + 180.0F)))
            .setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
    }

    @Override
    protected BlockState updateShape(BlockState p_57005_, Direction p_57006_, BlockState p_57007_, LevelAccessor p_57008_, BlockPos p_57009_, BlockPos p_57010_) {
        return p_57006_ == Direction.DOWN && !this.canSurvive(p_57005_, p_57008_, p_57009_)
            ? Blocks.AIR.defaultBlockState()
            : super.updateShape(p_57005_, p_57006_, p_57007_, p_57008_, p_57009_, p_57010_);
    }

    @Override
    public float getYRotationDegrees(BlockState p_277795_) {
        return RotationSegment.convertToDegrees(p_277795_.getValue(ROTATION));
    }

    @Override
    protected BlockState rotate(BlockState p_57002_, Rotation p_57003_) {
        return p_57002_.setValue(ROTATION, Integer.valueOf(p_57003_.rotate(p_57002_.getValue(ROTATION), 16)));
    }

    @Override
    protected BlockState mirror(BlockState p_56999_, Mirror p_57000_) {
        return p_56999_.setValue(ROTATION, Integer.valueOf(p_57000_.mirror(p_56999_.getValue(ROTATION), 16)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_57012_) {
        p_57012_.add(ROTATION, WATERLOGGED);
    }
}