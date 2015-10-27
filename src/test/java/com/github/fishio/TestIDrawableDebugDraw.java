package com.github.fishio;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import javafx.scene.canvas.GraphicsContext;

/**
 * Test class for the debugDraw in IDrawable.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(GraphicsContext.class)
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
		
		this.graphicsContext = PowerMockito.mock(GraphicsContext.class);
		
		Mockito.when(collisionArea.getRotation()).thenReturn(42D);
		Mockito.when(collisionArea.getCenterX()).thenReturn(69D);
		Mockito.when(collisionArea.getCenterY()).thenReturn(1024D);
		Mockito.when(collisionArea.getHeight()).thenReturn(22D);
		Mockito.when(collisionArea.getBottomLeft()).thenReturn(new Vec2d(1, 1));
		Mockito.when(collisionArea.getBottomRight()).thenReturn(new Vec2d(2, 2));
		Mockito.when(collisionArea.getTopLeft()).thenReturn(new Vec2d(3, 3));
		Mockito.when(collisionArea.getTopRight()).thenReturn(new Vec2d(4, 4));
	}
	
}
