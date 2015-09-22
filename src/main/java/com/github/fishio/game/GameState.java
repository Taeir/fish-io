package com.github.fishio.game;

/**
 * Enum for the different states the game can be in.<br>
 * <br>
 * The game state only changes like displayed below.<br>
 * <br>
 * <code>
 * STARTING --&gt; RUNNING or STOPPED<br>
 * RUNNING --&gt; STOPPING or STOPPED<br>
 * STOPPING --&gt; STOPPED<br>
 * STOPPED --&gt; STARTING<br>
 * </code>
 * <br>
 * As can be seen, every state can switch to the STOPPED state immediately,
 * but state changing otherwise follows the linear order:
 * <code>STOPPED --&gt; STARTING --&gt; RUNNING --&gt; STOPPING</code>
 */
public enum GameState {
	STARTING, RUNNING, STOPPING, STOPPED;
}
