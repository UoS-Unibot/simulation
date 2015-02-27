/*
 * Copyright (c) 2006-2014 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.startup;

import java.io.*;
import java.lang.management.*;
import java.util.*;

import mockit.internal.util.*;

import com.sun.tools.attach.*;
import com.sun.tools.attach.spi.*;
import org.jetbrains.annotations.*;
import sun.tools.attach.*;

final class AgentLoader
{
   private static final AttachProvider ATTACH_PROVIDER = new AttachProvider() {
      @Override @Nullable public String name() { return null; }
      @Override @Nullable public String type() { return null; }
      @Override @Nullable public VirtualMachine attachVirtualMachine(String id) { return null; }
      @Override @Nullable public List<VirtualMachineDescriptor> listVirtualMachines() { return null; }
   };

   @NotNull private final String jarFilePath;
   @NotNull private final String pid;

   AgentLoader(@NotNull String jarFilePath)
   {
      this.jarFilePath = jarFilePath;
      pid = discoverProcessIdForRunningVM();
   }

   @NotNull
   private static String discoverProcessIdForRunningVM()
   {
      String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
      int p = nameOfRunningVM.indexOf('@');

      return nameOfRunningVM.substring(0, p);
   }

   boolean loadAgent()
   {
      VirtualMachine vm;

      if (AttachProvider.providers().isEmpty()) {
         vm = getVirtualMachineImplementationFromEmbeddedOnes();
      }
      else {
         vm = attachToThisVM();
      }

      if (vm != null) {
         loadAgentAndDetachFromThisVM(vm);
         return true;
      }

      return false;
   }

   @Nullable @SuppressWarnings("UseOfSunClasses")
   private VirtualMachine getVirtualMachineImplementationFromEmbeddedOnes()
   {
      try {
         Class<? extends VirtualMachine> vmClass;

         if (File.separatorChar == '\\') {
            vmClass = WindowsVirtualMachine.class;
         }
         else {
            String osName = System.getProperty("os.name");

            if (osName.startsWith("Linux") || osName.startsWith("LINUX")) {
               vmClass = LinuxVirtualMachine.class;
            }
            else if (osName.startsWith("Mac OS X")) {
               vmClass = BsdVirtualMachine.class;
            }
            else if (osName.startsWith("Solaris")) {
               vmClass = SolarisVirtualMachine.class;
            }
            else {
               return null;
            }
         }

         // This is only done with Reflection to avoid the JVM pre-loading all the XyzVirtualMachine classes.
         Class<?>[] parameterTypes = {AttachProvider.class, String.class};
         VirtualMachine newVM = ConstructorReflection.newInstance(vmClass, parameterTypes, ATTACH_PROVIDER, pid);
         return newVM;
      }
      catch (UnsatisfiedLinkError e) {
         throw new IllegalStateException("Native library for Attach API not available in this JRE", e);
      }
   }

   @Nullable
   private VirtualMachine attachToThisVM()
   {
      try {
         return VirtualMachine.attach(pid);
      }
      catch (AttachNotSupportedException e) {
         throw new RuntimeException(e);
      }
      catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   private void loadAgentAndDetachFromThisVM(@NotNull VirtualMachine vm)
   {
      try {
         vm.loadAgent(jarFilePath, null);
         vm.detach();
      }
      catch (AgentLoadException e) {
         throw new RuntimeException(e);
      }
      catch (AgentInitializationException e) {
         throw new RuntimeException(e);
      }
      catch (IOException e) {
         throw new RuntimeException(e);
      }
   }
}
