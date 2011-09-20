package org.apache.abdera2.test.common.geo;

import org.apache.abdera2.common.geo.Box;
import org.apache.abdera2.common.geo.Coordinate;
import org.apache.abdera2.common.geo.Coordinates;
import org.apache.abdera2.common.geo.IsoPosition;
import org.apache.abdera2.common.geo.Line;
import org.apache.abdera2.common.geo.Multiple;
import org.apache.abdera2.common.geo.Point;
import org.apache.abdera2.common.geo.Polygon;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GeoTest {

  @Test
  public void isoPositionTest() {
    IsoPosition iso = IsoPosition.parse("+12.1234567+123.1234567+123.1234567");
    assertEquals(12.1234567,iso.getLatitude(),0);
    assertEquals(123.1234567,iso.getLongitude(),0);
    assertEquals(123.1234567,iso.getAltitude(),0);
    assertEquals("+12.123457+123.123457+123.123457/", iso.toString());
  }
  
  @Test
  public void boxTest() {
    Coordinate c1 = new Coordinate(1.0,2.0);
    Coordinate c2 = new Coordinate(3.0,4.0);
    Box box = new Box(c1,c2);
    assertEquals(c1,box.getLowerCorner());
    assertEquals(c2,box.getUpperCorner());
  }
  
  @Test
  public void coordinateTest() {
    Coordinate c1 = new Coordinate(1.0,2.0);
    assertEquals(1.0,c1.getLatitude(),0);
    assertEquals(2.0,c1.getLongitude(),0);
  }
  
  @Test
  public void coordinatesTest() {
    Coordinates cs = new Coordinates();
    cs.add(new Coordinate(1.0,2.0));
    cs.add(new Coordinate(3.0,4.0));
    cs.add(new Coordinate(5.0,6.0));
    assertEquals(3,cs.size());
    assertTrue(cs.contains(3.0,4.0));
    cs.remove(1.0,2.0);
    assertFalse(cs.contains(1.0,2.0));
    assertEquals(2,cs.size());
  }
  
  @Test
  public void lineTest() {
    Coordinates cs = new Coordinates();
    cs.add(new Coordinate(1.0,2.0));
    cs.add(new Coordinate(3.0,4.0));
    cs.add(new Coordinate(5.0,6.0));
    Line line = new Line(cs);
    line.verify();
  }
  
  @SuppressWarnings("unused")
  @Test
  public void multipleTest() {
    Coordinates cs = new Coordinates();
    cs.add(new Coordinate(1.0,2.0));
    cs.add(new Coordinate(3.0,4.0));
    cs.add(new Coordinate(5.0,6.0));
    Multiple mult = new Multiple(cs) {
      private static final long serialVersionUID = 2214118656567387273L;};
    int n = 0;
    for (Coordinate c : mult) n++;
    assertEquals(3,n);
  }
  
  @Test
  public void pointTest() {
    Coordinate c = new Coordinate(1.0,2.0);
    Point point = new Point(c);
    assertEquals(1.0,point.getCoordinate().getLatitude(),0);
  }
  
  @Test
  public void polygonTest() {
    Coordinates cs = new Coordinates();
    cs.add(new Coordinate(1.0,2.0));
    cs.add(new Coordinate(3.0,4.0));
    cs.add(new Coordinate(5.0,6.0));
    Polygon poly = new Polygon(cs);
    poly.verify();
  }
  
}
