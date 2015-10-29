package com.github.fishio.listeners;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.fishio.FakeException;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.test.util.TestUtil;

/**
 * Interface test for the {@link Listenable} interface.<br>
 * <br>
 * If you need to extend another class, take a look at {@link IListenableTest}.
 */
public abstract class TestListenable {
	
	/**
	 * Set up the logger for testing before running any tests.
	 * This prevents logger output
	 */
	@BeforeClass
	public static void setUpListenableClass() {
		TestUtil.setUpLoggerForTesting(LogLevel.ERROR);
	}
	
	/**
	 * Restore the logger after all tests are done.
	 */
	@AfterClass
	public static void tearDownListenableClass() {
		TestUtil.restoreLogger();
	}
	
	/**
	 * @return
	 * 		a new Listenable of the type that the class implementing this
	 * 		interface is testing.
	 */
	public abstract Listenable getListenable();
	
	/**
	 * Test for {@link Listenable#getListeners()}.
	 */
	@Test
	public void testGetListeners() {
		Listenable l = getListenable();
		
		//Initially, there should be no listeners.
		assertTrue(l.getListeners().isEmpty());
	}
	
	/**
	 * Test for {@link Listenable#registerListener(TickListener)}.
	 */
	@Test
	public void testRegisterListener() {
		Listenable l = getListenable();
		
		//Create a TickListener and register it.
		TickListener tl = mock(TickListener.class);
		l.registerListener(tl);
		
		//Our listener should now be registered.
		assertTrue(l.getListeners().contains(tl));
	}
	
	/**
	 * Test for {@link Listenable#unregisterListener(TickListener)}.
	 */
	@Test
	public void testUnregisterListener() {
		Listenable l = getListenable();
		
		//Create a TickListener and register it.
		TickListener tl = mock(TickListener.class);
		l.registerListener(tl);
		
		//Our listener should now be registered.
		assertTrue(l.getListeners().contains(tl));
		
		//Unregister the listener again.
		l.unregisterListener(tl);
		
		//Our listener should no longer be registered.
		assertFalse(l.getListeners().contains(tl));
	}
	
	/**
	 * Test for {@link Listenable#callPreTick(String)}.
	 */
	@Test
	public void testCallPreTick() {
		Listenable l = getListenable();
		
		//Create a TickListener and register it.
		TickListener tl = mock(TickListener.class);
		l.registerListener(tl);
		
		//Call pretick
		l.callPreTick("PRE");
		
		//Ensure that preTick was called, and only once.
		verify(tl, times(1)).preTick();
		
		//Ensure that postTick was not called.
		verify(tl, times(0)).postTick();
	}
	
	/**
	 * Test for {@link Listenable#callPreTick(String)}, when
	 * {@link TickListener#preTick()} throws an exception.
	 */
	@Test
	public void testCallPreTick2() {
		Listenable l = getListenable();
		
		//Create a TickListener that throws an exception when preTick is called.
		TickListener tl = mock(TickListener.class);
		Mockito.doThrow(new FakeException("Ignore this error")).when(tl).preTick();
		
		l.registerListener(tl);
		
		//Call pretick, no exception should occur.
		l.callPreTick("PRE");
		l.callPreTick(null);
		
		//Ensure that preTick was called twice.
		verify(tl, times(2)).preTick();
		
		//Ensure that postTick was not called.
		verify(tl, times(0)).postTick();
	}
	
	/**
	 * Test for {@link Listenable#callPostTick(String)}.
	 */
	@Test
	public void testCallPostTick() {
		Listenable l = getListenable();
		
		//Create a TickListener and register it.
		TickListener tl = mock(TickListener.class);
		l.registerListener(tl);
		
		//Call postTick
		l.callPostTick("POST");
		
		//Ensure that preTick was not called
		verify(tl, times(0)).preTick();
		
		//Ensure that postTick was called once.
		verify(tl, times(1)).postTick();
	}
	
	/**
	 * Test for {@link Listenable#callPostTick(String)}, when
	 * {@link TickListener#postTick()} throws an exception.
	 */
	@Test
	public void testCallPostTick2() {
		Listenable l = getListenable();
		
		//Create a TickListener that throws an exception when postTick is called.
		TickListener tl = mock(TickListener.class);
		Mockito.doThrow(new FakeException("Ignore this error")).when(tl).postTick();
		l.registerListener(tl);
		
		//Call postTick
		l.callPostTick("POST");
		l.callPostTick(null);
		
		//Ensure that preTick was not called
		verify(tl, times(0)).preTick();
		
		//Ensure that postTick was called twice.
		verify(tl, times(2)).postTick();
	}
}
