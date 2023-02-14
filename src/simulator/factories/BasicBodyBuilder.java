package simulator.factories;

import org.json.JSONObject;


import simulator.misc.Vector2D;
import simulator.model.Body;

public class BasicBodyBuilder extends Builder<Body>{
	public BasicBodyBuilder() {
		super();
		this.type = "basic";
		this.desc = "Basic body builder";
	}

	@Override
	public Body createTheInstance(JSONObject info) throws IllegalArgumentException{
		Body bd = null;
		if (info.getJSONObject("data").length() == 4) {
			Vector2D p, v;
			p = this.json2vector2d(info.getJSONObject("data").getJSONArray("p")); //throws exception in case not 2d
			v = this.json2vector2d(info.getJSONObject("data").getJSONArray("v")); //throws exception in case not 2d
			bd = new Body(info.getJSONObject("data").getString("id"), v, p, info.getJSONObject("data").getDouble("m"));
		} else {
			throw new IllegalArgumentException();
		}
		return bd;
	}
}
