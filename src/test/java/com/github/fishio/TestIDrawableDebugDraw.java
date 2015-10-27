package com.github.fishio;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
		
		this.graphicsContext = mock(GraphicsContext.class);
		
		when(collisionArea.getRotation()).thenReturn(42D);
		when(collisionArea.getCenterX()).thenReturn(69D);
		when(collisionArea.getCenterY()).thenReturn(1024D);
		when(collisionArea.getHeight()).thenReturn(22D);
		when(collisionArea.getSize()).thenReturn(33D);
		when(collisionArea.getBottomLeft()).thenReturn(new Vec2d(1, 1));
		when(collisionArea.getBottomRight()).thenReturn(new Vec2d(2, 2));
		when(collisionArea.getTopLeft()).thenReturn(new Vec2d(3, 3));
		when(collisionArea.getTopRight()).thenReturn(new Vec2d(4, 4));
		
		HashSet<Vec2d> set = new HashSet<>();
		set.add(new Vec2d());
		set.add(new Vec2d(100,100));
		set.add(new Vec2d(200,200));
		when(collisionArea.getMask()).thenReturn(set);
	}
	
	/**
	 * Tests the debugDrawMethod.
	 */
	@Test
	public void testDebugDraw() {
		drawable.debugDraw(graphicsContext, collisionArea);
		
		// Verifying the angle and sizes are being drawn in the correct place.
		verify(graphicsContext).fillText("angle: 42.0", 69D, 1003D);
		verify(graphicsContext).fillText("size: 33.0", 69D, 988D);
		
		// Verifying the collisionmask is properly being drawn.
		verify(graphicsContext).fillOval(0, 0, 1, 1);
		verify(graphicsContext).fillOval(100, 100, 1, 1);
		verify(graphicsContext).fillOval(200, 200, 1, 1);
		
		// Verifying the center is properly being drawn.
		verify(graphicsContext).fillOval(69D, 1024D, 2, 2);
		
		// Verifying the corners of collisionarea are properly being drawn.
		verify(graphicsContext).fillOval(1D, 1D, 2, 2);
		verify(graphicsContext).fillOval(2D, 2D, 2, 2);
		verify(graphicsContext).fillOval(3D, 3D, 2, 2);
		verify(graphicsContext).fillOval(4D, 4D, 2, 2);
	}
	
}
