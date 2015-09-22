package com.github.fishio.gui;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.github.fishio.IDrawable;
import com.github.fishio.PlayingField;
import com.github.fishio.listeners.Listenable;
import com.github.fishio.listeners.TickListener;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Duration;

/**
 * Class that renders a game.
 */
public class Renderer implements Listenable {
	public static final int WINDOW_X = 1280;
	public static final int WINDOW_Y = 670;
	
	private ConcurrentLinkedQueue<TickListener> listeners = new ConcurrentLinkedQueue<>();
	private Canvas canvas;
	private Timeline renderThread;
	private SimpleIntegerProperty fps = new SimpleIntegerProperty();
	private PlayingField playingField;
	private Image background;
	
	/**
	 * Create a new Renderer for the given PlayingField.
	 * 
	 * @param playingField
	 * 		the playing field this renderer should render.
	 * @param canvas
	 * 		the canvas to render on.
	 * @param fps
	 * 		the framerate in frames per second.
	 */
	public Renderer(PlayingField playingField, Canvas canvas, int fps) {
		if (canvas == null) {
			this.canvas = new Canvas(WINDOW_X, WINDOW_Y);
		} else {
			this.canvas = canvas;
		}
		
		this.fps.set(fps);
		this.playingField = playingField;
		this.renderThread = newRenderThread(fps);
		
		registerFpsListener();
	}
	
	/**
	 * Register a listener for the fps property, so that changes are reflected.
	 */
	protected final void registerFpsListener() {
		fps.addListener((observable, oldValue, newValue) -> {
			Log.getLogger().log(LogLevel.TRACE, "[Renderer] Changing framerate...");
			
			//If the framerate changes, we need to switch out the old render thread with the new one.
			Timeline newRenderThread = newRenderThread(newValue.intValue());

			//If render thread was running, start the new one and stop the old one.
			if (isRendering()) {
				newRenderThread.play();
				stopRendering();
				this.renderThread = newRenderThread;
				Log.getLogger().log(LogLevel.INFO, "[Renderer] Restarted renderer.");
			}
			
			Log.getLogger().log(LogLevel.INFO, "[Renderer] Changed framerate to: " + newValue.intValue() + " FPS.");
		});
	}
	
	/**
	 * Create a new render thread.
	 * 
	 * @param fps
	 * 		the framerate to use.
	 * 
	 * @return
	 * 		a new render thread with the given framerate.
	 */
	protected final Timeline newRenderThread(int fps) {
		Duration dur = Duration.millis(1000.0 / fps);

		KeyFrame frame = new KeyFrame(dur, event -> {
			//Call listeners pretick
			callPreTick("Renderer");

			//Render all drawables
			redraw();

			//Call listeners posttick
			callPostTick("Renderer");
		});

		Timeline tl = new Timeline(frame);
		tl.setCycleCount(-1);

		return tl;
	}
	
	/**
	 * Called to redraw the screen.
	 */
	public void redraw() {
		GraphicsContext gc = canvas.getGraphicsContext2D();

		//Clear screen
		gc.clearRect(0, 0, WINDOW_X, WINDOW_Y);

		//draw background image
		gc.drawImage(background, 0, 0);

		//Render all drawables, in reverse order
		Iterator<IDrawable> it = playingField.getDrawables().descendingIterator();
		while (it.hasNext()) {
			it.next().render(gc);
		}
		
		//Render all death animations
		IDrawable drawable;
		while ((drawable = playingField.getDeadDrawables().pollLast()) != null) {
			drawable.drawDeath(gc);
		}
	}
	
	/**
	 * @return
	 * 		the fps property.
	 */
	public SimpleIntegerProperty fpsProperty() {
		return fps;
	}
	
	/**
	 * @return
	 * 		the current framerate.
	 */
	public int getFps() {
		return fps.get();
	}
	
	/**
	 * Set the framerate in frames per second to the given value.<br>
	 * <br>
	 * Note: This has to restart the render thread.
	 * 
	 * @param fps
	 * 		the new framerate in frames per second.
	 */
	public void setFps(int fps) {
		this.fps.set(fps);
	}

	/**
	 * @return the canvas this renderer is drawing on.
	 */
	public Canvas getCanvas() {
		return canvas;
	}
	
	/**
	 * Sets the canvas this renderer should draw on.
	 * 
	 * @param canvas
	 * 		the canvas that this renderer should draw on.
	 */
	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}
	
	/**
	 * Set the background image of the level.
	 * @param image
	 * 			The background image.
	 */
	public void setBackground(Image image) {
		if (image.isError()) {
			Log.getLogger().log(LogLevel.ERROR, "[Renderer] Error loading the new background! Using old one instead");
			return;
		}

		background = image;
	}

	@Override
	public ConcurrentLinkedQueue<TickListener> getListeners() {
		return listeners;
	}
	
	/**
	 * Starts rendering (updating the screen).
	 * The game itself will be unaffected by this call.
	 */
	public void startRendering() {
		renderThread.play();
		Log.getLogger().log(LogLevel.TRACE, "[Renderer] Starting Renderer...");
	}
	
	/**
	 * Stops rendering (updating the screen).
	 * The game itself will be unaffected by this call.
	 */
	public void stopRendering() {
		renderThread.stop();
		Log.getLogger().log(LogLevel.TRACE, "[Renderer] Stopping Renderer...");
	}
	
	/**
	 * @return
	 * 		if the render thread is running or not.
	 */
	public boolean isRendering() {
		return renderThread.getStatus() == Status.RUNNING;
	}
}
