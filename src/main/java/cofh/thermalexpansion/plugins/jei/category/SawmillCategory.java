package cofh.thermalexpansion.plugins.jei.category;

import cofh.thermalexpansion.block.machine.BlockMachine;
import cofh.thermalexpansion.gui.client.machine.GuiSawmill;
import cofh.thermalexpansion.plugins.jei.JeiPluginTE;
import cofh.thermalexpansion.plugins.jei.MachineRecipeWrapper;
import cofh.thermalexpansion.plugins.jei.gui.*;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class SawmillCategory extends CategoryBase {

	private IGuiResource powerBar;
	private IProgressable progress;
	private int cycleTicks = 0;
	private IProgressable working;
	private int secondChance;

	public SawmillCategory() {

		super("thermalexpansion.sawmill", "Sawmill");

		powerBar = new PowerBar(8, 6);
		progress = Progressable.arrow(70, 32, false);
		working = Progressable.sawing(47, 43, false);
	}

	@Override
	public ItemStack getCraftingItem() {

		return BlockMachine.machineSawmill;
	}

	@Override
	public void registerClickToShowCategoryAreas(IModRegistry registry) {

		registry.addRecipeClickArea(GuiSawmill.class, 75, 30, 25, 25, uId);
	}

	@Override
	public void drawExtras(Minecraft minecraft) {

		if (cycleTicks++ / 120 >= 6) {
			cycleTicks = 0;
		}

		//draws charging slot.
		ResourceUtils.drawScaledTexturedRectFromModel(7, 50, 132, 96, 18, 18, JeiPluginTE.JEI_HANDLER_LOCATION);
		//draws input slot.
		ResourceUtils.drawScaledTexturedRectFromModel(45, 22, 132, 96, 18, 18, JeiPluginTE.JEI_HANDLER_LOCATION);
		//draws the output slot.
		ResourceUtils.drawScaledTexturedRectFromModel(105, 20, 150, 96, 26, 26, JeiPluginTE.JEI_HANDLER_LOCATION);
		//draws the secondary output slot.
		ResourceUtils.drawScaledTexturedRectFromModel(110, 50, 132, 96, 18, 18, JeiPluginTE.JEI_HANDLER_LOCATION);

		//draws animations.
		powerBar.draw(cycleTicks / 120);
		working.draw(cycleTicks / 6);
		progress.draw(cycleTicks % 120);
	}

	@Override
	public List<String> getTooltips(int mouseX, int mouseY) {

		if (powerBar.inBounds(mouseX, mouseY)) {
			return powerBar.getTooltip(20);
		}

		if (progress.inBounds(mouseX, mouseY)) {
			return progress.getTooltip();
		}

		if (working.inBounds(mouseX, mouseY)) {
			return working.getTooltip();
		}

		return null;
	}

	@ParametersAreNonnullByDefault
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, MachineRecipeWrapper recipeWrapper, IIngredients ingredients) {

		powerBar.setRequiredResource(recipeWrapper.getEnergyRequired());
		progress.setDuration(recipeWrapper.getDuration());
		working.setDuration(recipeWrapper.getDuration());
		this.secondChance = recipeWrapper.getChance();

		List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
		List<ItemStack> outputs = ingredients.getOutputs(ItemStack.class);
		IGuiItemStackGroup stackGroup = recipeLayout.getItemStacks();

		stackGroup.init(0, true, 45, 22);
		stackGroup.init(1, false, 109, 24);
		stackGroup.set(0, inputs.get(0));
		stackGroup.set(1, outputs.get(0));

		if (outputs.size() > 1) {
			stackGroup.init(2, false, 110, 50);
			stackGroup.set(2, outputs.get(1));
			stackGroup.addTooltipCallback(new ITooltipCallback<ItemStack>() {
				@Override
				public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {

					if (slotIndex == 2) {
						tooltip.add("chance: " + secondChance + "%");
					}
				}
			});
		}
	}
}
