package exceptions;

public class AppExceptionModel {


	public int code;
	public String message;
	
	
	public AppExceptionModel() {}
	
	public AppExceptionModel(int code, String message) {
		this.code = code;
		this.message = message;
	}

	
	
}
