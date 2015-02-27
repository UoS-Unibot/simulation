/*
 * Copyright (c) 2006-2014 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.junit4.internal;

import java.lang.annotation.*;
import java.util.*;

import org.jetbrains.annotations.*;

import org.junit.runners.model.*;

import mockit.*;
import mockit.internal.*;
import mockit.internal.mockups.*;
import mockit.internal.util.*;

/**
 * Startup mock that modifies the JUnit 4.5+ test runner so that it calls back to JMockit immediately after every test
 * executes.
 * When that happens, JMockit will assert any expectations set during the test, including expectations specified through
 * {@link Mock} as well as in {@link Expectations} subclasses.
 * <p/>
 * This class is not supposed to be accessed from user code. JMockit will automatically load it at startup.
 */
public final class MockFrameworkMethod extends MockUp<FrameworkMethod>
{
   public static boolean hasDependenciesInClasspath()
   {
      return ClassLoad.searchTypeInClasspath("org.junit.runners.model.FrameworkMethod", true) != null;
   }

   @NotNull private final JUnit4TestRunnerDecorator decorator = new JUnit4TestRunnerDecorator();

   @Mock
   @Nullable
   public Object invokeExplosively(@NotNull Invocation invocation, Object target, Object... params) throws Throwable
   {
      return decorator.invokeExplosively((MockInvocation) invocation, target, params);
   }

   @Mock
   public static void validatePublicVoidNoArg(@NotNull Invocation invocation, boolean isStatic, List<Throwable> errors)
   {
      FrameworkMethod it = invocation.getInvokedInstance();
      int previousErrorCount = errors.size();

      if (!isStatic && eachParameterContainsAMockingAnnotation(it.getMethod().getParameterAnnotations())) {
         it.validatePublicVoid(false, errors);
      }
      else {
         ((BaseInvocation) invocation).prepareToProceed();
         it.validatePublicVoidNoArg(isStatic, errors);
      }

      int errorCount = errors.size();

      for (int i = previousErrorCount; i < errorCount; i++) {
         Throwable errorAdded = errors.get(i);
         StackTrace.filterStackTrace(errorAdded);
      }
   }

   private static boolean eachParameterContainsAMockingAnnotation(@NotNull Annotation[][] parametersAndTheirAnnotations)
   {
      if (parametersAndTheirAnnotations.length == 0) {
         return false;
      }

      for (Annotation[] parameterAnnotations : parametersAndTheirAnnotations) {
         if (!containsAMockingAnnotation(parameterAnnotations)) {
            return false;
         }
      }

      return true;
   }

   private static boolean containsAMockingAnnotation(@NotNull Annotation[] parameterAnnotations)
   {
      if (parameterAnnotations.length == 0) {
         return false;
      }

      for (Annotation parameterAnnotation : parameterAnnotations) {
         String annotationTypeName = parameterAnnotation.annotationType().getName();

         if (!"mockit.Mocked mockit.Injectable mockit.Capturing".contains(annotationTypeName)) {
            return false;
         }
      }

      return true;
   }
}
