package minsal.divap.enums;

public enum FieldType{
	STRINGFIELD(0), INTEGERFIELD(1), DOUBLEFIELD(2), BOOLEANFIELD(3);

	private int id;

	private FieldType(int id) { this.id = id; }

	public int getId()
	{
		return this.id;
	}
}