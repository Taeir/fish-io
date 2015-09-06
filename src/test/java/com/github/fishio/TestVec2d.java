package com.github.fishio;

import static org.junit.Assert.*;

import org.junit.Test;

/** 
 * Class for testing the Vec2d class.
 * @author Jesse Arens
 *
 */
public class TestVec2d {
	/**
	 * Test for {@link Vec2d#Vec2d()}
	 * @return 
	 */
	@Test
	public void testVec2d()
	{
		Vec2d vec1 = new Vec2d(1,1);
		assertEquals(1,vec1.x,0.0);
		assertEquals(1,vec1.y,0.0);
		
		Vec2d vec2 = new Vec2d(vec1);
		assertEquals(1,vec2.x,0.0);
		assertEquals(1,vec2.y,0.0);
	}
	
	
	/**
	 * Test for {@link Vec2d#setX(double x)}
	 */
	@Test
	public void testsetX()
	{
		Vec2d vec1 = new Vec2d();
		vec1.setX(1);
		assertEquals(1,vec1.x,0.0);
	}
	
	/**
	 * Test for {@link Vec2d#setY(double y)}
	 */
	@Test
	public void testsetY()
	{
		Vec2d vec1 = new Vec2d();
		vec1.setY(1);
		assertEquals(1,vec1.y,0.0);
	}
	
	/**
	 * Test for {@link Vec2d#set(double x, double y)}
	 */
	@Test
	public void testset()
	{
		Vec2d vec1 = new Vec2d();
		vec1.set(1,1);
		assertEquals(1,vec1.x,0.0);
		assertEquals(1,vec1.y,0.0);
	}
	
	/**
	 * Test for {@link Vec2d#distanceSquared(Vec2d v)}
	 */
	@Test
	public void testdistanceSquared()
	{
		Vec2d vec1 = new Vec2d(1,1);
		Vec2d vec2 = new Vec2d(1,2);
		assertEquals(1,vec1.distanceSquared(vec2),0.0);
	}
	
	/**
	 * Test for {@link Vec2d#distance()}
	 */
	@Test
	public void testdistance()
	{
		Vec2d vec1 = new Vec2d(1,1);
		Vec2d vec2 = new Vec2d(1,2);
		assertEquals(1,vec1.distance(1,0),0.0);
		assertEquals(1,vec1.distance(vec2),0.0);
	}
	
	/**
	 * Test for {@link Vec2d#lengthSquared()}
	 */
	@Test
	public void testlengthSquared()
	{
		Vec2d vec1 = new Vec2d(1,1);
		assertEquals(2,vec1.lengthSquared(),0.0);
	}
	
	/**
	 * Test for {@link Vec2d#length()}
	 */
	@Test
	public void testlength()
	{
		Vec2d vec1 = new Vec2d(0,1);
		assertEquals(1,vec1.length(),0.0);
		
		Vec2d vec2 = new Vec2d(-1,0);
		assertEquals(1,vec2.length(),0.0);
	}
	
	/**
	 * Test for {@link Vec2d#normalize()} in the case of an exception.
	 */
	@Test
	public void testnormalizeException()
	{
		Vec2d vec1 = new Vec2d();
		try {vec1.normalize(); fail(); } catch (Exception e) {};
	}
	
	/**
	 * Test for {@link Vec2d#normalize()} in the case of no exception.
	 */
	@Test
	public void testnormalizeNoException()
	{
		Vec2d vec1 = new Vec2d(2,0);
		assertEquals(1,vec1.normalize().x,0.0);
		assertEquals(0,vec1.normalize().y,0.0);	
	}
	
	/**
	 * Test for {@link Vec2d#hashCode()}.
	 */
	@Test
	public void testHashCode() {
		Vec2d vec1 = new Vec2d(6,5);
		Vec2d vec2 = new Vec2d(6,5);
		
		assertEquals(vec1.hashCode(), vec2.hashCode());
	}
	
	/**
	 * Test for {@link Vec2d#equals(Object)} with null (false).
	 */
	@Test
	public void testEqualsObjectNull() {
		Vec2d vec1 = new Vec2d(6,5);
		assertNotEquals(vec1,null);
	}
	
	/**
	 * Test for {@link Vec2d#equals(Object)} with itself (true).
	 */
	@Test
	public void testEqualsObjectSelf() {
		Vec2d vec1 = new Vec2d(6,5);
		assertEquals(vec1, vec1);
	}
	
	/**
	 * Test for {@link Vec2d#equals(Object)} with an equal object (true).
	 */
	@Test
	public void testEqualsObjectSame() {
		Vec2d vec1 = new Vec2d(6,5);
		Vec2d vec2 = new Vec2d(6,5);
		
		assertEquals(vec1, vec2);
	}
	
	/**
	 * Test for {@link Vec2d#equals(Object)} with an unequal object.
	 * In this case both x and y are unequal.
	 */
	@Test
	public void testEqualsObjectUnequal1() {
		Vec2d vec1 = new Vec2d(6,5);
		Vec2d vec2 = new Vec2d(5,6);
		
		assertNotEquals(vec1, vec2);
		
	}
	/**
	 * Test for {@link Vec2d#equals(Object)} with an unequal object.
	 * In this case x is equal, but y is not.
	 */
	@Test
	public void testEqualsObjectUnequal2() {
		Vec2d vec1 = new Vec2d(4,5);
		Vec2d vec2 = new Vec2d(4,6);
		
		assertNotEquals(vec1, vec2);
	}
	
	/**
	 * Test for {@link Vec2d#equals(Object)} with an unequal object.
	 * In this case x is not equal, but y is.
	 */
	@Test
	public void testEqualsObjectUnequal3() {
		Vec2d vec1 = new Vec2d(6,5);
		Vec2d vec2 = new Vec2d(5,5);
		
		assertNotEquals(vec1, vec2);
	}
	
	/**
	 * Test for {@link Vec2d#toString()}
	 */
	@Test
	public void testtoString()
	{
		Vec2d vec1 = new Vec2d(6,5);
		String represent = new String("Vec2d [x=6.0, y=5.0]");
		assertEquals(represent,vec1.toString());
	}
}