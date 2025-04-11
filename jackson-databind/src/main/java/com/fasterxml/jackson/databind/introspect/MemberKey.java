package com.fasterxml.jackson.databind.introspect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Helper class needed to be able to efficiently access class member functions ({@link Method}s and
 * {@link Constructor}s) in {@link java.util.Map}s.
 */
public final class MemberKey {
  static final Class<?>[] NO_CLASSES = new Class<?>[0];

  final String _name;
  final Class<?>[] _argTypes;

  public MemberKey(Method m) {
    this(m.getName(), m.getParameterTypes());
  }

  public MemberKey(Constructor<?> ctor) {
    this("", ctor.getParameterTypes());
  }

  public MemberKey(String name, Class<?>[] argTypes) {
    _name = name;
    _argTypes = (argTypes == null) ? NO_CLASSES : argTypes;
  }

  public String getName() {
    return _name;
  }

  public int argCount() {
    return _argTypes.length;
  }

  @Override
  public String toString() {
    return _name + "(" + _argTypes.length + "-args)";
  }

  @Override
  public int hashCode() {
    return _name.hashCode() + _argTypes.length;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (o == null) return false;
    if (o.getClass() != getClass()) {
      return false;
    }
    MemberKey other = (MemberKey) o;
    if (!_name.equals(other._name)) {
      return false;
    }
    Class<?>[] otherArgs = other._argTypes;
    int len = _argTypes.length;
    if (otherArgs.length != len) {
      return false;
    }
    for (int i = 0; i < len; ++i) {
      Class<?> type1 = otherArgs[i];
      Class<?> type2 = _argTypes[i];
      if (type1 == type2) {
        continue;
      }
      /* buggy version 2 */
      if (type1.isAssignableFrom(type2)) {
        continue;
      }
      /*
      if (type1.isAssignableFrom(type2) || type2.isAssignableFrom(type1)) {
          continue;
      }
      */
      return false;
    }
    return true;
  }
}
