/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppeew.smallfries;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.ppeew.smallfries_I.MathUtil.equalPrimitiveValue;
import static org.ppeew.smallfries_I.MathUtil.geometricMean;

import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppeew.smallfries_I.GeometricStandardError;



public class TestGeometricStandardError {

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private GeometricStandardError $sampleGeometricStandardError = new GeometricStandardError(true);
  private GeometricStandardError $populationGeometricStandardError = new GeometricStandardError(false);

  @Test
  public void constructor1() {
    GeometricStandardError standardError = new GeometricStandardError();
    assertTrue(standardError.isBiasCorrected());
    assertEquals(0L, standardError.getN());
    assertEquals(Double.NaN, standardError.getResult());
  }

  @Test
  public void constructor2() {
    GeometricStandardError standardError1 = new GeometricStandardError(false);
    assertFalse(standardError1.isBiasCorrected());
    assertEquals(0L, standardError1.getN());
    assertEquals(Double.NaN, standardError1.getResult());
    GeometricStandardError standardError2 = new GeometricStandardError(true);
    assertTrue(standardError2.isBiasCorrected());
    assertEquals(0L, standardError2.getN());
    assertEquals(Double.NaN, standardError2.getResult());
  }

  @Test
  public void getResult() {
    double[] values;
    // []
    values = new double[0];
    assertEquals(0L, $sampleGeometricStandardError.getN());
    assertEquals(0L, $populationGeometricStandardError.getN());
    assertTrue(equalPrimitiveValue(sampleGeometricStandardErrorByHand(values), $sampleGeometricStandardError.getResult()));
    assertTrue(equalPrimitiveValue(populationGeometricStandardErrorByHand(values), $populationGeometricStandardError.getResult()));
    // [1.1]
    values = new double[] {1.1};
    $sampleGeometricStandardError.incrementAll(values);
    $populationGeometricStandardError.incrementAll(values);
    assertEquals(1L, $sampleGeometricStandardError.getN());
    assertEquals(1L, $populationGeometricStandardError.getN());
    assertTrue(equalPrimitiveValue(sampleGeometricStandardErrorByHand(values), $sampleGeometricStandardError.getResult()));
    assertTrue(equalPrimitiveValue(populationGeometricStandardErrorByHand(values), $populationGeometricStandardError.getResult()));
    // [1.1, 2.2]
    values = new double[] {1.1, 2.2};
    $sampleGeometricStandardError.clear();
    $populationGeometricStandardError.clear();
    $sampleGeometricStandardError.incrementAll(values);
    $populationGeometricStandardError.incrementAll(values);
    assertEquals(2L, $sampleGeometricStandardError.getN());
    assertEquals(2L, $populationGeometricStandardError.getN());
    assertTrue(equalPrimitiveValue(sampleGeometricStandardErrorByHand(values), $sampleGeometricStandardError.getResult()));
    assertTrue(equalPrimitiveValue(populationGeometricStandardErrorByHand(values), $populationGeometricStandardError.getResult()));
    // [1.1, 2.2, 3.3]
    values = new double[] {1.1, 2.2, 3.3};
    $sampleGeometricStandardError.clear();
    $populationGeometricStandardError.clear();
    $sampleGeometricStandardError.incrementAll(values);
    $populationGeometricStandardError.incrementAll(values);
    assertEquals(3L, $sampleGeometricStandardError.getN());
    assertEquals(3L, $populationGeometricStandardError.getN());
    assertTrue(equalPrimitiveValue(sampleGeometricStandardErrorByHand(values), $sampleGeometricStandardError.getResult()));
    assertTrue(equalPrimitiveValue(populationGeometricStandardErrorByHand(values), $populationGeometricStandardError.getResult()));
    // [1.1, 2.2, 3.3, -5.0]
    values = new double[] {1.1, 2.2, 3.3, -5.0};
    $sampleGeometricStandardError.clear();
    $populationGeometricStandardError.clear();
    $sampleGeometricStandardError.incrementAll(values);
    $populationGeometricStandardError.incrementAll(values);
    assertEquals(4L, $sampleGeometricStandardError.getN());
    assertEquals(4L, $populationGeometricStandardError.getN());
    assertTrue(equalPrimitiveValue(sampleGeometricStandardErrorByHand(values), $sampleGeometricStandardError.getResult()));
    assertTrue(equalPrimitiveValue(populationGeometricStandardErrorByHand(values), $populationGeometricStandardError.getResult()));
    // [1.1, 2.2, 3.3, 0.0]
    values = new double[] {1.1, 2.2, 3.3, 0.0};
    $sampleGeometricStandardError.clear();
    $populationGeometricStandardError.clear();
    $sampleGeometricStandardError.incrementAll(values);
    $populationGeometricStandardError.incrementAll(values);
    assertEquals(4L, $sampleGeometricStandardError.getN());
    assertEquals(4L, $populationGeometricStandardError.getN());
    assertTrue(equalPrimitiveValue(sampleGeometricStandardErrorByHand(values), $sampleGeometricStandardError.getResult()));
    assertTrue(equalPrimitiveValue(populationGeometricStandardErrorByHand(values), $populationGeometricStandardError.getResult()));
    // [1.1, 2.2, 3.3, Double.POSITIVE_INFINITY]
    values = new double[] {1.1, 2.2, 3.3, Double.POSITIVE_INFINITY};
    $sampleGeometricStandardError.clear();
    $populationGeometricStandardError.clear();
    $sampleGeometricStandardError.incrementAll(values);
    $populationGeometricStandardError.incrementAll(values);
    assertEquals(4L, $sampleGeometricStandardError.getN());
    assertEquals(4L, $populationGeometricStandardError.getN());
    assertTrue(equalPrimitiveValue(sampleGeometricStandardErrorByHand(values), $sampleGeometricStandardError.getResult()));
    assertTrue(equalPrimitiveValue(populationGeometricStandardErrorByHand(values), $populationGeometricStandardError.getResult()));
    // [1.1, 2.2, 3.3, Double.NEGATIVE_INFINITY]
    values = new double[] {1.1, 2.2, 3.3, Double.NEGATIVE_INFINITY};
    $sampleGeometricStandardError.clear();
    $populationGeometricStandardError.clear();
    $sampleGeometricStandardError.incrementAll(values);
    $populationGeometricStandardError.incrementAll(values);
    assertEquals(4L, $sampleGeometricStandardError.getN());
    assertEquals(4L, $populationGeometricStandardError.getN());
    assertTrue(equalPrimitiveValue(sampleGeometricStandardErrorByHand(values), $sampleGeometricStandardError.getResult()));
    assertTrue(equalPrimitiveValue(populationGeometricStandardErrorByHand(values), $populationGeometricStandardError.getResult()));
  }

