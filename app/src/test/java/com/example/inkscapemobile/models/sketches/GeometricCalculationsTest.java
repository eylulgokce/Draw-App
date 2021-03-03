package com.example.inkscapemobile.models.sketches;

import com.example.inkscapemobile.models.GeometricCalculations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GeometricCalculationsTest {

    @Test
    void vectorBetweenTwoPoints_returnsCorrectVector() {
        float[] pointA = {10, 10};
        float[] pointB = {20, 10};
        float[] vectorAB = GeometricCalculations.vectorBetweenTwoPoints(pointA, pointB);
        assertArrayEquals(new float[]{10, 0}, vectorAB);


        pointB[0] = 0;
        vectorAB = GeometricCalculations.vectorBetweenTwoPoints(pointA, pointB);
        assertArrayEquals(new float[]{-10, 0}, vectorAB);
    }

    @ParameterizedTest
    @CsvSource({"1,1,2,1,1", "10,10,10,5,5"})
    void distanceBetweenTwoPoints_returnsCorrectDistance(float xPointA, float yPointA,
                                                         float xPointB, float yPointB,
                                                         float distance) {
        float actual = GeometricCalculations.distanceBetweenTwoPoints(new float[]{xPointA, yPointA}
                , new float[]{xPointB, yPointB});
        assertEquals(distance, actual);
    }

    @Test
    void distancePointToLine_returnsCorrectResult() {
        float[] pointA = {10,10};
        float[] pointB = {10,20};
        float distance = GeometricCalculations.distanceLineToPoint(pointA, pointB, new float[] {15, 12});
        assertEquals(5, distance);

        distance = GeometricCalculations.distanceLineToPoint(pointA, pointB, new float[] {5, 12});
        assertEquals(5, distance);

        distance = GeometricCalculations.distanceLineToPoint(pointA, pointB, new float[] {0, 17});
        assertEquals(10, distance);
    }
}