package com.xyt.msgprocess.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.xyt.msgprocess.entity.Adjustment;
import com.xyt.msgprocess.entity.Constants;
import com.xyt.msgprocess.entity.Notification;
import com.xyt.msgprocess.entity.Operation;

public class SalesNotificationServiceTest {

	@Before
	public void setUp() {
	}

	@Test
	public void testIsBatchMode_occurence_is_zero() throws Exception {
		Method method = SalesNotificationService.class.getDeclaredMethod("isBatchMode", Notification.class);
		method.setAccessible(true);
		Notification noti = new Notification();
		Object[] args = { noti };
		Boolean flag = (Boolean) method.invoke(SalesNotificationService.getInstance(), args);
		assertFalse(flag);

	}

	@Test
	public void testIsBatchMode_occurence_more_than_zero() throws Exception {
		Method method = SalesNotificationService.class.getDeclaredMethod("isBatchMode", Notification.class);
		method.setAccessible(true);
		Notification noti = new Notification();
		noti.setOccurrence(1);
		Object[] args = { noti };
		Boolean flag = (Boolean) method.invoke(SalesNotificationService.getInstance(), args);
		assertTrue(flag);

	}

	@Test
	public void testIsBatchMode_occurence_less_than_zero() throws Exception {
		Method method = SalesNotificationService.class.getDeclaredMethod("isBatchMode", Notification.class);
		method.setAccessible(true);
		Notification noti = new Notification();
		noti.setOccurrence(-1);
		Object[] args = { noti };
		Boolean flag = (Boolean) method.invoke(SalesNotificationService.getInstance(), args);
		assertFalse(flag);

	}

	@Test
	public void testIsAdjustmentMode_Noti_with_adjustment() throws Exception {
		Method method = SalesNotificationService.class.getDeclaredMethod("isAdjustmentMode", Notification.class);
		method.setAccessible(true);
		Notification noti = new Notification();
		noti.setAdjustment(new Adjustment());

		Object[] args = { noti };
		Boolean flag = (Boolean) method.invoke(SalesNotificationService.getInstance(), args);
		assertTrue(flag);
	}

	@Test
	public void testIsAdjustmentMode_Noti_without_adjustment() throws Exception {
		Method method = SalesNotificationService.class.getDeclaredMethod("isAdjustmentMode", Notification.class);
		method.setAccessible(true);
		Notification noti = new Notification();

		Object[] args = { noti };
		Boolean flag = (Boolean) method.invoke(SalesNotificationService.getInstance(), args);
		assertFalse(flag);
	}

	@Test
	public void testIsTimeToPausing_msgCount_equal_PAUSING_NO() throws Exception {
		SalesNotificationService instance = SalesNotificationService.getInstance();
		Field field = SalesNotificationService.class.getDeclaredField("msgCount");
		field.setAccessible(true);
		field.setInt(instance, Constants.PAUSING_NO);

		Method method = SalesNotificationService.class.getDeclaredMethod("isTimeToPausing", null);
		method.setAccessible(true);
		Object[] args = {};
		Boolean flag = (Boolean) method.invoke(instance, args);
		assertTrue(flag);
	}

	@Test
	public void testIsTimeToPausing_msgCount_not_equal_PAUSING_NO() throws Exception {
		SalesNotificationService instance = SalesNotificationService.getInstance();
		Field field = SalesNotificationService.class.getDeclaredField("msgCount");
		field.setAccessible(true);
		field.setInt(instance, Constants.PAUSING_NO - 1);

		Method method = SalesNotificationService.class.getDeclaredMethod("isTimeToPausing", null);
		method.setAccessible(true);
		Object[] args = {};
		Boolean flag = (Boolean) method.invoke(instance, args);
		assertFalse(flag);
	}

