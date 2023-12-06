package de.schneile.schneiland2core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;


public class EndermanFucker {

	private EndermanFucker() {}


	@Mixin(targets = "net.minecraft.entity.mob.EndermanEntity$PickUpBlockGoal")
	public static class PickUpFucker {
		/**
		 * @author Nyde
		 * @reason fuck enderman griefing
		 */
		@Overwrite
		public boolean canStart() {
			return false;
		}
		/**
		 * @author Nyde
		 * @reason fuck enderman griefing
		 */
		@Overwrite
		public void tick() {}
	}

	@Mixin(targets = "net.minecraft.entity.mob.EndermanEntity$PlaceBlockGoal")
	public static class PlaceFucker {
		/**
		 * @author Nyde
		 * @reason fuck enderman griefing
		 */
		@Overwrite
		public boolean canStart() {
			return false;
		}
		/**
		 * @author Nyde
		 * @reason fuck enderman griefing
		 */
		@Overwrite
		public void tick() {}
	}

}
