/*
 * Copyright (c) 2006-2014 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations;

import java.util.*;

import org.jetbrains.annotations.*;

import mockit.internal.expectations.argumentMatching.*;
import static mockit.internal.util.Utilities.indexOfReference;

public abstract class TestOnlyPhase extends Phase
{
   protected int numberOfIterations;
   @Nullable protected Object nextInstanceToMatch;
   protected boolean matchInstance;
   @Nullable protected List<ArgumentMatcher<?>> argMatchers;
   @Nullable Expectation currentExpectation;

   TestOnlyPhase(@NotNull RecordAndReplayExecution recordAndReplay) { super(recordAndReplay); }

   public final void setNumberOfIterations(int numberOfIterations) { this.numberOfIterations = numberOfIterations; }

   public final void setNextInstanceToMatch(@Nullable Object nextInstanceToMatch)
   {
      this.nextInstanceToMatch = nextInstanceToMatch;
   }

   public final void addArgMatcher(@NotNull ArgumentMatcher<?> matcher)
   {
      getArgumentMatchers().add(matcher);
   }

   @NotNull
   private List<ArgumentMatcher<?>> getArgumentMatchers()
   {
      if (argMatchers == null) {
         argMatchers = new ArrayList<ArgumentMatcher<?>>();
      }

      return argMatchers;
   }

   public final void moveArgMatcher(int originalMatcherIndex, int toIndex)
   {
      List<ArgumentMatcher<?>> matchers = getArgumentMatchers();
      int i = 0;

      for (int matchersFound = 0; matchersFound <= originalMatcherIndex; i++) {
         if (matchers.get(i) != null) {
            matchersFound++;
         }
      }

      for (i--; i < toIndex; i++) {
         matchers.add(i, null);
      }
   }

   public final void setExpectedArgumentType(int parameterIndex, @NotNull Class<?> argumentType)
   {
      ArgumentMatcher<?> newMatcher = ClassMatcher.create(argumentType);
      getArgumentMatchers().set(parameterIndex, newMatcher);
   }

   @NotNull
   final Expectation getCurrentExpectation()
   {
      if (currentExpectation == null) {
         throw new IllegalStateException(
            "Missing invocation to mocked type at this point; please make sure such invocations appear only after " +
            "the declaration of a suitable mock field or parameter");
      }

      return currentExpectation;
   }

   public void setMaxInvocationCount(int maxInvocations)
   {
      int currentMinimum = getCurrentExpectation().constraints.minInvocations;

      if (numberOfIterations > 0) {
         currentMinimum /= numberOfIterations;
      }

      int minInvocations = maxInvocations < 0 ? currentMinimum : Math.min(currentMinimum, maxInvocations);

      handleInvocationCountConstraint(minInvocations, maxInvocations);
   }

   public abstract void handleInvocationCountConstraint(int minInvocations, int maxInvocations);

   public abstract void setCustomErrorMessage(@Nullable CharSequence customMessage);

   protected static boolean isEnumElement(@NotNull Object mock)
   {
      Class<?> mockedClass = mock.getClass();
      return mockedClass.isEnum() && indexOfReference(mockedClass.getEnumConstants(), mock) >= 0;
   }
}
