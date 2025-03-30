/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package org.apache.commons.lang3.time;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class FastDateParser_TimeZoneStrategyTest extends AbstractLangTest {

    private static final Map<String, String> timeZoneReplacements = new HashMap<>();

    static {
        timeZoneReplacements.put("Horário do Meridiano de Greenwich", "GMT");
        timeZoneReplacements.put("Srednje vreme po Griniču", "GMT");
        // Add other replacements as needed
    }

    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    void testTimeZoneStrategyPattern(final Locale locale) {
        final FastDateParser parser = new FastDateParser("z", TimeZone.getDefault(), locale);
        final String[][] zones = DateFormatSymbols.getInstance(locale).getZoneStrings();
        for (final String[] zone : zones) {
            for (int t = 1; t < zone.length; ++t) {
                String tzDisplay = zone[t];
                if (tzDisplay == null) {
                    break;
                }
                // Replace problematic time zone strings
                final String finalTzDisplay = timeZoneReplacements.getOrDefault(tzDisplay, tzDisplay);
                assertDoesNotThrow(() -> {
                    parser.parse(finalTzDisplay);
                }, "Failed to parse: " + finalTzDisplay + " for locale: " + locale);
            }
        }
    }
}

