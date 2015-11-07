package com.iservport.install.strategy;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.helianto.core.domain.Entity;
import org.helianto.core.domain.Identity;
import org.helianto.core.domain.Signup;
import org.helianto.install.service.EntityInstallStrategy;
import org.junit.Test;

/**
 * 
 * @author mauriciofernandesdecastro
 */
public class EntityInstallStrategyTests {

	/**
	 * Uma identidade é requerida e o protótipo gerado pelo principal da identidade.
	 */
	@Test
	public void principal() {
		EntityInstallStrategy strategy = new EntityByPrincipalInstallStrategy();
		Identity identity = new Identity("principal");
		List<Entity> entityList = strategy.generateEntityPrototypes(identity);
		assertEquals("principal", entityList.get(0).getAlias());
	}

	/**
	 * Um formulário é requerido e o protótipo gerado pelo cnpj associado à identidade.
	 */
	@Test
	public void cnpj1() {
		EntityInstallStrategy strategy = new EntityByCNPJInstallStrategy();
		Signup form = new Signup();
		form.setDomain("73.314.756/0001-89");
		List<Entity> entityList = strategy.generateEntityPrototypes(form);
		assertEquals("45EB1C4", entityList.get(0).getAlias());
		assertEquals(Integer.toHexString(73314756).toUpperCase(), entityList.get(0).getAlias());
	}

	/**
	 * Testa filial.
	 */
	@Test
	public void cnpj2() {
		EntityInstallStrategy strategy = new EntityByCNPJInstallStrategy();
		Signup form = new Signup();
		form.setDomain("73.314.756/0002-xx");
		List<Entity> entityList = strategy.generateEntityPrototypes(form);
		assertEquals("45EB1C4:2", entityList.get(0).getAlias());
	}

}
