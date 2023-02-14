package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;


import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel implements SimulatorObserver {

	private Controller _ctrl;
	private boolean _stopped;
	private JButton openButton, physicsButton, runButton, stopButton, exitButton;
	private JSpinner steps;
	private JTextField deltaTime;
	private JLabel stepsLabel, deltaTimeLabel;
	ForceLawsDialog _dialog;
	ControlPanel(Controller ctrl) {
			_ctrl = ctrl;
			_stopped = true;
			initGUI();
			_ctrl.addObserver(this);
	}
	private void initGUI() {
		this.setLayout(new FlowLayout());
		// TODO build the tool bar by adding buttons, etc.
		openButton = new JButton(new ImageIcon("resources/icons/open.png"));
		openButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					fileChooser();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		steps = new JSpinner();
		steps.setToolTipText("steps to run in simulation");
		steps.setPreferredSize(new Dimension(60,20));
		steps.setValue(10000);
		deltaTime = new JTextField("100");
		deltaTime.setToolTipText("time between steps in simulation");
		deltaTime.setPreferredSize(new Dimension(60,20));
		stepsLabel = new JLabel("Steps: ");
		deltaTimeLabel = new JLabel("Delta time: ");
		physicsButton = new JButton(new ImageIcon("resources/icons/physics.png"));
		physicsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				physicsSelectionDialog();
			}
		});
		runButton = new JButton(new ImageIcon("resources/icons/run.png"));
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				run();
			}
		});
		stopButton = new JButton(new ImageIcon("resources/icons/stop.png"));
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
		exitButton = new JButton(new ImageIcon("resources/icons/exit.png"));
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});
		this.add(openButton);
		this.add(physicsButton);
		this.add(runButton);
		this.add(stopButton);
		this.add(stepsLabel);
		this.add(steps);
		this.add(deltaTimeLabel);
		this.add(deltaTime);
		this.add(exitButton);
		
	}
	

// other private/protected methods
// ...


	protected void exit() {
		if(JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION) == 0)
			System.exit(0);
	}
	protected void stop() {
		_stopped = true;
		runButton.setEnabled(true);
		physicsButton.setEnabled(true);
		openButton.setEnabled(true);
		exitButton.setEnabled(true);
		
	}
	protected void run() {
		_stopped = false;
		double dt = Double.parseDouble(deltaTime.getText());
		if (dt > 0)
			_ctrl.setDeltaTime(dt);
		else
			_ctrl.setDeltaTime(100);
		Object o = steps.getValue();
		Number n = (Number) o;
		int i = n.intValue();
		run_sim(i);
		runButton.setEnabled(false);
		physicsButton.setEnabled(false);
		openButton.setEnabled(false);
		exitButton.setEnabled(false);
	}
	protected void physicsSelectionDialog() {
		if (_dialog == null) {
			_dialog = new ForceLawsDialog((Frame) SwingUtilities.getWindowAncestor(this), _ctrl);
		}
		
		if(_dialog.getStatus() == 0) {
			_dialog = null;
		} else if (_dialog.getStatus() == 1) {
			try {
				_dialog.setForceLaws();			
			} catch (Exception e) {
				JOptionPane.showMessageDialog((Frame) SwingUtilities.getWindowAncestor(this), e.getMessage());
				_dialog = null;
				return;
			}
			_dialog = null;
		}
	}
	protected void fileChooser() throws Exception {
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "JSON files", "json");
		fc.setDialogTitle("Open a file");
		fc.setFileFilter(filter);
		InputStream in;
		int returnVal = fc.showOpenDialog(this);
		File f = fc.getSelectedFile();
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			if (f.isFile()) { // 
				in = new FileInputStream(f);
				_ctrl.reset();
				_ctrl.loadBodies(in);
			}
		}
		    
	}
	private void run_sim(int n) {
		if ( n>0 && !_stopped ) {
			try {
				_ctrl.run(1);			
			} catch (Exception e) {
				runButton.setEnabled(true);
				physicsButton.setEnabled(true);
				openButton.setEnabled(true);
				exitButton.setEnabled(true);
				_stopped = true;
				JOptionPane.showMessageDialog((Frame) SwingUtilities.getWindowAncestor(this), e.getMessage());
				return;
			}
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					run_sim(n-1);
				}
			});
		} else {
			_stopped = true;
			//TODO enable all buttons
			
			runButton.setEnabled(true);
			physicsButton.setEnabled(true);
			openButton.setEnabled(true);
			exitButton.setEnabled(true);
		}
	}
// 	SimulatorObserver methods

	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		deltaTime.setText(String.valueOf(dt));
	}
	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		deltaTime.setText(String.valueOf(dt));
	}
	@Override
	public void onDeltaTimeChanged(double dt) {
		deltaTime.setText(String.valueOf(dt));
	}
	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {}
	@Override
	public void onAdvance(List<Body> bodies, double time) {}
	@Override
	public void onForceLawsChanged(String fLawsDesc) {}
}