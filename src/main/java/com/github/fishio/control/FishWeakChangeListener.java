package com.github.fishio.control;

import java.lang.ref.WeakReference;

import javafx.beans.WeakListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * A Decorator ChangeListener that can get garbage collected if there no
 * longer is a reference to the decorated ChangeListener.
 * 
 * @param <T>
 *            The type of the observed value
 */
public class FishWeakChangeListener<T> implements ChangeListener<T>, WeakListener {

	private final WeakReference<ChangeListener<T>> ref;

	/**
	 * Creates a new FishWeakChangeListener.
	 * 
	 * @param listener
	 *            The original listener that should be notified
	 */
	public FishWeakChangeListener(ChangeListener<T> listener) {
		if (listener == null) {
			throw new NullPointerException("Listener must be specified.");
		}
		this.ref = new WeakReference<ChangeListener<T>>(listener);
	}

	@Override
	public boolean wasGarbageCollected() {
		return (ref.get() == null);
	}

	@Override
	public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
		ChangeListener<T> listener = ref.get();
		if (listener != null) {
			listener.changed(observable, oldValue, newValue);
		} else {
			observable.removeListener(this);
		}
	}

	@Override
	public int hashCode() {
		ChangeListener<T> cl = this.ref.get();

		if (cl == null) {
			return 0;
		} else {
			return cl.hashCode();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (!(obj instanceof FishWeakChangeListener)) {
			return false;
		}

		FishWeakChangeListener<?> other = (FishWeakChangeListener<?>) obj;
		return this.ref.get() == other.ref.get();
	}

}
