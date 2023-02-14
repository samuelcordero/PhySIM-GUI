package simulator.factories;

import org.json.JSONObject;

import simulator.control.EpsilonEqualStates;
import simulator.control.StateComparator;

public class EpsilonEqualStatesBuilder extends Builder<StateComparator>{
	public EpsilonEqualStatesBuilder() {
		super();
		this.type = "epseq";
		this.desc = "Epsilon equal states comparator builder";
	}

	@Override
	public StateComparator createTheInstance(JSONObject info) throws IllegalArgumentException{
		StateComparator sc = null;
		if (info.getJSONObject("data").isEmpty()) {
			sc = new EpsilonEqualStates();
		} else if(info.getJSONObject("data").length() == 1) {
			sc = new EpsilonEqualStates(info.getJSONObject("data").getDouble("eps"));
		} else {
			throw new IllegalArgumentException();
		}
		return sc;
	}
}
