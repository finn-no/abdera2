/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */
package org.apache.abdera2.common.geo;

import java.io.Serializable;
import static com.google.common.base.Preconditions.*;

public class Coordinate 
  implements Serializable, 
             Cloneable, 
             Comparable<Coordinate> {

    public static Coordinate at(double latitude, double longitude) {      
      return new Coordinate(latitude,longitude);
    }
    
    public static Coordinate at(String position) {
      return new Coordinate(position);
    }
    
    public static Coordinate at(IsoPosition position) {
      return new Coordinate(position);
    }
  
    private static final long serialVersionUID = -916272885213668761L;

    private final double latitude;
    private final double longitude;
    
    public Coordinate(IsoPosition pos) {
      this(pos.getLatitude(),pos.getLongitude());
    }

    public Coordinate(double latitude, double longitude) {
      checkArgument(!(Double.compare(latitude, 90.0d) > 0), "Latitude > 90.0 degrees");
      checkArgument(!(Double.compare(latitude, -90.0d) < 0), "Latitude < -90.0 degrees");
      checkArgument(!(Double.compare(longitude, 180.0d) >= 0), "Longitude >= 180.0 degrees");
      checkArgument(!(Double.compare(longitude, -180.0d) <= 0), "Longitude <= -180.0 degrees");
      this.latitude = latitude;
      this.longitude = longitude;
    }

    public Coordinate(String value) {
      Coordinate c = parse(value);
      this.latitude = c.latitude;
      this.longitude = c.longitude;
      checkArgument(!(Double.compare(latitude, 90.0d) > 0), "Latitude > 90.0 degrees");
      checkArgument(!(Double.compare(latitude, -90.0d) < 0), "Latitude < -90.0 degrees");
      checkArgument(!(Double.compare(longitude, 180.0d) >= 0), "Longitude >= 180.0 degrees");
      checkArgument(!(Double.compare(longitude, -180.0d) <= 0), "Longitude <= -180.0 degrees");
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String toString() {
        return Double.toString(latitude) + " " + Double.toString(longitude);
    }
    
    public IsoPosition asIsoPosition() {
      return new IsoPosition(latitude,longitude,0.0);
    }
    
    public String toIsoString() {
        return asIsoPosition().toString();
    }

    public Coordinate clone() {
        try {
            return (Coordinate)super.clone();
        } catch (CloneNotSupportedException e) {
            return new Coordinate(latitude, longitude); // not going to happen
        }
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = PRIME * result + (int)(temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = PRIME * result + (int)(temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Coordinate other = (Coordinate)obj;
        if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude))
            return false;
        if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude))
            return false;
        return true;
    }

    public static Coordinate parse(String value) {
        try {
            String[] points = value.trim().split("\\s+", 2);
            double latitude = Double.parseDouble(points[0].trim());
            double longitude = Double.parseDouble(points[1].trim());
            return new Coordinate(latitude, longitude);
        } catch (Throwable t) {
            throw new RuntimeException("Error parsing coordinate pair", t);
        }
    }
    
    public static Coordinate parseIso(String value) {
      return new Coordinate(IsoPosition.parse(value));
    }

    public int compareTo(Coordinate o) {
        if (o == null || equals(o))
            return 0;
        int l1 = Double.compare(latitude, o.latitude);
        int l2 = Double.compare(longitude, o.longitude);
        if (l1 < 0)
            return -1;
        if (l1 == 0 && l2 < -1)
            return -1;
        if (l1 == 0 && l2 == 0)
            return 0;
        return 1;
    }

}
