package com.github.fishio.power_ups;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.github.fishio.EnemyFish;
import com.github.fishio.Entity;
import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;

/**
 * Test class for the PuFreeze class.
 */
public class TestPuFreeze extends TestDurationPowerUp {

	private PuFreeze pu;
	private PlayingField pf;
	
	/**
	 * Creates a new PoFreeze object to test and sets
	 * the PlayingField pf field.
	 */
	@Before
	public void setUp() {
		this.pf = Mockito.mock(PlayingField.class);
		this.pu = Mockito.spy(new PuFreeze(null, pf));
		
		//To prevent nullPointerException, mock the target of the PowerUp.
		this.pu.setTarget(Mockito.mock(PlayerFish.class));
	}
	
	@Override
	public DurationPowerUp getDurationPowerUp() {
		return pu;
	}

	@Override
	public void testGetDuration() {
		assertEquals(10, pu.getDuration());
	}

	@Override
	public void testStartEffect() {
		//Creating our own list of entities for in the PlayingField.
		List<Entity> entities = new ArrayList<Entity>();
		entities.add(Mockito.mock(EnemyFish.class));
		entities.add(Mockito.mock(PlayerFish.class));
		entities.add(Mockito.mock(PuFreeze.class));
		entities.add(Mockito.mock(EnemyFish.class));
		
		//Making the PlayingField return our own entities.
		when(pf.getEntities()).thenReturn(entities);
		
		//Invoking the startEffect method
		pu.startEffect();
		
		EnemyFish ef1 = (EnemyFish) entities.get(0);
		EnemyFish ef2 = (EnemyFish) entities.get(3);
		
		//Making sure the EnemyFishes got frozen.
		verify(ef1).setFrozen(true);
		verify(ef2).setFrozen(true);
	}

	@Override
	public void testPreTickEffect() { }

	@Override
	public void testPostTickEffect() { }

	@Override
	public void testEndEffect() {
		//Creating our own list of entities for in the PlayingField.
		List<Entity> entities = new ArrayList<Entity>();
		entities.add(Mockito.mock(EnemyFish.class));
		entities.add(Mockito.mock(PlayerFish.class));
		entities.add(Mockito.mock(PuFreeze.class));
		entities.add(Mockito.mock(EnemyFish.class));
		
		//Making the PlayingField return our own entities.
		when(pf.getEntities()).thenReturn(entities);
		
		//Invoking the startEffect method
		pu.endEffect();
		
		EnemyFish ef1 = (EnemyFish) entities.get(0);
		EnemyFish ef2 = (EnemyFish) entities.get(3);
		
		//Making sure the EnemyFishes got unfrozen (even though they weren't frozen in the first place).
		verify(ef1).setFrozen(false);
		verify(ef2).setFrozen(false);
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
