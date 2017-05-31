package com.xyt.msgprocess.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.math.BigDecimal;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NotificationTest {
	private Notification oldNoti;

	@Before
	public void setUp() throws Exception {
		oldNoti = new Notification();
		oldNoti.setProductType("cherry");
		oldNoti.setValue(new BigDecimal(20));
		oldNoti.setOccurrence(5);
		Adjustment adj = new Adjustment();
		adj.setOperation(Operation.ADD);
		adj.setValue(new BigDecimal(10));
		oldNoti.setAdjustment(adj);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testClone() {

		Notification newNoti = oldNoti.clone();
		assertNotSame(oldNoti, newNoti);

		assertEquals(oldNoti.getProductType(), newNoti.getProductType());
		assertEquals(oldNoti.getValue(), newNoti.getValue());
		assertEquals(oldNoti.getOccurrence(), newNoti.getOccurrence());
		assertEquals(oldNoti.getAdjustment().getOperation(), newNoti.getAdjustment().getOperation());
		assertEquals(oldNoti.getAdjustment().getValue(), newNoti.getAdjustment().getValue());
	}

	@Test
	public void testClone_with_param_Number_more_than_0() {
		List<Notification> newNotiList = oldNoti.clone(5);

		Assert.assertNotNull(newNotiList);
		assertEquals(5, newNotiList.size());

		for (Notification newNoti : newNotiList) {
			assertEquals(oldNoti.getProductType(), newNoti.getProductType());
			assertEquals(oldNoti.getValue(), newNoti.getValue());
			assertEquals(oldNoti.getOccurrence(), newNoti.getOccurrence());
			assertEquals(oldNoti.getAdjustment().getOperation(), newNoti.getAdjustment().getOperation());
			assertEquals(oldNoti.getAdjustment().getValue(), newNoti.getAdjustment().getValue());
		}

	}

	@Test
	public void testClone_with_param_Number_equal_0() {
		List<Notification> newNotiList = oldNoti.clone(0);
		Assert.assertNotNull(newNotiList);
		assertEquals(0, newNotiList.size());

	}

	@Test
	public void testClone_with_param_Number_less_than_0() {
		List<Notification> newNotiList = oldNoti.clone(-1);
		Assert.assertNotNull(newNotiList);
		assertEquals(0, newNotiList.size());
	}
}
