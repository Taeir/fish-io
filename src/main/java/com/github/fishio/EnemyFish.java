package com.github.fishio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.github.fishio.audio.AudioEngine;
import com.github.fishio.behaviours.IMoveBehaviour;
import com.github.fishio.behaviours.RandomBehaviour;
import com.github.fishio.logging.LogLevel;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * EnemyFish class. This class contains all methods concerning non-player or
 * enemy fish on the screen.
 */
public class EnemyFish extends Entity implements IEatable {
	private static final long serialVersionUID = -3625685120024363192L;

	private String spriteLocation;
	private Image sprite;

	private IMoveBehaviour behaviour;
	
	/**
	 * Main constructor of the enemy fish.
	 * 
	 * @param ca
	 *            CollisionMask of enemy fish object.
	 * @param spriteLocation
	 *            String URL of the sprite of the enemy fish object.
	 * @param startvx
	 *            Starting speed of the enemy fish object in the x direction.
	 * @param startvy
	 *            Starting speed of the enemy fish object in the y direction.
	 */
	public EnemyFish(CollisionMask ca, String spriteLocation, double startvx, double startvy) {
		super(ca);
		
		this.spriteLocation = spriteLocation;
		if (this.spriteLocation != null) {
			this.sprite = Preloader.getImageOrLoad(this.spriteLocation);
		}
		
		this.behaviour = new RandomBehaviour(startvx, startvy, settings.getDouble("DIRECTION_CHANGE_CHANCE"));
		
		logger.log(LogLevel.TRACE, "Created Enemfish: Properties{[position = " + ca.getCenterX() 
				+ ", " + ca.getCenterY() + "],[height = " + ca.getHeight() + "],[width = "
				+ ca.getWidth() + "],[Vx = " + startvx + "],[Vy = " + startvy + "]}.");
	}

	@Override
	public void render(GraphicsContext gc) {
		//Don't render if dead.
		if (isDead()) {
			return;
		}
		
		//TODO Move this to game thread.
		getBoundingArea().setRotation(behaviour);	//update rotation
		
		//Only render if we have a sprite.
		if (sprite != null) {
			drawRotatedImage(gc, sprite, getBoundingArea());
		} else {
			//Call the render of the super (entity), which simply renders a red box.
			super.render(gc);
		}
	}

	/**
	 * Enemy fish should die if they hit the wall from the inside.
	 */
	@Override
	public void hitWall() {
		logger.log(LogLevel.TRACE, "EnemyFish collided with wall.");
		kill();
	}

	@Override
	public void onCollide(ICollidable other) { }

	@Override
	public boolean canBeEatenBy(IEatable other) {
		if (other.getSize() > getSize() * settings.getDouble("FISH_EAT_THRESHOLD")) {
			return true;
		}
		return false;
	}

	@Override
	public void eat() {
		kill();
		AudioEngine.getInstance().playEffect("crunch");
	}

	@Override
	public double getSize() {
		return getBoundingArea().getSize();
	}

	@Override
	public IMoveBehaviour getBehaviour() {
		return behaviour;
	}

	@Override
	public void setBehaviour(IMoveBehaviour behaviour) {
		this.behaviour = behaviour;
	}

	@Override
	public boolean canMoveThroughWall() {
		return true;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeUTF(this.spriteLocation);
		out.writeObject(this.behaviour);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.spriteLocation = in.readUTF();
		this.behaviour = (IMoveBehaviour) in.readObject();
		
		//Load the sprite
		this.sprite = Preloader.getImageOrLoad(this.spriteLocation);
	}
}
