package com.xyt.msgprocessor.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.xyt.msgprocess.util.StringUtils;

public class StringUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsBlank() {
		String input = "";
		assertTrue(StringUtils.isBlank(input));

		input = "   ";
		assertTrue(StringUtils.isBlank(input));

		input = null;
		assertTrue(StringUtils.isBlank(input));

		input = "123";
		assertFalse(StringUtils.isBlank(input));

		input = " 123 ";
		assertFalse(StringUtils.isBlank(input));
	}

	@Test
	public void testIsNotBlank() {
		String input = "";
		assertFalse(StringUtils.isNotBlank(input));

		input = "   ";
		assertFalse(StringUtils.isNotBlank(input));

		input = null;
		assertFalse(StringUtils.isNotBlank(input));

		input = "123";
		assertTrue(StringUtils.isNotBlank(input));

		input = " 123 ";
		assertTrue(StringUtils.isNotBlank(input));
	}

	@Test
	public void testTrimToEmpty() {
		String input = "";
		assertEquals("", StringUtils.trimToEmpty(input));

		input = null;
		assertEquals("", StringUtils.trimToEmpty(input));

		input = "   ";
		assertEquals("", StringUtils.trimToEmpty(input));

		input = "123";
		assertEquals("123", StringUtils.trimToEmpty(input));

		input = " 123 ";
		assertEquals("123", StringUtils.trimToEmpty(input));

	}

}
