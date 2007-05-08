/*<license>
Copyright 2007 - $Date$ by PeopleWare n.v..

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</license>*/

package org.ppeew.smallfries_I;


import static java.lang.Math.signum;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * General math utility methods, missing from {@link Math}.
 *
 * @author Jan Dockx
 */
@Copyright("2007 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@CvsInfo(revision    = "$Revision$",
         date        = "$Date$",
         state       = "$State$",
         tag         = "$Name$")
public final class MathUtil {

  private MathUtil() {
    // NOP
  }

  /**
   * NaN's are equal.
   */
  public static boolean equalValue(Number n1, Number n2) {
    if (n1 instanceof Double) {
      return equalValue((Double)n1, n2);
    }
    else if (n1 instanceof Float) {
      return equalValue((Float)n1, n2);
    }
    else if (n1 instanceof Long) {
      return equalValue((Long)n1, n2);
    }
    else {
      if (n1 == null) {
        return n2 == null;
      }
      else if (n2 == null) {
        return false;
      }
      else {
        return n1.equals(n2);
      }
    }
  }

  /**
   * NaN's are equal. Serious doubles are compared, taking
   * into account the {@link Math#ulp(double)}.
   */
  public static boolean equalValue(Double d, Number n) {
    if (d == null) {
      return n == null;
    }
    else if (n == null) {
      return false;
    }
    else {
//      assert d != null;
//      assert n != null;
      if (d.isNaN()) {
        return isNaN(n);
      }
      else if (isNaN(n)) {
        return false;
      }
      else {
        double dv = d.doubleValue();
        double nv = n.doubleValue();
        double delta;
        if (Double.isInfinite(dv)) {
          if (Double.isInfinite(nv) && signum(dv) == signum(nv)) {
            delta = 0;
          }
          else {
            delta = Double.POSITIVE_INFINITY;
          }
        }
        else {
          delta = Math.abs(dv - nv);
        }
        double ulp2 = 2 * ulp(dv);
        return delta <= ulp2;
      }
    }
  }

  public static double ulp(double d) {
    return Double.isInfinite(d) ? 0 : Math.ulp(d);
  }

  public static double ulp(float d) {
    return Double.isInfinite(d) ? 0 : Math.ulp(d);
  }

  public static boolean isNaN(Number n) {
    if (n == null) {
      return false;
    }
    else if (n instanceof Double) {
      return ((Double)n).isNaN();
    }
    else if (n instanceof Float) {
      return ((Float)n).isNaN();
    }
    else {
      return false;
    }
  }

  /**
   * NaN's are equal. Serious floats are compared, taking
   * into account the {@link Math#ulp(float)}.
   */
  public static boolean equalValue(Float f, Number n) {
    if (f == null) {
      return n == null;
    }
    else if (n == null) {
      return false;
    }
    else {
//      assert f1 != null;
//      assert f2 != null;
      if (f.isNaN()) {
        return isNaN(n);
      }
      else if (isNaN(n)) {
        return false;
      }
      else {
        float fv = f.floatValue();
        float nv = n.floatValue();
        float delta;
        if (Float.isInfinite(fv)) {
          if (Float.isInfinite(nv) && signum(fv) == signum(nv)) {
            delta = 0;
          }
          else {
            delta = Float.POSITIVE_INFINITY;
          }
        }
        else {
          delta = Math.abs(fv - nv);
        }
        return delta <= 2 * ulp(fv);
      }
    }
  }

  public static boolean equalValue(BigInteger bi, Number n) {
    return (bi == null) ?
             (n == null) :
             (n != null) && (bi.compareTo(BigInteger.valueOf(n.longValue())) == 0);
  }

  public static boolean equalValue(Long l, Number n) {
    return (l == null) ?
             (n == null) :
             (n != null) && (l.longValue() == n.longValue());
  }

  /**
   * @result equalValue(result, floatObject);
   */
  public static BigDecimal castToBigDecimal(Double doubleObject) {
    if (doubleObject == null) {
      return null;
    }
    else {
      return BigDecimal.valueOf(doubleObject);
    }
  }

