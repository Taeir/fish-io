package com.github.fishio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

/**
 * Class for testing the BoundingBox.
 */
public class TestBoundingBox {

	/**
	 * Test for {@link BoundingBox#hashCode()}.
	 */
	@Test
	public void testHashCode() {
		BoundingBox bb1 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		BoundingBox bb2 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		
		assertEquals(bb1.hashCode(), bb2.hashCode());
	}

	/**
	 * Test for {@link BoundingBox#getMinX()}.
	 */
	@Test
	public void testGetMinX() {
		BoundingBox bb = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		
		assertEquals(1.0, bb.getMinX(), 0.0D);
	}
	
	/**
	 * Test for {@link BoundingBox#getMinX()} using 
	 * the constructor {@link BoundingBox#BoundingBox(Vec2d, double, double)}.
	 */
	@Test
	public void testGetMinX2() {
		BoundingBox bb = new BoundingBox(new Vec2d(5.0, 5.0), 4.0, 5.0);
		
		assertEquals(3.0, bb.getMinX(), 0.0D);
	}

	/**
	 * Test for {@link BoundingBox#getMaxX()}.
	 */
	@Test
	public void testGetMaxX() {
		BoundingBox bb = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		
		assertEquals(3.0, bb.getMaxX(), 0.0D);
	}
	
	/**
	 * Test for {@link BoundingBox#getMaxX()} using 
	 * the constructor {@link BoundingBox#BoundingBox(Vec2d, double, double)}.
	 */
	@Test
	public void testGetMaxX2() {
		BoundingBox bb = new BoundingBox(new Vec2d(5.0, 5.0), 4.0, 5.0);
		
		assertEquals(7.0, bb.getMaxX(), 0.0D);
	}

	/**
	 * Test for {@link BoundingBox#getMinY()}.
	 */
	@Test
	public void testGetMinY() {
		BoundingBox bb = new BoundingBox(1.0, 2.0, 3.0, 5.0);
		
		assertEquals(2.0, bb.getMinY(), 0.0D);
	}
	
	/**
	 * Test for {@link BoundingBox#getMinY()} using 
	 * the constructor {@link BoundingBox#BoundingBox(Vec2d, double, double)}.
	 */
	@Test
	public void testGetMinY2() {
		BoundingBox bb = new BoundingBox(new Vec2d(5.0, 5.0), 4.0, 5.0);
		
		assertEquals(2.5, bb.getMinY(), 0.0D);
	}

	/**
	 * Test for {@link BoundingBox#getMaxY()}.
	 */
	@Test
	public void testGetMaxY() {
		BoundingBox bb = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		
		assertEquals(4.0, bb.getMaxY(), 0.0D);
	}
	
	/**
	 * Test for {@link BoundingBox#getMaxY()} using 
	 * the constructor {@link BoundingBox#BoundingBox(Vec2d, double, double)}.
	 */
	@Test
	public void testGetMaxY2() {
		BoundingBox bb = new BoundingBox(new Vec2d(5.0, 5.0), 4.0, 5.0);
		
		assertEquals(7.5, bb.getMaxY(), 0.0D);
	}

	/**
	 * Test for {@link BoundingBox#getCenterX()}.
	 */
	@Test
	public void testGetCenterX() {
		BoundingBox bb = new BoundingBox(2.0, 1.0, 4.0, 7.0);
		
		assertEquals(3.0, bb.getCenterX(), 0.0D);
	}

	/**
	 * Test for {@link BoundingBox#getCenterY()}.
	 */
	@Test
	public void testGetCenterY() {
		BoundingBox bb = new BoundingBox(2.0, 1.0, 4.0, 7.0);
		
		assertEquals(4.0, bb.getCenterY(), 0.0D);
	}

	/**
	 * Test for {@link BoundingBox#getWidth()}.
	 */
	@Test
	public void testGetWidth() {
		BoundingBox bb = new BoundingBox(3.0, 1.0, 5.0, 7.0);
		
		assertEquals(2.0, bb.getWidth(), 0.0D);
	}

	/**
	 * Test for {@link BoundingBox#getHeight()}.
	 */
	@Test
	public void testGetHeight() {
		BoundingBox bb = new BoundingBox(3.0, 1.0, 5.0, 7.0);
		
		assertEquals(6.0, bb.getHeight(), 0.0D);
	}

