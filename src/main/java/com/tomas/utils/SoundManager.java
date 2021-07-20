package com.tomas.utils;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.math.Vector3f;

public class SoundManager {

	/**Calculates the volume of a hit from the velocity of the object
	 * @param velocity Velocity vector of the object. The hit force will be calculated by the y coordinate
	 * @return Volume of the hit
	 */
	public static float calculateHitForce(Vector3f velocity) {
		return (float) Math.min(1, Math.max(0, Math.pow(Math.abs(velocity.y * 100), 1.5)));
	}

	/**Plays the sound of the drum
	 * @param audioName name of the sound file. Must be a wav and end as ".wav"
	 * @param volume The volume is proportional the hit force
	 */
	public static void playDrum(String audioName, float volume, AssetManager assetManager) {
		AudioNode sound = new AudioNode(assetManager, "Sounds/" + audioName + ".wav", AudioData.DataType.Buffer);
		sound.setPositional(false);
		sound.setVolume(volume);
		sound.playInstance();
	}
}
