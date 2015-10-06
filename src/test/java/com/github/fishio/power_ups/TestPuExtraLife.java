package com.github.fishio.power_ups;

import static org.junit.Assert.assertEquals;
import javafx.scene.image.Image;

import org.junit.Before;
import org.mockito.Mockito;

import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;
import com.github.fishio.SinglePlayerPlayingField;

/**
 * Tests the PuExtraLife class.
 */
public class TestPuExtraLife extends TestPowerUp {

	private PuExtraLife pu;
	private PlayingField pf;
	
	/**
	 * Initialises the PuExtraLife field and the PlayingField field
	 * before every test.
	 */
	@Before
	public void setUp() {
		this.pf = Mockito.mock(SinglePlayerPlayingField.class);
		this.pu = Mockito.spy(new PuExtraLife(null, pf, Mockito.mock(Image.class)));
	}
	
	@Override
	public PowerUp getPowerUp() {
		return pu;
	}

	@Override
	public PlayingField getPlayingField() {
		return pf;
	}

	@Override
	public void testExecuteEffect() {
		PlayerFish pf = Mockito.mock(PlayerFish.class);
		
		pu.executeEffect(pf);
		
		//A life should have been added to the PlayerFish.
		Mockito.verify(pf).addLife();
	}

	@Override
	public void testGetName() {
		assertEquals("Extra Life", pu.getName());
	}


}
