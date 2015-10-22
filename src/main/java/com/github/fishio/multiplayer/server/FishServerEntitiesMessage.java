package com.github.fishio.multiplayer.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.github.fishio.Entity;
import com.github.fishio.PlayingField;

/**
 * Server message for Entity information.
 */
public class FishServerEntitiesMessage implements FishServerMessage {
	private static final long serialVersionUID = -6352446970035595155L;
	
	private Collection<Entity> entities;
	
	/**
	 * @param field
	 * 		the playingfield to get the drawables from.
	 */
	public FishServerEntitiesMessage(PlayingField field) {
		this.entities = field.getEntities();
	}
	
	/**
	 * @return
	 * 		the entities in this message.
	 */
	public Collection<Entity> getEntities() {
		return entities;
	}
	
	/**
	 * Compares the given Entities collection with the one in this
	 * message and returns a set of entities that have died.
	 * 
	 * @param old
	 * 		the old collection of entities.
	 * 
	 * @return
	 * 		all entities that have died, according to this message.
	 */
	public Set<Entity> getDeadEntities(Collection<Entity> old) {
		HashSet<Entity> dead = new HashSet<Entity>(old);
		for (Entity e : this.entities) {
			if (e.isDead()) {
				dead.add(e);
			} else {
				//This happens when entities are reincarnated
				dead.remove(e);
			}
		}

		return dead;
	}
	
	/**
	 * Compares the given Entities collection with the one in this
	 * message and returns a set of entities that are new.<br>
	 * <br>
	 * NOTE: the set returned by this method might contain dead entities.
	 * 		 This happens if an entity dies before it is received by the
	 * 		 client.
	 * 
	 * @param old
	 * 		the old collection of entities.
	 * 
	 * @return
	 * 		all entities that are new, according to this message.
	 */
	public Set<Entity> getNewEntities(Collection<Entity> old) {
		HashSet<Entity> newEntities = new HashSet<Entity>(this.entities);
		newEntities.removeAll(old);
		
		return newEntities;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeObject(entities.toArray(new Entity[0]));
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.entities = Arrays.asList((Entity[]) in.readObject());
	}
}
