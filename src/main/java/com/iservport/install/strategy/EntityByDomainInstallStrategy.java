package com.iservport.install.strategy;

import java.util.ArrayList;
import java.util.List;

import org.helianto.core.domain.Entity;
import org.helianto.core.domain.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entity prototype generation by domain.
 * 
 * @author Eldevan Nery Junior.
 */
public class EntityByDomainInstallStrategy 
	extends AbstractCustomTypeEntityInstallStrategy
{
	
	private static final Logger logger = LoggerFactory.getLogger(EntityByDomainInstallStrategy.class);
	
	/**
	 * Gera protótipos a partir de domínios.
	 * 
	 * @param params
	 */
	public List<Entity> generateEntityPrototypes(Object... params) {
		if (params!=null && params.length>0 && params[0] instanceof Identity) {
			Identity identity = (Identity) params[0];
			List<Entity> entityList = new ArrayList<>();
			Entity prototype = createPrototype(identity.getOptionalSourceAlias(), "", 'C');
			logger.info("Created prototype from domain {}.", prototype);
			entityList.add(prototype);
			return entityList;
		}
		throw new IllegalArgumentException("Identity required.");

	}

}
