package com.iservport.install.strategy;

import org.helianto.core.domain.Entity;
import org.helianto.core.domain.Operator;
import org.helianto.install.service.AbstractEntityInstallStrategy;
import org.helianto.user.domain.User;

/**
 * Custom Entity prototype generation.
 * 
 * @author mauriciofernandesdecastro
 */
public abstract class AbstractCustomTypeEntityInstallStrategy 
	extends AbstractEntityInstallStrategy
{
	
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
	
	@Override
	protected void runOnce(Operator arg0, Entity arg1, User arg2) { }

}
