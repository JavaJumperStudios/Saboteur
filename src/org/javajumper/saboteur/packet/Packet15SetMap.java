package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

/**
 * Sent from server to client, containing the information about the map
 */
public class Packet15SetMap extends Packet {

	/** the filename of the map */
	public String mapName;
	/** the width of the map */
	public int width;
	/** the eight of the map */
	public int height;
	/** the map tiles */
	public int[][] map;

	public Packet15SetMap() {
		super((byte) 15);
	}

	@Override
	public void readFromByteBuffer(ByteBuffer bb) {

		mapName = "";

		for (int i = 0; i < 16; i++) {
			mapName = mapName + bb.getChar();
		}

		width = bb.getInt();
		height = bb.getInt();

		map = new int[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				map[i][j] = bb.getInt();
			}
		}

	}

	@Override
	public ByteBuffer writeToByteBuffer() {
		ByteBuffer bb = ByteBuffer.allocate(getLength());

		bb.put(id);
		bb.putInt(getLength());

		for (int i = 0; i < 16; i++) {
			if (i < mapName.length())
				bb.putChar(mapName.charAt(i));
			else
				bb.putChar(' ');
		}

		bb.putInt(width);
		bb.putInt(height);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				bb.putInt(map[i][j]);
			}
		}

		return bb;
	}

	@Override
	public int getLength() {
		return super.getLength() + Character.BYTES * 16 + Integer.BYTES * 2 + Integer.BYTES * (width * height);
	}

}
