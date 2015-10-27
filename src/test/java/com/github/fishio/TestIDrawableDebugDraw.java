package com.github.fishio;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * Test class for the debugDraw in IDrawable.
 */
public class TestIDrawableDebugDraw {
	
	private IDrawable drawable;
	private CollisionMask collisionArea;
	private GraphicsContext graphicsContext;
	
	/**
	 * Initialises the important fields for testing.
	 * The collisionArea field and graphicsContext field are both
	 * mocked classes so stubbing and verifying will be easy.
	 */
	@Before
	public void setUp() {
		this.collisionArea = Mockito.mock(CollisionMask.class);
		this.drawable = new EnemyFish(collisionArea, null, 0, 0);
		
		graphicsContext = Mockito.spy((new Canvas()).getGraphicsContext2D());
	}
	
}
