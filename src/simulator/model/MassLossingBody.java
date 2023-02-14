package simulator.model;

import simulator.misc.Vector2D;

public class MassLossingBody extends Body {
	
	private double lossFactor;
	private double lossFrequency;
	private double c;
	
	public MassLossingBody(String id, Vector2D v, Vector2D p, double m, double lossFactor, double lossFrequency) throws IllegalArgumentException{
		super(id, v, p, m);
		if(lossFactor >= 0 && lossFactor <= 1) {
			this.lossFactor = lossFactor;
		}
		else {
			throw new IllegalArgumentException();       
		}
		if(lossFrequency > 0) {
			this.lossFrequency = lossFrequency;
		}
		else {
			throw new IllegalArgumentException();
		}
		this.c = 0.0;
	}
	@Override
	void move(double t) {
		Vector2D a;
		//Calculamos aceleracion
		if(getMass() == 0) {
			a = new Vector2D();
		}
		else {
			a = f.scale((double)1.0/m);
		}
		//Cambiamos p y v:
		p =  p.plus(a.scale((double) 0.5 * t * t)).plus(v.scale(t));
		v = v.plus(a.scale(t));
		c += t;
		if(getC() >= getLossFrequency()) {
			setMass(getMass()*(1.0-getLossFactor()));
			setC(0.0);
		}
	}
	void setMass(double m) {
		this.m = m;
	}
	public double getLossFactor() {
		return lossFactor;
	}
	public void setLossFactor(double lossFactor) {
		this.lossFactor = lossFactor;
	}
	public double getLossFrequency() {
		return lossFrequency;
	}
	public void setLossFrequency(double lossFrequency) {
		this.lossFrequency = lossFrequency;
	}
	public double getC() {
		return c;
	}
	public void setC(double c) {
		this.c = c;
	}
	
 
}
