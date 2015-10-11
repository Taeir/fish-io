package com.github.fishio.power_ups;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

import org.junit.Before;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

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

	private PuFreeze pu;
	private PlayingField pf;
	
	/**
	 * Creates a new PoFreeze object to test and sets
	 * the PlayingField pf field.
	 */
	@Before
	public void setUp() {
		this.pf = Mockito.mock(SinglePlayerPlayingField.class);
		GameThread gt = Mockito.spy(new GameThread(pf));
		when(pf.getGameThread()).thenReturn(gt);
		this.pu = Mockito.spy(new PuFreeze(null, pf, Mockito.mock(Image.class)));
		
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
		verify(ef1).setBehaviour(Mockito.any(FrozenBehaviour.class));
		verify(ef2).setBehaviour(Mockito.any(FrozenBehaviour.class));
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
		
		//Invoking the startEffect method and then the endEffect
		pu.startEffect();
		pu.endEffect();
		
		EnemyFish ef1 = (EnemyFish) entities.get(0);
		EnemyFish ef2 = (EnemyFish) entities.get(3);
		
		//Making sure the EnemyFishes got unfrozen (even though they weren't frozen in the first place).
		verify(ef1, Mockito.atLeastOnce()).setBehaviour(Mockito.any(RandomBehaviour.class));
		verify(ef2, Mockito.atLeastOnce()).setBehaviour(Mockito.any(RandomBehaviour.class));
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
