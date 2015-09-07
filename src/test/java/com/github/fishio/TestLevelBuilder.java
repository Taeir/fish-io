package com.github.fishio;

import static org.junit.Assert.assertEquals;

import javax.swing.text.Position;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Class for testing the LevelBuilder class.
 * 
 * @author Jesse Arens
 * 		
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(LevelBuilder.class)
public class TestLevelBuilder {

	@Before
	public void setUp(){
		PowerMock.mockStatic(Math.class);
		EasyMock.expect(Math.random().andReturn(0.5).anyTimes();
		PowerMock.replay(Math.class);
		
		LevelBuilder level = new LevelBuilder();
	}
	
	public void EnemyFish
	
	randomizedFish(){
		assertEquals(100,width,0.0);
		assertEquals(37.5,height,0.0);
		assertEquals(null,position.x,0.0);
		assertEquals(null,position.y,0.0);
	}

