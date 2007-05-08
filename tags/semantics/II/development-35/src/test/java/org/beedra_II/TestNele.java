package org.beedra_II;

import org.apache.commons.math.stat.descriptive.moment.Variance;


public class TestNele {

  /**
   * @param args
   */
  public static void main(String[] args) {
//    Variance v = new Variance();
//    System.out.println(v.getResult());
//    v.increment(1);
//    System.out.println(v.getResult());
//    v.increment(2);
//    System.out.println(v.getResult());
//    v.increment(3);
//    System.out.println(v.getResult());
//    v.increment(4);
//    System.out.println(v.getResult());


//      GeometricStandardDeviation sd = new GeometricStandardDeviation();
//      double[] values = new double[] {1.0,2.0,0.0, Double.POSITIVE_INFINITY, -9.0};
//      sd.incrementAll(values);
//      System.out.println(sd.getResult());

      // difference between getResult and evaluate
//      double[] values = new double[] {1.0, 2.0, Double.POSITIVE_INFINITY};
//      Variance var1 = new Variance();
//      double value1 = var1.evaluate(values);
//      Variance var2 = new Variance();
//      var2.incrementAll(values);
//      double value2 = var2.getResult();
//      System.out.println(value1);
//      System.out.println(value2);

    // difference between getResult and evaluate
    double[] values = new double[] {1.0, 2.0, Double.NEGATIVE_INFINITY};
    Variance var1 = new Variance();
    double value1 = var1.evaluate(values);
    Variance var2 = new Variance();
    var2.incrementAll(values);
    double value2 = var2.getResult();
    var2.evaluate(values);
    System.out.println(value1);
    System.out.println(value2);

//    // evaluate method changes internal representation
//    double[] values = new double[] {1.0, 2.0, Double.NEGATIVE_INFINITY};
//    Variance var1 = new Variance();
//    var1.incrementAll(values);
//    System.out.println(var1.getN());
//    System.out.println(var1.getResult());
//    double value1 = var1.evaluate(values);
//    System.out.println(var1.getN());
//    System.out.println(var1.getResult());
//    System.out.println(value1);

  }

}
