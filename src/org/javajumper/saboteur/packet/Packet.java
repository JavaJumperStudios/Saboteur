package org.javajumper.saboteur.packet;

import java.nio.ByteBuffer;

/**
 * An abstract class for packets that are sent between the server and its
 * clients
 */
public abstract class Packet {

	/**
	 * The id of the packet, used to determine what infortmation is stored in
	 * the packet. Each packet class has its own id. The naming convention for
	 * packet classes is Packet[ID][Use], e.g. Packet18BulletShoot
	 */
	public byte id;

	/**
	 * The lenght of the packet in bytes, including the space reserved for the
	 * id and length field itself
	 */
	public int length;

	Packet(byte id) {
		this.id = id;
		this.length = getLength();
	}

	/*
	 * This must be overwritten by any new packet class which adds information
	 * to the payload of the packet, so the client or server knows, how many
	 * byte should be read from the byte stream. If this method is not correct,
	 * the reading host will probably report a "malformed package" error.
	 * 
	 * Use super.getLength() in subclasses and add it to your byte count!
	 */
	/**
	 * @return the length of the whole packet, including bytes reserved for id
	 *         and length
	 */
	protected int getLength() {
		return Byte.BYTES + Integer.BYTES;
	}

	/**
	 * Takes an array of bytes containing the packet information in the right
	 * order, wraps it into a bytebuffer and reads its information into the
	 * packet
	 * 
	 * @param data
	 *            the array containing the packet information in the right order
	 */
	public void readFromByteArray(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);

		id = bb.get();
		length = bb.getInt();

		readFromByteBuffer(bb);
	}

	/**
	 * Writes the packet to a ByteBuffer and then converts it into an array
	 * 
	 * @return an array of the bytes of the package
	 */
	public byte[] writeToByteArray() {
		ByteBuffer bb = writeToByteBuffer();
		return bb.array();
	}

	/**
	 * Reads information from a ByteBuffer and stores it into this package
	 * 
	 * @param bb
	 *            the ByteBuffer containing the packet information (in the right
	 *            order!)
	 * 
	 */
	public abstract void readFromByteBuffer(ByteBuffer bb);

	/**
	 * Writes the packet to a ByteBuffer
	 * 
	 * @return a ByteBuffer containing the packet information
	 */
	public abstract ByteBuffer writeToByteBuffer();

}
