package com.github.fishio.control;

import java.util.HashMap;

import com.github.fishio.Preloader;
import com.github.fishio.Vec2d;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.settings.Settings;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

/**
 * Help screen controller.
 */
public class HelpScreenController implements ScreenController {

	private Log log = Log.getLogger();
	private Settings settings = Settings.getInstance();
	private static final HashMap<KeyCode, Vec2d> KEYMAP = getKeyMap();
	
	@FXML
	private Button btnBackToMenu;
	
	@FXML
	private Canvas keyboard;
	
	@Override
	public void init(Scene scene) { }

	/**
	 * Shows the connection between a key and its function.
	 * @param key
	 * 		the key
	 * @param y
	 * 		the y coordinate of the label containing the keys function
	 */
	private void displayKeyBinding(KeyCode key, int y) {
		Vec2d pos = KEYMAP.get(key);
		GraphicsContext gc = keyboard.getGraphicsContext2D();
		if (pos == null) {
			gc.fillText(key.getName(), 950, y + 10);
			return;
		}
		gc.strokeLine(pos.x / 2 + 18, pos.y / 2 + 18, 850, y + 8);
		Image img = Preloader.getImageOrLoad("keyboard.png");
		gc.drawImage(img, pos.x, pos.y, 72, 72, pos.x / 2, pos.y / 2, 36, 36);
	}

	/**
	 * @return
	 * 		a set of (relative) key locations in the keyboard image.
	 */
	private static HashMap<KeyCode, Vec2d> getKeyMap() {
		HashMap<KeyCode, Vec2d> map = new HashMap<KeyCode, Vec2d>();
		map.put(KeyCode.UP, new Vec2d(1418, 345));
		map.put(KeyCode.DOWN, new Vec2d(1418, 415));
		map.put(KeyCode.LEFT, new Vec2d(1334, 415));
		map.put(KeyCode.RIGHT, new Vec2d(1501, 415));
		map.put(KeyCode.W, new Vec2d(228, 202));
		map.put(KeyCode.S, new Vec2d(249, 275));
		map.put(KeyCode.A, new Vec2d(169, 275));
		map.put(KeyCode.D, new Vec2d(334, 275));
		//TODO map more keys when allowing key changes
		return map;
	}

	@Override
	public void onSwitchTo() {
		GraphicsContext gc = keyboard.getGraphicsContext2D();
		gc.clearRect(0, 0, 1000, 500);
		ColorAdjust ca = new ColorAdjust();
		ca.setBrightness(0.5);
		gc.save();
		gc.drawImage(Preloader.getImageOrLoad("keyboard.png"), 0, 0, 795, 251);
		gc.applyEffect(ca);
		gc.restore();
		
		gc.setLineWidth(1.0);
		displayKeyBinding(settings.getKeyCode("SWIM_UP"), 42);
		displayKeyBinding(settings.getKeyCode("SWIM_DOWN"), 58);
		displayKeyBinding(settings.getKeyCode("SWIM_LEFT"), 26);
		displayKeyBinding(settings.getKeyCode("SWIM_RIGHT"), 74);
	}
	
	/**
	 * Go back to the main menu.
	 */
	@FXML
	public void backToMenu() {
		log.log(LogLevel.INFO, "Player Pressed the back to menu Button.");
		Preloader.switchTo("mainMenu", 400);
	}

	/**
	 * @return
	 * 		the button that returns to the main menu.
	 */
	public Button getBtnBackToMenu() {
		return btnBackToMenu;
	}

}
