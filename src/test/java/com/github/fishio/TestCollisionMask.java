package com.github.fishio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import java.util.HashSet;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javafx.application.Platform;
import javafx.scene.image.Image;

/**
 * Test class for the CollisionMask class.
 */
public class TestCollisionMask extends TestICollisionArea {
	private static boolean started = true;
	private CollisionMask ca;
	
	/**
	 * Setup the GUI, needed to create Image(sprite) objects.
	 * @throws Exception
	 * 			when something goes wrong with the thread.
	 */
	@BeforeClass
	public static void setUpClass() throws Exception {
		if (FishIO.getInstance() != null) {
			started = false;
			return;
		}
		
		new Thread(() -> FishIO.main(new String[0])).start();
		
		// wait for FishIO to initialize
		while (FishIO.getInstance() == null) {
			Thread.sleep(50L);
		}
	}
	
	/**
	 * Stop the GUI thread.
	 */
	@AfterClass
	public static void breakDownClass() {
		if (started) {
			Platform.exit();
		}
	}
	
	/**
	 * Before method
	 * Creates a mask with a simple sprite.
	 */
	@Before
	public void before() {
		Image image = new Image("AlphaDataTest.png");
		boolean[][] data = CollisionMask.buildData(image);
		ca = new CollisionMask(new Vec2d(0, 0), 4, 4, data, 1);
	}
	
	@Override
	public ICollisionArea getCollisionArea() {
		return new CollisionMask(new Vec2d(0, 0), 10, 5, null, 1);
	}

	/**
	 * Test for {@link CollisionMask#buildData(Image).
	 */
	@Test
	public void testBuildData() {
		boolean[][] exp = {{true, false, false},
						   {true, true, true},
						   {false, false, true}};
		
		Image image = new Image("AlphaDataTest.png");
		boolean[][] data = CollisionMask.buildData(image);
		
		assertArrayEquals(exp, data);
	}
	
	/**
	 * Test for {@link CollisionMask#getAlphaRatio(boolean[][]).
	 */
	@Test
	public void testGetAlphaRatio() {
		Image image = new Image("AlphaDataTest.png");
		boolean[][] data = CollisionMask.buildData(image);
		double ratio = CollisionMask.getAlphaRatio(data);
		assertEquals(5.0 / 9.0 , ratio, 0.1E-12);
	}
	
	/**
	 * Test for {@link CollisionMask#getMask().
	 * Test to check the size of the mask.
	 */
	@Test
	public void testGetMaskSize() {
		Image image = new Image("AlphaDataTest.png");
		boolean[][] data = CollisionMask.buildData(image);
		CollisionMask ca = new CollisionMask(new Vec2d(0, 0), 10, 10, data, 1);	
		
		assertEquals((int) (100 / 9.0 * 4.0), ca.getMask().size());
	}
	
	/**
	 * Test for {@link CollisionMask#getMask().
	 * Test for getting the mask of a rotated mask.
	 */
	@Test
	public void testGetMask() {
		Image image = new Image("AlphaDataTest.png");
		boolean[][] data = CollisionMask.buildData(image);
		CollisionMask ca = new CollisionMask(new Vec2d(0, 0), 4, 4, data, 1);	
		ca.setRotation(23.45);
		
		HashSet<Vec2d> exp = new HashSet<Vec2d>();
		exp.add(new Vec2d(-2, -1));
		exp.add(new Vec2d(-2, 0));
		exp.add(new Vec2d(-1, -1));
		exp.add(new Vec2d(-1, 0));
		exp.add(new Vec2d(0, -1));
		exp.add(new Vec2d(0, 0));
		exp.add(new Vec2d(1, 0));
		
		assertEquals(exp, ca.getMask());
	}
	
	/**
	 * Test for {@link CollisionMask#intersects(ICollisionArea)}.
	 * Test for intersection with a normal bounding box.
	 */
	@Test
	public void testIntersectsBox() {
		BoundingBox box = new BoundingBox(new Vec2d(0, 0), 5, 5);
		assertTrue(ca.intersects(box));
	}
	
	/**
	 * Test for {@link CollisionMask#intersects(ICollisionArea)}.
	 * Test for no intersection with a normal box;
	 */
	@Test
	public void testNotIntersectsBox() {
		BoundingBox box = new BoundingBox(new Vec2d(20, 20), 5, 5);
		assertFalse(ca.intersects(box));
	}
	
	/**
	 * Test for {@link CollisionMask#intersects(ICollisionArea)}.
	 * Test for intersection with itself.
	 */
	@Test
	public void testIntersectsSelf() {
		assertTrue(ca.intersects(ca));
	}
}
