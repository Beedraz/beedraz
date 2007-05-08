package org.beedraz.semantics_II;

import org.apache.commons.math.stat.descriptive.moment.Variance;


public class MathBugDemonstration {

  /**
   * @param args
   */
  public static void main(String[] args) {

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
