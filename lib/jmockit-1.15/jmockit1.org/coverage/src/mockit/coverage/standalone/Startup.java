/*
 * Copyright (c) 2006-2014 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.coverage.standalone;

import java.io.*;
import java.lang.instrument.*;

import org.jetbrains.annotations.*;

import mockit.coverage.*;

public final class Startup
{
   private static Instrumentation instrumentation;
   private static boolean inATestRun = true;
   private static boolean jmockitAvailable = true;

   private Startup() {}

   public static void premain(String agentArgs, @NotNull Instrumentation inst) throws IOException
   {
      instrumentation = inst;
      discoverOptionalDependenciesThatAreAvailableInClassPath();

      String startupMessage;

      if (inATestRun) {
         startupMessage = "JMockit Coverage tool loaded";
      }
      else {
         startupMessage = "JMockit Coverage tool loaded; connect with a JMX client to control operation";
         CoverageControl.create();
      }

      ClassFileTransformer coverageTransformer = CodeCoverage.create(inATestRun);
      inst.addTransformer(coverageTransformer);

      System.out.println();
      System.out.println(startupMessage);
      System.out.println();

      if (jmockitAvailable) {
         mockit.internal.startup.Startup.initialize(inst);
      }
   }

   private static void discoverOptionalDependenciesThatAreAvailableInClassPath()
   {
      inATestRun = isAvailableInClassPath("org.junit.Assert") || isAvailableInClassPath("org.testng.Assert");
      jmockitAvailable = isAvailableInClassPath("mockit.Invocations");
   }

   private static boolean isAvailableInClassPath(@NotNull String className)
   {
      ClassLoader currentLoader = Startup.class.getClassLoader();

      try {
         Class.forName(className, false, currentLoader);
         return true;
      }
      catch (ClassNotFoundException ignore) {
         return false;
      }
   }

   @NotNull public static Instrumentation instrumentation()
   {
      if (instrumentation == null) {
         instrumentation = mockit.internal.startup.Startup.instrumentation();
      }

      return instrumentation;
   }

   public static boolean isTestRun() { return inATestRun; }
   public static boolean isJMockitAvailable() { return jmockitAvailable; }
   public static boolean isInitialized() { return instrumentation != null; }
}