	@Test
	public void testIsTimeToReportDetail_multiple_of_REPORT_CYCLE_NO() throws Exception {
		SalesNotificationService instance = SalesNotificationService.getInstance();
		Field field = SalesNotificationService.class.getDeclaredField("msgCount");
		field.setAccessible(true);
		field.setInt(instance, Constants.REPORT_CYCLE_NO * 2);

		Method method = SalesNotificationService.class.getDeclaredMethod("isTimeToReportDetail", null);
		method.setAccessible(true);
		Object[] args = {};
		Boolean flag = (Boolean) method.invoke(instance, args);
		assertTrue(flag);

	}

	@Test
	public void testIsTimeToReportDetail_equal_REPORT_CYCLE_NO() throws Exception {
		SalesNotificationService instance = SalesNotificationService.getInstance();
		Field field = SalesNotificationService.class.getDeclaredField("msgCount");
		field.setAccessible(true);
		field.setInt(instance, Constants.REPORT_CYCLE_NO);

		Method method = SalesNotificationService.class.getDeclaredMethod("isTimeToReportDetail", null);
		method.setAccessible(true);
		Object[] args = {};
		Boolean flag = (Boolean) method.invoke(instance, args);
		assertTrue(flag);

	}

	@Test
	public void testIsTimeToReportDetail_not_equal_or_multiple_of_REPORT_CYCLE_NO() throws Exception {
		SalesNotificationService instance = SalesNotificationService.getInstance();
		Field field = SalesNotificationService.class.getDeclaredField("msgCount");
		field.setAccessible(true);
		field.setInt(instance, Constants.REPORT_CYCLE_NO + 1);

		Method method = SalesNotificationService.class.getDeclaredMethod("isTimeToReportDetail", null);
		method.setAccessible(true);
		Object[] args = {};
		Boolean flag = (Boolean) method.invoke(instance, args);
		assertFalse(flag);

	}

	@Test
	public void testIncreaseMsgCount() throws Exception {
		int initValue = 100;
		SalesNotificationService instance = SalesNotificationService.getInstance();
		Field field = SalesNotificationService.class.getDeclaredField("msgCount");
		field.setAccessible(true);
		field.setInt(instance, initValue);
		Method method = SalesNotificationService.class.getDeclaredMethod("increaseMsgCount", null);
		method.setAccessible(true);
		Object[] args = {};
		method.invoke(instance, args);

		int afterIncrease = field.getInt(instance);
		assertEquals(101, afterIncrease);
	}

	@Test
	public void testAdjustValue_No_exist_match_productyType() throws Exception {
		SalesNotificationService instance = SalesNotificationService.getInstance();
		Method method = SalesNotificationService.class.getDeclaredMethod("adjustValue", Notification.class,
				String.class);
		method.setAccessible(true);

		HashMap<String, List<Notification>> map = new HashMap<String, List<Notification>>();

		List<Notification> list = new ArrayList<Notification>();
		Notification existNoti = new Notification();
		existNoti.setProductType("Cherry");
		existNoti.setValue(new BigDecimal(20));
		list.add(existNoti);

		Field field = SalesNotificationService.class.getDeclaredField("map");
		field.setAccessible(true);
		field.set(instance, map);

		Notification noti = new Notification();
		Adjustment adj = new Adjustment();
		adj.setOperation(Operation.ADD);
		adj.setValue(new BigDecimal(1.0));

		noti.setAdjustment(adj);
		Object[] args = { noti, "Apple" };
		method.invoke(instance, args);

		List<Notification> after = map.get("Apple");
		assertNull(after);

	}

	@Test
	public void testAdjustValue_exist_match_productType_but_adjustValue_is_Null() throws Exception {
		SalesNotificationService instance = SalesNotificationService.getInstance();
		Method method = SalesNotificationService.class.getDeclaredMethod("adjustValue", Notification.class,
				String.class);
		method.setAccessible(true);

		HashMap<String, List<Notification>> map = new HashMap<String, List<Notification>>();

		int existNotiValue = 20;
		String productType = "Cherry";
		List<Notification> list = new ArrayList<Notification>();
		Notification existNoti = new Notification();
		existNoti.setProductType(productType);
		existNoti.setValue(new BigDecimal(existNotiValue));
		list.add(existNoti);

		map.put(productType, list);
		Field field = SalesNotificationService.class.getDeclaredField("map");
		field.setAccessible(true);
		field.set(instance, map);

		Notification noti = new Notification();
		Adjustment adj = new Adjustment();
		adj.setOperation(Operation.ADD);

		noti.setAdjustment(adj);
		Object[] args = { noti, productType };
		method.invoke(instance, args);

		List<Notification> after = map.get(productType);
		assertEquals(1, after.size());

		Notification afterNoti = after.get(0);
		assertEquals(existNotiValue, afterNoti.getValue().intValue());

	}

