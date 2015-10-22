package com.github.fishio.multiplayer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.group.ChannelGroup;

import java.util.LinkedHashMap;

/**
 * Class that contains methods to send a certain message multiple times.<br>
 * The message is only sent to a specific client when they have received
 * the previous instance of the message.
 * 
 * @see #queueToAvailable(ChannelGroup)
 */
public class RepeatingFishMessageSender {
	private LinkedHashMap<Channel, ChannelFuture> map = new LinkedHashMap<>();
	private FishMessage message;
	
	/**
	 * Creates a new RepeatingFishMessageSender for the given message.
	 * 
	 * @param message
	 * 		the message to send repeatedly.
	 */
	public RepeatingFishMessageSender(FishMessage message) {
		this.message = message;
	}
	
	/**
	 * Queues the message in this RepeatingFishMessageSender to all the
	 * channels in the given group, excluding those who have not received
	 * the previous message yet.<br>
	 * <br>
	 * The behaviour of this method can be better explained by an example:<br>
	 * Given:<br>
	 * - It takes <b>10 seconds</b> to send the message to <b>Client A</b>.<br>
	 * - It takes <b>1 second</b> to send the message to <b>Client B</b>.<br>
	 * - This method is called once every second.<br>
	 * <br>
	 * After exactly 12 seconds, <b>12 messages</b> were queued to be sent
	 * to <b>Client B</b>, but only <b>2 messages</b> were queued to be
	 * sent to <b>Client A</b>.
	 * 
	 * @param group
	 * 		the ChannelGroup to send the message to.
	 */
	public void queueWriteFlushToAvailable(ChannelGroup group) {
		for (Channel channel : group) {
			ChannelFuture cf = map.get(channel);
			
			//If not currently in the map, or done, we send the message again
			if (cf == null || cf.isDone()) {
				map.put(channel, channel.writeAndFlush(message));
			}
		}
	}
}
