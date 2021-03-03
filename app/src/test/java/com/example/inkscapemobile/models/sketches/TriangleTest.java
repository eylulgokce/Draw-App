package com.example.inkscapemobile.models.sketches;

import com.example.inkscapemobile.models.GeometricCalculations;
import com.example.inkscapemobile.models.attributes.Attribute;
import com.example.inkscapemobile.models.attributes.AttributeFactory;
import com.example.inkscapemobile.models.attributes.AttributeType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TriangleTest {
    private Triangle triangle;

    /**
     * Create a equally sided triangle with
     * top point [100,100], left point [50,100 * sqrt(3)/2] , right point [150, 100 * sqrt(3)/2]
     */
    @BeforeEach
    void setUp() {
        EnumMap<AttributeType, Attribute<?>> attributes = AttributeFactory.createDefaultToolbarAttributes();
        attributes.replace(AttributeType.width, Attribute.createWidthAttribute(100));
        triangle = new Triangle(attributes, new float[]{100, 100});
        System.out.println("triangle: top: " + Arrays.toString(triangle.getTopPoint())
                + " left: " + Arrays.toString(triangle.getLeftPoint())
                + " right: " + Arrays.toString(triangle.getRightPoint()));
    }

    @Test
    void triangleIsEquilateral() {
        float distanceAB = GeometricCalculations.distanceBetweenTwoPoints(triangle.getLeftPoint(), triangle.getRightPoint());
        float distanceCA = GeometricCalculations.distanceBetweenTwoPoints(triangle.getTopPoint(), triangle.getLeftPoint());
        float distanceCB = GeometricCalculations.distanceBetweenTwoPoints(triangle.getTopPoint(), triangle.getRightPoint());
        assertEquals(distanceAB, distanceCA);
        assertEquals(distanceAB, distanceCB);
        assertEquals(distanceCA, distanceCB);
        System.out.println("distance: "+distanceCA);
    }

    @Test
    void vivianiImplementationCorrect() {
        float[] pointP = {100, 100+triangle.getHeight()/2};
        float distanceACP = GeometricCalculations.distanceLineToPoint(triangle.getLeftPoint(), triangle.getTopPoint(), pointP);
        float distanceABP = GeometricCalculations.distanceLineToPoint(triangle.getLeftPoint(), triangle.getRightPoint(), pointP);
        float distanceBCP = GeometricCalculations.distanceLineToPoint(triangle.getRightPoint(), triangle.getTopPoint(), pointP);
        float vivianiTheorem = Math.abs(distanceACP + distanceABP + distanceBCP - triangle.getHeight());
        assertEquals(0, vivianiTheorem);
    }

    @Test
    void positionInsideTriangle_isSelected() {
        assertTrue(triangle.isSelected(triangle.getTopPoint()[0], triangle.getTopPoint()[1])); //top
        assertTrue(triangle.isSelected(triangle.getLeftPoint()[0], triangle.getLeftPoint()[1])); //left
        assertTrue(triangle.isSelected(triangle.getRightPoint()[0], triangle.getRightPoint()[1])); //right
        assertTrue(triangle.isSelected(triangle.getTopPoint()[0], triangle.getTopPoint()[1]+triangle.getHeight()/2)); //center
    }

    @Test
    void positionOutsideTriangle_notSelected() {
        assertFalse(triangle.isSelected(triangle.getTopPoint()[0], triangle.getTopPoint()[1]-5)); //top
        assertFalse(triangle.isSelected(triangle.getLeftPoint()[0]-5, triangle.getLeftPoint()[1])); //left
        assertFalse(triangle.isSelected(triangle.getRightPoint()[0], triangle.getRightPoint()[1]-5)); //right
        assertFalse(triangle.isSelected(triangle.getTopPoint()[0]+100, triangle.getTopPoint()[1]+triangle.getHeight()/2)); //center
    }
}