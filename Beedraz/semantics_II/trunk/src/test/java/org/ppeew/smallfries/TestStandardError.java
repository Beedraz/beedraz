/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppeew.smallfries;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.ppeew.smallfries_I.MathUtil.arithmeticMean;
import static org.ppeew.smallfries_I.MathUtil.equalPrimitiveValue;

import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppeew.smallfries_I.StandardError;



public class TestStandardError {

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private StandardError $sampleStandardError = new StandardError(true);
  private StandardError $populationStandardError = new StandardError(false);

  @Test
  public void constructor1() {
    StandardError standardError = new StandardError();
    assertTrue(standardError.isBiasCorrected());
    assertEquals(0L, standardError.getN());
    assertEquals(Double.NaN, standardError.getResult());
    assertEquals(Double.NaN, standardError.getResult());
  }

  @Test
  public void constructor2() {
    StandardError standardError1 = new StandardError(false);
    assertFalse(standardError1.isBiasCorrected());
    assertEquals(0L, standardError1.getN());
    assertEquals(Double.NaN, standardError1.getResult());
    StandardError standardError2 = new StandardError(true);
    assertTrue(standardError2.isBiasCorrected());
    assertEquals(0L, standardError2.getN());
    assertEquals(Double.NaN, standardError2.getResult());
  }

  @Test
  public void getResult() {
    double[] values;
    // []
    values = new double[0];
    assertEquals(0L, $sampleStandardError.getN());
    assertEquals(0L, $populationStandardError.getN());
    assertTrue(equalPrimitiveValue(sampleStandardErrorByHand(values), $sampleStandardError.getResult()));
    assertTrue(equalPrimitiveValue(populationStandardErrorByHand(values), $populationStandardError.getResult()));
    // [1.1]
    values = new double[] {1.1};
    $sampleStandardError.incrementAll(values);
    $populationStandardError.incrementAll(values);
    assertEquals(1L, $sampleStandardError.getN());
    assertEquals(1L, $populationStandardError.getN());
    assertTrue(equalPrimitiveValue(sampleStandardErrorByHand(values), $sampleStandardError.getResult()));
    assertTrue(equalPrimitiveValue(populationStandardErrorByHand(values), $populationStandardError.getResult()));
    // [1.1, 2.2]
    values = new double[] {1.1, 2.2};
    $sampleStandardError.clear();
    $populationStandardError.clear();
    $sampleStandardError.incrementAll(values);
    $populationStandardError.incrementAll(values);
    assertEquals(2L, $sampleStandardError.getN());
    assertEquals(2L, $populationStandardError.getN());
    assertTrue(equalPrimitiveValue(sampleStandardErrorByHand(values), $sampleStandardError.getResult()));
    assertTrue(equalPrimitiveValue(populationStandardErrorByHand(values), $populationStandardError.getResult()));
    // [1.1, 2.2, 3.3]
    values = new double[] {1.1, 2.2, 3.3};
    $sampleStandardError.clear();
    $populationStandardError.clear();
    $sampleStandardError.incrementAll(values);
    $populationStandardError.incrementAll(values);
    assertEquals(3L, $sampleStandardError.getN());
    assertEquals(3L, $populationStandardError.getN());
    assertTrue(equalPrimitiveValue(sampleStandardErrorByHand(values), $sampleStandardError.getResult()));
    assertTrue(equalPrimitiveValue(populationStandardErrorByHand(values), $populationStandardError.getResult()));
    // [1.1, 2.2, 3.3, -5.0]
    values = new double[] {1.1, 2.2, 3.3, -5.0};
    $sampleStandardError.clear();
    $populationStandardError.clear();
    $sampleStandardError.incrementAll(values);
    $populationStandardError.incrementAll(values);
    assertEquals(4L, $sampleStandardError.getN());
    assertEquals(4L, $populationStandardError.getN());
    assertTrue(equalPrimitiveValue(sampleStandardErrorByHand(values), $sampleStandardError.getResult()));
    assertTrue(equalPrimitiveValue(populationStandardErrorByHand(values), $populationStandardError.getResult()));
    // [1.1, 2.2, 3.3, 0.0]
    values = new double[] {1.1, 2.2, 3.3, 0.0};
    $sampleStandardError.clear();
    $populationStandardError.clear();
    $sampleStandardError.incrementAll(values);
    $populationStandardError.incrementAll(values);
    assertEquals(4L, $sampleStandardError.getN());
    assertEquals(4L, $populationStandardError.getN());
    assertTrue(equalPrimitiveValue(sampleStandardErrorByHand(values), $sampleStandardError.getResult()));
    assertTrue(equalPrimitiveValue(populationStandardErrorByHand(values), $populationStandardError.getResult()));
    // [1.1, 2.2, 3.3, Double.POSITIVE_INFINITY]
    values = new double[] {1.1, 2.2, 3.3, Double.POSITIVE_INFINITY};
    $sampleStandardError.clear();
    $populationStandardError.clear();
    $sampleStandardError.incrementAll(values);
    $populationStandardError.incrementAll(values);
    assertEquals(4L, $sampleStandardError.getN());
    assertEquals(4L, $populationStandardError.getN());
    assertTrue(equalPrimitiveValue(sampleStandardErrorByHand(values), $sampleStandardError.getResult()));
    assertTrue(equalPrimitiveValue(populationStandardErrorByHand(values), $populationStandardError.getResult()));
    // [1.1, 2.2, 3.3, Double.NEGATIVE_INFINITY]
    values = new double[] {1.1, 2.2, 3.3, Double.NEGATIVE_INFINITY};
    $sampleStandardError.clear();
    $populationStandardError.clear();
    $sampleStandardError.incrementAll(values);
    $populationStandardError.incrementAll(values);
    assertEquals(4L, $sampleStandardError.getN());
    assertEquals(4L, $populationStandardError.getN());
    assertTrue(equalPrimitiveValue(sampleStandardErrorByHand(values), $sampleStandardError.getResult()));
    assertTrue(equalPrimitiveValue(populationStandardErrorByHand(values), $populationStandardError.getResult()));
  }

