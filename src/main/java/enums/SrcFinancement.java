package enums;

public enum SrcFinancement {
	
	
	BG(1), BP(2), INDH(3), PRDTS_INDH(4), PRDTS_FDRZM(5), CT(10);
	
	public int val;
	
	private SrcFinancement(int val) {
		this.val = val;
	}
	
	public int val() {
		return this.val;
	}

}
