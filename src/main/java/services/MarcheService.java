package services;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import beans.MarcheBean;

@Service
public class MarcheService {
	
	
	
	
	@Transactional(rollbackOn = Exception.class)
	public Integer saveMarche(MarcheBean bean) {
		return null;
		
		
		
	}

}
