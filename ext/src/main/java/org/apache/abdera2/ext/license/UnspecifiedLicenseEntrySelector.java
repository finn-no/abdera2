package org.apache.abdera2.ext.license;

import org.apache.abdera2.common.selector.Selector;
import org.apache.abdera2.model.Entry;

/**
 * Used to only select entries that do not specify a license
 */
public class UnspecifiedLicenseEntrySelector implements Selector {

  public boolean select(Object item) {
    if (!(item instanceof Entry))
      return false;
    Entry entry = (Entry)item;
    return LicenseHelper.hasUnspecifiedLicense(entry, true);
  }

}
