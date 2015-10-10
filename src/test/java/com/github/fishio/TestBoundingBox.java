package com.github.fishio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

/**
 * Class for testing the BoundingBox.
 */
public class TestBoundingBox extends TestICollisionArea {

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
	 * Test for {@link BoundingBox#getTopLeft()}.
	 */
	@Test
	public void testGetTopLeft() {
		BoundingBox bb = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		
		assertEquals(new Vec2d(1.0, 2.0), bb.getTopLeft());
	}
	
	/**
	 * Test for {@link BoundingBox#getTopLeft()} using 
	 * the constructor {@link BoundingBox#BoundingBox(Vec2d, double, double)}.
	 */
	@Test
	public void testGetTopLeft2() {
		BoundingBox bb = new BoundingBox(new Vec2d(5.0, 5.0), 4.0, 5.0);
		
		assertEquals(new Vec2d(3.0, 2.5), bb.getTopLeft());
	}

	/**
	 * Test for {@link BoundingBox#getTopRight()}.
	 */
	@Test
	public void testGetTopRight() {
		BoundingBox bb = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		
		assertEquals(new Vec2d(3.0, 2.0), bb.getTopRight());
	}
	
	/**
	 * Test for {@link BoundingBox#getTopRight()} using 
	 * the constructor {@link BoundingBox#BoundingBox(Vec2d, double, double)}.
	 */
	@Test
	public void testGetTopRight2() {
		BoundingBox bb = new BoundingBox(new Vec2d(5.0, 5.0), 4.0, 5.0);
		
		assertEquals(new Vec2d(7.0, 2.5), bb.getTopRight());
	}

	/**
	 * Test for {@link BoundingBox#getBottomLeft()}.
	 */
	@Test
	public void testGetBottomLeft() {
		BoundingBox bb = new BoundingBox(1.0, 2.0, 3.0, 5.0);
		
		assertEquals(new Vec2d(1.0, 5.0), bb.getBottomLeft());
	}
	
	/**
	 * Test for {@link BoundingBox#getBottomLeft()} using 
	 * the constructor {@link BoundingBox#BoundingBox(Vec2d, double, double)}.
	 */
	@Test
	public void testGetBottomLeft2() {
		BoundingBox bb = new BoundingBox(new Vec2d(5.0, 5.0), 4.0, 5.0);
		
		assertEquals(new Vec2d(3.0, 7.5), bb.getBottomLeft());
	}

	/**
	 * Test for {@link BoundingBox#getBottomRight()}.
	 */
	@Test
	public void testGetBottomRight() {
		BoundingBox bb = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		
		assertEquals(new Vec2d(3.0, 4.0), bb.getBottomRight());
	}
	
	/**
	 * Test for {@link BoundingBox#getMBottomRight()} using 
	 * the constructor {@link BoundingBox#BoundingBox(Vec2d, double, double)}.
	 */
	@Test
	public void testGetBottomRight2() {
		BoundingBox bb = new BoundingBox(new Vec2d(5.0, 5.0), 4.0, 5.0);
		
		assertEquals(new Vec2d(7.0, 7.5), bb.getBottomRight());
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
	 * Test for moving a bounding vox by vectors.
	 * {@link BoundingBox#move(Vec2d)}
	 */
	@Test
	public void testMoveVec2d() {
		BoundingBox bb = new BoundingBox(1.0, 2.0, 3.0, 4.0);
		
		bb.move(new Vec2d(-5.3, 7.1));
		
		assertEquals(-4.3, bb.getTopLeft().x, 0.0000001D);
		assertEquals(-5.1, bb.getTopLeft().y, 0.0000001D);
		assertEquals(-2.3, bb.getBottomRight().x, 0.0000001D);
		assertEquals(-3.1, bb.getBottomRight().y, 0.0000001D);
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
	 * Test for {@link BoundingBox#increaseSize(double)}.
	 */
	@Test
	public void testIncreaseSize() {
		BoundingBox bb = new BoundingBox(1.0, 1.0, 3.0, 2.0);
		
		bb.increaseSize(10.0);
		
		assertEquals(0.0, bb.getTopLeft().x, 0000001D);
		assertEquals(0.0, bb.getTopLeft().y, 0000001D);
		assertEquals(4.0, bb.getBottomRight().x, 0000001D);
		assertEquals(3.0, bb.getBottomRight().y, 0000001D);
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

	@Override
	public ICollisionArea getCollisionArea() {
		return new BoundingBox(new Vec2d(0, 0), 10, 5);
	}
}