	/**
	 * Test for {@link BoundingBox#move(Direction, double)}.
	 */
	@Test
	public void testMoveDirectionDouble() {
		BoundingBox bb = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		
		bb.move(Direction.UP, 1.0);
		assertEquals(1.0, bb.getMinX(), 0.0000001D);
		assertEquals(1.0, bb.getMinY(), 0.0000001D);
		assertEquals(3.0, bb.getMaxX(), 0.0000001D);
		assertEquals(3.0, bb.getMaxY(), 0.0000001D);
	}

	/**
	 * Test for moving a bounding box by radians.
	 * {@link BoundingBox#move(double, double)}
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testMoveDoubleDouble() {
		BoundingBox bb = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		
		//Move up right
		bb.move(0.25 * Math.PI, 1.0);
		
		assertEquals(1.0 + 0.5 * Math.sqrt(2), bb.getMinX(), 0.0000001D);
		assertEquals(2.0 + 0.5 * Math.sqrt(2), bb.getMinY(), 0.0000001D);
		assertEquals(3.0 + 0.5 * Math.sqrt(2), bb.getMaxX(), 0.0000001D);
		assertEquals(4.0 + 0.5 * Math.sqrt(2), bb.getMaxY(), 0.0000001D);
	}
	
	/**
	 * Test for moving a bounding vox by vectors.
	 * {@link BoundingBox#move(Vec2d)}
	 */
	@Test
	public void testMoveVec2d() {
		BoundingBox bb = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		
		bb.move(new Vec2d(-5.3, 7.1));
		
		assertEquals(-4.3, bb.getMinX(), 0.0000001D);
		assertEquals(-5.1, bb.getMinY(), 0.0000001D);
		assertEquals(-2.3, bb.getMaxX(), 0.0000001D);
		assertEquals(-3.1, bb.getMaxY(), 0.0000001D);
	}
	
	/**
	 * Test for {@link BoundingBox#getSize()}.
	 */
	@Test
	public void testGetSize() {
		BoundingBox bb = new BoundingBox(new Vec2d(5.0, 5.0), 3.0, 5.0);
		
		assertEquals(15.0, bb.getSize(), 0.0000001D);
	}

	/**
	 * Test for {@link BoundingBox#equals(Object)} with null (false).
	 */
	@Test
	public void testEqualsObjectNull() {
		BoundingBox bb = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		assertNotEquals(null, bb);
	}
	
	/**
	 * Test for {@link BoundingBox#equals(Object)} with itself (true).
	 */
	@Test
	public void testEqualsObjectSelf() {
		BoundingBox bb = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		assertEquals(bb, bb);
	}
	
	/**
	 * Test for {@link BoundingBox#equals(Object)} with an equal object (true).
	 */
	@Test
	public void testEqualsObjectSame() {
		BoundingBox bb1 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		BoundingBox bb2 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		
		assertEquals(bb1, bb2);
	}
	
	/**
	 * Test for {@link BoundingBox#equals(Object)} with an unequal object.
	 */
	@Test
	public void testEqualsObjectUnequal1() {
		BoundingBox bb1 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		BoundingBox bb2 = new BoundingBox(2.0, 2.0, 3.0, 4.0);
		
		assertNotEquals(bb1, bb2);
	}
	
	/**
	 * Test for {@link BoundingBox#equals(Object)} with an unequal object.
	 */
	@Test
	public void testEqualsObjectUnequal2() {
		BoundingBox bb1 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		BoundingBox bb2 = new BoundingBox(1.0, 1.0, 3.0, 4.0);
		
		assertNotEquals(bb1, bb2);
	}
	
	/**
	 * Test for {@link BoundingBox#equals(Object)} with an unequal object.
	 */
	@Test
	public void testEqualsObjectUnequal3() {
		BoundingBox bb1 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		BoundingBox bb2 = new BoundingBox(1.0, 2.0, 2.0, 4.0);
		
		assertNotEquals(bb1, bb2);
	}

	/**
	 * Test for {@link BoundingBox#equals(Object)} with an unequal object.
	 */
	@Test
	public void testEqualsObjectUnequal4() {
		BoundingBox bb1 = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		BoundingBox bb2 = new BoundingBox(1.0, 2.0, 3.0, 5.0);
		
		assertNotEquals(bb1, bb2);
	}
}
