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

package org.ppeew.smallfries;


import org.ppeew.annotations.vcs.CvsInfo;


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
  public static boolean equalValue(Double d, Number n) {
    if (d == null) {
      return n == null;
    }
    else if (n == null) {
      return false;
    }
    else {
//      assert d != null;
//      assert f != null;
      if (d.isNaN()) {
        return isNaN(n);
      }
      else if (isNaN(n)) {
        return false;
      }
      else {
        return d.doubleValue() == n.doubleValue();
      }
    }
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
   * NaN's are equal.
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
        return f.floatValue() == n.floatValue();
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

}

