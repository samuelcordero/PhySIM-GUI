package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;

public class MassEqualStates implements StateComparator{
	public MassEqualStates() {
		
	}
	@Override
	public boolean equal(JSONObject s1, JSONObject s2) {
		boolean eq = true;
		int i = 0;
		JSONArray bodies1, bodies2 = new JSONArray();
		bodies1 = s1.getJSONArray("bodies");
		bodies2 = s2.getJSONArray("bodies");

		if (s1.getDouble("time") == s2.getDouble("time")) {
			if (bodies1.length()==bodies2.length()){
				while (eq && (i < bodies1.length())) {
					if(bodies1.getJSONObject(i).getDouble("m") != bodies2.getJSONObject(i).getDouble("m") 
					   || !bodies1.getJSONObject(i).getString("id").equals(bodies2.getJSONObject(i).getString("id")))
						eq=false;
					i++;
				}	
			}
			else { eq = false; }
		}else { eq = false; }
		return eq;
	}
}
