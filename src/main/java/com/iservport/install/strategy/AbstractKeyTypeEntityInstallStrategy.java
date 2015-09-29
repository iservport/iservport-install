package com.iservport.install.strategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.helianto.core.domain.Entity;
import org.helianto.core.domain.KeyType;
import org.helianto.core.domain.Operator;
import org.helianto.core.repository.KeyTypeRepository;
import org.helianto.install.service.AbstractEntityInstallStrategy;
import org.helianto.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Entity prototype generation.
 * 
 * @author mauriciofernandesdecastro
 */
public abstract class AbstractKeyTypeEntityInstallStrategy 
	extends AbstractEntityInstallStrategy
{
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractKeyTypeEntityInstallStrategy.class);
	
	private static final String KEY_TYPE_PATH_FILE = "/META-INF/data/keyType/";
	
    protected String contextDataPath;
    
	@Inject
	private ObjectMapper mapper;
	
	@Inject
	private KeyTypeRepository keyTypeRepository;
	
	/**
	 * Brasil.
	 */
	protected String getDefaultCountry() {
		return "1058";
	}
	
	/**
	 * Estados são lidos a partir deste arquivo.
	 */
	protected String getDefaultStateFile() {
		return "states-1058.xml";
	}
	
	/**
	 * Senha inicial.
	 */
	protected String getInitialSecret() {
		return "123rooT#";
	}
	
	/**
	 * Após a criação de usuário, inicia instalação dos tipos de chave.
	 */
	protected void runOnce(Operator context, Entity rootEntity, User rootUser) {
		try {
			createKeyTypes(context);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Cria tipos de chave.
	 * 
	 * @param context
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void createKeyTypes(Operator context) throws JsonParseException, JsonMappingException, IOException{
		logger.debug("Creating Key types for {}.", context);
		ClassPathResource resource =  new ClassPathResource(KEY_TYPE_PATH_FILE+"keyTypes.json");
		ArrayList<KeyType> user = 
				mapper.readValue(resource.getFile(), TypeFactory.defaultInstance().constructCollectionType(List.class,  
				KeyType.class));
			if(context!=null){
			for (KeyType keyType : user) {
				logger.debug("Search ketType to context {} with keyCode {}.", context, keyType.getKeyCode());
				KeyType exists = keyTypeRepository.findByOperatorAndKeyCode(context, keyType.getKeyCode());
				//update
				if(exists!=null){
					exists.setKeyCode(keyType.getKeyCode());
					exists.setKeyGroup(keyType.getKeyGroup());
					exists.setKeyName(keyType.getKeyName());
					exists.setPurpose(keyType.getPurpose());
					exists.setSynonyms(keyType.getSynonyms());
					keyTypeRepository.saveAndFlush(exists);
					logger.debug("update ketType to context {} with keyCode {} to {} .", context, exists.getKeyCode(), keyType);
				}
				//save
				else{
					keyType.setOperator(context);
					keyType = keyTypeRepository.saveAndFlush(keyType);
					logger.debug("saved ketType  {} .", keyType);
				}
			}
		}else{
			//TODO create?
		}
	}

}
