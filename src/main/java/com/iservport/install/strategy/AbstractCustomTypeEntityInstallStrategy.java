package com.iservport.install.strategy;

import org.helianto.install.service.AbstractEntityInstallStrategy;

/**
 * Custom Entity prototype generation.
 * 
 * @author mauriciofernandesdecastro
 */
public abstract class AbstractCustomTypeEntityInstallStrategy 
	extends AbstractEntityInstallStrategy
{
	
	/**
	 * Senha inicial.
	 */
	protected String getInitialSecret() {
		return "123rooT#";
	}
	
}
