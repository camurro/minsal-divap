package minsal.divap.enums;

public enum DocumentType{
	EXCEL(0), WORD(1);

	private int id;

	private DocumentType(int id) { this.id = id; }

	public int getId()
	{
		return this.id;
	}
}