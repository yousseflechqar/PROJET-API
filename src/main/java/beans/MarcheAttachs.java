package beans;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public class MarcheAttachs {
	
	

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
