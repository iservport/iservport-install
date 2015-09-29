package com.iservport.install.strategy;

import java.util.ArrayList;
import java.util.List;

import org.helianto.core.domain.Entity;
import org.helianto.core.domain.Signup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entity prototype generation.
 * 
 * @author mauriciofernandesdecastro
 * @author Eldevan Nery Junior.
 */
public class EntityByCNPJInstallStrategy 
	extends AbstractKeyTypeEntityInstallStrategy
{
	
	private static final Logger logger = LoggerFactory.getLogger(EntityByCNPJInstallStrategy.class);
	
	/**
	 * Gera entidades por CNPJ.
	 */
	public List<Entity> generateEntityPrototypes(Object... params) {
		//TODO validar o cnpj com expressão regular : d{2}.d{3}.d{3}/d{4}-d{2}$
		if (params!=null && params.length>0 && params[0] instanceof Signup) {
			Signup form = (Signup) params[0];
			List<Entity> entityList = new ArrayList<>();
			
			if(form.getDomain().contains(".")){
				String[] parts = form.getDomain().replace(".", "").split("/");
				Integer toInteger = Integer.parseInt(parts[0]);
				String alias = Integer.toHexString(toInteger); 
				if (parts.length>1) {
					//fornecido com os separadores
					Integer tail = Integer.parseInt(parts[1].split("-")[0]) ; 
					if(tail >1){
						alias += ":"+Integer.toHexString(tail);
					}
					form.setDomain(alias.toUpperCase());
				}
				else {
					// TODO processar sem os separadores
					throw new IllegalArgumentException("CNPJ must match 00.000.000/0000-00 format.");
				}
			}
			Entity prototype = createPrototype(form.getDomain(), form.getFirstName(), 'C');
			logger.info("Created prototype from CNPJ as {}.", prototype);
			entityList.add(prototype);
			//retorna entidade
			return entityList;
		}
		throw new IllegalArgumentException("Signup required.");

	}

}
