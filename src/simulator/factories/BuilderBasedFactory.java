package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T>{
	List<Builder<T>> builders;
	public BuilderBasedFactory(List<Builder<T>> builders) {
		this.builders = builders;
	}

	@Override
	public T createInstance(JSONObject info) throws IllegalArgumentException {
		boolean notFound = true;
		int i = 0;
		T instance = null;
		while(notFound && (i < builders.size())) {
			instance = builders.get(i).createInstance(info);
			if (instance != null) {
				notFound = false;
			}
			i++;
		}
		if(notFound) {
			throw new IllegalArgumentException();
		}
		return instance;
	}


	@Override
	public List<JSONObject> getInfo() {
		List<JSONObject> list = new ArrayList<JSONObject>();
		int i = 0;
		while( i < builders.size()) {
			list.add(builders.get(i).getBuilderInfo());
			i++;
		}
		return list;
	}

	

}
