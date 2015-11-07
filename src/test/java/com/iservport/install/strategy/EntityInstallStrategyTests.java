package com.iservport.install.strategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.easymock.EasyMock;
import org.helianto.core.domain.City;
import org.helianto.core.domain.Entity;
import org.helianto.core.domain.Signup;
import org.helianto.core.repository.CityRepository;
import org.helianto.install.service.EntityInstallStrategy;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 
 * @author mauriciofernandesdecastro
 */
public class EntityInstallStrategyTests {

	Signup form;
	City city;
	CityRepository cityRepository;
	
	@Before
	public void setUp() {
		form =	new Signup();
		form.setCityId(1);
		city = new City();
		cityRepository = EasyMock.createMock(CityRepository.class);
	}
	
	/**
	 * Uma identidade é requerida e o protótipo gerado pelo principal da identidade.
	 */
	@Test
	public void principal() {
		form.setPrincipal("principal");
		EntityInstallStrategy strategy = new EntityByPrincipalInstallStrategy();
		
		ReflectionTestUtils.setField(strategy, "cityRepository", cityRepository);
		EasyMock.expect(cityRepository.findOne(form.getCityId())).andReturn(city);
		EasyMock.replay(cityRepository);
		
		List<Entity> entityList = strategy.generateEntityPrototypes(form);
		assertEquals("principal", entityList.get(0).getAlias());
		assertSame(city, entityList.get(0).getCity());
	}

	/**
	 * Um formulário é requerido e o protótipo gerado pelo cnpj associado à identidade.
	 */
	@Test
	public void cnpj1() {
		form.setAlias("73.314.756/0001-89");
		EntityInstallStrategy strategy = new EntityByCNPJInstallStrategy();

		ReflectionTestUtils.setField(strategy, "cityRepository", cityRepository);
		EasyMock.expect(cityRepository.findOne(form.getCityId())).andReturn(city);
		EasyMock.replay(cityRepository);
		
		List<Entity> entityList = strategy.generateEntityPrototypes(form);
		assertEquals("45EB1C4", entityList.get(0).getAlias());
		assertEquals(Integer.toHexString(73314756).toUpperCase(), entityList.get(0).getAlias());
		assertSame(city, entityList.get(0).getCity());
	}

	/**
	 * Testa filial.
	 */
	@Test
	public void cnpj2() {
		form.setAlias("73.314.756/0002-xx");
		EntityInstallStrategy strategy = new EntityByCNPJInstallStrategy();

		ReflectionTestUtils.setField(strategy, "cityRepository", cityRepository);
		EasyMock.expect(cityRepository.findOne(form.getCityId())).andReturn(city);
		EasyMock.replay(cityRepository);
		
		List<Entity> entityList = strategy.generateEntityPrototypes(form);
		assertEquals("45EB1C4:2", entityList.get(0).getAlias());
		assertSame(city, entityList.get(0).getCity());
	}

}
