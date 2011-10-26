package org.apache.abdera2.test.server.custom;

import java.util.Map;

import org.apache.abdera2.common.protocol.Provider;
import org.apache.abdera2.protocol.server.AtompubServiceManager;

public class CustomAtompubServiceManager extends AtompubServiceManager {

  @SuppressWarnings("unchecked")
  @Override
  public <P extends Provider> P newProvider(Map<String, Object> properties) {
    CustomProvider cp = new CustomProvider("/");
    cp.init(properties);
    return (P)cp;
  }

}
