/*
 * Copyright (c) 2006-2014 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.mockups;

import java.lang.reflect.*;
import java.util.*;
import static java.lang.reflect.Modifier.*;

import mockit.internal.*;
import mockit.internal.state.*;
import mockit.internal.util.*;
import mockit.internal.util.GenericTypeReflection.*;
import static mockit.internal.util.ObjectMethods.*;

import org.jetbrains.annotations.*;

/**
 * A container for the mock methods "collected" from a mock class, separated in two sets: one with all the mock methods,
 * and another with just the subset of static methods.
 */
final class MockMethods
{
   @NotNull final Class<?> realClass;
   private final boolean mockedTypeIsAClass;
   private final boolean reentrantRealClass;
   @NotNull private final List<MockMethod> methods;
   @Nullable private MockMethod adviceMethod;
   @NotNull private final GenericTypeReflection typeParametersToTypeArguments;
   @NotNull private String mockClassInternalName;
   @Nullable private List<MockState> mockStates;

   final class MockMethod
   {
      private final int access;
      @NotNull final String name;
      @NotNull final String desc;
      final boolean isAdvice;
      final boolean hasInvocationParameter;
      @NotNull final String mockDescWithoutInvocationParameter;
      private boolean hasMatchingRealMethod;
      @Nullable private GenericSignature mockSignature;
      private int indexForMockState;
      private boolean nativeRealMethod;

      private MockMethod(int access, @NotNull String name, @NotNull String desc)
      {
         this.access = access;
         this.name = name;
         this.desc = desc;

         int p = desc.lastIndexOf("Lmockit/Invocation;");

         if (p > 1) {
            throw new IllegalArgumentException(
               "Mock method with Invocation parameter not as first one:\n" +
               new MethodFormatter(mockClassInternalName, getMockNameAndDesc()));
         }

         hasInvocationParameter = p > 0;
         mockDescWithoutInvocationParameter = hasInvocationParameter ? '(' + desc.substring(20) : desc;
         isAdvice =
            hasInvocationParameter &&
            "$advice".equals(name) && "()Ljava/lang/Object;".equals(mockDescWithoutInvocationParameter);
         hasMatchingRealMethod = false;
         indexForMockState = -1;
      }

      boolean isMatch(int realAccess, @NotNull String realName, @NotNull String realDesc, @Nullable String signature)
      {
         if (name.equals(realName) && hasMatchingParameters(realDesc, signature)) {
            hasMatchingRealMethod = true;
            nativeRealMethod = isNative(realAccess);
            return true;
         }

         return false;
      }

      private boolean hasMatchingParameters(@NotNull String methodDesc, @Nullable String signature)
      {
         boolean sameParametersIgnoringGenerics = mockDescWithoutInvocationParameter.equals(methodDesc);

         if (sameParametersIgnoringGenerics || signature == null) {
            return sameParametersIgnoringGenerics;
         }

         if (mockSignature == null) {
            mockSignature = typeParametersToTypeArguments.parseSignature(mockDescWithoutInvocationParameter);
         }

         return mockSignature.satisfiesGenericSignature(signature);
      }

      @NotNull Class<?> getRealClass() { return realClass; }
      @NotNull String getMockNameAndDesc() { return name + desc; }
      int getIndexForMockState() { return indexForMockState; }

      boolean isStatic() { return Modifier.isStatic(access); }
      boolean isPublic() { return Modifier.isPublic(access); }
      boolean isForGenericMethod() { return mockSignature != null; }
      boolean isForNativeMethod() { return nativeRealMethod; }
      boolean requiresMockState() { return hasInvocationParameter || reentrantRealClass; }

      boolean canBeReentered()
      {
         return mockedTypeIsAClass && !nativeRealMethod && (hasInvocationParameter || reentrantRealClass);
      }

      @NotNull String errorMessage(@NotNull String quantifier, int numExpectedInvocations, int timesInvoked)
      {
         String nameAndDesc = getMockNameAndDesc();
         return
            "Expected " + quantifier + ' ' + numExpectedInvocations + " invocation(s) of " +
            new MethodFormatter(mockClassInternalName, nameAndDesc) + ", but was invoked " + timesInvoked + " time(s)";
      }

