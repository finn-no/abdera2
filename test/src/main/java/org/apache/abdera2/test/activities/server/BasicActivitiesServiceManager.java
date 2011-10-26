package org.apache.abdera2.test.activities.server;

import java.util.Map;

import org.apache.abdera2.activities.protocol.ActivitiesServiceManager;
import org.apache.abdera2.activities.protocol.basic.BasicProvider;
import org.apache.abdera2.common.protocol.Provider;

public class BasicActivitiesServiceManager 
  extends ActivitiesServiceManager {

  public BasicActivitiesServiceManager() {}
  
  @SuppressWarnings("unchecked")
  public <P extends Provider> P newProvider(Map<String, Object> properties) {
    BasicProvider bp = new BasicProvider();
    bp.init(properties);
    return (P)bp;
  }

}
