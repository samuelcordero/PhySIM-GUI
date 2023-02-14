package simulator.model;

import java.util.List;

public class NewtonUniversalGravitation implements ForceLaws{
	private double f;
	private double g;
	public NewtonUniversalGravitation() {
		 g = 6.67E-11;
	}
	public NewtonUniversalGravitation(double g) {
		 this.g = g;
	}
	
	
	@Override
	public void apply(List<Body> bs) {
		for(int i = 0; i < bs.size(); i++) {
            for(int j = 0; j < bs.size(); j++) {
		    	if (i != j) {
		    		f = g*((bs.get(i).getMass()*bs.get(j).getMass()))*Math.pow(bs.get(i).getPosition().distanceTo(bs.get(j).getPosition()), 2);
		    	    bs.get(i).addForce((bs.get(j).getPosition().minus(bs.get(i).getPosition())).direction().scale(f));
		    	}
		    }
		}
		    
	}
	@Override
	public String toString() {
		return "Newton’s Universal Gravitation with G=" + String.valueOf(g);
	}

}
