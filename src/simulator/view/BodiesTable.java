package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;

public class BodiesTable extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	BodiesTable(Controller ctrl) {
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(990,160));
		setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.black, 2),
				"Bodies",
				TitledBorder.LEFT, TitledBorder.TOP));
		this.add(new JScrollPane(new JTable(new BodiesTableModel(ctrl))));
		this.setVisible(true);
	}
}