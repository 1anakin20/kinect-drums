package com.tomas.kinect;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;

public class Kinect extends J4KSDK {
	private Joint leftHand;
	private Joint rightHand;
	private Skeleton playerSkeleton;

	@Override
	public void onDepthFrameEvent(short[] shorts, int[] ints, int[] ints1) {
	}

	@Override
	public void onSkeletonFrameEvent(float[] floats, boolean[] booleans) {
		int firstTracked = 0;
		boolean tracking = false;
		for (int i = 0; i < booleans.length; i++) {
			if (booleans[i]) {
				firstTracked = i;
				tracking = true;
			}
		}

		if (tracking) {
			playerSkeleton = Skeleton.getSkeleton(firstTracked, floats, booleans);
			leftHand = new Joint(playerSkeleton, Skeleton.HAND_LEFT);
			rightHand = new Joint(playerSkeleton, Skeleton.HAND_RIGHT);
		}
	}

	@Override
	public void onVideoFrameEvent(byte[] bytes) {
	}

	public Joint getLeftHand() {
		return leftHand;
	}

	public Joint getRightHand() {
		return rightHand;
	}
}
