package com.guitar.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.guitar.db.repository.ModelJpaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.guitar.db.model.Model;
import com.guitar.db.repository.ModelRepository;

@ContextConfiguration(locations={"classpath:com/guitar/db/applicationTests-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ModelPersistenceTests {
	@Autowired
	private ModelRepository modelRepository;

	@Autowired
	private ModelJpaRepository modelJpaRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	@Transactional
	public void testSaveAndGetAndDelete() throws Exception {
		Model m = new Model();
		m.setFrets(10);
		m.setName("Test Model");
		m.setPrice(BigDecimal.valueOf(55L));
		m.setWoodType("Maple");
		m.setYearFirstMade(new Date());
		m = modelRepository.create(m);
		
		// clear the persistence context so we don't return the previously cached location object
		// this is a test only thing and normally doesn't need to be done in prod code
		entityManager.clear();

		Model otherModel = modelRepository.find(m.getId());
		assertEquals("Test Model", otherModel.getName());
		assertEquals(10, otherModel.getFrets());
		
		//delete BC location now
		modelRepository.delete(otherModel);
	}

	@Test
	public void testGetModelsInPriceRange() throws Exception {
		List<Model> mods = modelRepository.getModelsInPriceRange(BigDecimal.valueOf(1000L), BigDecimal.valueOf(2000L));
		assertEquals(4, mods.size());
	}

	@Test
	public void testGetModelsByPriceRangeAndWoodType() throws Exception {
		List<Model> mods = modelRepository.getModelsByPriceRangeAndWoodType(BigDecimal.valueOf(1000L), BigDecimal.valueOf(2000L), "Maple");
		assertEquals(3, mods.size());
	}

	@Test
	public void testGetModelsByPriceRangeAndWoodTypeWithQuery() throws Exception {
		Pageable page = new PageRequest(0, 3, Sort.Direction.ASC, "name");
		List<Model> mods = modelJpaRepository.queryByPriceRangeAndWoodType(BigDecimal.valueOf(1000L), BigDecimal.valueOf(2000L), "%Maple%", page);
		assertEquals(3, mods.size());
	}

	@Test
	public void testPagingAndSorting() throws Exception {
		Pageable page = new PageRequest(0, 3);
		Page<Model> mods = modelJpaRepository.findAll(page); // retuns Page object which can give insights into next and previous pages. (Caveat: Fires count query every time)
		mods.forEach(model -> {
			System.out.println(model.getName());
		});
		assertEquals(3, mods.getSize());
	}

	@Test
	public void testGetModelsByTypeIn() throws Exception {
		List<String> modelTypes = new ArrayList<>();
		modelTypes.add("Electric");
		List<Model> mods = modelRepository.getModelsByType(modelTypes);
		assertEquals(4, mods.size());
		mods.forEach(model -> {
			assertEquals("Electric", model.getModelType().getName());
		});
	}

	@Test
	public void testGetModelsByType() throws Exception {
		List<Model> mods = modelJpaRepository.findAllModelsByType("Electric");
		assertEquals(4, mods.size());
		mods.forEach(model -> {
			assertEquals("Electric", model.getModelType().getName());
		});
	}

	@Test
	public void testGetModelsCount() throws Exception {
		long modelCount = modelRepository.getModelCount();
		assertEquals(9L, modelCount);
	}
}
