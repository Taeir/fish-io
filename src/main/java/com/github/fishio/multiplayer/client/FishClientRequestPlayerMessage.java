package com.github.fishio.multiplayer.client;

/**
 * Message from the client to the server, asking for a (new) playerfish.
 * The client sends this message to the server when it wants to create a
 * player fish, e.g. when (re)spawning.
 */
public class FishClientRequestPlayerMessage implements FishClientMessage {
	private static final long serialVersionUID = -6688007165970189062L;
}
