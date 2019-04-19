package beans;

public class ProgrammeBean extends ParentChildBean {

	public Integer phase;

	public ProgrammeBean() {
		super();
	}

	public ProgrammeBean(Integer id, String label, Integer idParent, String labelParent, Integer phase) {
		super(id, label, idParent, labelParent);
		this.phase = phase;
	}
	


	
}
