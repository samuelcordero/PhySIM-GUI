package simulator.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.json.JSONObject;

import simulator.control.Controller;

public class ForceLawsDialog extends JDialog implements ActionListener{
	private static final long serialVersionUID = 1L;

	private int _status;
	private JComboBox<String> _forceLawsComboBox;
	private DefaultComboBoxModel<String> _forceLawsComboBoxModel;
	private ForceLawsTableModel _forceLawsTableModel;
	private Controller ctrl;
	private class ForceLawsTableModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private String[] _header = { "Key", "Value", "Description" };
		String[][] _data;

		ForceLawsTableModel() {
			_data = new String[5][3];
			clear();
		}

		public void clear() {
			for (int i = 0; i < 5; i++)
				for (int j = 0; j < 3; j++)
					_data[i][j] = "";
			fireTableStructureChanged();
		}

		@Override
		public String getColumnName(int column) {
			return _header[column];
		}

		@Override
		public int getRowCount() {
			return _data.length;
		}

		@Override
		public int getColumnCount() {
			return _header.length;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return true;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return _data[rowIndex][columnIndex];
		}

		@Override
		public void setValueAt(Object o, int rowIndex, int columnIndex) {
			_data[rowIndex][columnIndex] = o.toString();
		}

		
		public String getData() {
			StringBuilder s = new StringBuilder();
			s.append('{');
			for (int i = 0; i < 5; i++) {
				if (!_data[i][0].isEmpty() && !_data[i][1].isEmpty()) {
					s.append('"');
					s.append(_data[i][0]);
					s.append('"');
					s.append(':');
					s.append(_data[i][1]);
					s.append(',');
				}
			}

			if (s.length() > 1)
				s.deleteCharAt(s.length() - 1);
			s.append('}');

			return s.toString();
		}
		public void setData(JSONObject info) {
			for (int i = 0; i < info.getJSONArray("data").length(); i++) {
				setValueAt(info.getJSONArray("data").getJSONObject(i).getString("name"), i, 0);
				setValueAt(info.getJSONArray("data").getJSONObject(i).getString("desc"), i, 2);
			}
			fireTableStructureChanged();
		}
	}

	public ForceLawsDialog(Frame parent, Controller ctrl) {
		super(parent, true);
		this.ctrl= ctrl;
		initGUI();
	}


	private void initGUI() {

		_status = 0;

		setTitle("Force Laws Selection");
		JPanel mainPanel = new JPanel();
		JPanel helpMessagePanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		helpMessagePanel.setLayout(new FlowLayout());
		setContentPane(mainPanel);

		JLabel helpMsg = new JLabel("Select a force law and provide values for the parameters in the Value column (default values are used for parameters with no value).");


		helpMessagePanel.add(helpMsg);
		mainPanel.add(helpMessagePanel);


		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		_forceLawsTableModel = new ForceLawsTableModel();
		JTable dataTable = new JTable(_forceLawsTableModel) {
			private static final long serialVersionUID = 1L;

			// we override prepareRenderer to resized rows to fit to content
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				tableColumn.setPreferredWidth(
						Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
				return component;
			}
		};
		_forceLawsTableModel.setData(ctrl.getForceLawsInfo().get(0));
		JScrollPane tableScroll = new JScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mainPanel.add(tableScroll);

		_forceLawsComboBoxModel = new DefaultComboBoxModel<>();
		_forceLawsComboBoxModel.addElement("Newton's law of universal gravitation");
		_forceLawsComboBoxModel.addElement("Moving towards a fixed point");
		_forceLawsComboBox = new JComboBox<>(_forceLawsComboBoxModel);
		_forceLawsComboBox.addActionListener(this);

		buttonsPanel.add(_forceLawsComboBox);
		mainPanel.add(buttonsPanel);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 0;
				ForceLawsDialog.this.setVisible(false);
			}
		});
		buttonsPanel.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_forceLawsComboBoxModel.getSelectedItem() != null) {
					_status = 1;
					ForceLawsDialog.this.setVisible(false);
				}
			}
		});
		buttonsPanel.add(okButton);

		setPreferredSize(new Dimension(900, 400));
		pack();
		setResizable(false);
		setVisible(true);
	}



	public int getStatus() {
		return _status; 
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == _forceLawsComboBox) {
			switch(_forceLawsComboBox.getSelectedIndex()) {
				case 0:
					_forceLawsTableModel.clear();
					_forceLawsTableModel.setData(ctrl.getForceLawsInfo().get(0));
					break;
				case 1:
					_forceLawsTableModel.clear();
					_forceLawsTableModel.setData(ctrl.getForceLawsInfo().get(1));
					break;
				default:
					break;
			}
		}
		
	}
	public void setForceLaws() {
		JSONObject info = new JSONObject();
		JSONObject data = new JSONObject(_forceLawsTableModel.getData());
		switch(_forceLawsComboBox.getSelectedIndex()) {
			case 0:
				info.put("type", "nlug");
				break;
			case 1:
				info.put("type", "mtcp");
				break;
			default:
				break;
		}
		info.put("data", data);
		ctrl.setForceLaws(info);
	}
}
