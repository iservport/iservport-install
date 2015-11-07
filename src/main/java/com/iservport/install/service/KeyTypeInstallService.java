package com.iservport.install.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.helianto.core.domain.KeyType;
import org.helianto.core.repository.KeyTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.iservport.install.strategy.AbstractCustomTypeEntityInstallStrategy;

/**
 * Key type install service.
 * 
 * @author mauriciofernandesdecastro
 */
public class KeyTypeInstallService 
	implements InitializingBean
{

	private static final Logger logger = LoggerFactory.getLogger(KeyTypeInstallService.class);
	
	private static final String KEY_TYPE_PATH_FILE = "/META-INF/data/keyType/";
	
	@Inject
	private KeyTypeRepository keyTypeRepository;
	
	@Inject
	private AbstractCustomTypeEntityInstallStrategy entityInstallStrategy;
	
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
	 * Cria tipos de chave.
	 * 
	 * @param context
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void afterPropertiesSet() throws JsonParseException, JsonMappingException, IOException{
		String contextName = entityInstallStrategy.getContextName();
		logger.debug("Creating Key types for {}.", contextName);
		ClassPathResource resource =  new ClassPathResource(KEY_TYPE_PATH_FILE+"keyTypes.json");
		ObjectMapper mapper = new ObjectMapper(
				new JsonFactory()
				.configure(Feature.ALLOW_COMMENTS, true))
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		ArrayList<KeyType> user = 
				mapper.readValue(resource.getFile(), TypeFactory.defaultInstance().constructCollectionType(List.class,  
				KeyType.class));
		for (KeyType keyType : user) {
			logger.debug("Search ketType to context {} with keyCode {}.", contextName, keyType.getKeyCode());
			KeyType exists = keyTypeRepository.findByOperator_operatorNameAndKeyCode(contextName, keyType.getKeyCode());
			//update
			if(exists!=null){
				exists.setKeyCode(keyType.getKeyCode());
				exists.setKeyGroup(keyType.getKeyGroup());
				exists.setKeyName(keyType.getKeyName());
				exists.setPurpose(keyType.getPurpose());
				exists.setSynonyms(keyType.getSynonyms());
				keyTypeRepository.saveAndFlush(exists);
				logger.debug("update ketType to context {} with keyCode {} to {} .", contextName, exists.getKeyCode(), keyType);
			}
		}
	}

}
