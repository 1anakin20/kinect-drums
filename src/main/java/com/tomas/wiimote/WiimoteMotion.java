package com.tomas.wiimote;

import com.jme3.math.Quaternion;
import wiiusej.values.Orientation;
import wiiusej.values.RawAcceleration;
import wiiusej.wiiusejevents.physicalevents.MotionSensingEvent;

public class WiimoteMotion extends WiimoteEventsAdapter {
	private Quaternion rotation = new Quaternion();

	final float sampleInt = 0.0307f; // 32.6 samples per second

	private float q0 = 1, q1 = 0, q2 = 0, q3 = 0;	// Quaternion elements representing the estimated orientation
	private float exInt = 0, eyInt = 0, ezInt = 0;	// scaled integral error
	private final float Kp = 2.0f;			// proportional gain governs rate of convergence to accelerometer/magnetometer
	private final float  Ki = 0.005f;		// integral gain governs rate of convergence of gyroscope biases
	private final float  halfT = 0.5f;

	@Override
	public void onMotionSensingEvent(MotionSensingEvent motionSensingEvent) {
		final float DEGREES_TO_RADIAN = (float) (Math.PI / 180);

		Orientation orientation = motionSensingEvent.getOrientation();
		RawAcceleration acceleration = motionSensingEvent.getRawAcceleration();
		IMUupdate(
				acceleration.getX(),
				acceleration.getY(),
				acceleration.getZ(),
				orientation.getPitch() * DEGREES_TO_RADIAN,
				orientation.getYaw() * DEGREES_TO_RADIAN,
				orientation.getRoll() * DEGREES_TO_RADIAN,
				sampleInt
		);

		rotation = new Quaternion(q0, q1, q2, q3);
	}

	private void IMUupdate(float ax, float ay, float az, float gx, float gy, float gz, float T) {
		float norm;
		float vx, vy, vz;
		float ex, ey, ez;
		float filtCoef = 0.75f, filtCoef2 = 0.75f;

		// normalise the measurements
		norm = (float) Math.sqrt(ax*ax + ay*ay + az*az);
		ax = ax / norm;
		ay = ay / norm;
		az = az / norm;

		// estimated direction of gravity
		vx = 2*(q1*q3 - q0*q2);
		vy = 2*(q0*q1 + q2*q3);
		vz = q0*q0 - q1*q1 - q2*q2 + q3*q3;

		// error is sum of cross product between reference direction of field and direction measured by sensor
		ex = (ay*vz - az*vy);
		ey = (az*vx - ax*vz);
		ez = (ax*vy - ay*vx);

		// integral error scaled integral gain
		exInt = filtCoef*exInt + (1-filtCoef)*ex;
		eyInt = filtCoef*eyInt + (1-filtCoef)*ey;
		ezInt = filtCoef*ezInt + (1-filtCoef)*ez;

		// adjusted gyroscope measurements
		gx = filtCoef2*gx + (1-filtCoef2)*(Kp*ex + Ki*exInt)/T;
		gy = filtCoef2*gy + (1-filtCoef2)*(Kp*ey + Ki*eyInt)/T;
		gz = filtCoef2*gz + (1-filtCoef2)*(Kp*ez + Ki*ezInt)/T;

		// integrate quaternion rate and normalise
		q0 = q0 + (-q1*gx - q2*gy - q3*gz)*halfT*T;
		q1 = q1 + (q0*gx + q2*gz - q3*gy)*halfT*T;
		q2 = q2 + (q0*gy - q1*gz + q3*gx)*halfT*T;
		q3 = q3 + (q0*gz + q1*gy - q2*gx)*halfT*T;

		// normalise quaternion
		norm = (float) Math.sqrt(q0*q0 + q1*q1 + q2*q2 + q3*q3);
		q0 = q0 / norm;
		q1 = q1 / norm;
		q2 = q2 / norm;
		q3 = q3 / norm;
	}

	public Quaternion getRotation() {
		return rotation;
	}
}
