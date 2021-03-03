package com.example.inkscapemobile.models;

public class GeometricCalculations {
    public static float distanceBetweenTwoPoints(float[] pointA, float[] pointB) {
        float[] vectorBetween = vectorBetweenTwoPoints(pointA, pointB);
        return (float) Math.sqrt(Math.pow(vectorBetween[0], 2) + Math.pow(vectorBetween[1], 2));
    }

    public static float[] vectorBetweenTwoPoints(float[] pointA, float[] pointB) {
        return new float[]{pointB[0] - pointA[0], pointB[1] - pointA[1]};
    }


    /**
     * point L is the "Lot punkt", the point between A and B, where vector LP is orthogonal to AB
     *
     * @param pointA start point of the line
     * @param pointB endpoint of the line
     * @param pointP distant point
     * @return distance from point P to line AB
     */
    public static float distanceLineToPoint(float[] pointA, float[] pointB, float[] pointP) {
        float distanceAB = distanceBetweenTwoPoints(pointA, pointB);
        float[] vectorAB = vectorBetweenTwoPoints(pointA, pointB);
        float distanceAP = distanceBetweenTwoPoints(pointA, pointP);
        float distanceBP = distanceBetweenTwoPoints(pointB, pointP);
        float constant1 = (float) (Math.pow(distanceAP, 2) - Math.pow(distanceBP, 2) + Math.pow(distanceAB, 2));

        float distanceAL = constant1 / (2 * distanceAB);

        //L = A + (unit vector AB) * distanceAL
        //L = A + (einheitsvektor AB) * distanceAL
        float[] pointL = {pointA[0] + (vectorAB[0] / distanceAB) * distanceAL, pointA[1] + (vectorAB[1] / distanceAB) * distanceAL};

        return distanceBetweenTwoPoints(pointL, pointP);
    }
}
