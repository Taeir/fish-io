package com.github.fishio.multiplayer.client;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Set;

import org.junit.Test;

import javafx.scene.canvas.Canvas;

import com.github.fishio.CollisionMask;
import com.github.fishio.EnemyFish;
import com.github.fishio.Entity;
import com.github.fishio.ICollidable;
import com.github.fishio.ICollisionArea;
import com.github.fishio.PlayerFish;
import com.github.fishio.behaviours.FrozenBehaviour;
import com.github.fishio.behaviours.IMoveBehaviour;
import com.github.fishio.multiplayer.TestMultiplayerPlayingField;
import com.github.fishio.multiplayer.server.FishServerEntitiesMessage;

/**
 * Test class for the MultiplayerClientPlayingField.
 */
public class TestMultiplayerClientPlayingField extends TestMultiplayerPlayingField {
	
	@Override
	public MultiplayerClientPlayingField getPlayingField(int fps, Canvas canvas) {
		return new MultiplayerClientPlayingField(fps, canvas, 1280, 720);
	}
	
	@Override
	public MultiplayerClientPlayingField getField() {
		return (MultiplayerClientPlayingField) super.getField();
	}
	
	/**
	 * @param <T>
	 * 		a type which is a subclass of Entity.
	 * @param clazz
	 * 		the Class of the Entity to mock.
	 * 
	 * @return
	 * 		a new mocked entity with a mocked collision mask and a
	 * 		spied FrozenBehaviour.
	 */
	private <T extends Entity> T mockEntity(Class<T> clazz) {
		//Mock a collision mask
		CollisionMask collisionMask = mock(CollisionMask.class);
		when(collisionMask.getSize()).thenReturn(13.2);
		
		//Spy a Frozen Behaviour
		IMoveBehaviour moveBehaviour = spy(new FrozenBehaviour());
		
		//Mock the entity
		T current = mock(clazz);
		when(current.getBoundingArea()).thenReturn(collisionMask);
		when(current.getBehaviour()).thenReturn(moveBehaviour);
		when(current.getEntityId()).thenReturn(12);
		
		return current;
	}
	
	/**
	 * @return
	 * 		a new spied EnemyFish with a mocked collision mask and a
	 * 		spied FrozenBehaviour.
	 */
	private EnemyFish spyEntity(int id) {
		//Mock a collision mask
		CollisionMask collisionMask = mock(CollisionMask.class);
		when(collisionMask.getSize()).thenReturn(13.2);
		
		//Spy a Frozen Behaviour
		IMoveBehaviour moveBehaviour = spy(new FrozenBehaviour());
		
		EnemyFish enemyFish = new EnemyFish(collisionMask, null, 0, 0);
		enemyFish.setBehaviour(moveBehaviour);
		
		//Spy on the fish
		EnemyFish spiedFish = spy(enemyFish);
		
		//"Set" the entity id.
		when(spiedFish.getEntityId()).thenReturn(id);
		return spiedFish;
	}
	
	
	/**
	 * Test for {@link MultiplayerClientPlayingField#updateEntity(Entity, Entity)}.
	 */
	@Test
	public void testUpdateEntity() {
		Entity current = mockEntity(Entity.class);
		Entity updated = mockEntity(Entity.class);
		
		getField().updateEntity(current, updated);
		
		verify(current.getBoundingArea()).updateTo(updated.getBoundingArea());
		verify(current.getBehaviour()).updateTo(updated.getBehaviour());
	}
	
	/**
	 * Test for {@link MultiplayerClientPlayingField#updateEntity(Entity, Entity)},
	 * when the updated entity is dead.
	 */
	@Test
	public void testUpdateEntityDead() {
		Entity current = mockEntity(Entity.class);
		Entity updated = mockEntity(Entity.class);
		when(updated.isDead()).thenReturn(true);
		
		getField().updateEntity(current, updated);
		
		verify(current).kill();
		verify(current.getBoundingArea()).updateTo(updated.getBoundingArea());
		verify(current.getBehaviour()).updateTo(updated.getBehaviour());
	}
	
	/**
	 * Test for {@link MultiplayerClientPlayingField#updateEntity(Entity, Entity)},
	 * when the entity being updated is a PlayerFish.
	 */
	@Test
	public void testUpdateEntityPlayer() {
		PlayerFish current = mockEntity(PlayerFish.class);
		PlayerFish updated = mockEntity(PlayerFish.class);
		
		getField().updateEntity(current, updated);
		
		//Updating an entity that is not our own player should not impact things
		verify(current.getBoundingArea()).updateTo(updated.getBoundingArea());
		verify(current.getBehaviour()).updateTo(updated.getBehaviour());
	}
	
	/**
	 * Test for {@link MultiplayerClientPlayingField#updateEntity(Entity, Entity)},
	 * when the entity being updated is our own playerfish.
	 */
	@Test
	public void testUpdateEntityOwnPlayer() {
		PlayerFish current = mockEntity(PlayerFish.class);
		PlayerFish updated = mockEntity(PlayerFish.class);
		
		getField().setOwnPlayer(current);
		
		getField().updateEntity(current, updated);
		
		//Updating our own player should NOT update the bounding area or behaviour.
		verify(current.getBoundingArea(), never()).updateTo(updated.getBoundingArea());
		verify(current.getBehaviour(), never()).updateTo(updated.getBehaviour());
		
		//It should update the size of the bounding area.
		verify(current.getBoundingArea()).setSize(updated.getBoundingArea().getSize());
	}
	
	/**
	 * Test for
	 * {@link MultiplayerClientPlayingField#updateEntities(FishServerEntitiesMessage)}.
	 */
	@Test
	public void testUpdateEntities() {
		//Create 3 entities with different entityId's
		Entity entity1 = spyEntity(1);
		Entity entity2 = spyEntity(2);
		Entity entity3 = spyEntity(3);
		
		//Add 1 and 2 to the message
		ArrayList<Entity> list = new ArrayList<>();
		list.add(entity1);
		list.add(entity2);
		FishServerEntitiesMessage fsem = new FishServerEntitiesMessage(list);
		
		//Add 1 and 3 to the field
		getField().add(entity1);
		getField().add(entity3);
		
		//Spy on the field and update the entities
		MultiplayerClientPlayingField fieldSpy = spy(getField());
		fieldSpy.updateEntities(fsem);
		
		//Entity1 should have been updated
		verify(fieldSpy).updateEntity(entity1, entity1);
		
		//Entity2 should have been added
		verify(fieldSpy).add(entity2);
		
		//Entity3 should have been removed
		verify(fieldSpy).remove(entity3);
	}
}
