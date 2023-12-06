package de.schneile.schneiland2core.mixin;

import de.schneile.schneiland2core.EventType;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

@Mixin(ServerWorld.class)
public abstract class WorldEventLogger {

	@Unique
	final Path LOGS_PATH = Path.of(System.getenv("HOME"), "schneile", "smp", "eventlogs");

	@Unique
	final ImmutableMap<GameEvent, Double> EVENTS_MOVEMENT = ImmutableMap.of(
		GameEvent.ELYTRA_GLIDE, 50.0,
		GameEvent.STEP,         25.0,
		GameEvent.SWIM,         10.0
	);

	@Unique
	final Map<UUID, Vec3d> lastLocations = new HashMap<>();

	@Unique
	final Deque<byte[]> eventQueue = new ArrayDeque<>();


	@Inject(method = "emitGameEvent", at = @At("HEAD"))
	private void emitGameEventHeadInject(final GameEvent event, final Vec3d emitterPos, final GameEvent.Emitter emitter, CallbackInfo ci) {
		if (emitter.sourceEntity() instanceof PlayerEntity player) new Thread(() -> {
			BlockState affectedState = emitter.affectedState();

			@SuppressWarnings("DataFlowIssue") // more like SkillIssue
			double requiredDistance = EVENTS_MOVEMENT.getOrDefault(event, 0.0);
			if (requiredDistance > 0.0) {
				Vec3d lastLocation = lastLocations.get(player.getUuid());
				if (lastLocation != null && emitterPos.distanceTo(lastLocation) < requiredDistance) return;
			}
			lastLocations.put(player.getUuid(), emitterPos);

			ByteBuffer buf = EventType.WORLD.createBuffer(Registries.GAME_EVENT.getRawId(event));
			buf.putInt((int) Math.round(emitterPos.x - .5));
			buf.putInt((int) Math.round(emitterPos.y - .5));
			buf.putInt((int) Math.round(emitterPos.z - .5));
			buf.putInt(affectedState == null ? ~0 : Registries.BLOCK.getRawId(affectedState.getBlock()));

			byte[] arr = null;
			String key = player.getName().getString() + '@' + player.getUuidAsString() + ".log";
			LOGS_PATH.toFile().mkdirs();
			synchronized (eventQueue) {
				eventQueue.addLast(buf.array());
				try {
					while (!eventQueue.isEmpty()) {
						arr = eventQueue.removeFirst();
						Files.write(LOGS_PATH.resolve(key), arr, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
					}
				} catch (IOException e) {
					eventQueue.addFirst(arr);
				}
			}
		}).start();
	}



}