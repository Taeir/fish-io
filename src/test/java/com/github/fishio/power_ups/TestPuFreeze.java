package com.github.fishio.power_ups;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.HashSet;

import javafx.scene.image.Image;

import org.junit.Before;

import com.github.fishio.EnemyFish;
import com.github.fishio.Entity;
import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;
import com.github.fishio.behaviours.FrozenBehaviour;
import com.github.fishio.behaviours.RandomBehaviour;
import com.github.fishio.SinglePlayerPlayingField;
import com.github.fishio.game.GameThread;

/**
 * Test class for the PuFreeze class.
 */
public class TestPuFreeze extends TestDurationPowerUp {

	private FreezePowerUp pu;
	private PlayingField pf;
	
	/**
	 * Creates a new PoFreeze object to test and sets
	 * the PlayingField pf field.
	 */
	@Before
	public void setUp() {
		this.pf = mock(SinglePlayerPlayingField.class);
		GameThread gt = spy(new GameThread(pf));
		HashSet<Entity> entities = new HashSet<Entity>();
		when(pf.getGameThread()).thenReturn(gt);
		when(pf.getEntities()).thenReturn(entities);
		this.pu = spy(new FreezePowerUp(null, pf, mock(Image.class)));
		
		//To prevent nullPointerException, mock the target of the PowerUp.
		this.pu.setTarget(mock(PlayerFish.class));
	}
	
	@Override
	public PowerUpDuration getDurationPowerUp() {
		return pu;
	}

	@Override
	public void testGetDuration() {
		assertEquals(10, pu.getDuration());
	}

	@Override
	public void testStartEffect() {
		//Creating our own list of entities for in the PlayingField.
		HashSet<Entity> entities = new HashSet<Entity>();
		EnemyFish ef1 = mock(EnemyFish.class);
		EnemyFish ef2 = mock(EnemyFish.class);
		entities.add(ef1);
		entities.add(mock(PlayerFish.class));
		entities.add(mock(FreezePowerUp.class));
		entities.add(ef2);
		
		//Making the PlayingField return our own entities.
		when(pf.getEntities()).thenReturn(entities);
		
		//Invoking the startEffect method
		pu.startEffect();
		
		//Making sure the EnemyFishes got frozen.
		verify(ef1).setBehaviour(any(FrozenBehaviour.class));
		verify(ef2).setBehaviour(any(FrozenBehaviour.class));
	}

	@Override
	public void testPreTickEffect() { }

	@Override
	public void testPostTickEffect() { }

	@Override
	public void testEndEffect() {
		//Creating our own list of entities for in the PlayingField.
		HashSet<Entity> entities = new HashSet<Entity>();
		EnemyFish ef1 = mock(EnemyFish.class);
		EnemyFish ef2 = mock(EnemyFish.class);
		entities.add(ef1);
		entities.add(mock(PlayerFish.class));
		entities.add(mock(FreezePowerUp.class));
		entities.add(ef2);
		
		//Making the PlayingField return our own entities.
		when(pf.getEntities()).thenReturn(entities);
		
		//Invoking the startEffect method and then the endEffect
		pu.startEffect();
		pu.endEffect();
		
		//Making sure the EnemyFishes got unfrozen (even though they weren't frozen in the first place).
		verify(ef1, atLeastOnce()).setBehaviour(any(RandomBehaviour.class));
		verify(ef2, atLeastOnce()).setBehaviour(any(RandomBehaviour.class));
	}

	@Override
	public PlayingField getPlayingField() {
		return pf;
	}

	@Override
	public void testGetName() {
		assertEquals("Freeze", pu.getName());
	}

}
