/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppeew.smallfries;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.ppeew.smallfries_I.MathUtil.castToDouble;
import static org.ppeew.smallfries_I.MathUtil.castToFloat;
import static org.ppeew.smallfries_I.MathUtil.castToLong;
import static org.ppeew.smallfries_I.MathUtil.equalValue;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;



public class TestMathUtil {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    ds = new LinkedList<Double>();
    ds.add(null);
    ds.add(Double.NEGATIVE_INFINITY);
    ds.add(Double.MIN_VALUE);
    ds.add(Double.valueOf(-7));
    ds.add(Double.valueOf(-1));
    ds.add(Double.valueOf(0));
    ds.add(Double.valueOf(1));
    ds.add(Double.valueOf(1.5d));
    ds.add(Double.valueOf(13d/7d));
    ds.add(Math.E);
    ds.add(Math.PI);
    ds.add(Double.valueOf(7));
    ds.add(Double.MAX_VALUE);
    ds.add(Double.POSITIVE_INFINITY);
    ds.add(Double.NaN);
    fs = new LinkedList<Float>();
    fs.add(null);
    fs.add(Float.NEGATIVE_INFINITY);
    fs.add(Float.MIN_VALUE);
    fs.add(Float.valueOf(-7));
    fs.add(Float.valueOf(-1));
    fs.add(Float.valueOf(0));
    fs.add(Float.valueOf(1));
    fs.add(Float.valueOf(1.5f));
    fs.add(Float.valueOf(13f/7f));
    fs.add((float)Math.E);
    fs.add((float)Math.PI);
    fs.add(Float.valueOf(7));
    fs.add(Float.MAX_VALUE);
    fs.add(Float.POSITIVE_INFINITY);
    fs.add(Float.NaN);
    ls = new LinkedList<Long>();
    ls.add(null);
    ls.add(Long.MIN_VALUE);
    ls.add(Long.valueOf(-7));
    ls.add(Long.valueOf(-1));
    ls.add(Long.valueOf(0));
    ls.add(Long.valueOf(1));
    ls.add(Long.valueOf(7));
    ls.add(Long.MAX_VALUE);
    is = new LinkedList<Integer>();
    is.add(null);
    is.add(Integer.MIN_VALUE);
    is.add(Integer.valueOf(-7));
    is.add(Integer.valueOf(-1));
    is.add(Integer.valueOf(0));
    is.add(Integer.valueOf(1));
    is.add(Integer.valueOf(7));
    is.add(Integer.MAX_VALUE);
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    ds = null;
    fs = null;
    ls = null;
    is = null;
  }

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private static List<Double> ds;
  private static List<Float> fs;
  private static List<Long> ls;
  private static List<Integer> is;

  @Test
  public void testEqualValueDoubleDouble() {
    for (Double d1 : ds) {
      for (Double d2 : ds) {
        testEqualValueDoubleNumber(d1, d2, equalValue(d1, d2));
      }
    }
  }

  @Test
  public void testEqualValueDoubleFloat() {
    for (Double d : ds) {
      for (Float f : fs) {
        testEqualValueDoubleNumber(d, f, equalValue(d, f));
      }
    }
  }

  @Test
  public void testEqualValueDoubleLong() {
    for (Double d : ds) {
      for (Long l : ls) {
        testEqualValueDoubleNumber(d, l, equalValue(d, l));
      }
    }
  }

  @Test
  public void testEqualValueDoubleInteger() {
    for (Double d : ds) {
      for (Integer i : is) {
        testEqualValueDoubleNumber(d, i, equalValue(d, i));
      }
    }
  }

  private void testEqualValueDoubleNumber(Double d, Number n, boolean result) {
    if (d == null) {
      assertEquals(n == null, result);
    }
    else if (n == null) {
      assertEquals(false, result);
    }
    else {
      if (d.isNaN()) {
        assertEquals(isNaN(n), result);
      }
      else if (isNaN(n)) {
        assertEquals(false, result);
      }
      else {
        double dValue = d.doubleValue();
        double nValue = n.doubleValue();
        assertEquals(dValue == nValue, result);
      }
    }
  }

  private boolean isNaN(Number n) {
    if (n == null) {
      return false;
    }
    if (n instanceof Double) {
      return ((Double)n).isNaN();
    }
    else if (n instanceof Float) {
      return ((Float)n).isNaN();
    }
    else {
      return false;
    }
  }

  @Test
  public void testEqualValueFloatFloat() {
    for (Float f1 : fs) {
      for (Float f2 : fs) {
        testEqualValueFloatNumber(f1, f2, equalValue(f1, f2));
      }
    }
  }

  @Test
  public void testEqualValueFloatLong() {
    for (Float f : fs) {
      for (Long l : ls) {
        testEqualValueFloatNumber(f, l, equalValue(f, l));
      }
    }
  }

  @Test
  public void testEqualValueFloatInteger() {
    for (Float f : fs) {
      for (Integer i : is) {
        testEqualValueFloatNumber(f, i, equalValue(f, i));
      }
    }
  }

  private void testEqualValueFloatNumber(Float f, Number n, boolean result) {
    if (f == null) {
      assertEquals(n == null, result);
    }
    else if (n == null) {
      assertEquals(false, result);
    }
    else {
      if (f.isNaN()) {
        assertEquals(isNaN(n), result);
      }
      else if (isNaN(n)) {
        assertEquals(false, result);
      }
      else {
        float fValue = f.floatValue();
        float nValue = n.floatValue();
        assertEquals(fValue == nValue, result);
      }
    }
  }

  @Test
  public void testEqualValueLongInteger() {
    for (Long l : ls) {
      for (Integer i : is) {
        testEqualValueLongNumber(l, i, equalValue(l, i));
      }
    }
  }

  private void testEqualValueLongNumber(Long l, Number n, boolean result) {
    if (l == null) {
      assertEquals(n == null, result);
    }
    else if (n == null) {
      assertEquals(false, result);
    }
    else {
      long lValue = l.longValue();
      long nValue = n.longValue();
      assertEquals(lValue == nValue, result);
    }
  }

  @Test
  public void testCastToDoubleFloat() {
    for (Float f : fs) {
      Double result = castToDouble(f);
      System.out.println(f + " --> " + result);
      assertTrue(equalValue(result, f));
    }
  }

  @Test
  public void testCastToDoubleLong() {
    for (Long l : ls) {
      Double result = castToDouble(l);
      assertTrue(equalValue(result, l));
    }
  }

  @Test
  public void testCastToDoubleInteger() {
    for (Integer i : is) {
      Double result = castToDouble(i);
      assertTrue(equalValue(result, i));
    }
  }

  @Test
  public void testCastToFloatLong() {
    for (Long l : ls) {
      Float result = castToFloat(l);
      assertTrue(equalValue(result, l));
    }
  }

  @Test
  public void testCastToFloatInteger() {
    for (Integer i : is) {
      Float result = castToFloat(i);
      assertTrue(equalValue(result, i));
    }
  }

  @Test
  public void testCastToLong() {
    for (Integer i : is) {
      Long result = castToLong(i);
      assertTrue(equalValue(result, i));
    }
  }

}