	@Test
	public void testAdjustValue_exist_match_productType_with_ADD_Operation() throws Exception {
		SalesNotificationService instance = SalesNotificationService.getInstance();
		Method method = SalesNotificationService.class.getDeclaredMethod("adjustValue", Notification.class,
				String.class);
		method.setAccessible(true);

		HashMap<String, List<Notification>> map = new HashMap<String, List<Notification>>();

		String productType = "Cherry";
		int existNotiValue = 20;
		List<Notification> list = new ArrayList<Notification>();
		Notification existNoti = new Notification();
		existNoti.setProductType(productType);
		existNoti.setValue(new BigDecimal(existNotiValue));
		list.add(existNoti);

		map.put(productType, list);
		Field field = SalesNotificationService.class.getDeclaredField("map");
		field.setAccessible(true);
		field.set(instance, map);

		int adjValue = 1;
		Notification noti = new Notification();
		Adjustment adj = new Adjustment();
		adj.setOperation(Operation.ADD);
		adj.setValue(new BigDecimal(adjValue));

		noti.setAdjustment(adj);
		Object[] args = { noti, productType };
		method.invoke(instance, args);

		List<Notification> after = map.get(productType);
		assertEquals(1, after.size());

		Notification afterNoti = after.get(0);
		assertEquals(existNotiValue + adjValue, afterNoti.getValue().intValue());

	}

	@Test
	public void testAdjustValue_exist_match_productType_with_SUBSTRACT_Operation() throws Exception {
		SalesNotificationService instance = SalesNotificationService.getInstance();
		Method method = SalesNotificationService.class.getDeclaredMethod("adjustValue", Notification.class,
				String.class);
		method.setAccessible(true);

		HashMap<String, List<Notification>> map = new HashMap<String, List<Notification>>();

		String productType = "Cherry";
		int existNotiValue = 20;
		List<Notification> list = new ArrayList<Notification>();
		Notification existNoti = new Notification();
		existNoti.setProductType(productType);
		existNoti.setValue(new BigDecimal(existNotiValue));
		list.add(existNoti);

		map.put(productType, list);
		Field field = SalesNotificationService.class.getDeclaredField("map");
		field.setAccessible(true);
		field.set(instance, map);

		int adjValue = 1;
		Notification noti = new Notification();
		Adjustment adj = new Adjustment();
		adj.setOperation(Operation.SUBSTRACT);
		adj.setValue(new BigDecimal(adjValue));

		noti.setAdjustment(adj);
		Object[] args = { noti, productType };
		method.invoke(instance, args);

		List<Notification> after = map.get(productType);
		assertEquals(1, after.size());

		Notification afterNoti = after.get(0);
		assertEquals(existNotiValue - adjValue, afterNoti.getValue().intValue());

	}

	@Test
	public void testAdjustValue_exist_match_productType_with_MULTIPLY_Operation() throws Exception {
		SalesNotificationService instance = SalesNotificationService.getInstance();
		Method method = SalesNotificationService.class.getDeclaredMethod("adjustValue", Notification.class,
				String.class);
		method.setAccessible(true);

		HashMap<String, List<Notification>> map = new HashMap<String, List<Notification>>();

		String productType = "Cherry";
		int existNotiValue = 20;
		List<Notification> list = new ArrayList<Notification>();
		Notification existNoti = new Notification();
		existNoti.setProductType(productType);
		existNoti.setValue(new BigDecimal(existNotiValue));
		list.add(existNoti);

		map.put(productType, list);
		Field field = SalesNotificationService.class.getDeclaredField("map");
		field.setAccessible(true);
		field.set(instance, map);

		int adjValue = 2;
		Notification noti = new Notification();
		Adjustment adj = new Adjustment();
		adj.setOperation(Operation.MULTIPLY);
		adj.setValue(new BigDecimal(adjValue));

		noti.setAdjustment(adj);
		Object[] args = { noti, productType };
		method.invoke(instance, args);

		List<Notification> after = map.get(productType);
		assertEquals(1, after.size());

		Notification afterNoti = after.get(0);
		assertEquals(existNotiValue * adjValue, afterNoti.getValue().intValue());

	}

