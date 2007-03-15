/*<license>
Copyright 2007 - $Date$ by the authors mentioned below.

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

import org.ppeew.annotations_I.vcs.CvsInfo;


@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class MathUtil {

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
        return delta <= ulp(dv);
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
        return delta <= ulp(fv);
      }
    }
  }

  public static boolean equalValue(Long l, Number n) {
    return (l == null) ?
             (n == null) :
             (n != null) && (l.longValue() == n.longValue());
  }

  /**
   * @result equalValue(result, floatObject);
   */
  public static Double castToDouble(Float floatObject) {
    if (floatObject == null) {
      return null;
    }
    else {
      return Double.valueOf(floatObject.doubleValue());
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
      return Double.valueOf(longObject.doubleValue());
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
      return Double.valueOf(integerObject.doubleValue());
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
      return Float.valueOf(longObject.floatValue());
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
      return Float.valueOf(integerObject.floatValue());
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
      return Long.valueOf(integerObject.longValue());
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
