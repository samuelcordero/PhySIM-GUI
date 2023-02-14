package simulator.control;

import org.json.JSONObject;

public class differentStateException extends Exception {
	/**
	 * Creo que esto sirve para diferenciar clases con el mismo nombre
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Pero no lo se xd
	 */
	
	
	public differentStateException(int i, JSONObject state1, JSONObject state2) {
		super("Different states in step " + String.valueOf(i) + "\n" +state1.toString() + "\n" + state2.toString());
	}

}
