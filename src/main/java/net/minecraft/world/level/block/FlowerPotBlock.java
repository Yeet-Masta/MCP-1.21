package net.minecraft.world.level.block;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FlowerPotBlock extends Block {
    public static final MapCodec<FlowerPotBlock> CODEC = RecordCodecBuilder.mapCodec(
        p_341830_ -> p_341830_.group(BuiltInRegistries.BLOCK.byNameCodec().fieldOf("potted").forGetter(p_310137_ -> p_310137_.potted), propertiesCodec())
                .apply(p_341830_, FlowerPotBlock::new)
    );
    private static final Map<Block, Block> POTTED_BY_CONTENT = Maps.newHashMap();
    public static final float AABB_SIZE = 3.0F;
    protected static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0);
    private final Block potted;

    @Override
    public MapCodec<FlowerPotBlock> codec() {
        return CODEC;
    }

    public FlowerPotBlock(Block p_53528_, BlockBehaviour.Properties p_53529_) {
        super(p_53529_);
        this.potted = p_53528_;
        POTTED_BY_CONTENT.put(p_53528_, this);
    }

    @Override
    protected VoxelShape getShape(BlockState p_53556_, BlockGetter p_53557_, BlockPos p_53558_, CollisionContext p_53559_) {
        return SHAPE;
    }

    @Override
    protected ItemInteractionResult useItemOn(
        ItemStack p_329639_, BlockState p_328047_, Level p_328816_, BlockPos p_334572_, Player p_329206_, InteractionHand p_329142_, BlockHitResult p_330607_
    ) {
        BlockState blockstate = (p_329639_.getItem() instanceof BlockItem blockitem
                ? POTTED_BY_CONTENT.getOrDefault(blockitem.getBlock(), Blocks.AIR)
                : Blocks.AIR)
            .defaultBlockState();
        if (blockstate.isAir()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        } else if (!this.isEmpty()) {
            return ItemInteractionResult.CONSUME;
        } else {
            p_328816_.setBlock(p_334572_, blockstate, 3);
            p_328816_.gameEvent(p_329206_, GameEvent.BLOCK_CHANGE, p_334572_);
            p_329206_.awardStat(Stats.POT_FLOWER);
            p_329639_.consume(1, p_329206_);
            return ItemInteractionResult.sidedSuccess(p_328816_.isClientSide);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState p_335777_, Level p_334489_, BlockPos p_330334_, Player p_333787_, BlockHitResult p_335374_) {
        if (this.isEmpty()) {
            return InteractionResult.CONSUME;
        } else {
            ItemStack itemstack = new ItemStack(this.potted);
            if (!p_333787_.addItem(itemstack)) {
                p_333787_.drop(itemstack, false);
            }

            p_334489_.setBlock(p_330334_, Blocks.FLOWER_POT.defaultBlockState(), 3);
            p_334489_.gameEvent(p_333787_, GameEvent.BLOCK_CHANGE, p_330334_);
            return InteractionResult.sidedSuccess(p_334489_.isClientSide);
        }
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader p_312345_, BlockPos p_53532_, BlockState p_53533_) {
        return this.isEmpty() ? super.getCloneItemStack(p_312345_, p_53532_, p_53533_) : new ItemStack(this.potted);
    }

    private boolean isEmpty() {
        return this.potted == Blocks.AIR;
    }

    @Override
    protected BlockState updateShape(BlockState p_53547_, Direction p_53548_, BlockState p_53549_, LevelAccessor p_53550_, BlockPos p_53551_, BlockPos p_53552_) {
        return p_53548_ == Direction.DOWN && !p_53547_.canSurvive(p_53550_, p_53551_)
            ? Blocks.AIR.defaultBlockState()
            : super.updateShape(p_53547_, p_53548_, p_53549_, p_53550_, p_53551_, p_53552_);
    }

    public Block getPotted() {
        return this.potted;
    }

    @Override
    protected boolean isPathfindable(BlockState p_53535_, PathComputationType p_53538_) {
        return false;
    }
}