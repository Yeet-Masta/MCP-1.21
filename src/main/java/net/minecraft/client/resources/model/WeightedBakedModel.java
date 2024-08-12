package net.minecraft.client.resources.model;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WeightedBakedModel implements BakedModel {
    private final int totalWeight;
    private final List<WeightedEntry.Wrapper<BakedModel>> list;
    private final BakedModel wrapped;

    public WeightedBakedModel(List<WeightedEntry.Wrapper<BakedModel>> p_119544_) {
        this.list = p_119544_;
        this.totalWeight = WeightedRandom.getTotalWeight(p_119544_);
        this.wrapped = p_119544_.get(0).data();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState p_235058_, @Nullable Direction p_235059_, RandomSource p_235060_) {
        return WeightedRandom.getWeightedItem(this.list, Math.abs((int)p_235060_.nextLong()) % this.totalWeight)
            .map(p_325578_ -> p_325578_.data().getQuads(p_235058_, p_235059_, p_235060_))
            .orElse(Collections.emptyList());
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.wrapped.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.wrapped.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return this.wrapped.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return this.wrapped.isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.wrapped.getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms() {
        return this.wrapped.getTransforms();
    }

    @Override
    public ItemOverrides getOverrides() {
        return this.wrapped.getOverrides();
    }

    @OnlyIn(Dist.CLIENT)
    public static class Builder {
        private final List<WeightedEntry.Wrapper<BakedModel>> list = Lists.newArrayList();

        public WeightedBakedModel.Builder add(@Nullable BakedModel p_119560_, int p_119561_) {
            if (p_119560_ != null) {
                this.list.add(WeightedEntry.wrap(p_119560_, p_119561_));
            }

            return this;
        }

        @Nullable
        public BakedModel build() {
            if (this.list.isEmpty()) {
                return null;
            } else {
                return (BakedModel)(this.list.size() == 1 ? this.list.get(0).data() : new WeightedBakedModel(this.list));
            }
        }
    }
}