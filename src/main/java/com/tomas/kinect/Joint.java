package com.tomas.kinect;

import com.jme3.math.Vector3f;
import edu.ufl.digitalworlds.j4k.Skeleton;

/**
 * Represents a Kinect body point
 */
public class Joint {
	private final Vector3f translation;

	public Joint(Skeleton skeleton, int joint_id) {
		translation = new Vector3f(
				skeleton.get3DJointX(joint_id) * -1 + skeleton.get3DJointX(Skeleton.SHOULDER_CENTER),
				skeleton.get3DJointY(joint_id),
				skeleton.get3DJointZ(joint_id) * -1  + skeleton.get3DJointZ(Skeleton.SHOULDER_CENTER)
		);
	}

	public Vector3f getTranslation() {
		return translation;
	}

	@Override
	public String toString() {
		return "Joint{" + "translation=" + translation +
				'}';
	}
}
