/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2015 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package com.github.fishio.gui;

import javafx.application.Application;
import javafx.stage.Stage;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.toolkit.ApplicationFixture;

/**
 * Modification of {@link org.testfx.framework.junit.ApplicationTest}, so that 
 * tests work on the travis ci server.
 */
public abstract class AppTest extends FxRobot implements ApplicationFixture {
	private static AppTest instance;
	
	//---------------------------------------------------------------------------------------------
	// STATIC METHODS.
	//---------------------------------------------------------------------------------------------

	/**
	 * Launch the application.
	 * 
	 * @param appClass
	 * 		the application class.
	 * @param appArgs
	 * 		the arguments used to start the application.
	 * 
	 * @throws Exception
	 * 		if any exception occurrs, it is thrown.
	 */
	public static void launch(Class<? extends Application> appClass, String... appArgs) throws Exception {
		FxToolkit.registerPrimaryStage();
		FxToolkit.setupApplication(appClass, appArgs);
	}

	//---------------------------------------------------------------------------------------------
	// METHODS.
	//---------------------------------------------------------------------------------------------

	/**
	 * Called before every test.<br>
	 * Registers the primary stage and sets up the application.
	 * 
	 * @throws Exception
	 * 		if an exception occurs.
	 */
	@Before
	public void internalBefore() throws Exception {
		instance = this;
		FxToolkit.registerPrimaryStage();
		FxToolkit.setupApplication(this);
	}

	/**
	 * Called after every test.<br>
	 * Cleans up the application.
	 * 
	 * @throws Exception
	 * 		if an exception occurs.
	 */
	@After
	public void internalAfter() throws Exception {
		//FxToolkit.cleanupApplication(this);

		Thread.sleep(50L);
	}
	
	/**
	 * Called after all tests in this class are done.<br>
	 * <br>
	 * Sleeps for a second to allow the next test to start up.
	 * 
	 * @throws Exception
	 * 		if anything goes wrong while sleeping.
	 */
	@AfterClass
	public static void internalAfterClass() throws Exception {
		if (instance != null) {
			FxToolkit.cleanupApplication(instance);
			instance = null;
		}
		
		Thread.sleep(1000L);
	}

	@Override
	public void init() throws Exception { }

	@Override
	public abstract void start(Stage stage) throws Exception;

	@Override
	public void stop() throws Exception { }

}
