package org.apache.abdera2.activities.model.objects;

import java.util.Date;

import org.apache.abdera2.activities.io.gson.Properties;
import org.apache.abdera2.activities.io.gson.Property;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.common.anno.Name;
import org.apache.abdera2.common.iri.IRI;
import org.joda.time.DateTime;

@Name("organization")
@Properties({
  @Property(name="webpage",to=IRI.class),
  @Property(name="endDate",to=Date.class),
  @Property(name="startDate",to=Date.class)
})
public class OrganizationObject extends ASObject {

  private static final long serialVersionUID = -6983890349791967283L;

  public OrganizationObject() {}
  
  public OrganizationObject(String displayName) {
    super(displayName);
  }
  
  public Address getAddress() {
    return getProperty("address");
  }
  
  public void setAddress(Address address) {
    setProperty("address", address);
  }
  
  public String getDepartment() {
    return getProperty("department");
  }
  
  public void setDepartment(String dept) {
    setProperty("department", dept);
  }
 
  public String getDescription() {
    return getProperty("description");
  }
  
  public void setDescription(String description) {
    setProperty("description", description);
  }
  
  public DateTime getEndDate() {
    return getProperty("endDate");
  }
  
  public void setEndDate(DateTime date) {
    setProperty("endDate", date);
  }
  
  public String getField() {
    return getProperty("field");
  }
  
  public void setField(String field) {
    setProperty("field", field);
  }
  
  public String getName() {
    return getProperty("name");
  }
  
  public void setName(String name) {
    setProperty("name", name);
  }
  
  public String getSalary() {
    return getProperty("salary");
  }
  
  public void setSalary(String salary) {
    setProperty("salary", salary);
  }
  
  public DateTime getStartDate() {
    return getProperty("startDate");
  }
  
  public void setStartDate(DateTime date) {
    setProperty("startDate", date);
  }
  
  public String getSubfield() {
    return getProperty("subfield");
  }
  
  public void setSubfield(String val) {
    setProperty("subfield", val);
  }
  
  public String getTitle() {
    return getProperty("title");
  }
  
  public void setTitle(String title) {
    setProperty("title", title);
  }
  
  public String getType() {
    return getProperty("type");
  }
  
  public void setType(String type) {
    setProperty("type", type);
  }
  
  public IRI getWebpage() {
    return getProperty("webpage");
  }
  
  public void setWebpage(IRI val) {
    setProperty("webpage", val);
  }
  
  public void setWebpage(String val) {
    setWebpage(new IRI(val));
  }
  
  public static <T extends OrganizationObject>OrganizationObjectGenerator<T> makeOrganization() {
    return new OrganizationObjectGenerator<T>();
  }
  
  @SuppressWarnings("unchecked")
  public static class OrganizationObjectGenerator<T extends OrganizationObject> extends ASObjectGenerator<T> {
    public OrganizationObjectGenerator() {
      super((Class<? extends T>) OrganizationObject.class);
    }
    public OrganizationObjectGenerator(Class<T> _class) {
      super(_class);
    }
    public <X extends OrganizationObjectGenerator<T>>X address(Address address) {
      item.setAddress(address);
      return (X)this;
    }
    public <X extends OrganizationObjectGenerator<T>>X department(String val) {
      item.setDepartment(val);
      return (X)this;
    }
    public <X extends OrganizationObjectGenerator<T>>X description(String val) {
      item.setDescription(val);
      return (X)this;
    }
    public <X extends OrganizationObjectGenerator<T>>X endDate(DateTime dt) {
      item.setEndDate(dt);
      return (X)this;
    }
    public <X extends OrganizationObjectGenerator<T>>X field(String field) {
      item.setField(field);
      return (X)this;
    }
    public <X extends OrganizationObjectGenerator<T>>X name(String name) {
      item.setName(name);
      return (X)this;
    }
    public <X extends OrganizationObjectGenerator<T>>X salary(String salary) {
      item.setSalary(salary);
      return (X)this;
    }
    public <X extends OrganizationObjectGenerator<T>>X startDate(DateTime dt) {
      item.setStartDate(dt);
      return (X)this;
    }
    public <X extends OrganizationObjectGenerator<T>>X subfield(String val) {
      item.setSubfield(val);
      return (X)this;
    }
    public <X extends OrganizationObjectGenerator<T>>X title(String val) {
      item.setTitle(val);
      return (X)this;
    }
    public <X extends OrganizationObjectGenerator<T>>X type(String val) {
      item.setType(val);
      return (X)this;
    }
    public <X extends OrganizationObjectGenerator<T>>X webpage(String uri) {
      item.setWebpage(uri);
      return (X)this;
    }
    public <X extends OrganizationObjectGenerator<T>>X webpage(IRI uri) {
      item.setWebpage(uri);
      return (X)this;
    }
  }
}
