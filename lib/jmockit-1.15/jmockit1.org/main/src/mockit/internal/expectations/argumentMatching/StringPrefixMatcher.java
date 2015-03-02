/*
 * Copyright (c) 2006-2014 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.argumentMatching;

import org.jetbrains.annotations.*;

public final class StringPrefixMatcher extends SubstringMatcher
{
   public StringPrefixMatcher(@NotNull CharSequence substring) { super(substring); }

   @Override
   public boolean matches(@Nullable Object argValue)
   {
      return argValue instanceof CharSequence && argValue.toString().startsWith(substring);
   }

   @Override
   public void writeMismatchPhrase(@NotNull ArgumentMismatch argumentMismatch)
   {
      argumentMismatch.append("a string starting with ").appendFormatted(substring);
   }
}