package com.example.ec.web;

import com.example.ec.domain.Tour;
import com.example.ec.domain.TourRating;
import com.example.ec.service.TourRatingService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class TourRatingControllerTest {

    private static final int TOUR_ID = 999;
    private static final int CUSTOMER_ID = 1000;
    private static final int SCORE = 3;
    private static final String COMMENT = "comment";
    private static final String TOUR_RATINGS_URL = "/tours/" + TOUR_ID + "/ratings";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtRequestHelper jwtRequestHelper;

    @MockBean
    private TourRatingService serviceMock;

    @Mock
    private TourRating tourRatingMock;

    @Mock
    private Tour tourMock;

    private RatingDto ratingDto = new RatingDto(SCORE, COMMENT,CUSTOMER_ID);

    @Before
    public void setupReturnValuesOfMockMethods() {
        when(tourRatingMock.getComment()).thenReturn(COMMENT);
        when(tourRatingMock.getScore()).thenReturn(SCORE);
        when(tourRatingMock.getCustomerId()).thenReturn(CUSTOMER_ID);
        when(tourRatingMock.getTour()).thenReturn(tourMock);
        when(tourMock.getId()).thenReturn(TOUR_ID);
    }

    @Test
    public void createTourRating() throws Exception {
        restTemplate.exchange(TOUR_RATINGS_URL, POST, new HttpEntity(ratingDto, jwtRequestHelper.withRole("ROLE_CSR")), Void.class);
        verify(this.serviceMock).createNew(TOUR_ID, CUSTOMER_ID, SCORE, COMMENT);
    }

    @Test
    public void delete() throws Exception {
        restTemplate.exchange(TOUR_RATINGS_URL + "/" + CUSTOMER_ID, HttpMethod.DELETE, new HttpEntity(jwtRequestHelper.withRole("ROLE_CSR")), Void.class);
        verify(serviceMock).delete(TOUR_ID, CUSTOMER_ID);
    }

    @Test
    public void createManyTourRatings() throws Exception {        restTemplate.exchange(TOUR_RATINGS_URL + "/" + SCORE + "?customers=" + CUSTOMER_ID, POST,
            new HttpEntity(ratingDto, jwtRequestHelper.withRole("ROLE_CSR")), Void.class);

        verify(serviceMock).rateMany(TOUR_ID, SCORE, new Integer[] {CUSTOMER_ID});
    }

    @Test
    public void getAllRatingsForTour() throws Exception {
        when(serviceMock.lookupRatings(anyInt(),any(Pageable.class))).thenReturn(new PageImpl(Arrays.asList(tourRatingMock), PageRequest.of(0,10),1));
        ResponseEntity<String> response = restTemplate.getForEntity(TOUR_RATINGS_URL,String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        verify(serviceMock).lookupRatings(anyInt(), any(Pageable.class));
    }

    @Test
    public void getAverage() throws Exception {
        when(serviceMock.getAverageScore(TOUR_ID)).thenReturn(3.2);
        ResponseEntity<String> response = restTemplate.getForEntity(TOUR_RATINGS_URL + "/average", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is("{\"average\":3.2}"));
    }

    @Test
    public void getAverage_TourNotFound() {
        when(serviceMock.getAverageScore(TOUR_ID)).thenThrow(NoSuchElementException.class);
        ResponseEntity<String> response = restTemplate.getForEntity(TOUR_RATINGS_URL + "/average", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void updateWithPut() throws Exception {
        when(serviceMock.update(TOUR_ID, CUSTOMER_ID, SCORE, COMMENT)).thenReturn(tourRatingMock);
        restTemplate.exchange(TOUR_RATINGS_URL, PUT, new HttpEntity(ratingDto, jwtRequestHelper.withRole("ROLE_CSR")), Void.class);
        verify(serviceMock).update(TOUR_ID, CUSTOMER_ID, SCORE, COMMENT);
    }

    @Test
    @Ignore
    public  void updateWithPatch() {
        when(serviceMock.updateSome(TOUR_ID, CUSTOMER_ID, SCORE, COMMENT)).thenReturn(tourRatingMock);
        restTemplate.exchange(TOUR_RATINGS_URL, PATCH, new HttpEntity(ratingDto, jwtRequestHelper.withRole("ROLE_CSR")), Void.class);
        verify(serviceMock).updateSome(TOUR_ID, CUSTOMER_ID, SCORE, COMMENT);
    }
}
