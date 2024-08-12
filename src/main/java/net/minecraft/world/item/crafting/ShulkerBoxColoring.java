package net.minecraft.world.item.crafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;

public class ShulkerBoxColoring extends CustomRecipe {
    public ShulkerBoxColoring(CraftingBookCategory p_250756_) {
        super(p_250756_);
    }

    public boolean matches(CraftingInput p_345153_, Level p_44325_) {
        int i = 0;
        int j = 0;

        for (int k = 0; k < p_345153_.size(); k++) {
            ItemStack itemstack = p_345153_.getItem(k);
            if (!itemstack.isEmpty()) {
                if (Block.byItem(itemstack.getItem()) instanceof ShulkerBoxBlock) {
                    i++;
                } else {
                    if (!(itemstack.getItem() instanceof DyeItem)) {
                        return false;
                    }

                    j++;
                }

                if (j > 1 || i > 1) {
                    return false;
                }
            }
        }

        return i == 1 && j == 1;
    }

    public ItemStack assemble(CraftingInput p_343576_, HolderLookup.Provider p_336251_) {
        ItemStack itemstack = ItemStack.EMPTY;
        DyeItem dyeitem = (DyeItem)Items.WHITE_DYE;

        for (int i = 0; i < p_343576_.size(); i++) {
            ItemStack itemstack1 = p_343576_.getItem(i);
            if (!itemstack1.isEmpty()) {
                Item item = itemstack1.getItem();
                if (Block.byItem(item) instanceof ShulkerBoxBlock) {
                    itemstack = itemstack1;
                } else if (item instanceof DyeItem) {
                    dyeitem = (DyeItem)item;
                }
            }
        }

        Block block = ShulkerBoxBlock.getBlockByColor(dyeitem.getDyeColor());
        return itemstack.transmuteCopy(block, 1);
    }

    @Override
    public boolean canCraftInDimensions(int p_44314_, int p_44315_) {
        return p_44314_ * p_44315_ >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.SHULKER_BOX_COLORING;
    }
}