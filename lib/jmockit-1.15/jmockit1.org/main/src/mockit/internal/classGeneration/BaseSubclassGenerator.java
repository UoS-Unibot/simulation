/*
 * Copyright (c) 2006-2014 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.classGeneration;

import java.lang.reflect.*;
import java.lang.reflect.Type;
import java.util.*;
import static java.util.Arrays.*;

import mockit.external.asm.*;
import mockit.internal.*;
import mockit.internal.util.*;
import static mockit.external.asm.Opcodes.*;

import org.jetbrains.annotations.*;

public class BaseSubclassGenerator extends BaseClassModifier
{
   private static final int CLASS_ACCESS_MASK = 0xFFFF - ACC_ABSTRACT;
   private static final int CONSTRUCTOR_ACCESS_MASK = ACC_PUBLIC + ACC_PROTECTED;

   // Fixed initial state:
   @NotNull Class<?> baseClass;
   @NotNull private final String subclassName;
   @Nullable protected final MockedTypeInfo mockedTypeInfo;
   private final boolean copyConstructors;

   // Helper fields for mutable state:
   @NotNull private final List<String> implementedMethods;
   @Nullable private String superClassOfSuperClass;
   @NotNull private Set<String> superInterfaces;

   protected BaseSubclassGenerator(
      @NotNull Class<?> baseClass, @NotNull ClassReader classReader,
      @Nullable Type genericMockedType, @NotNull String subclassName,
      boolean copyConstructors)
   {
      super(classReader);
      this.baseClass = baseClass;
      this.subclassName = subclassName.replace('.', '/');
      mockedTypeInfo = genericMockedType == null ? null : new MockedTypeInfo(genericMockedType);
      this.copyConstructors = copyConstructors;
      implementedMethods = new ArrayList<String>();
   }

   @Override
   public void visit(
      int version, int access, @NotNull String name, @Nullable String signature, @Nullable String superName,
      @Nullable String[] interfaces)
   {
      int subclassAccess = access & CLASS_ACCESS_MASK | ACC_FINAL;
      String subclassSignature = mockedTypeInfo == null ? signature : mockedTypeInfo.implementationSignature;

      super.visit(version, subclassAccess, subclassName, subclassSignature, name, null);

      superClassOfSuperClass = superName;
      superInterfaces = new HashSet<String>();

      if (interfaces != null && interfaces.length > 0) {
         superInterfaces.addAll(asList(interfaces));
      }
   }

   @Override
   public final void visitInnerClass(
      @NotNull String name, @Nullable String outerName, @Nullable String innerName, int access) {}

   @Override
   public final void visitOuterClass(@NotNull String owner, @Nullable String name, @Nullable String desc) {}

   @Override
   public final void visitAttribute(Attribute attr) {}

   @Override
   public final void visitSource(@Nullable String source, @Nullable String debug) {}

   @Override @Nullable
   public final FieldVisitor visitField(
      int access, @NotNull String name, @NotNull String desc, @Nullable String signature, @Nullable Object value)
   { return null; }

   @Override @Nullable
   public MethodVisitor visitMethod(
      int access, @NotNull String name, @NotNull String desc, @Nullable String signature, @Nullable String[] exceptions)
   {
      if (copyConstructors && "<init>".equals(name)) {
         if ((access & CONSTRUCTOR_ACCESS_MASK) != 0) {
            generateConstructorDelegatingToSuper(desc, signature, exceptions);
         }
      }
      else {
         // Inherits from super-class when non-abstract; otherwise, creates implementation for abstract method.
         generateImplementationIfAbstractMethod(superClassName, access, name, desc, signature, exceptions);
      }

      return null;
   }

   private void generateConstructorDelegatingToSuper(
      @NotNull String desc, @Nullable String signature, @Nullable String[] exceptions)
   {
      mw = cw.visitMethod(ACC_PUBLIC, "<init>", desc, signature, exceptions);
      mw.visitVarInsn(ALOAD, 0);
      int varIndex = 1;

      for (mockit.external.asm.Type paramType : mockit.external.asm.Type.getArgumentTypes(desc)) {
         int loadOpcode = getLoadOpcodeForParameterType(paramType.getSort());
         mw.visitVarInsn(loadOpcode, varIndex);
         varIndex++;
      }

      mw.visitMethodInsn(INVOKESPECIAL, superClassName, "<init>", desc, false);
      generateEmptyImplementation();
   }

   private static int getLoadOpcodeForParameterType(int paramType)
   {
      if (paramType <= mockit.external.asm.Type.INT) {
         return ILOAD;
      }

      switch (paramType) {
         case mockit.external.asm.Type.FLOAT:  return FLOAD;
         case mockit.external.asm.Type.LONG:   return LLOAD;
         case mockit.external.asm.Type.DOUBLE: return DLOAD;
         default: return ALOAD;
      }
   }

   private void generateImplementationIfAbstractMethod(
      String className, int access, @NotNull String name, @NotNull String desc, @Nullable String signature,
      @Nullable String[] exceptions)
   {
      if (!"<init>".equals(name)) {
         String methodNameAndDesc = name + desc;

         if (!implementedMethods.contains(methodNameAndDesc)) {
            if ((access & ACC_ABSTRACT) != 0) {
               generateMethodImplementation(className, access, name, desc, signature, exceptions);
            }

            implementedMethods.add(methodNameAndDesc);
         }
      }
   }

   protected void generateMethodImplementation(
      String className, int access, @NotNull String name, @NotNull String desc,
      @Nullable String signature, @Nullable String[] exceptions)
   {
   }

   @Override
   public void visitEnd()
   {
      generateImplementationsForInheritedAbstractMethods(superClassOfSuperClass);

      while (!superInterfaces.isEmpty()) {
         String superInterface = superInterfaces.iterator().next();
         generateImplementationsForInterfaceMethods(superInterface);
         superInterfaces.remove(superInterface);
      }
   }

   private void generateImplementationsForInheritedAbstractMethods(@Nullable String superName)
   {
      if (superName != null && !"java/lang/Object".equals(superName)) {
         new MethodModifierForSuperclass(superName);
      }
   }

   private void generateImplementationsForInterfaceMethods(String superName)
   {
      if (!"java/lang/Object".equals(superName)) {
         new MethodModifierForImplementedInterface(superName);
      }
   }

   private static class BaseMethodModifier extends ClassVisitor
   {
      @NotNull final String typeName;

      BaseMethodModifier(@NotNull String typeName)
      {
         this.typeName = typeName;
         ClassFile.visitClass(typeName, this);
      }

      @Nullable @Override
      public final FieldVisitor visitField(
         int access, @NotNull String name, @NotNull String desc, String signature, Object value) { return null; }
   }

   private final class MethodModifierForSuperclass extends BaseMethodModifier
   {
      String superClassName;

      MethodModifierForSuperclass(@NotNull String className) { super(className); }

      @Override
      public void visit(
         int version, int access, @NotNull String name, @Nullable String signature, @Nullable String superName,
         @Nullable String[] interfaces)
      {
         superClassName = superName;

         if (interfaces != null) {
            superInterfaces.addAll(asList(interfaces));
         }
      }

      @Nullable @Override
      public MethodVisitor visitMethod(
         int access, @NotNull String name, @NotNull String desc, String signature, String[] exceptions)
      {
         generateImplementationIfAbstractMethod(typeName, access, name, desc, signature, exceptions);
         return null;
      }

      @Override
      public void visitEnd()
      {
         generateImplementationsForInheritedAbstractMethods(superClassName);
      }
   }

   private final class MethodModifierForImplementedInterface extends BaseMethodModifier
   {
      MethodModifierForImplementedInterface(String interfaceName) { super(interfaceName); }

      @Override
      public void visit(
         int version, int access, @NotNull String name, @Nullable String signature, @Nullable String superName,
         @Nullable String[] interfaces)
      {
         assert interfaces != null;
         superInterfaces.addAll(asList(interfaces));
      }

      @Nullable @Override
      public MethodVisitor visitMethod(
         int access, @NotNull String name, @NotNull String desc, String signature, String[] exceptions)
      {
         generateImplementationForInterfaceMethodIfMissing(access, name, desc, signature, exceptions);
         return null;
      }

      private void generateImplementationForInterfaceMethodIfMissing(
         int access, @NotNull String name, @NotNull String desc, String signature, String[] exceptions)
      {
         String methodNameAndDesc = name + desc;

         if (!implementedMethods.contains(methodNameAndDesc)) {
            if (!hasMethodImplementation(name, desc)) {
               generateMethodImplementation(typeName, access, name, desc, signature, exceptions);
            }

            implementedMethods.add(methodNameAndDesc);
         }
      }

      private boolean hasMethodImplementation(@NotNull String name, @NotNull String desc)
      {
         Class<?>[] paramTypes = TypeDescriptor.getParameterTypes(desc);

         try {
            Method method = baseClass.getMethod(name, paramTypes);
            return !method.getDeclaringClass().isInterface();
         }
         catch (NoSuchMethodException ignore) {
            return false;
         }
      }
   }
}
