package com.example.ec.service;

import com.example.ec.domain.TourRating;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TourRatingServiceIntegrationTest {
    private static final int CUSTOMER_ID = 456;
    private static final int TOUR_ID = 1;
    private static final int NOT_A_TOUR_ID = 123;

    @Autowired
    private TourRatingService service;

    @Test
    public void delete() {
        List<TourRating> tourRatings = service.lookupAll();
        service.delete(tourRatings.get(0).getTour().getId(), tourRatings.get(0).getCustomerId());
        assertThat(service.lookupAll().size(), is(tourRatings.size() - 1));
    }

    @Test(expected = NoSuchElementException.class)
    public void deleteException() {
        service.delete(NOT_A_TOUR_ID, 1234);
    }

    @Test
    public void createNew() {
        service.createNew(TOUR_ID, CUSTOMER_ID, 2, "it was fair");
        TourRating newTourRating = service.verifyTourRating(TOUR_ID, CUSTOMER_ID);
        assertThat(newTourRating.getTour().getId(), is(TOUR_ID));
        assertThat(newTourRating.getCustomerId(), is(CUSTOMER_ID));
        assertThat(newTourRating.getScore(), is(2));
        assertThat(newTourRating.getComment(), is ("it was fair"));
    }

    @Test(expected = NoSuchElementException.class)
    public void createNewException() {
        service.createNew(NOT_A_TOUR_ID, CUSTOMER_ID, 2, "it was fair");
    }

    @Test
    public void rateMany() {
        int ratings = service.lookupAll().size();
        service.rateMany(TOUR_ID, 5, new int[]{100, 101, 102});
        assertThat(service.lookupAll().size(), is(ratings + 3));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void rateManyProveRollback() {
        int ratings = service.lookupAll().size();
        int customers[] = {100, 101, 102};
        service.rateMany(TOUR_ID, 3, customers);
        service.rateMany(TOUR_ID, 3, customers);
    }

    @Test
    public void update() {
        createNew();
        TourRating tourRating = service.update(TOUR_ID, CUSTOMER_ID, 1, "one");
        assertThat(tourRating.getTour().getId(), is(TOUR_ID));
        assertThat(tourRating.getCustomerId(), is(CUSTOMER_ID));
        assertThat(tourRating.getScore(), is(1));
        assertThat(tourRating.getComment(), is("one"));
    }

    @Test(expected = NoSuchElementException.class)
    public void updateException() throws Exception {
        service.update(1, 1, 1, "one");
    }

    @Test
    public void updateSome() {
        createNew();
        TourRating tourRating = service.update(TOUR_ID, CUSTOMER_ID, 1, "one");
        assertThat(tourRating.getTour().getId(), is(TOUR_ID));
        assertThat(tourRating.getCustomerId(), is(CUSTOMER_ID));
        assertThat(tourRating.getScore(), is(1));
        assertThat(tourRating.getComment(), is("one"));
    }

    @Test(expected = NoSuchElementException.class)
    public void updateSomeException() throws Exception {
        service.update(1, 1, 1, "one");
    }

    @Test
    public void getAverageScore() {
        assertTrue(service.getAverageScore(TOUR_ID) == 4.0);
    }

    @Test(expected = NoSuchElementException.class)
    public void getAverageScoreException() {
        service.getAverageScore(NOT_A_TOUR_ID); //That tour does not exist
    }
}