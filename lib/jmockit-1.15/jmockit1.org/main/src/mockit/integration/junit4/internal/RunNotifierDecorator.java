/*
 * Copyright (c) 2006-2014 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.junit4.internal;

import org.junit.runner.*;
import org.junit.runner.notification.*;

import mockit.*;
import mockit.integration.internal.*;
import mockit.internal.*;
import mockit.internal.state.*;

/**
 * Startup mock which works in conjunction with {@link JUnit4TestRunnerDecorator} to provide JUnit 4.5+ integration.
 * <p/>
 * This class is not supposed to be accessed from user code. JMockit will automatically load it at startup.
 */
public final class RunNotifierDecorator extends MockUp<RunNotifier>
{
   @Mock
   public static void fireTestRunFinished(Invocation invocation, Result result)
   {
      TestRun.enterNoMockingZone();

      try {
         TestRunnerDecorator.cleanUpMocksFromPreviousTestClass();

         RunNotifier it = invocation.getInvokedInstance();
         ((BaseInvocation) invocation).prepareToProceed();
         it.fireTestRunFinished(result);
      }
      finally {
         TestRun.exitNoMockingZone();
      }
   }
}
