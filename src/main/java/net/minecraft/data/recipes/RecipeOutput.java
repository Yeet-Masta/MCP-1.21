package net.minecraft.data.recipes;

import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

public interface RecipeOutput {
    void accept(ResourceLocation p_310578_, Recipe<?> p_312265_, @Nullable AdvancementHolder p_310407_);

    Advancement.Builder advancement();
}