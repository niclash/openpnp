package org.openpnp.serialization;

import com.fasterxml.jackson.databind.util.StdConverter;
public class CommitCaller extends StdConverter<Object,Object> {

  @Override
  public Object convert(Object value) {

    return value;
  }
}