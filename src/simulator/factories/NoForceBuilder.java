package simulator.factories;

import org.json.JSONObject;


import simulator.model.ForceLaws;
import simulator.model.NoForce;

public class NoForceBuilder extends Builder<ForceLaws>{
	public NoForceBuilder() {
		super();
		this.type = "ng";
		this.desc = "No force";
	}

	@Override
	public ForceLaws createTheInstance(JSONObject info) throws IllegalArgumentException{
		ForceLaws fl = null;
		if (info.getJSONObject("data").isEmpty()) {
			fl = new NoForce();
		} else {
			throw new IllegalArgumentException();
		}
		return fl;
	}
	@Override
	public JSONObject getBuilderInfo() {
		JSONObject out = new JSONObject();
		out.put("type", this.type);
		out.put("data", new JSONObject());
		out.put("desc", this.desc);
		return out;
	}
}
