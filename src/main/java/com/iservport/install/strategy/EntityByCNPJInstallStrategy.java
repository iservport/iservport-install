package com.iservport.install.strategy;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.MaskFormatter;

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
	extends AbstractCustomTypeEntityInstallStrategy
{

	private static final Logger logger = LoggerFactory.getLogger(EntityByCNPJInstallStrategy.class);

	/**
	 * Gera entidades por CNPJ.
	 */
	public List<Entity> generateEntityPrototypes(Object... params) {
		if (params!=null && params.length>0 && params[0] instanceof Signup) {
			Signup form = (Signup) params[0];
			List<Entity> entityList = new ArrayList<>();
			String newAlias = form.getAlias();
			if(form.getAlias().contains(".")){
				newAlias = getNewAlias(form.getAlias());
			}
			else {
				try {
					MaskFormatter mask = new MaskFormatter("##.###.###/####-##");
					mask.setValueContainsLiteralCharacters(false);
					newAlias = getNewAlias(mask.valueToString(form.getAlias()));
				} catch (ParseException e) {
					throw new IllegalArgumentException("CNPJ Format Error.");
				}

			}
			Entity prototype = createPrototype(newAlias, form);
			logger.info("Created prototype from CNPJ as {}.", prototype);
			entityList.add(prototype);
			return entityList;
		}
		throw new IllegalArgumentException("Signup required.");

	}

	private String getNewAlias(String alias){
		String[] parts = alias.replace(".", "").split("/");
		Integer toInteger = Integer.parseInt(parts[0]);
		String newAlias = Integer.toHexString(toInteger); 
		if (parts.length>1) {
			Integer tail = Integer.parseInt(parts[1].split("-")[0]) ; 
			if(tail >1){
				newAlias += ":"+Integer.toHexString(tail);
			}
		}else{
			throw new IllegalArgumentException("CNPJ Format Error.");
		}
		return newAlias.toUpperCase();
	}

}