  /**
   * @result equalValue(result, floatObject);
   */
  public static BigDecimal castToBigDecimal(Float floatObject) {
    if (floatObject == null) {
      return null;
    }
    else {
      return BigDecimal.valueOf(floatObject);
    }
  }

  /**
   * @result equalValue(result, longObject);
   *
   * @mudo is this a range problem?
   */
  public static BigDecimal castToBigDecimal(BigInteger biObject) {
    if (biObject == null) {
      return null;
    }
    else {
      return new BigDecimal(biObject);
    }
  }

  /**
   * @result equalValue(result, longObject);
   *
   * @mudo is this a range problem?
   */
  public static BigDecimal castToBigDecimal(Long longObject) {
    if (longObject == null) {
      return null;
    }
    else {
      return BigDecimal.valueOf(longObject);
    }
  }

  /**
   * @result equalValue(result, integerObject);
   */
  public static BigDecimal castToBigDecimal(Integer integerObject) {
    if (integerObject == null) {
      return null;
    }
    else {
      return BigDecimal.valueOf(integerObject);
    }
  }

  /**
   * @result equalValue(result, floatObject);
   */
  public static Double castToDouble(Float floatObject) {
    if (floatObject == null) {
      return null;
    }
    else {
      return Double.valueOf(floatObject);
    }
  }

  /**
   * @result equalValue(result, longObject);
   *
   * @mudo is this a range problem?
   */
  public static Double castToDouble(Long longObject) {
    if (longObject == null) {
      return null;
    }
    else {
      return Double.valueOf(longObject);
    }
  }

  /**
   * @result equalValue(result, integerObject);
   */
  public static Double castToDouble(Integer integerObject) {
    if (integerObject == null) {
      return null;
    }
    else {
      return Double.valueOf(integerObject);
    }
  }

  /**
   * @result equalValue(result, longObject);
   *
   * @mudo is this a range problem?
   */
  public static Float castToFloat(Long longObject) {
    if (longObject == null) {
      return null;
    }
    else {
      return Float.valueOf(longObject);
    }
  }

  /**
   * @result equalValue(result, integerObject);
   *
   * @mudo is this a range problem?
   */
  public static Float castToFloat(Integer integerObject) {
    if (integerObject == null) {
      return null;
    }
    else {
      return Float.valueOf(integerObject);
    }
  }

  /**
   * @result equalValue(result, integerObject);
   */
  public static BigInteger castToBigInteger(Long longObject) {
    if (longObject == null) {
      return null;
    }
    else {
      return BigInteger.valueOf(longObject);
    }
  }

  /**
   * @result equalValue(result, integerObject);
   */
  public static BigInteger castToBigInteger(Integer integerObject) {
    if (integerObject == null) {
      return null;
    }
    else {
      return BigInteger.valueOf(integerObject);
    }
  }

  /**
   * @result equalValue(result, integerObject);
   */
  public static Long castToLong(Integer integerObject) {
    if (integerObject == null) {
      return null;
    }
    else {
      return Long.valueOf(integerObject);
    }
  }

  /**
   * @pre  doubles != null;
   * @pre  doubles.length > 0;
   */
  public static double arithmeticMean(double... doubles) {
    assert doubles != null;
    assert doubles.length > 0;
    double mean = 0.0;
    for (double value : doubles) {
      mean += value;
}
    return mean / doubles.length;
  }

  /**
   * @pre  doubles != null;
   * @pre  doubles.length > 0;
   */
  public static double geometricMean(double... doubles) {
    assert doubles != null;
    assert doubles.length > 0;
    double mean = 1.0;
    for (double value : doubles) {
      mean *= value;
    }
    return Math.pow(mean, 1.0 / doubles.length);
  }

  /**
   * @pre  doubles != null;
   * @pre  doubles.length > 1;
   */
  public static double standardError(double... doubles) {
    assert doubles != null;
    assert doubles.length > 1;
    double mean = arithmeticMean(doubles);
    double standardError = 0.0;
    for (double value : doubles) {
      standardError += Math.pow(value - mean, 2);
    }
    standardError = standardError / (doubles.length * (doubles.length - 1));
    return Math.sqrt(standardError);
  }
}
