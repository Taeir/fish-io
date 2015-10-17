package com.github.fishio.power_ups;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import org.junit.Before;
import org.junit.Test;

import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;
import com.github.fishio.SinglePlayerPlayingField;
import com.github.fishio.behaviours.FrozenBehaviour;
import com.github.fishio.behaviours.KeyListenerBehaviour;
import com.github.fishio.game.GameThread;

/**
 * Tests the PuSuperSpeed class.
 */
public class TestPuSuperSpeed extends TestDurationPowerUp {

	private SuperSpeedPowerUp pu;
	private PlayingField pf;
	
	private static final double DELTA = 0.0000001D;
			
	/**
	 * Creates a new PoFreeze object to test and sets
	 * the PlayingField pf field.
	 */
	@Before
	public void setUp() {
		this.pf = mock(SinglePlayerPlayingField.class);
		GameThread gt = spy(new GameThread(pf));
		when(pf.getGameThread()).thenReturn(gt);
		
		this.pu = spy(new SuperSpeedPowerUp(null, pf, mock(Image.class)));
		
		//To prevent NullPointerExceptions, mocking the target of the PowerUp.
		PlayerFish fish = mock(PlayerFish.class);
		this.pu.setTarget(fish);
		
		KeyListenerBehaviour klb = spy(new KeyListenerBehaviour(
				mock(Scene.class), KeyCode.A, KeyCode.A, KeyCode.A, KeyCode.A, 5.0, 7.0));
		when(fish.getBehaviour()).thenReturn(klb);
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
		PlayerFish pf = pu.getTarget();
		KeyListenerBehaviour klb = (KeyListenerBehaviour) pf.getBehaviour();
		
		double oldAcceleration = klb.getAcceleration();
		double oldMaxSpeed = klb.getMaxSpeed();
		
		pu.startEffect();
		
		assertEquals(oldAcceleration *  SuperSpeedPowerUp.ACCELERATION_FACTOR, klb.getAcceleration(), DELTA);
		assertEquals(oldMaxSpeed * SuperSpeedPowerUp.MAX_SPEED_FACTOR, klb.getMaxSpeed(), DELTA);
	}

	@Override
	public void testPreTickEffect() { /* Nothing happens */ }

	@Override
	public void testPostTickEffect() { /* Nothing happens */ }

	@Override
	public void testEndEffect() {
		PlayerFish pf = pu.getTarget();
		KeyListenerBehaviour klb = (KeyListenerBehaviour) pf.getBehaviour();
		
		double oldAcceleration = klb.getAcceleration();
		double oldMaxSpeed = klb.getMaxSpeed();
		
		//Starting the Speed and Acceleration change, then ending it immediately.
		pu.startEffect();
		pu.endEffect();
		
		//Making sure that acceleration and MaxSpeed haven't changed.
		assertEquals(oldAcceleration, klb.getAcceleration(), DELTA);
		assertEquals(oldMaxSpeed, klb.getMaxSpeed(), DELTA);
	}
	
	/**
	 * Tests the endEffect method by setting the behaviour of the PlayerFish to a
	 * non-KeyListenerBehaviour.
	 */
	@Test
	public void testEndEffectOtherBehaviour() {
		PlayerFish pf = pu.getTarget();
		pf.setBehaviour(new FrozenBehaviour());
		
		//Starting the Speed and Acceleration change, then ending it immediately.
		pu.startEffect();
		
		try {
			pu.endEffect();
		} catch (ClassCastException e) {
			fail(); // The endEffect should not result in a ClassException as a result of the
			// behaviour being a non-KeyListenerBehaviour
		}
		
	}

	@Override
	public PlayingField getPlayingField() {
		return pf;
	}

	@Override
	public void testGetName() {
		assertEquals("Super Speed", pu.getName());
	}

}
