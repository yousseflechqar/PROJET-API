package beans;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public class MarcheAttachs {
	
	

	/**
	 * it won't work if we MultipartFile[][] (as multi-dimensional array)
	 * and we can't send a MutipartFile within a json format
	 * we have to send it as a blob in javascript with formData()
	 */
	public List<MultipartFile>[] osAttachs;
	public List<MultipartFile>[] decAttachs;

	public List<MultipartFile>[] getOsAttachs() {
		return osAttachs;
	}

	public void setOsAttachs(List<MultipartFile>[] osAttachs) {
		this.osAttachs = osAttachs;
	}

	public List<MultipartFile>[] getDecAttachs() {
		return decAttachs;
	}

	public void setDecAttachs(List<MultipartFile>[] decAttachs) {
		this.decAttachs = decAttachs;
	}


	
	

}
