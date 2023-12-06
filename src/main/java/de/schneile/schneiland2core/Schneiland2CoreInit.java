package de.schneile.schneiland2core;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Schneiland2CoreInit implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	@SuppressWarnings("unused")
    public static final Logger LOGGER = LoggerFactory.getLogger("Schneiland2Core");


    private static final String MOTD_HEAD;

    static {
    	StringBuilder str = new StringBuilder("§rDer offizielle ");
    	char[] cols = "c6eab9dc6erab9rd".toCharArray();
    	char[] text = "Schneiland SMP 2".toCharArray();
    	for (int i = 0; i < text.length; ++i) str
			.append('§')
			.append(cols[i])
			.append("§l")
			.append(text[i]);
		MOTD_HEAD = str.append("§r\n§o").toString();
    }

	@Override
	public void onInitialize() {
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if ((server.getTicks() & 0xFF) != 0) return;
			long time = server.getOverworld().getTimeOfDay() + 6000;

			server.setMotd(String.format("%sOverworld-Zeit: §f§o%,2d§r§o:§f§o%02d§r§o Uhr",
				MOTD_HEAD,
				(time % 24000) / 1000,
				(time %  1000) / 500 * 30
			));
		});
	}

}