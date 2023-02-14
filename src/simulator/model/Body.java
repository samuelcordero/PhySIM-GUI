package simulator.model;

import org.json.JSONObject;

import simulator.misc.Vector2D;

public class Body {
	protected String id;
	protected Vector2D v;
	protected Vector2D f;
	protected Vector2D p;
	protected double m;
	

	public Body(String id, Vector2D v,Vector2D p, double m) {
		this.id=id;
		this.v = v;
		this.f = new Vector2D();
		this.p = p;
		this.m = m;
	}
	
	public String getId() {
		return this.id;
	}
	public Vector2D getVelocity() {
		return this.v;
	}
	public Vector2D getForce() {
		return this.f;
	}
	public Vector2D getPosition() {
		return this.p;
	}
	public double getMass() {
		return this.m;
	}
	void addForce(Vector2D f) {
		this.f = this.f.plus(f);
	}
	void resetForce() {
		f = new Vector2D();		
	}
	void move(double t) {
		Vector2D a;
		//Calculamos aceleracion;
		if(getMass() == 0) {
			a = new Vector2D();
		}
		else {
			a = f.scale((double)1.0/m);
		}
		//Cambiamos p y v:
		p =  p.plus(a.scale((double) 0.5 * t * t)).plus(v.scale(t));
		v = v.plus(a.scale(t));
	}
	public JSONObject getState() {
		JSONObject a = new JSONObject();
		a.put("id" ,getId());
		a.put("m" ,getMass());
		a.put("p" ,getPosition().asJSONArray());
		a.put("v" ,getVelocity().asJSONArray());
		a.put("f" ,getForce().asJSONArray());
		
		return a;
	}
	public String toString() {
		return getState().toString();
	}	
}