  /**
   * @pre   doubles != null;
   */
  private double sampleStandardErrorByHand(double... doubles) {
    assert doubles != null;
    if (doubles.length == 0) {
      return Double.NaN;
    }
    else if (doubles.length == 1) {
      return 0.0;
    }
    else if (Util.containsInfinity(doubles)) {
      return Double.POSITIVE_INFINITY;
    }
    else {
      double sum = 0.0;
      double mean = arithmeticMean(doubles);
      for (int i = 0; i < doubles.length; i++) {
        sum += Math.pow(doubles[i]-mean, 2);
      }
      double error = Math.sqrt(sum / ((doubles.length - 1)*doubles.length));
      return error;
    }
  }

  /**
   * @pre   doubles != null;
   */
  private double populationStandardErrorByHand(double... doubles) {
    assert doubles != null;
    if (doubles.length == 0) {
      return Double.NaN;
    }
    else if (doubles.length == 1) {
      return 0.0;
    }
    else if (Util.containsInfinity(doubles)) {
      return Double.POSITIVE_INFINITY;
    }
    else {
      double sum = 0.0;
      double mean = arithmeticMean(doubles);
      for (int i = 0; i < doubles.length; i++) {
        sum += Math.pow(doubles[i]-mean, 2);
      }
      double error = Math.sqrt(sum / ((doubles.length)*doubles.length));
      return error;
    }
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
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values2, 0, 0)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values2, mean, 0, 0)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values2, 0, 0)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values2, mean, 0, 0)));
    // [1.1]
    values = new double[] {1.1};
    mean = m.evaluate(values);
    values2 = new double[] {6.6, 7.7, 1.1, 8.8, 9.9};
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values2, 2, 1)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values2, mean, 2, 1)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values2, 2, 1)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values2, mean, 2, 1)));
    // [1.1, 2.2]
    values = new double[] {1.1, 2.2};
    mean = m.evaluate(values);
    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 8.8, 9.9};
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values2, 2, 2)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values2, mean, 2, 2)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values2, 2, 2)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values2, mean, 2, 2)));
    // [1.1, 2.2, 3.3]
    values = new double[] {1.1, 2.2, 3.3};
    mean = m.evaluate(values);
    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 3.3, 8.8, 9.9};
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values2, 2, 3)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values2, mean, 2, 3)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values2, 2, 3)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values2, mean, 2, 3)));
    // [1.1, 2.2, 3.3, -5.0]
    values = new double[] {1.1, 2.2, 3.3, -5.0};
    mean = m.evaluate(values);
    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 8.8, 9.9};
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values2, 2, 4)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values2, mean, 2, 4)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values2, 2, 4)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values2, mean, 2, 4)));
    // [1.1, 2.2, 3.3, 0.0]
    values = new double[] {1.1, 2.2, 3.3, 0.0};
    mean = m.evaluate(values);
    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 8.8, 9.9};
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values2, 2, 4)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values2, mean, 2, 4)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values2, 2, 4)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values2, mean, 2, 4)));
    // [1.1, 2.2, 3.3, Double.POSITIVE_INFINITY]
    values = new double[] {1.1, 2.2, 3.3, Double.POSITIVE_INFINITY};
    mean = m.evaluate(values);
    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 8.8, 9.9};
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values2, 2, 4)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values2, mean, 2, 4)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values2, 2, 4)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values2, mean, 2, 4)));
    // [1.1, 2.2, 3.3, Double.NEGATIVE_INFINITY]
    values = new double[] {1.1, 2.2, 3.3, Double.NEGATIVE_INFINITY};
    mean = m.evaluate(values);
    values2 = new double[] {6.6, 7.7, 1.1, 2.2, 8.8, 9.9};
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values2, 2, 4)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($sampleStandardError.getResult(), $sampleStandardError.evaluate(values2, mean, 2, 4)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values, 0, values.length)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values2, 2, 4)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values, mean)));
    assertTrue(equalPrimitiveValue($populationStandardError.getResult(), $populationStandardError.evaluate(values2, mean, 2, 4)));
  }

}

