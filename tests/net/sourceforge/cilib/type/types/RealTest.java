/*
 * RealTest.java
 * 
 * Created on Jul 19, 2005
 *
 * Copyright (C) 2003, 2004 - CIRG@UP 
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science 
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package net.sourceforge.cilib.type.types;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.sourceforge.cilib.type.types.Real;
import junit.framework.TestCase;


/**
 * 
 * @author Gary Pampara
 */
public class RealTest extends TestCase {
	
	public void testClone() {
		Real r = new Real();
		r.setReal(-10.0);
		
		Real test = r.clone();
		
		assertEquals(r.getReal(), test.getReal());
		assertNotSame(r, test);
	}
	
	public void testEquals() {
		Real i1 = new Real(10.0);
		Real i2 = new Real(10.0);
		Real i3 = new Real(-5.0);
		
		assertTrue(i1.equals(i1));
		assertTrue(i2.equals(i2));
		assertTrue(i3.equals(i3));
		
		assertTrue(i1.equals(i2));
		assertFalse(i1.equals(i3));
		assertTrue(i2.equals(i1));
		assertFalse(i2.equals(i3));
	}
	
	public void testHashCode() {
		Real r = new Real(10.0);
		
		assertEquals(Double.valueOf(10).hashCode(), r.hashCode());
	}
	
	public void testCompareTo() {
		Real r1 = new Real(0.0, 30.0);
		Real r2 = new Real(-30.0, 0.0);
		
		assertEquals(0, r1.compareTo(r1));
		assertEquals(0, r2.compareTo(r2));
		assertEquals(1, r1.compareTo(r2));
		assertEquals(-1, r2.compareTo(r1));
	}
	
	
	public void testGetRepresentation() {
		Real r = new Real(-30.0, 30.0);
		
		assertEquals("R(-30.0,30.0)", r.getRepresentation());
	}
	
	
	/**
	 * 
	 *
	 */
	public void testRandomize() {
		Real r1 = new Real(-30.0, 30.0);
		Real r2 = r1.clone();
		
		assertTrue(r1.getReal() == r2.getReal());
		r1.randomise();
		assertTrue(r1.getReal() != r2.getReal());
	}
	
	
	/**
	 * 
	 *
	 */
	public void testSerialisation() {
		Real r = new Real(55.0);
		double target = r.getReal();
		
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			
			r.serialise(oos);
			oos.close();
			
			byte [] data = bos.toByteArray();
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			
			Real result = new Real();
			result.deserialise(ois);
			
			assertTrue(result instanceof Real);
			assertEquals(target, ((Real) result).getReal());
		}
		catch (IOException e) {
			fail("Serialisation fails for Types.Real!");
		}
		catch (ClassNotFoundException c) {
			fail();
		}
	}
	
}
