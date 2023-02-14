package simulator.control;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

public class Controller {
	
	private PhysicsSimulator simulator;
	private Factory<Body> bodyFactory;
	private Factory<ForceLaws> forceLawsFactory;
	
	public Controller(PhysicsSimulator simulator,  Factory<Body> bodyFactory, Factory<ForceLaws> forceLawsFactory) {
		this.bodyFactory = bodyFactory;
		this.forceLawsFactory = forceLawsFactory;
		this.simulator = simulator;
	}
	
	public void loadBodies(InputStream in) {
		JSONObject jsonInput = new JSONObject(new JSONTokener(in));
		JSONArray arrayBodies = jsonInput.getJSONArray("bodies");
		Body b = null;
		for (int i = 0; i < arrayBodies.length();i++) {
			b = bodyFactory.createInstance(arrayBodies.getJSONObject(i));
			simulator.addBody(b);
		}
	}
	
	public void run(int n, OutputStream out, InputStream expOut, StateComparator cmp) throws differentStateException {
		PrintStream p = new PrintStream(out);
		p.println("{");
		p.println("\"states\": [");
		p.println(simulator.getState());
		boolean compare = false;
		JSONObject scomp = null;
		if(expOut != null) {
			BufferedReader br = new BufferedReader(new InputStreamReader(expOut));
		    StringBuilder sb = new StringBuilder();
		    String line;
		    try {
				while ((line = br.readLine()) != null) {
				    sb.append(line+"\n");
				}
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			scomp =  new JSONObject(sb.toString());
			compare = true;
		}
		for (int i = 1; i <= n; i++) {
			//System.out.println(String.valueOf(i)+"/"+n);
			simulator.advance();
			p.println("," + simulator.getState());
			if(compare == true) {
				if(!cmp.equal(simulator.getState(), scomp.getJSONArray("states").getJSONObject(i)))
					throw new differentStateException(i, simulator.getState(), scomp.getJSONArray("states").getJSONObject(i));// TODO añadir excepciones
			}
			
		}
		// run the sumulation n steps , etc.
		p.println("]");
		p.println("}");
	}
	public void reset() {
		simulator.reset();
	}
	public void setDeltaTime(double dt){
		 simulator.setDeltaTime(dt);
	}
	public void addObserver(SimulatorObserver o) {
		simulator.addObserver(o);
	}
	public List<JSONObject> getForceLawsInfo(){
		return forceLawsFactory.getInfo();
	}
	public void setForceLaws(JSONObject info) {
		simulator.setForceLawsLaws(forceLawsFactory.createInstance(info));
	}

	public void run(int n) {
		for (int i = 1; i <= n; i++) {
			simulator.advance();
		}
	}
}
