package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;

public class EpsilonEqualStates implements StateComparator{
	protected double eps;
	private boolean eq;
	public EpsilonEqualStates(){
		this.eps = 0.0;
	}
	public EpsilonEqualStates(double eps){
		this.eps = eps;
	}
	@Override
	public boolean equal(JSONObject s1, JSONObject s2) {
		eq = true;
		int i = 0;
		JSONArray bodies1, bodies2 = new JSONArray();
		JSONObject body1, body2;
		bodies1 = s1.getJSONArray("bodies");
		bodies2 = s2.getJSONArray("bodies");
		Vector2D p1, p2, v1, v2, f1, f2;
		
		if (s1.getDouble("time") == s2.getDouble("time")) {
			if (bodies1.length()==bodies2.length()){
				while (eq && (i < bodies1.length())) {
					body1 = bodies1.getJSONObject(i);
					body2 = bodies2.getJSONObject(i);
					if(body1.getString("id").equals(body2.getString("id"))) {
						if (Math.abs(truncarBaseDiez(body1.getDouble("m"))-truncarBaseDiez(body2.getDouble("m"))) <= eps) {
							p1 = json2vector2d(body1.getJSONArray("p"));
							p2 = json2vector2d(body2.getJSONArray("p"));
							v1 = json2vector2d(body1.getJSONArray("v"));
							v2 = json2vector2d(body2.getJSONArray("v"));
							f1 = json2vector2d(body1.getJSONArray("f"));
							f2 = json2vector2d(body2.getJSONArray("f"));
							//System.out.println(distanciaEntreVectoresTruncados(p1,p2));
							if(distanciaEntreVectoresTruncados(p1,p2) > eps)
								eq = false;
							//System.out.println(distanciaEntreVectoresTruncados(v1,v2));
							if(distanciaEntreVectoresTruncados(v1,v2) > eps)
								eq = false;
							//System.out.println(distanciaEntreVectoresTruncados(f1,f2)); //para mostrar diferencias por pantalla
							if(distanciaEntreVectoresTruncados(f1,f2) > eps) 
								eq = false;
						} else { eq = false; }		
					} else { eq = false; }
					i++;
				}
				
			}
			else { eq = false; }
		}
		else { eq = false; }
		return eq;
	}
	//esta clase facilita la conversion de JSONArray a Vector2D, a su vez comprueba que sea bidimensional y lanza excepción en caso contrario
	private Vector2D json2vector2d(JSONArray v) throws IllegalArgumentException{
		Vector2D vec = null;
		if(v.length() != 2)
			throw new IllegalArgumentException("Vector not 2D");			
		vec = new Vector2D(v.getDouble(0), v.getDouble(1));
		return vec;
	}
	
	
	//con esto he arreglado el problema de las potencias de base 10
	private double truncarBaseDiez(double num) {
		if (num == 0)
			return 0.0;
		return num*Math.pow(10, -potenciaBase10(num));
	}
	private double potenciaBase10(double num) {
		if (num == 0)
			return -1.0;
		if (num < 0) 
			return Math.floor(Math.log10(num*-1));
		return Math.floor(Math.log10(num));
	}
	private double distanciaEntreVectoresTruncados(Vector2D v1, Vector2D v2) {
		if((potenciaBase10(v1.getX())!=potenciaBase10(v2.getX()))||(potenciaBase10(v1.getY())!= potenciaBase10(v2.getY()))) {
			this.eq = false;//esto creo que puede ahorrarse con lo de abajo pero por si acaso
			return Double.MAX_VALUE;
		}
		Vector2D vaux = new Vector2D(truncarBaseDiez(v1.getX()), truncarBaseDiez(v1.getY()));
		return vaux.distanceTo(new Vector2D (truncarBaseDiez(v2.getX()), truncarBaseDiez(v2.getY())));
	}
}
