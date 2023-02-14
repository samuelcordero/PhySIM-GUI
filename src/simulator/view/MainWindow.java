package simulator.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.control.Controller;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	Controller _ctrl;
	
	public MainWindow(Controller ctrl) {
		super("Physics Simulator");
		_ctrl = ctrl;
		initGUI();
	}
	
	private void initGUI() {
		this.setPreferredSize(new Dimension(1000, 800));
		this.setResizable(false);
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		JPanel bodiesTablePanel = new JPanel(new FlowLayout());
		bodiesTablePanel.add(new BodiesTable(_ctrl));
		bodiesTablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		centerPanel.add(bodiesTablePanel);
		centerPanel.add(Box.createHorizontalGlue());
		JPanel viewerPanel = new JPanel(new FlowLayout());
		viewerPanel.add(new Viewer(_ctrl));
		viewerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		centerPanel.add(viewerPanel);
		mainPanel.add(new ControlPanel(_ctrl), BorderLayout.PAGE_START);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(new StatusBar(_ctrl), BorderLayout.PAGE_END);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
}
