package me.xtrm.delta.client.utils.vector;

import javax.vecmath.Vector3f;

import me.xtrm.delta.client.utils.Location;

@SuppressWarnings("serial")
public class CVector3f extends Vector3f {

	public CVector3f(float x, float y, float z) {
		super(x, y, z);
	}

	public CVector3f(Location pos) {
		super((float)pos.getX(), (float)pos.getY(), (float)pos.getZ());
	}

	public double squareDistanceTo(CVector3f vec) {
		double var2 = vec.x - this.x;
		double var4 = vec.y - this.y;
		double var5 = vec.z - this.z;
		return var2 * var2 + var4 * var4 + var5 * var5;
	}

	public CVector3f add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public CVector3f add(CVector3f vec) {
		this.x += vec.x;
		this.y += vec.y;
		this.z += vec.z;
		return this;
	}

	public CVector3f cScale(float amount) {
		this.x *= amount;
		this.y *= amount;
		this.z *= amount;
		return this;
	}

	public CVector3f clone() {
		return new CVector3f(x, y, z);
	}
}