	@Test
	public void testStore_exist_same_product_in_map() throws Exception {
		SalesNotificationService instance = SalesNotificationService.getInstance();
		String existProductType = "Cherry";
		int existNotiValue = 20;
		List<Notification> list = new ArrayList<Notification>();

		Notification existNoti = new Notification();
		existNoti.setProductType(existProductType);
		existNoti.setValue(new BigDecimal(existNotiValue));
		list.add(existNoti);

		Method method = SalesNotificationService.class.getDeclaredMethod("store", Notification.class);
		method.setAccessible(true);

		HashMap<String, List<Notification>> map = new HashMap<String, List<Notification>>();
		Field field = SalesNotificationService.class.getDeclaredField("map");
		field.setAccessible(true);
		map.put(existProductType, list);
		field.set(instance, map);

		int newNotiValue = 105;
		Notification newNoti = new Notification();
		newNoti.setProductType(existProductType);
		newNoti.setValue(new BigDecimal(newNotiValue));

		Object[] args = { newNoti };

		method.invoke(instance, args);

		assertEquals(1, map.size());
		List<Notification> after = map.get(existProductType);
		assertEquals(2, after.size());

		Notification afterNoti = after.get(1);
		assertEquals(newNotiValue, afterNoti.getValue().intValue());

	}

	@Test
	public void testStore_exist_same_product_in_map_with_batch_mode() throws Exception {
		SalesNotificationService instance = SalesNotificationService.getInstance();
		String existProductType = "Cherry";
		int existNotiValue = 20;
		List<Notification> list = new ArrayList<Notification>();

		Notification existNoti = new Notification();
		existNoti.setProductType(existProductType);
		existNoti.setValue(new BigDecimal(existNotiValue));
		list.add(existNoti);

		Method method = SalesNotificationService.class.getDeclaredMethod("store", Notification.class);
		method.setAccessible(true);

		HashMap<String, List<Notification>> map = new HashMap<String, List<Notification>>();
		Field field = SalesNotificationService.class.getDeclaredField("map");
		field.setAccessible(true);
		map.put(existProductType, list);
		field.set(instance, map);

		int newNotiValue = 105;
		int occurrence = 2;
		Notification newNoti = new Notification();
		newNoti.setProductType(existProductType);
		newNoti.setValue(new BigDecimal(newNotiValue));
		newNoti.setOccurrence(occurrence);

		Object[] args = { newNoti };

		method.invoke(instance, args);

		assertEquals(1, map.size());
		List<Notification> after = map.get(existProductType);
		assertEquals(1 + occurrence, after.size());

		Notification afterNoti = after.get(1);
		assertEquals(newNotiValue, afterNoti.getValue().intValue());
		afterNoti = after.get(2);
		assertEquals(newNotiValue, afterNoti.getValue().intValue());
	}

