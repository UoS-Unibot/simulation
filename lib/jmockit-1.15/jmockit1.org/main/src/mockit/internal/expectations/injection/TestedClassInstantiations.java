/*
 * Copyright (c) 2006-2014 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.injection;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import mockit.*;
import mockit.internal.expectations.mocking.*;

import org.jetbrains.annotations.*;

public final class TestedClassInstantiations
{
   @NotNull private final List<TestedField> testedFields;
   @NotNull private final List<MockedType> injectableFields;
   @NotNull private final InjectionState injectionState;

   public TestedClassInstantiations()
   {
      testedFields = new LinkedList<TestedField>();
      injectableFields = new ArrayList<MockedType>();
      injectionState = new InjectionState();
   }

   public boolean findTestedAndInjectableFields(@NotNull Class<?> testClass)
   {
      boolean foundTestedFields = findAllTestedAndInjectableFieldsInTestClassHierarchy(testClass);

      if (foundTestedFields) {
         new ParameterNameExtractor(true).extractNames(testClass);
      }

      return foundTestedFields;
   }

   private boolean findAllTestedAndInjectableFieldsInTestClassHierarchy(@NotNull Class<?> testClass)
   {
      Class<?> classWithFields = testClass;

      do {
         Field[] fields = classWithFields.getDeclaredFields();
         findTestedAndInjectableFields(fields);
         classWithFields = classWithFields.getSuperclass();
      }
      while (classWithFields.getClassLoader() != null);

      return !testedFields.isEmpty();
   }

   private void findTestedAndInjectableFields(@NotNull Field[] fieldsDeclaredInTestClass)
   {
      for (Field field : fieldsDeclaredInTestClass) {
         addAsTestedOrInjectableFieldIfApplicable(field);
      }
   }

   private void addAsTestedOrInjectableFieldIfApplicable(@NotNull Field fieldFromTestClass)
   {
      boolean testedAnnotationFound = false;
      boolean injectableAnnotationFound = false;

      for (Annotation fieldAnnotation : fieldFromTestClass.getDeclaredAnnotations()) {
         Tested testedMetadata = testedAnnotationFound ? null : getTestedAnnotationIfPresent(fieldAnnotation);

         if (testedMetadata != null) {
            TestedField testedField = new TestedField(injectionState, fieldFromTestClass, testedMetadata);
            testedFields.add(testedField);
            testedAnnotationFound = true;
         }
         else if (fieldAnnotation instanceof Injectable) {
            MockedType mockedType = new MockedType(fieldFromTestClass);
            injectableFields.add(mockedType);
            injectableAnnotationFound = true;
         }

         if (testedAnnotationFound && injectableAnnotationFound) {
            Collections.swap(testedFields, 0, testedFields.size() - 1);
            break;
         }
      }
   }

   @Nullable
   private static Tested getTestedAnnotationIfPresent(@NotNull Annotation fieldAnnotation)
   {
      if (fieldAnnotation instanceof Tested) {
         return (Tested) fieldAnnotation;
      }

      return fieldAnnotation.annotationType().getAnnotation(Tested.class);
   }

   public void assignNewInstancesToTestedFields(@NotNull Object testClassInstance)
   {
      injectionState.buildListsOfInjectables(testClassInstance, injectableFields);

      TestedField previousField = null;

      for (TestedField testedField : testedFields) {
         if (previousField != null && !testedField.isAtSameLevelInTestClassHierarchy(previousField)) {
            injectionState.discardInjectablesFromLowerTestClassHierarchyLevels(testedField.getDeclaringTestClass());
         }

         try {
            testedField.instantiateWithInjectableValues(testClassInstance);
         }
         finally {
            injectionState.resetConsumedInjectables();
         }

         previousField = testedField;
      }
   }

   public void clearTestedFields()
   {
      injectionState.lifecycleMethods.executePreDestroyMethodsIfAny();

      for (TestedField testedField : testedFields) {
         testedField.clearIfAutomaticCreation();
      }
   }
}
