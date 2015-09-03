package com.github.fishio;

import java.util.ArrayList;

import com.github.fishio.listeners.TickListener;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Represents the PlayingField.
 */
public class PlayingField {
	private static final int WINDOW_X = 1280;
	private static final int WINDOW_Y = 720;
	
	private Timeline gameThread;
	private int fps;
	
	private String title;
	private Canvas canvas;
	private Scene surface;
	private Group root;
	
	private ArrayList<TickListener> listeners = new ArrayList<>();
	private ArrayList<IDrawable> drawables = new ArrayList<>();
	private ArrayList<Entity> entities = new ArrayList<>();
	
	/**
	 * @param fps
	 * 		the (target) framerate.
	 * @param title
	 * 		the title of the window.
	 */
	public PlayingField(int fps, String title) {
		this.fps = fps;
		this.title = title;
		
		createCanvas();
		createGameThread();
	}
	
	/**
	 * @return
	 * 		the (target) framerate in frames per second.
	 */
	public int getFPS() {
		return fps;
	}
	
	/**
	 * @return
	 * 		the width of the field.
	 */
	public int getWidth() {
		return WINDOW_X;
	}
	
	/**
	 * @return
	 * 		the height of the field.
	 */
	public int getHeigth() {
		return WINDOW_Y;
	}
	
	/**
	 * Creates the canvas.
	 */
	protected final void createCanvas() {
		canvas = new Canvas(1280, 720);
		root = new Group();
		root.getChildren().add(canvas);
		
		surface = new Scene(root, 1280.0, 720.0);
	}
	
	/**
	 * Creates the game thread.
	 */
	protected final void createGameThread() {
		Duration dur = Duration.millis(1000 / getFPS());
		KeyFrame frame = new KeyFrame(dur, event -> {
			//Call listeners pretick
			preListeners();
			
			//Add new entities
			addEntities();
			
			//Re-render items
			redraw();
			
			//Check for collisions
			checkCollisions();
			
			//Cleanup dead entities.
			cleanupDead();
			
			//Call listeners posttick
			postListeners();
		}, new KeyValue[0]);
		
		Timeline tl = new Timeline(frame);
		tl.setCycleCount(-1);
		
		gameThread = tl;
	}
	
	/**
	 * Called to redraw the screen.
	 */
	public void redraw() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		//Clear screen
		gc.clearRect(0, 0, WINDOW_X, WINDOW_Y);
		
		//Render all drawables
		for (IDrawable d : drawables) {
			d.render(gc);
		}
	}
	
	/**
	 * Checks for collisions.
	 */
	public void checkCollisions() {
		//TODO
	}
	
	/**
	 * Cleans up dead entities.
	 */
	public void cleanupDead() {
		ArrayList<Entity> tbr = new ArrayList<Entity>();
		for (Entity e : entities) {
			if (e.isDead()) {
				tbr.add(e);
			}
		}
		
		for (Entity e : tbr) {
			remove(e);
		}
	}
	
	/**
	 * Adds new entities.
	 */
	public void addEntities() {
		//TODO
	}
	
	/**
	 * Calls all listeners pre tick.
	 */
	public void preListeners() {
		//TODO Concurrency + try catch
		for (TickListener tl : listeners) {
			try {
				tl.preTick();
			} catch (Exception ex) {
				//TODO Handle exception differently
				System.out.println("Error in preTick!");
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Calls all listeners post tick.
	 */
	public void postListeners() {
		//TODO Concurrency + try catch
		for (TickListener tl : listeners) {
			try {
				tl.postTick();
			} catch (Exception ex) {
				//TODO Handle exception differently
				System.out.println("Error in postTick!");
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Shows this Playing Field on the given stage.
	 * 
	 * @param stage
	 * 		the stage to show this PlayingField on.
	 */
	public void show(Stage stage) {
		stage.setTitle(title);
		stage.setScene(surface);
		stage.show();
	}
	
	/**
	 * @return
	 * 		the gamethread.
	 */
	public Timeline getGameThread() {
		return gameThread;
	}
	
	/**
	 * Starts the game.
	 */
	public void startGame() {
		gameThread.play();
	}
	
	/**
	 * Stops the game.
	 */
	public void stopGame() {
		gameThread.stop();
		//gameThread.pause();
	}
	
	/**
	 * @return
	 * 		the root node where the canvas is drawn on.
	 */
	public Group getRootNode() {
		return root;
	}
	
	/**
	 * Adds the given object to this Playing Field.
	 * 
	 * @param o
	 * 		the object to add.
	 */
	public void add(Object o) {
		if (o instanceof IDrawable) {
			drawables.add((IDrawable) o);
		}
		
		if (o instanceof Entity) {
			entities.add((Entity) o);
		}
	}
	
	/**
	 * Removes the given object from this playing field.
	 * 
	 * @param o
	 * 		the object to remove.
	 */
	public void remove(Object o) {
		if (o instanceof IDrawable) {
			drawables.remove(o);
		}
		
		if (o instanceof Entity) {
			entities.remove(o);
		}
	}
	
	/**
	 * Clear this PlayingField.<br>
	 * <br>
	 * This removes all Entities and Drawables.
	 */
	public void clear() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		for (Entity e : entities) {
			e.setDead();
		}
		
		for (IDrawable d : drawables) {
			d.drawDeath(gc);
		}
		
		entities.clear();
		drawables.clear();
	}
	
	/**
	 * Registers the given TickListener.
	 * 
	 * @param tl
	 * 		the TickListener to register.
	 */
	public void registerListener(TickListener tl) {
		listeners.add(tl);
	}
	
	/**
	 * Unregisters the given TickListener.
	 * 
	 * @param tl
	 * 		the TickListener to unregister.
	 */
	public void unregisterListener(TickListener tl) {
		listeners.remove(tl);
	}
}
