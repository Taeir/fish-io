package com.github.fishio;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

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
		Mockito.when(collisionArea.getSize()).thenReturn(33D);
		Mockito.when(collisionArea.getBottomLeft()).thenReturn(new Vec2d(1, 1));
		Mockito.when(collisionArea.getBottomRight()).thenReturn(new Vec2d(2, 2));
		Mockito.when(collisionArea.getTopLeft()).thenReturn(new Vec2d(3, 3));
		Mockito.when(collisionArea.getTopRight()).thenReturn(new Vec2d(4, 4));
		
		HashSet<Vec2d> set = new HashSet<>();
		set.add(new Vec2d());
		set.add(new Vec2d(100,100));
		set.add(new Vec2d(200,200));
		Mockito.when(collisionArea.getMask()).thenReturn(set);
	}
	
	/**
	 * Tests the debugDrawMethod.
	 */
	@Test
	public void testDebugDraw() {
		drawable.debugDraw(graphicsContext, collisionArea);
		
		// Verifying the angle and sizes are being drawn in the correct place.
		Mockito.verify(graphicsContext).fillText("angle: 42.0", 69D, 1003D);
		Mockito.verify(graphicsContext).fillText("size: 33.0", 69D, 988D);
		
		// Verifying the collisionmask is properly being drawn.
		Mockito.verify(graphicsContext).fillOval(0, 0, 1, 1);
		Mockito.verify(graphicsContext).fillOval(100, 100, 1, 1);
		Mockito.verify(graphicsContext).fillOval(200, 200, 1, 1);
		
		// Verifying the center is properly being drawn.
		Mockito.verify(graphicsContext).fillOval(69D, 1024D, 2, 2);
		
		// Verifying the corners of collisionarea are properly being drawn.
		Mockito.verify(graphicsContext).fillOval(1D, 1D, 2, 2);
		Mockito.verify(graphicsContext).fillOval(2D, 2D, 2, 2);
		Mockito.verify(graphicsContext).fillOval(3D, 3D, 2, 2);
		Mockito.verify(graphicsContext).fillOval(4D, 4D, 2, 2);
	}
	
}
