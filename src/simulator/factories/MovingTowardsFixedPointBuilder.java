package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;


public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws>{
	public MovingTowardsFixedPointBuilder() {
		super();
		this.type = "mtcp";
		this.desc = "Moving towards a fixed point";
	}

	@Override
	public ForceLaws createTheInstance(JSONObject info) throws IllegalArgumentException{
		ForceLaws fl = null;
		Vector2D c = null;
		if (info.getJSONObject("data").isEmpty()) {
			fl = new MovingTowardsFixedPoint();
		} else if(info.getJSONObject("data").length() == 1) {
			if(info.getJSONObject("data").isNull("c")) {
				fl = new MovingTowardsFixedPoint(info.getJSONObject("data").getDouble("g"));
			} else {
				c = this.json2vector2d(info.getJSONObject("data").getJSONArray("c")); //throws exception in case not 2d
				fl = new MovingTowardsFixedPoint(c);
			}	
		} else if(info.getJSONObject("data").length() == 2) {
			c = this.json2vector2d(info.getJSONObject("data").getJSONArray("c")); //throws exception in case not 2d
			fl = new MovingTowardsFixedPoint(info.getJSONObject("data").getDouble("g"), c);
		}else {
			throw new IllegalArgumentException();
		}
		return fl;
	}
	@Override
	public JSONObject getBuilderInfo() {
		JSONObject out = new JSONObject();
		JSONObject g = new JSONObject();
		JSONObject c = new JSONObject();
		JSONArray data = new JSONArray();
		g.put("name", "g");
		g.put("desc", "the length of the acceleration vector (a number)");
		c.put("name", "c");
		c.put("desc", "the point towards which bodies move (a json list of 2 numbers, e.g., [100.0,50.0])");
		data.put(0, c);
		data.put(1, g);
		out.put("type", this.type);
		out.put("data", data);
		out.put("desc", this.desc);
		return out;
	}
}
