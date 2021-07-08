package com.tomas.properties;

import com.jme3.bullet.collision.PhysicsCollisionObject;

public enum CollisionGroups {
	DRUMS(PhysicsCollisionObject.COLLISION_GROUP_01),
	STICKS(PhysicsCollisionObject.COLLISION_GROUP_02);

	private final int collisionGroup;

	CollisionGroups(int collisionGroup) {
		this.collisionGroup = collisionGroup;
	}

	public int getCollisionGroup() {
		return collisionGroup;
	}
}
