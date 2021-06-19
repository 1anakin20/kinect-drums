package com.tomas.kinect;

import com.jme3.math.Vector3f;
import edu.ufl.digitalworlds.j4k.Skeleton;

public class Joint {
	private final Vector3f translation;

	public Joint(Skeleton skeleton, int joint_id) {
		translation = new Vector3f(
				skeleton.get3DJointX(joint_id) * -1,
				skeleton.get3DJointY(joint_id),
				skeleton.get3DJointZ(joint_id) * -1
		);
	}

	public Vector3f getTranslation() {
		return translation;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Joint{");
		sb.append("translation=").append(translation);
		sb.append('}');
		return sb.toString();
	}
}
