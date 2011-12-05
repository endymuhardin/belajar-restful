/**
 * Copyright (C) 2011 ArtiVisi Intermedia <info@artivisi.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.artivisi.belajar.restful.ui.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RangeTest {

	@Test
	public void testParseRangeNormal() {
		assertEquals(new Range(10,20,null), Range.fromRequestHeader("items=10-20"));
		assertEquals(new Range(10,20,null), Range.fromRequestHeader("items = 10 - 20"));
	}
	
	@Test
	public void testConstructor(){
		assertEquals(new Range(10,20,30L), new Range(10,20,30L));
		assertEquals(new Range(10,30,30L), new Range(10,40,30L));
	}
	
	@Test
	public void testParseRangeNull() {
		String input = null;
		
		Range hasil = Range.fromRequestHeader(input);
		assertEquals(new Range(0,20,null), hasil);
	}
	
	@Test
	public void testParseRangeInvalidString() {
		assertEquals(new Range(0,20,null), Range.fromRequestHeader("halo=10-20"));
		assertEquals(new Range(0,20,null), Range.fromRequestHeader("halo1020"));
	}

	@Test
	public void testParseRangeInvalidNumber() {
		String input = "items=10-2a";
		
		Range hasil = Range.fromRequestHeader(input);
		assertEquals(new Range(0,20,null), hasil);
	}
	
	@Test
	public void testToResponseHeader(){
		Range r = new Range(11,30,100L);
		assertEquals("items 11-30/100", r.toResponseHeader());
	}
}
