package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class MovingTowardsFixedPoint implements ForceLaws {
	private double g;
	Vector2D c;
	
	public MovingTowardsFixedPoint() {
		g = 9.81;
		c = new Vector2D(0, 0);
	}
	public MovingTowardsFixedPoint(Vector2D c) {
		g = 9.81;
		this.c = c;
	}
	public MovingTowardsFixedPoint(double g) {
		this.g = g;
		c = new Vector2D(0, 0);
	}
	public MovingTowardsFixedPoint(double g, Vector2D c) {
		this.g = g;
		this.c = c;
	}

	@Override
	public void apply(List<Body> bs) {
		for(int i = 0; i < bs.size(); i++) {
			bs.get(i).addForce(c.minus(bs.get(i).getPosition()).direction().scale(g).scale(bs.get(i).getMass()));
		}

	}
	@Override
	public String toString() {
		return "Moving towards " + c.toString() + " with constant acceleration " + String.valueOf(g);
	}

}
