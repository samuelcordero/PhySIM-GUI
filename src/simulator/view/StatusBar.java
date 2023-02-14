package simulator.view;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

@SuppressWarnings("serial")
public class StatusBar extends JPanel implements SimulatorObserver {
// ...
	private JLabel _currTime; // for current time
	private JLabel _currLaws; // for gravity laws
	private JLabel _numOfBodies; // for number of bodies
	public StatusBar(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}
	private void initGUI() { 
		this.setLayout(new FlowLayout(FlowLayout.TRAILING, 20, 0));
		this.setBorder(BorderFactory.createBevelBorder(1));
		// TODO complete the code to build the tool bar
		_currTime = new JLabel();
		_currTime.setSize(50, 10);
		_currLaws = new JLabel();
		_numOfBodies = new JLabel();
		this.add(_currTime, FlowLayout.LEFT);
		this.add(_numOfBodies, FlowLayout.CENTER);
		this.add(_currLaws, FlowLayout.RIGHT);
	}
	// SimulatorObserver methods
	// ...
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		_currTime.setText("Time: "  + String.valueOf(time));
		_currLaws.setText("Laws: " + fLawsDesc);
		_numOfBodies.setText("Bodies: "  + String.valueOf(bodies.size()));
	}
	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		_currTime.setText("Time: "  + String.valueOf(time));
		_currLaws.setText("Laws: " + fLawsDesc);
		_numOfBodies.setText("Bodies: "  + String.valueOf(bodies.size()));
	}
	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		_numOfBodies.setText("Bodies: "  + String.valueOf(bodies.size()));
	}
	@Override
	public void onAdvance(List<Body> bodies, double time) {
		_currTime.setText("Time: "  + String.valueOf(time));
		_numOfBodies.setText("Bodies: "  + String.valueOf(bodies.size()));
	}
	@Override
	public void onDeltaTimeChanged(double dt) {}
	@Override
	public void onForceLawsChanged(String fLawsDesc) {
		_currLaws.setText("Laws: " + fLawsDesc);
	}
}	