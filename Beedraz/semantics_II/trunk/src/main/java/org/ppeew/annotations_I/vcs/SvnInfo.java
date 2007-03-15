package org.ppeew.annotations_I.vcs;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation for Subversion meta-data. By using this annotation,
 * the Subversion data about the source revision the compiled code is based on,
 * is available in the code.
 *
 * Usage pattern:
 * <pre>
 * ATSvnInfo(revision = &quot;$Revision$&quot;,
 *           date     = &quot;$Date$&quot;,
 *           url      = &quot;$URL$&quot;)
 * public class ... {
 *  ...
 * }
 * </pre>
 *
 * @author    Jan Dockx
 */
@Copyright(copyright = {"Jan Dockx", "PeopleWare n.v."}, from = "2007", until = "$Revision$")
@License(license     = "Apache2",
         notice      = {"Licensed under the Apache License, Version 2.0 (the \"License\")",
                        "you may not use this file except in compliance with the License.",
                        "You may obtain a copy of the License at",
                        "    http://www.apache.org/licenses/LICENSE-2.0",
                        "Unless required by applicable law or agreed to in writing, software",
                        "distributed under the License is distributed on an \"AS IS\" BASIS,",
                        "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.",
                        "See the License for the specific language governing permissions and",
                        "limitations under the License."})
@CvsInfo(revision    = "$Revision$",
         date        = "$Date$",
         state       = "$State$",
         tag         = "$Name$")
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface SvnInfo {

  /**
   * Source code revision. Fill out with &quot;$Revision$&quot;
   */
  String revision();

  /**
   * Source code revision. Fill out with &quot;$Date$&quot;
   */
  String date();

}