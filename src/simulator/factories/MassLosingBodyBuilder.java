package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.MassLossingBody;

public class MassLosingBodyBuilder extends Builder<Body>{
	public MassLosingBodyBuilder() {
		super();
		this.type = "mlb";
		this.desc = "Mass losing body builder";
	}

	@Override
	public Body createTheInstance(JSONObject info) throws IllegalArgumentException{
		Body bd = null;
		if (info.getJSONObject("data").length() == 6) {
			Vector2D p, v;
			p = this.json2vector2d(info.getJSONObject("data").getJSONArray("p")); //throws exception in case not 2d
			v = this.json2vector2d(info.getJSONObject("data").getJSONArray("v")); //throws exception in case not 2d
			bd = new MassLossingBody(info.getJSONObject("data").getString("id"), v, p, info.getJSONObject("data").getDouble("m"), info.getJSONObject("data").getDouble("factor"), info.getJSONObject("data").getDouble("freq"));
		} else {
			throw new IllegalArgumentException();
		}
		return bd;
	}
}
