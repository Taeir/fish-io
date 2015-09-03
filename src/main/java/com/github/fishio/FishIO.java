package com.github.fishio;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main application class.
 */
public class FishIO extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Fish.io");
		
//		//Simple testing of PlayingField
//		PlayingField pf = new PlayingField(30, "Test");
//		pf.add(new IDrawable() {
//
//			@Override
//			public void drawDeath(GraphicsContext gc) { }
//
//			@Override
//			public void render(GraphicsContext gc) {
//				gc.setFill(Color.MAGENTA);
//				gc.fillRect(30, 30, 50, 20);
//			}
//			
//		});
//		pf.show(primaryStage);
//		
//		pf.startGame();
	}
	
	/**
	 * Startup method.
	 * @param args
	 * 		program arguments.
	 */
	public static void main(String[] args) {
		launch();
	}
}
