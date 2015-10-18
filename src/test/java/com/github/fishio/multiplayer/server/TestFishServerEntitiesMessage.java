package com.github.fishio.multiplayer.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.github.fishio.Entity;
import com.github.fishio.PlayingField;

/**
 * Test for {@link FishServerEntitiesMessage}.
 */
public class TestFishServerEntitiesMessage {

	private PlayingField pf;
	private Queue<Entity> queue;
	
	/**
	 * Create a new playingfield and queue for every test.
	 */
	@Before
	public void setUp() {
		pf = mock(PlayingField.class);
		
		Entity entity = mock(Entity.class);
		when(entity.getEntityId()).thenReturn(10);
		
		queue = new LinkedList<Entity>();
		queue.add(entity);
		when(pf.getEntities()).thenReturn(queue);
	}

	/**
	 * Test for {@link FishServerEntitiesMessage#getEntities()}.
	 */
	@Test
	public void testGetEntities() {
		FishServerEntitiesMessage fsem = new FishServerEntitiesMessage(pf);
		
		assertSame(queue, fsem.getEntities());
	}

	/**
	 * Test for {@link FishServerEntitiesMessage#getDeadEntities()}.
	 */
	@Test
	public void testGetDeadEntities() {
		FishServerEntitiesMessage fsem = new FishServerEntitiesMessage(pf);
		
		Set<Entity> entities = fsem.getDeadEntities(new ArrayList<Entity>());
		assertTrue(entities.isEmpty());
	}

	/**
	 * Test for {@link FishServerEntitiesMessage#getNewEntities(java.util.Collection)}.
	 */
	@Test
	public void testGetNewEntities() {
		FishServerEntitiesMessage fsem = new FishServerEntitiesMessage(pf);
		
		Set<Entity> entities = fsem.getNewEntities(new ArrayList<Entity>());
		assertEquals(1, entities.size());
	}

}