  /**
   * @pre   doubles != null;
   */
  private double geometricStandardErrorByHand(boolean sample, double... doubles) {
    assert doubles != null;
    if (doubles.length == 0) {
      return Double.NaN;
    }
    else if (doubles.length == 1) {
      return Double.POSITIVE_INFINITY;
    }
    else if (Util.containsNegative(doubles)) {
      return Double.NaN;
    }
    else if (Util.containsZero(doubles) || Util.containsInfinity(doubles)) {
      return Double.POSITIVE_INFINITY;
    }
    else {
      double sum = 0.0;
      double geometricMean = geometricMean(doubles);
      for (int i = 0; i < doubles.length; i++) {
        sum += Math.pow(Math.log(doubles[i])-Math.log(geometricMean), 2);
      }
      double x = sample ? 1 : 0;
      double error = Math.exp(Math.sqrt(sum / ((doubles.length - x)*doubles.length)));
      return error;
    }
  }

  /**
   * @pre   doubles != null;
   */
  private double sampleGeometricStandardErrorByHand(double... doubles) {
    return geometricStandardErrorByHand(true, doubles);
  }

  /**
   * @pre   doubles != null;
   */
  private double populationGeometricStandardErrorByHand(double... doubles) {
    return geometricStandardErrorByHand(false, doubles);
  }

  public void evaluate() {
    double[] values;
    double[] values2;
    double mean;
    Mean m = new Mean();
    // []
    values = new double[0];
    mean = m.evaluate(values);
    values2 = new double[] {5.5, 6.6, 7.7, 8.8};
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values2, 0, 0)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values2, mean, 0, 0)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values2, 0, 0)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values2, mean, 0, 0)));
    // [1.1]
    values = new double[] {1.1};
    mean = m.evaluate(values);
    values2 = new double[] {6.6, 7.7, 1.1, 8.8, 9.9};
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values2, 2, 1)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values2, mean, 2, 1)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values2, 2, 1)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values2, mean, 2, 1)));
    // [1.1, 2.2]
    values = new double[] {1.1, 2.2};
    mean = m.evaluate(values);
    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 8.8, 9.9};
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values2, 2, 2)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values2, mean, 2, 2)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values2, 2, 2)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values2, mean, 2, 2)));
    // [1.1, 2.2, 3.3]
    values = new double[] {1.1, 2.2, 3.3};
    mean = m.evaluate(values);
    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 3.3, 8.8, 9.9};
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values2, 2, 3)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values2, mean, 2, 3)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values2, 2, 3)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values2, mean, 2, 3)));
    // [1.1, 2.2, 3.3, -5.0]
    values = new double[] {1.1, 2.2, 3.3, -5.0};
    mean = m.evaluate(values);
    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 8.8, 9.9};
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values2, 2, 4)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values2, mean, 2, 4)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values2, 2, 4)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values2, mean, 2, 4)));
    // [1.1, 2.2, 3.3, 0.0]
    values = new double[] {1.1, 2.2, 3.3, 0.0};
    mean = m.evaluate(values);
    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 8.8, 9.9};
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values2, 2, 4)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values2, mean, 2, 4)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values2, 2, 4)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values2, mean, 2, 4)));
    // [1.1, 2.2, 3.3, Double.POSITIVE_INFINITY]
    values = new double[] {1.1, 2.2, 3.3, Double.POSITIVE_INFINITY};
    mean = m.evaluate(values);
    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 8.8, 9.9};
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values2, 2, 4)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values2, mean, 2, 4)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values2, 2, 4)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values2, mean, 2, 4)));
    // [1.1, 2.2, 3.3, Double.NEGATIVE_INFINITY]
    values = new double[] {1.1, 2.2, 3.3, Double.NEGATIVE_INFINITY};
    mean = m.evaluate(values);
    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 8.8, 9.9};
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values2, 2, 4)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($sampleGeometricStandardError.getResult(), $sampleGeometricStandardError.evaluate(values2, mean, 2, 4)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values2, 2, 4)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($populationGeometricStandardError.getResult(), $populationGeometricStandardError.evaluate(values2, mean, 2, 4)));
  }

}