	@Test
	public void testStore_exist_same_product_in_map_with_adjustment_mode() throws Exception {
		SalesNotificationService instance = SalesNotificationService.getInstance();
		String existProductType = "Cherry";
		int existNotiValue = 20;
		List<Notification> list = new ArrayList<Notification>();

		Notification existNoti = new Notification();
		existNoti.setProductType(existProductType);
		existNoti.setValue(new BigDecimal(existNotiValue));
		list.add(existNoti);

		Method method = SalesNotificationService.class.getDeclaredMethod("store", Notification.class);
		method.setAccessible(true);

		HashMap<String, List<Notification>> map = new HashMap<String, List<Notification>>();
		Field field = SalesNotificationService.class.getDeclaredField("map");
		field.setAccessible(true);
		map.put(existProductType, list);
		field.set(instance, map);

		int newNotiValue = 105;
		int adjValue = 2;
		Notification newNoti = new Notification();
		newNoti.setProductType(existProductType);
		newNoti.setValue(new BigDecimal(newNotiValue));
		Adjustment adj = new Adjustment();
		adj.setOperation(Operation.ADD);
		adj.setValue(new BigDecimal(adjValue));
		newNoti.setAdjustment(adj);

		Object[] args = { newNoti };

		method.invoke(instance, args);

		assertEquals(1, map.size());
		List<Notification> after = map.get(existProductType);
		assertEquals(2, after.size());

		Notification afterNoti = after.get(0);
		assertEquals(existNotiValue + adjValue, afterNoti.getValue().intValue());

		afterNoti = after.get(1);
		assertEquals(newNotiValue, afterNoti.getValue().intValue());
	}

	@Test
	public void testStore_not_exist_same_product_in_map() throws Exception {
		SalesNotificationService instance = SalesNotificationService.getInstance();
		String existProductType = "Cherry";
		int existNotiValue = 20;
		List<Notification> list = new ArrayList<Notification>();

		Notification existNoti = new Notification();
		existNoti.setProductType(existProductType);
		existNoti.setValue(new BigDecimal(existNotiValue));
		list.add(existNoti);

		Method method = SalesNotificationService.class.getDeclaredMethod("store", Notification.class);
		method.setAccessible(true);

		HashMap<String, List<Notification>> map = new HashMap<String, List<Notification>>();
		Field field = SalesNotificationService.class.getDeclaredField("map");
		field.setAccessible(true);
		map.put(existProductType, list);
		field.set(instance, map);

		int newNotiValue = 105;
		String newNotiProductType = "Apple";
		Notification newNoti = new Notification();
		newNoti.setProductType(newNotiProductType);
		newNoti.setValue(new BigDecimal(newNotiValue));

		Object[] args = { newNoti };

		method.invoke(instance, args);

		assertEquals(2, map.size());
		assertEquals(1, map.get(existProductType).size());
		assertEquals(1, map.get(newNotiProductType).size());

		List<Notification> after = map.get(newNotiProductType);
		assertEquals(1, after.size());

		Notification afterNoti = after.get(0);
		assertEquals(newNotiValue, afterNoti.getValue().intValue());

	}

	@Test
	public void testStore_not_exist_same_product_in_map_with_batch_mode() throws Exception {
		SalesNotificationService instance = SalesNotificationService.getInstance();
		String existProductType = "Cherry";
		int existNotiValue = 20;
		List<Notification> list = new ArrayList<Notification>();

		Notification existNoti = new Notification();
		existNoti.setProductType(existProductType);
		existNoti.setValue(new BigDecimal(existNotiValue));
		list.add(existNoti);

		Method method = SalesNotificationService.class.getDeclaredMethod("store", Notification.class);
		method.setAccessible(true);

		HashMap<String, List<Notification>> map = new HashMap<String, List<Notification>>();
		Field field = SalesNotificationService.class.getDeclaredField("map");
		field.setAccessible(true);
		map.put(existProductType, list);
		field.set(instance, map);

		int newNotiValue = 105;
		int occurrence = 2;
		String newNotiProductType = "Apple";
		Notification newNoti = new Notification();
		newNoti.setProductType(newNotiProductType);
		newNoti.setValue(new BigDecimal(newNotiValue));
		newNoti.setOccurrence(occurrence);

		Object[] args = { newNoti };

		method.invoke(instance, args);

		assertEquals(2, map.size());
		assertEquals(1, map.get(existProductType).size());

		List<Notification> after = map.get(newNotiProductType);
		assertEquals(occurrence, after.size());

		Notification afterNoti = after.get(0);
		assertEquals(newNotiValue, afterNoti.getValue().intValue());
		afterNoti = after.get(1);
		assertEquals(newNotiValue, afterNoti.getValue().intValue());
	}

}
