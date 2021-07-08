package com.tomas.kinect;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Kinect extends J4KSDK {
	private Joint leftHand;
	private Joint rightHand;
	private final List<KinectEvents> listeners = new ArrayList<>();

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
			Skeleton playerSkeleton = Skeleton.getSkeleton(firstTracked, floats, booleans);
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

	public void loadKinect(boolean skeleton, int depth, int video, boolean seated) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Runnable loadKinect = () -> {
			System.out.println("Loading kinect");
			if (start(skeleton, depth, video) == 1) {
				startSkeletonTracking(seated);
				kinectLoaded();
			} else {
				kinectCouldNotLoad();
			}
		};
		executorService.execute(loadKinect);
	}

	public void registerListener(KinectEvents listener) {
		listeners.add(listener);
	}

	public void removeListener(KinectEvents listener) {
		listeners.remove(listener);
	}

	private void kinectLoaded() {
		for (KinectEvents listener : listeners) {
			listener.kinectLoaded();
		}
	}

	private void kinectCouldNotLoad() {
		for (KinectEvents listener : listeners) {
			listener.kinectCouldNotLoad();
		}
	}
}
