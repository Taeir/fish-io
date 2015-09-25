package com.github.fishio.power_ups;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.mockito.Mockito;

import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;

/**
 * Tests the PuSuperSpeed class.
 */
public class TestPuSuperSpeed extends TestDurationPowerUp {

	private PuSuperSpeed pu;
	private PlayingField pf;
	
	/**
	 * Creates a new PoFreeze object to test and sets
	 * the PlayingField pf field.
	 */
	@Before
	public void setUp() {
		this.pf = Mockito.mock(PlayingField.class);
		this.pu = Mockito.spy(new PuSuperSpeed(null, pf));
		
		//To prevent NullPointerExceptions, mocking the target of the PowerUp.
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
		PlayerFish pf = pu.getTarget();
		double oldAcceleration = pf.getAcceleration();
		double oldMaxSpeed = pf.getMaxSpeed();
		
		pu.startEffect();
		
		assertEquals(oldAcceleration *  PuSuperSpeed.ACCELERATION_FACTOR, 
				pf.getAcceleration(), 0.0000001D);
		assertEquals(oldMaxSpeed * PuSuperSpeed.MAX_SPEED_FACTOR, 
				pf.getMaxSpeed(), 0.0000001D);
	}

	@Override
	public void testPreTickEffect() { /* Nothing happens */ }

	@Override
	public void testPostTickEffect() { /* Nothing happens */ }

	@Override
	public void testEndEffect() {
		PlayerFish pf = pu.getTarget();
		double oldAcceleration = pf.getAcceleration();
		double oldMaxSpeed = pf.getMaxSpeed();
		
		//Starting the Speed and Acceleration change, then ending it immediately.
		pu.startEffect();
		pu.endEffect();
		
		//Making sure that acceleration and MaxSpeed haven't changed.
		assertEquals(oldAcceleration, pf.getAcceleration(), 0.0000001D);
		assertEquals(oldMaxSpeed, pf.getMaxSpeed(), 0.0000001D);
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
