package ch.compass.gonzoproxy.mvc.model;

import javax.swing.table.AbstractTableModel;

import ch.compass.gonzoproxy.mvc.listener.ApduListener;

public class DetailTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1437358812481945385L;
	private Apdu apdu;
	private ApduData data;
	private String[] columnNames = { "Field", "Value", "Description" };
	  
	public DetailTableModel(Apdu apdu, ApduData data) {
		this.apdu = apdu;
		this.data = data;
		this.data.addApduListener(createApduListener());
	}

	private ApduListener createApduListener() {
		return new ApduListener() {
			
			@Override
			public void apduReceived(Apdu apdu) {
				DetailTableModel.this.setApdu(apdu);
			}
			
			@Override
			public void apduCleared() {
				DetailTableModel.this.setApdu(new Apdu(new byte[0]));
			}
		};
	}

	public String getColumnName(int col) {
		return this.columnNames[col].toString();
	}

	@Override
	public int getColumnCount() {
		return this.columnNames.length;
	}

	@Override
	public int getRowCount() {
		return apdu.getFields().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Field field = apdu.getFields().get(rowIndex);

		switch (columnIndex) {
		case 0:
			return field.getName();
		case 1:
			return field.getValue();
		case 2:
			return field.getDescription();
		}
		return null;

	}

	public void setApdu(Apdu editApdu) {
		this.apdu = editApdu;
		fireTableDataChanged();
	}

}