package com.guitar.db;

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.guitar.db.repository.LocationJpaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.guitar.db.model.Location;

@ContextConfiguration(locations={"classpath:com/guitar/db/applicationTests-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class LocationPersistenceTests {
	@Autowired
	private LocationJpaRepository locationJpaRepository;

	@Test
	@Transactional
	public void testSaveAndGetAndDelete() throws Exception {
		Location location = new Location();
		location.setCountry("Canada");
		location.setState("British Columbia");
		location = locationJpaRepository.save(location);

		Location otherLocation = locationJpaRepository.getOne(location.getId());
		assertEquals("Canada", otherLocation.getCountry());
		assertEquals("British Columbia", otherLocation.getState());
		
		//delete BC location now
		locationJpaRepository.delete(otherLocation);
	}

	@Test
	public void testFindWithLike() throws Exception {
		List<Location> locs = locationJpaRepository.findByStateStartingWith("New");
		assertEquals(4, locs.size());
	}

	@Test
	public void testFindWithLikeAndOrderBy() throws Exception {
		List<Location> locs = locationJpaRepository.findByStateStartingWithOrderByStateDesc("New");
		assertEquals("New York", locs.get(0).getState());
	}

	@Test
	public void testFindWithFirstLikeAndOrderBy() throws Exception {
		Location location = locationJpaRepository.findFirstByStateStartingWithOrderByStateAsc("New");
		assertEquals("New Hampshire", location.getState());
	}

	@Test
	public void testFindStateAndCountryWithLike() throws Exception {
		List<Location> locs = locationJpaRepository.findByCountryLikeAndStateLike("%States", "New%");
		assertEquals(4, locs.size());
	}

	@Test
	public void testFindStateAndCountryWithEquals() throws Exception {
		List<Location> locs = locationJpaRepository.queryByStateNot("New York");
		assertEquals(49, locs.size());
	}

	@Test
	public void testCountStateWithLike() throws Exception {
		long stateCount = locationJpaRepository.countByStateLike("New%");
		assertEquals(4, stateCount);
	}

	@Test
	public void testCountStateIgnoreCaseWithLike() throws Exception {
		long stateCount = locationJpaRepository.countByStateIgnoreCaseLike("new%");
		assertEquals(4, stateCount);
	}

	@Test
	@Transactional  //note this is needed because we will get a lazy load exception unless we are in a tx
	public void testFindWithChildren() throws Exception {
		Location arizona = locationJpaRepository.getOne(3L);
		assertEquals("United States", arizona.getCountry());
		assertEquals("Arizona", arizona.getState());
		
		assertEquals(1, arizona.getManufacturers().size());
		
		assertEquals("Fender Musical Instruments Corporation", arizona.getManufacturers().get(0).getName());
	}
}