      @Override
      public boolean equals(Object obj)
      {
         MockMethod other = (MockMethod) obj;
         return realClass == other.getRealClass() && name.equals(other.name) && desc.equals(other.desc);
      }

      @Override
      public int hashCode()
      {
         return 31 * (31 * realClass.hashCode() + name.hashCode()) + desc.hashCode();
      }
   }

   MockMethods(@NotNull Class<?> realClass, @Nullable Type mockedType)
   {
      this.realClass = realClass;

      if (mockedType == null || realClass == mockedType) {
         mockedTypeIsAClass = true;
      }
      else {
         Class<?> mockedClass = Utilities.getClassType(mockedType);
         mockedTypeIsAClass = !mockedClass.isInterface();
      }

      reentrantRealClass = mockedTypeIsAClass && MockingBridge.instanceOfClassThatParticipatesInClassLoading(realClass);
      methods = new ArrayList<MockMethod>();
      typeParametersToTypeArguments = new GenericTypeReflection(realClass, mockedType);
      mockClassInternalName = "";
   }

   @NotNull Class<?> getRealClass() { return realClass; }

   @Nullable MockMethod addMethod(boolean fromSuperClass, int access, @NotNull String name, @NotNull String desc)
   {
      if (fromSuperClass && isMethodAlreadyAdded(name, desc)) {
         return null;
      }

      MockMethod mockMethod = new MockMethod(access, name, desc);

      if (mockMethod.isAdvice) {
         adviceMethod = mockMethod;
      }
      else {
         methods.add(mockMethod);
      }

      return mockMethod;
   }

   private boolean isMethodAlreadyAdded(@NotNull String name, @NotNull String desc)
   {
      int p = desc.lastIndexOf(')');
      String params = desc.substring(0, p + 1);

      for (MockMethod mockMethod : methods) {
         if (mockMethod.name.equals(name) && mockMethod.desc.startsWith(params)) {
            return true;
         }
      }

      return false;
   }

   void addMockState(@NotNull MockState mockState)
   {
      if (mockStates == null) {
         mockStates = new ArrayList<MockState>(4);
      }

      mockState.mockMethod.indexForMockState = mockStates.size();
      mockStates.add(mockState);
   }

   @Nullable List<MockState> getMockStates() { return mockStates; }

   /**
    * Finds a mock method with the same signature of a given real method, if previously collected from the mock class.
    * This operation can be performed only once for any given mock method in this container, so that after the last real
    * method is processed there should be no mock methods left unused in the container.
    */
   @Nullable
   MockMethod findMethod(int access, @NotNull String name, @NotNull String desc, @Nullable String signature)
   {
      for (MockMethod mockMethod : methods) {
         if (mockMethod.isMatch(access, name, desc, signature)) {
            return mockMethod;
         }
      }

      if (
         adviceMethod != null && !isNative(access) && !"$init".equals(name) && !"$clinit".equals(name) &&
         !isMethodFromObject(name, desc)
      ) {
         return adviceMethod;
      }

      return null;
   }

   @NotNull String getMockClassInternalName() { return mockClassInternalName; }

   void setMockClassInternalName(@NotNull String mockClassInternalName)
   {
      this.mockClassInternalName = mockClassInternalName.intern();
   }

   boolean hasUnusedMocks()
   {
      for (MockMethod method : methods) {
         if (!method.hasMatchingRealMethod) {
            return true;
         }
      }

      return false;
   }

   @NotNull List<String> getUnusedMockSignatures()
   {
      List<String> signatures = new ArrayList<String>(methods.size());

      for (MockMethod mockMethod : methods) {
         if (!"$clinit()V".equals(mockMethod.getMockNameAndDesc()) && !mockMethod.hasMatchingRealMethod) {
            signatures.add(mockMethod.getMockNameAndDesc());
         }
      }

      return signatures;
   }

   void registerMockStates(@NotNull Object mockUp, boolean forStartupMock)
   {
      if (mockStates != null) {
         MockStates allMockStates = TestRun.getMockStates();

         if (forStartupMock) {
            allMockStates.addStartupMockUpAndItsMockStates(mockUp, mockStates);
         }
         else {
            allMockStates.addMockStates(mockStates);
            allMockStates.addMockUpAndItsMockStates(mockUp, mockStates);
         }
      }
   }
}
