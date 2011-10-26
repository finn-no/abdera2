package org.apache.abdera2.test.server.basic;

import java.util.Map;

import org.apache.abdera2.common.protocol.Provider;
import org.apache.abdera2.protocol.server.AtompubServiceManager;
import org.apache.abdera2.protocol.server.provider.basic.BasicProvider;

public class BasicAtompubServiceManager 
  extends AtompubServiceManager {

  public BasicAtompubServiceManager() {}
  
  @SuppressWarnings("unchecked")
  @Override
  public <P extends Provider> P newProvider(
    Map<String, Object> properties) {
      BasicProvider bp = new BasicProvider();
      bp.init(properties);
      return (P)bp;
  }
  
}
