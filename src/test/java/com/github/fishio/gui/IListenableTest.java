package com.github.fishio.gui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.mockito.Mockito;

import com.github.fishio.listeners.Listenable;
import com.github.fishio.listeners.TickListener;

/**
 * Interface test for the {@link Listenable} interface.<br>
 * <br>
 * <b>NOTE: all methods in this class must be overridden in order for
 * them to be actually tested.</b><br>
 * <br>
 * This can be done in the following way:<br>
 * <code>
 * &#64;Test<br>
 * &#64;Override<br>
 * public void testGetListeners() {<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;IListenableTest.super.testGetListeners();<br>
 * }
 * </code>
 */
public interface IListenableTest {
	
	/**
	 * @return
	 * 		a new Listenable of the type that the class implementing this
	 * 		interface is testing.
	 */
	Listenable getListenable();
	
	/**
	 * Test for {@link Listenable#getListeners()}.
	 */
	default void testGetListeners() {
		Listenable l = getListenable();
		
		//Initially, there should be no listeners.
		assertTrue(l.getListeners().isEmpty());
	}
	
	/**
	 * Test for {@link Listenable#registerListener(TickListener)}.
	 */
	default void testRegisterListener() {
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
	default void testUnregisterListener() {
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
	default void testCallPreTick() {
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
	default void testCallPreTick2() {
		Listenable l = getListenable();
		
		//Create a TickListener that throws an exception when preTick is called.
		TickListener tl = mock(TickListener.class);
		Mockito.doThrow(new RuntimeException("Ignore this error")).when(tl).preTick();
		l.registerListener(tl);
		
		//Call pretick, no exception should occur.
		l.callPreTick("PRE");
		
		//Ensure that preTick was called once.
		verify(tl, times(1)).preTick();
		
		//Ensure that postTick was not called.
		verify(tl, times(0)).postTick();
	}
	
	/**
	 * Test for {@link Listenable#callPostTick(String)}.
	 */
	default void testCallPostTick() {
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
	default void testCallPostTick2() {
		Listenable l = getListenable();
		
		//Create a TickListener that throws an exception when postTick is called.
		TickListener tl = mock(TickListener.class);
		Mockito.doThrow(new RuntimeException("Ignore this error")).when(tl).postTick();
		l.registerListener(tl);
		
		//Call postTick
		l.callPostTick("POST");
		
		//Ensure that preTick was not called
		verify(tl, times(0)).preTick();
		
		//Ensure that postTick was called once.
		verify(tl, times(1)).postTick();
	}
}
