/*  Copyright (c) 2000-2006 hamcrest.org
 */
package org.hamcrest.core;

import java.util.regex.Pattern;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Factory;

/**
 * Provides a custom description to another matcher.
 */
public class DescribedAs<T> implements Matcher<T> {
    private final String descriptionTemplate;
    private final Matcher<T> matcher;
    private final Object[] values;
    
    private final static Pattern ARG_PATTERN = Pattern.compile("%([0-9]+)"); 
    
    public DescribedAs(String descriptionTemplate, Matcher<T> matcher, Object[] values) {
        this.descriptionTemplate = descriptionTemplate;
        this.matcher = matcher;
        this.values = values.clone();
    }
    
    public boolean match(T o) {
        return matcher.match(o);
    }

    public void describeTo(Description description) {
        java.util.regex.Matcher arg = ARG_PATTERN.matcher(descriptionTemplate);
        
        int textStart = 0;
        while (arg.find()) {
            description.appendText(descriptionTemplate.substring(textStart, arg.start()));
            int argIndex = Integer.parseInt(arg.group(1));
            description.appendValue(values[argIndex]);
            textStart = arg.end();
        }
        
        if (textStart < descriptionTemplate.length()) {
            description.appendText(descriptionTemplate.substring(textStart));
        }
    }
    
    @Factory
    public static <T> Matcher<T> describedAs(String description, Matcher<T> matcher, Object... values) {
        return new DescribedAs<T>(description, matcher, values);
    }
}
