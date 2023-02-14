package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PhysicsSimulator {
	
	private double tiempo;
	private double trpp;
	private ForceLaws lf;
	private List<Body> bodyList;
	private List<SimulatorObserver> observerList;
	
	//constructor
	public PhysicsSimulator(double trpp, ForceLaws lf) throws IllegalArgumentException{
		
		if(trpp > 0) {
			this.trpp = trpp;
		}
		else {
			throw new IllegalArgumentException();
		}
		this.lf = lf;
		this.tiempo = 0.0;
		if(this.lf == null) {
			throw new IllegalArgumentException();
		}
		bodyList = new ArrayList<Body>();
		observerList = new ArrayList<SimulatorObserver>();
	}

	public void advance() {
		//reseteamos fuerzas
		for(int i = 0; i < bodyList.size(); i++) {
			bodyList.get(i).resetForce();
		}
		//llamamos a la ley de fuerzas
		lf.apply(bodyList);
		//llamamos al move de cada cuerpo
		for(int i = 0; i < bodyList.size(); i++) {
			bodyList.get(i).move(trpp);
		}
		//sumamos el tiempo real por paso al tiempo para actualizarlo
		tiempo += trpp;
		for (int j = 0; j < observerList.size(); j++) {
			observerList.get(j).onAdvance(bodyList, tiempo);
		}
	}
	
	public void addBody(Body b) throws IllegalArgumentException{
		boolean encontrado = false;
		int i = 0;
		while (!encontrado && (i < bodyList.size())) {
			if (bodyList.get(i).getId().equals(b.getId())) { //si el cuerpo tiene el mismo id que uno ya perteneciente a la lista lanzamos excepcion
				encontrado = true;
				throw new IllegalArgumentException();
			}
			i++;
		}
		if(!encontrado) {
			bodyList.add(b);
		}
		for (int j = 0; j < observerList.size(); j++) {
			observerList.get(j).onBodyAdded(bodyList, b);
		}
	}
	
	public JSONObject getState() {
		JSONObject a = new JSONObject();
		JSONArray aux = new JSONArray();

		a.put("time" ,tiempo);
		for(int i = 0; i < bodyList.size(); i++) {
			aux.put(bodyList.get(i).getState());
		}
		a.put("bodies" , aux);
		
		return a;
	}
	
	public String toString() {
		return getState().toString();
	}
	
	
	
	//nuevos métodos para MVC
	
	public void reset() {
		bodyList.clear();
		tiempo = 0.0;
		for (int i = 0; i < observerList.size(); i++) {
			observerList.get(i).onReset(bodyList, tiempo, trpp, lf.toString());
		}
	}
	
	public void setDeltaTime(double dt) throws IllegalArgumentException{
		if (dt > 0) 
			trpp = dt;
		else 
			throw new IllegalArgumentException();
		for (int i = 0; i < observerList.size(); i++) {
			observerList.get(i).onDeltaTimeChanged(trpp);
		}
	}
	
	public void setForceLawsLaws(ForceLaws forceLaws) throws IllegalArgumentException{
		if(forceLaws != null)
			lf = forceLaws;
		else
			throw new IllegalArgumentException();
		for (int i = 0; i < observerList.size(); i++) {
			observerList.get(i).onForceLawsChanged(lf.toString());
		}
	}
	public void addObserver(SimulatorObserver o) {
		observerList.add(o);
		o.onRegister(bodyList, tiempo, trpp, lf.toString());
	}
}
