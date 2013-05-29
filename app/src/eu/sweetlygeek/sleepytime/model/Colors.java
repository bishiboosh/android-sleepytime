package eu.sweetlygeek.sleepytime.model;

import android.graphics.Color;

/**
 * Colors used.
 * 
 * @author bishiboosh
 */
public enum Colors {

	/** Dark green. */
	DARK_GREEN("#00CC33"),
	/** Light green. */
	LIGHT_GREEN("#99CC66"),
	/** Green. */
	GREEN("#01DF74"),
	/** Light purple. */
	LIGHT_PURPLE("#9966CC");

	private int value;

	private Colors(String hexa) {
		value = Color.parseColor(hexa);
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public int getValue() {
		return value;
	}
}
