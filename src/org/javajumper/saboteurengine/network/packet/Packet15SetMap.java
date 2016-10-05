package org.javajumper.saboteurengine.network.packet;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;

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
	/** the collision shapes */
	public ArrayList<Shape> collisionShapes;
	
	private int pointCount;
	private int shapeCount;

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
		Polygon collisionElement = new Polygon();
		collisionShapes = new ArrayList<Shape>();
		
		while (bb.remaining() > 0) {
			
			if (bb.getChar() != '_') {
				float x = bb.getFloat();
				float y = bb.getFloat();
				
				collisionElement.addPoint(x, y);
			} else {
				
				collisionShapes.add(collisionElement);
				collisionElement = new Polygon();
				
			}
		}
	}

	@Override
	public ByteBuffer writeToByteBuffer() {
		pointCount = 0;
		shapeCount = 0;
		
		for (Shape s : collisionShapes) {
			for (int i = 0; i < s.getPointCount(); i++) {
				pointCount++;
				System.out.println("PC: " + pointCount);
			}
			shapeCount++;
			System.out.println("SC: " + shapeCount);
		}
		
		ByteBuffer bb = ByteBuffer.allocate(getLength());

		System.out.println("Länge: " + getLength());
		
		bb.put(id);
		bb.putInt(getLength());

		for (int i = 0; i < 16; i++) {
			if (i < mapName.length())
				bb.putChar(mapName.charAt(i));
			else
				bb.putChar('_');
		}

		bb.putInt(width);
		bb.putInt(height);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				bb.putInt(map[i][j]);
			}
		}
		

		
		for (Shape s : collisionShapes) {
			for (int i = 0; i < s.getPointCount(); i++) {
				System.out.println("Putting in Point..");
				bb.putChar(' ');
				bb.putFloat(s.getPoint(i)[0]);
				bb.putFloat(s.getPoint(i)[1]);
			}
			bb.putChar('_');
		}

		return bb;
	}

	@Override
	public int getLength() {
		return super.getLength() + Character.BYTES * 16 + Integer.BYTES * 2 + Integer.BYTES * (width * height)
				+ pointCount * (Float.BYTES * 2 + Character.BYTES) + shapeCount * Character.BYTES;
	}

}
