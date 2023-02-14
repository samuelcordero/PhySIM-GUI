package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;

public abstract class Builder<T> {
	protected String type, desc;
	public Builder() {
	}
	
	public T createInstance(JSONObject info) throws IllegalArgumentException{
		if (this.type.equals(info.getString("type"))) {
			return createTheInstance(info);
		} else {
			return null;
		}
	}
	public JSONObject getBuilderInfo() {
		JSONObject out = new JSONObject();
		out.put("type", this.type);
		out.put("desc", this.desc);
		return out;
	}
	//esta clase facilita la conversion de JSONArray a Vector2D, a su vez comprueba que sea bidimensional y lanza excepción en caso contrario
	public Vector2D json2vector2d(JSONArray v) throws IllegalArgumentException{
		Vector2D vec = null;
		if(v.length() != 2)
			throw new IllegalArgumentException("Vector not 2D");
		vec = new Vector2D(v.getDouble(0), v.getDouble(1));
		return vec;
	}
	public abstract T createTheInstance(JSONObject info) throws IllegalArgumentException;
}
