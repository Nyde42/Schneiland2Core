package de.schneile.schneiland2core;

import java.nio.ByteBuffer;


public enum EventType {

	WORLD(4 * Integer.BYTES);


	public final int size;


	EventType(int size) {
		this.size = size;
	}

	public ByteBuffer createBuffer(int distinguisher) {
		return ByteBuffer.allocate(Long.BYTES + 2 * Integer.BYTES + size)
			.putLong(System.currentTimeMillis())
			.putInt(ordinal())
			.putInt(distinguisher);
	}

}
