package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.model.ForceLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder  extends Builder<ForceLaws>{
	public NewtonUniversalGravitationBuilder() {
		super();
		this.type = "nlug";
		this.desc = "Newton’s law of universal gravitation";
	}

	@Override
	public ForceLaws createTheInstance(JSONObject info) throws IllegalArgumentException{
		ForceLaws fl = null;
		if (info.getJSONObject("data").isEmpty()) {
			fl = new NewtonUniversalGravitation();
		} else if(info.getJSONObject("data").length() == 1) {
			fl = new NewtonUniversalGravitation(info.getJSONObject("data").getDouble("G"));
		} else {
			throw new IllegalArgumentException();
		}
		return fl;
	}
	@Override
	public JSONObject getBuilderInfo() {
		JSONObject out = new JSONObject();
		JSONObject g = new JSONObject();
		JSONArray data = new JSONArray();
		g.put("name", "G");
		g.put("desc", "the gravitational constant(a number)");
		data.put(0, g);
		out.put("type", this.type);
		out.put("data", data);
		out.put("desc", this.desc);
		return out;
	}
}
