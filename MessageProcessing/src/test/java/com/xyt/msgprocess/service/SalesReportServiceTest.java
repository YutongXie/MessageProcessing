package com.xyt.msgprocess.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.xyt.msgprocess.entity.Adjustment;
import com.xyt.msgprocess.entity.Constants;
import com.xyt.msgprocess.entity.Operation;
import com.xyt.msgprocess.entity.Notification;

public class SalesReportServiceTest {
	private SalesReportService service;

	@Before
	public void setUp() throws Exception {
		service = SalesReportService.getInstance();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetAdjustmentTitle() {
		String title = service.getAdjustmentTitle();

		boolean hasTwoLines = title.split(Constants.LINE_END_INDICATOR).length == 2;
		assertTrue(hasTwoLines);

		boolean containProductType = title.indexOf("ProductType") >= 0;
		assertTrue(containProductType);

		boolean containValue = title.indexOf("Operation") > 0;
		assertTrue(containValue);

		boolean containNumber = title.indexOf("Value") > 0;
		assertTrue(containNumber);

	}

	@Test
	public void testGetSummaryTitle() {
		String title = service.getSummaryTitle();

		boolean hasTwoLines = title.split(Constants.LINE_END_INDICATOR).length == 2;

		assertTrue(hasTwoLines);

		boolean containProductType = title.indexOf("ProductType") >= 0;
		assertTrue(containProductType);

		boolean containValue = title.indexOf("TotalValue") > 0;
		assertTrue(containValue);

		boolean containNumber = title.indexOf("Number") > 0;
		assertTrue(containNumber);
	}

	@Test
	public void testGetSummaryBody_single() {

		String productType = "cherry";
		int count = 1;
		int value = 20;

		Notification noti = mock(Notification.class);
		when(noti.getProductType()).thenReturn(productType);
		when(noti.getValue()).thenReturn(new BigDecimal(value));

		List<Notification> list = mock(List.class);
		when(list.size()).thenReturn(count);
		Iterator<Notification> mockIter = mock(Iterator.class);
		when(list.iterator()).thenReturn(mockIter);
		when(mockIter.hasNext()).thenReturn(true).thenReturn(false);
		when(mockIter.next()).thenReturn(noti);

		Iterator<String> mockSetIter = mock(Iterator.class);
		when(mockSetIter.hasNext()).thenReturn(true).thenReturn(false);
		when(mockSetIter.next()).thenReturn(productType);

		Set<String> set = mock(Set.class);
		when(set.iterator()).thenReturn(mockSetIter);

		HashMap<String, List<Notification>> map = mock(HashMap.class);
		when(map.keySet()).thenReturn(set);
		when(map.get(anyString())).thenReturn(list);

		String body = service.getSummaryBody(map);
		StringBuffer expected = new StringBuffer();
		expected.append(productType).append(Constants.REPORT_FORMAT_INTERVAL_SPACE).append(count)
				.append(Constants.REPORT_FORMAT_INTERVAL_SPACE).append(value).append(Constants.LINE_END_INDICATOR);

		assertEquals(expected.toString(), body);
	}

	@Test
	public void testGetSummaryBody_multiple() {

		String productType_1 = "cherry";
		int value_1 = 20;

		String productType_2 = "apply";
		int value_2 = 15;

		int count = 1;

		Notification noti1 = mock(Notification.class);
		when(noti1.getProductType()).thenReturn(productType_1);
		when(noti1.getValue()).thenReturn(new BigDecimal(value_1));

		Notification noti2 = mock(Notification.class);
		when(noti2.getProductType()).thenReturn(productType_2);
		when(noti2.getValue()).thenReturn(new BigDecimal(value_2));

		List<Notification> list_1 = mock(List.class);
		when(list_1.size()).thenReturn(count);
		Iterator<Notification> mockIter_1 = mock(Iterator.class);
		when(list_1.iterator()).thenReturn(mockIter_1);
		when(mockIter_1.hasNext()).thenReturn(true).thenReturn(false);
		when(mockIter_1.next()).thenReturn(noti1);

		List<Notification> list_2 = mock(List.class);
		when(list_2.size()).thenReturn(count);
		Iterator<Notification> mockIter_2 = mock(Iterator.class);
		when(list_2.iterator()).thenReturn(mockIter_2);
		when(mockIter_2.hasNext()).thenReturn(true).thenReturn(false);
		when(mockIter_2.next()).thenReturn(noti2);

		Iterator<String> mockSetIter = mock(Iterator.class);
		when(mockSetIter.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
		when(mockSetIter.next()).thenReturn(productType_1).thenReturn(productType_2);

		Set<String> set = mock(Set.class);
		when(set.iterator()).thenReturn(mockSetIter);

		HashMap<String, List<Notification>> map = mock(HashMap.class);
		when(map.keySet()).thenReturn(set);
		when(map.get(anyString())).thenReturn(list_1).thenReturn(list_2);

		String body = service.getSummaryBody(map);
		StringBuffer expected = new StringBuffer();
		expected.append(productType_1).append(Constants.REPORT_FORMAT_INTERVAL_SPACE).append(count)
				.append(Constants.REPORT_FORMAT_INTERVAL_SPACE).append(value_1).append(Constants.LINE_END_INDICATOR);
		expected.append(productType_2).append(Constants.REPORT_FORMAT_INTERVAL_SPACE).append(count)
				.append(Constants.REPORT_FORMAT_INTERVAL_SPACE).append(value_2).append(Constants.LINE_END_INDICATOR);

		assertEquals(expected.toString(), body);
	}

	@Test
	public void testGetSummaryBody_none() {

		Iterator<String> mockSetIter = mock(Iterator.class);
		when(mockSetIter.hasNext()).thenReturn(false);

		Set<String> set = mock(Set.class);
		when(set.iterator()).thenReturn(mockSetIter);

		HashMap<String, List<Notification>> map = mock(HashMap.class);
		when(map.keySet()).thenReturn(set);

		String body = service.getSummaryBody(map);
		assertEquals("", body);
	}

	@Test
	public void testGetAdjustmentBody_has_single_adjustment() {

		String productType = "cherry";
		int value = 10;
		Operation oper = Operation.ADD;

		Adjustment adj = mock(Adjustment.class);
		when(adj.getValue()).thenReturn(new BigDecimal(value));
		when(adj.getOperation()).thenReturn(oper);

		Notification noti = mock(Notification.class);
		when(noti.getProductType()).thenReturn(productType);
		when(noti.getValue()).thenReturn(new BigDecimal(value));
		when(noti.getAdjustment()).thenReturn(adj);

		Iterator<Notification> notiIter = mock(Iterator.class);
		when(notiIter.hasNext()).thenReturn(true).thenReturn(false);
		when(notiIter.next()).thenReturn(noti);
		List<Notification> list = mock(List.class);
		when(list.iterator()).thenReturn(notiIter);

		HashMap<String, List<Notification>> map = mock(HashMap.class);
		Iterator<String> iter = mock(Iterator.class);
		when(iter.next()).thenReturn(productType);
		when(iter.hasNext()).thenReturn(true).thenReturn(false);

		Set<String> set = mock(Set.class);
		when(set.iterator()).thenReturn(iter);

		when(map.keySet()).thenReturn(set);
		when(map.get(anyString())).thenReturn(list);
		String body = service.getAdjustmentBody(map);

		StringBuffer expected = new StringBuffer();
		expected.append(productType).append(Constants.REPORT_FORMAT_INTERVAL_SPACE);
		expected.append(oper).append(Constants.REPORT_FORMAT_INTERVAL_SPACE);
		expected.append(value).append(Constants.LINE_END_INDICATOR);

		assertEquals(expected.toString(), body);
	}

	@Test
	public void testGetAdjustmentBody_has_multiple_adjustment() {

		String productType_1 = "cherry";
		int adjValue_1 = 10;
		Operation oper_1 = Operation.ADD;

		String productType_2 = "apply";
		int adjValue_2 = 5;
		Operation oper_2 = Operation.MULTIPLY;

		Adjustment adj_1 = mock(Adjustment.class);
		when(adj_1.getValue()).thenReturn(new BigDecimal(adjValue_1));
		when(adj_1.getOperation()).thenReturn(oper_1);

		Adjustment adj_2 = mock(Adjustment.class);
		when(adj_2.getValue()).thenReturn(new BigDecimal(adjValue_2));
		when(adj_2.getOperation()).thenReturn(oper_2);

		Notification noti_1 = mock(Notification.class);
		when(noti_1.getProductType()).thenReturn(productType_1);
		when(noti_1.getValue()).thenReturn(new BigDecimal(adjValue_1));
		when(noti_1.getAdjustment()).thenReturn(adj_1);

		Notification noti_2 = mock(Notification.class);
		when(noti_2.getProductType()).thenReturn(productType_2);
		when(noti_2.getValue()).thenReturn(new BigDecimal(adjValue_2));
		when(noti_2.getAdjustment()).thenReturn(adj_2);

		Iterator<Notification> notiIter_1 = mock(Iterator.class);
		when(notiIter_1.hasNext()).thenReturn(true).thenReturn(false);
		when(notiIter_1.next()).thenReturn(noti_1);

		Iterator<Notification> notiIter_2 = mock(Iterator.class);
		when(notiIter_2.hasNext()).thenReturn(true).thenReturn(false);
		when(notiIter_2.next()).thenReturn(noti_2);

		List<Notification> list_1 = mock(List.class);
		when(list_1.iterator()).thenReturn(notiIter_1);

		List<Notification> list_2 = mock(List.class);
		when(list_2.iterator()).thenReturn(notiIter_2);

		HashMap<String, List<Notification>> map = mock(HashMap.class);
		Iterator<String> iter = mock(Iterator.class);
		when(iter.next()).thenReturn(productType_1).thenReturn(productType_2);
		when(iter.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);

		Set<String> set = mock(Set.class);
		when(set.iterator()).thenReturn(iter);

		when(map.keySet()).thenReturn(set);
		when(map.get(anyString())).thenReturn(list_1).thenReturn(list_2);
		String body = service.getAdjustmentBody(map);

		StringBuffer expected = new StringBuffer();
		expected.append(productType_1).append(Constants.REPORT_FORMAT_INTERVAL_SPACE);
		expected.append(oper_1).append(Constants.REPORT_FORMAT_INTERVAL_SPACE);
		expected.append(adjValue_1).append(Constants.LINE_END_INDICATOR);

		expected.append(productType_2).append(Constants.REPORT_FORMAT_INTERVAL_SPACE);
		expected.append(oper_2).append(Constants.REPORT_FORMAT_INTERVAL_SPACE);
		expected.append(adjValue_2).append(Constants.LINE_END_INDICATOR);
		assertEquals(expected.toString(), body);
	}

	@Test
	public void testGetAdjustmentBody_no_adjustment() {

		String productType = "cherry";
		int value = 10;

		Notification noti = mock(Notification.class);
		when(noti.getProductType()).thenReturn(productType);
		when(noti.getValue()).thenReturn(new BigDecimal(value));
		when(noti.getAdjustment()).thenReturn(null);

		Iterator<Notification> notiIter = mock(Iterator.class);
		when(notiIter.hasNext()).thenReturn(true).thenReturn(false);
		when(notiIter.next()).thenReturn(noti);
		List<Notification> list = mock(List.class);
		when(list.iterator()).thenReturn(notiIter);

		HashMap<String, List<Notification>> map = mock(HashMap.class);
		Iterator<String> iter = mock(Iterator.class);
		when(iter.next()).thenReturn(productType);
		when(iter.hasNext()).thenReturn(true).thenReturn(false);

		Set<String> set = mock(Set.class);
		when(set.iterator()).thenReturn(iter);

		when(map.keySet()).thenReturn(set);
		when(map.get(anyString())).thenReturn(list);
		String body = service.getAdjustmentBody(map);

		assertEquals("", body);
	}

}
