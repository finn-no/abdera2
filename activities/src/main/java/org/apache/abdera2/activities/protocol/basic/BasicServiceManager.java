package org.apache.abdera2.activities.protocol.basic;

import java.util.Map;

import org.apache.abdera2.activities.protocol.ActivitiesServiceManager;
import org.apache.abdera2.common.protocol.Provider;

public class BasicServiceManager extends ActivitiesServiceManager {

  @SuppressWarnings("unchecked")
  @Override
  public <P extends Provider> P newProvider(Map<String, Object> properties) {
    BasicProvider bp = new BasicProvider();
    bp.init(properties);
    return (P)bp;
  }

}
