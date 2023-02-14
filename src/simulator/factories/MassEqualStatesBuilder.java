package simulator.factories;

import org.json.JSONObject;

import simulator.control.MassEqualStates;
import simulator.control.StateComparator;


public class MassEqualStatesBuilder extends  Builder<StateComparator>{
	public MassEqualStatesBuilder() {
		super();
		this.type = "masseq";
		this.desc = "Mass equal states comparator builder";
	}

	@Override
	public StateComparator createTheInstance(JSONObject info) throws IllegalArgumentException{
		StateComparator sc = null;
		if (info.getJSONObject("data").isEmpty()) {
			sc = new MassEqualStates();
		} else {
			throw new IllegalArgumentException();
		}
		return sc;
	}
}
