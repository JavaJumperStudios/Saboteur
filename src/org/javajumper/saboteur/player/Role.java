package org.javajumper.saboteur.player;

/**
 * Represent the role of a player
 */
public enum Role {
	/**
	 * The role of a playing player who is a traitor
	 */
	TRAITOR,
	/**
	 * The role of a playing player who is innocent
	 */
	INNOCENT,
	/**
	 * The role of a spectating player
	 */
	SPECTATE,
	/**
	 * The of a player in the waiting lobby
	 */
	LOBBY;

	/**
	 * Checks whether the given role has a restricted view, that is if the role
	 * sees traitors with the texture of an innocent
	 * 
	 * @param role
	 *            the role to check
	 * @return if the role has restricted view
	 */
	public static boolean isRestriced(Role role) {
		return role == INNOCENT;
	}
}
