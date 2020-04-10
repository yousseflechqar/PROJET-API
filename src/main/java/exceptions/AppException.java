package exceptions;

public class AppException {


	public int code;
	public String message;
	
	
	public AppException() {}
	
	public AppException(int code, String message) {
		this.code = code;
		this.message = message;
	}

	
	
}
