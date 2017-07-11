package cofh.thermalexpansion.gui.client.machine;

import cofh.core.gui.element.*;
import cofh.thermalexpansion.block.machine.TileSmelter;
import cofh.thermalexpansion.gui.client.GuiPoweredBase;
import cofh.thermalexpansion.gui.container.machine.ContainerSmelter;
import cofh.thermalexpansion.gui.element.ElementSlotOverlay;
import cofh.thermalexpansion.gui.element.ElementSlotOverlay.SlotColor;
import cofh.thermalexpansion.gui.element.ElementSlotOverlay.SlotRender;
import cofh.thermalexpansion.gui.element.ElementSlotOverlay.SlotType;
import cofh.thermalexpansion.init.TEProps;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GuiSmelter extends GuiPoweredBase {

	public static final String TEX_PATH = TEProps.PATH_GUI_MACHINE + "smelter.png";
	public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

	private TileSmelter myTile;

	private ElementSlotOverlay[] slotPrimaryInput = new ElementSlotOverlay[2];
	private ElementSlotOverlay[] slotSecondaryInput = new ElementSlotOverlay[2];
	private ElementSlotOverlay[] slotPrimaryOutput = new ElementSlotOverlay[2];
	private ElementSlotOverlay[] slotSecondaryOutput = new ElementSlotOverlay[2];

	private ElementDualScaled progress;
	private ElementDualScaled speed;

	private ElementButton mode;
	private ElementSimple modeOverlay;

	private ElementSimple tankBackground;
	private ElementFluidTank tank;
	private ElementFluid progressFluid;
	private ElementDualScaled progressOverlay;

	public GuiSmelter(InventoryPlayer inventory, TileEntity tile) {

		super(new ContainerSmelter(inventory, tile), tile, inventory.player, TEXTURE);

		generateInfo("tab.thermalexpansion.machine.smelter");

		myTile = (TileSmelter) tile;
	}

	@Override
	public void initGui() {

		super.initGui();

		tankBackground = (ElementSimple) addElement(new ElementSimple(this, 151, 8).setTextureOffsets(176, 104).setSize(18, 62).setTexture(TEX_PATH, 256, 256));

		slotPrimaryInput[0] = (ElementSlotOverlay) addElement(new ElementSlotOverlay(this, 32, 26).setSlotInfo(SlotColor.BLUE, SlotType.STANDARD, SlotRender.FULL));
		slotPrimaryInput[1] = (ElementSlotOverlay) addElement(new ElementSlotOverlay(this, 32, 26).setSlotInfo(SlotColor.GREEN, SlotType.STANDARD, SlotRender.BOTTOM));
		slotSecondaryInput[0] = (ElementSlotOverlay) addElement(new ElementSlotOverlay(this, 56, 26).setSlotInfo(SlotColor.BLUE, SlotType.STANDARD, SlotRender.FULL));
		slotSecondaryInput[1] = (ElementSlotOverlay) addElement(new ElementSlotOverlay(this, 56, 26).setSlotInfo(SlotColor.PURPLE, SlotType.STANDARD, SlotRender.BOTTOM));
		slotPrimaryOutput[0] = (ElementSlotOverlay) addElement(new ElementSlotOverlay(this, 112, 22).setSlotInfo(SlotColor.ORANGE, SlotType.OUTPUT, SlotRender.FULL));
		slotPrimaryOutput[1] = (ElementSlotOverlay) addElement(new ElementSlotOverlay(this, 112, 22).setSlotInfo(SlotColor.RED, SlotType.OUTPUT, SlotRender.BOTTOM));
		slotSecondaryOutput[0] = (ElementSlotOverlay) addElement(new ElementSlotOverlay(this, 116, 53).setSlotInfo(SlotColor.ORANGE, SlotType.STANDARD, SlotRender.FULL));
		slotSecondaryOutput[1] = (ElementSlotOverlay) addElement(new ElementSlotOverlay(this, 116, 53).setSlotInfo(SlotColor.YELLOW, SlotType.STANDARD, SlotRender.BOTTOM));

		addElement(new ElementEnergyStored(this, 8, 8, myTile.getEnergyStorage()));

		progress = (ElementDualScaled) addElement(new ElementDualScaled(this, 79, 34).setMode(1).setSize(24, 16).setTexture(TEX_ARROW_RIGHT, 64, 16));
		speed = (ElementDualScaled) addElement(new ElementDualScaled(this, 44, 44).setSize(16, 16).setTexture(TEX_FLAME, 32, 16));

		mode = (ElementButton) addElement(new ElementButton(this, 80, 53, "Mode", 176, 0, 176, 16, 176, 32, 16, 16, TEX_PATH));
		modeOverlay = (ElementSimple) addElement(new ElementSimple(this, 32, 26).setTextureOffsets(176, 48).setSize(16, 16).setTexture(TEX_PATH, 256, 256));

		tank = (ElementFluidTank) addElement(new ElementFluidTank(this, 152, 9, myTile.getTank()).setGauge(0).setAlwaysShow(true));
		progressFluid = (ElementFluid) addElement(new ElementFluid(this, 79, 34).setFluid(myTile.getTankFluid()).setSize(24, 16));
		progressOverlay = (ElementDualScaled) addElement(new ElementDualScaled(this, 79, 34).setBackground(false).setMode(1).setSize(24, 16).setTexture(TEX_ARROW_FLUID_RIGHT, 64, 16));

		tankBackground.setVisible(myTile.augmentPyrotheum());
		tank.setVisible(myTile.augmentPyrotheum());
		progressFluid.setVisible(myTile.fluidArrow());
		progressOverlay.setVisible(myTile.fluidArrow());
	}

	@Override
	protected void updateElementInformation() {

		super.updateElementInformation();

		slotPrimaryInput[0].setVisible(myTile.hasSideType(INPUT_ALL) || baseTile.hasSideType(OMNI));
		slotPrimaryInput[1].setVisible(myTile.hasSideType(INPUT_PRIMARY));
		slotSecondaryInput[0].setVisible(myTile.hasSideType(INPUT_ALL) || baseTile.hasSideType(OMNI));
		slotSecondaryInput[1].setVisible(myTile.hasSideType(INPUT_SECONDARY));

		slotPrimaryOutput[0].setVisible(myTile.hasSideType(OUTPUT_ALL) || baseTile.hasSideType(OMNI));
		slotPrimaryOutput[1].setVisible(myTile.hasSideType(OUTPUT_PRIMARY));
		slotSecondaryOutput[0].setVisible(myTile.hasSideType(OUTPUT_ALL) || baseTile.hasSideType(OMNI));
		slotSecondaryOutput[1].setVisible(myTile.hasSideType(OUTPUT_SECONDARY));

		if (!baseTile.hasSideType(INPUT_ALL) && !baseTile.hasSideType(OMNI)) {
			slotPrimaryInput[1].setSlotRender(SlotRender.FULL);
			slotSecondaryInput[1].setSlotRender(SlotRender.FULL);
		} else {
			slotPrimaryInput[1].setSlotRender(SlotRender.BOTTOM);
			slotSecondaryInput[1].setSlotRender(SlotRender.BOTTOM);
		}
		if (!baseTile.hasSideType(OUTPUT_ALL) && !baseTile.hasSideType(OMNI)) {
			slotPrimaryOutput[1].setSlotRender(SlotRender.FULL);
			slotSecondaryOutput[1].setSlotRender(SlotRender.FULL);
		} else {
			slotPrimaryOutput[1].setSlotRender(SlotRender.BOTTOM);
			slotSecondaryOutput[1].setSlotRender(SlotRender.BOTTOM);
		}
		progress.setQuantity(myTile.getScaledProgress(PROGRESS));
		speed.setQuantity(myTile.getScaledSpeed(SPEED));

		if (myTile.lockPrimary) {
			mode.setToolTip("gui.thermalexpansion.machine.smelter.modeLocked");
			mode.setSheetX(176);
			mode.setHoverX(176);
			modeOverlay.setVisible(true);
		} else {
			mode.setToolTip("gui.thermalexpansion.machine.smelter.modeUnlocked");
			mode.setSheetX(192);
			mode.setHoverX(192);
			modeOverlay.setVisible(false);
		}
		progressFluid.setSize(baseTile.getScaledProgress(PROGRESS), 16);
		progressOverlay.setQuantity(baseTile.getScaledProgress(PROGRESS));

		progress.setVisible(!myTile.fluidArrow());

		tankBackground.setVisible(myTile.augmentPyrotheum());
		tank.setVisible(myTile.augmentPyrotheum());
		progressFluid.setVisible(myTile.fluidArrow());
		progressOverlay.setVisible(myTile.fluidArrow());
	}

	@Override
	public void handleElementButtonClick(String buttonName, int mouseButton) {

		if (buttonName.equals("Mode")) {
			if (myTile.lockPrimary) {
				playClickSound(0.6F);
			} else {
				playClickSound(0.8F);
			}
			myTile.setMode(!myTile.lockPrimary);
		}
	}

}
