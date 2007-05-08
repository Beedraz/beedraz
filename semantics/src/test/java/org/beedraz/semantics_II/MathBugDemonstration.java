package org.beedraz.semantics_II;

import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.apache.commons.math.stat.descriptive.moment.Variance;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
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
