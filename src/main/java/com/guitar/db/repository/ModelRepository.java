package com.guitar.db.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.guitar.db.model.Model;

@Repository
public class ModelRepository {
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ModelJpaRepository modelJpaRepository;

	/**
	 * Create
	 */
	public Model create(Model mod) {
		return modelJpaRepository.saveAndFlush(mod);
	}

	/**
	 * Update
	 */
	public Model update(Model mod) {
		return modelJpaRepository.saveAndFlush(mod);
	}

	/**
	 * Delete
	 */
	public void delete(Model mod) {
		modelJpaRepository.delete(mod);
	}

	/**
	 * Find
	 */
	public Model find(Long id) {
		return modelJpaRepository.findOne(id);
	}

	/**
	 * Custom finder
	 */
	public List<Model> getModelsInPriceRange(BigDecimal lowest, BigDecimal highest) {
		List<Model> mods = modelJpaRepository.findByPriceBetween(lowest, highest);
		return mods;
	}

	/**
	 * Custom finder
	 */
	public List<Model> getModelsByPriceRangeAndWoodType(BigDecimal lowest, BigDecimal highest, String wood) {
		List<Model> mods = modelJpaRepository.findByPriceBetweenAndWoodTypeContaining(lowest, highest, wood);
		return mods;
	}

	/**
	 * NamedQuery finder
	 */
	public List<Model> getModelsByType(List<String> modelTypes) {
        List<Model> mods = modelJpaRepository.findByModelTypeNameIn(modelTypes);
		return mods;
	}

	/**
	 * Count
	 */
	public long getModelCount() {
		return modelJpaRepository.count();
	}
}
