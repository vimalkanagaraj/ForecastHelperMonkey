package com.helpermonkey.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.olingo.client.api.domain.ClientCollectionValue;
import org.apache.olingo.client.api.domain.ClientComplexValue;
import org.apache.olingo.client.api.domain.ClientEnumValue;
import org.apache.olingo.client.api.domain.ClientProperty;
import org.apache.olingo.client.api.domain.ClientValue;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class PrinterMonkey {

	public PrinterMonkey() throws Exception {
	}

	public static String prettyPrint(Collection<ClientProperty> properties, int level) {
		StringBuilder b = new StringBuilder();

		for (ClientProperty entry : properties) {
			intend(b, level);
			ClientValue value = entry.getValue();
			if (value.isCollection()) {
				ClientCollectionValue cclvalue = value.asCollection();
				b.append(prettyPrint(cclvalue.asJavaCollection(), level + 1));
			} else if (value.isComplex()) {
				ClientComplexValue cpxvalue = value.asComplex();
				b.append(prettyPrint(cpxvalue.asJavaMap(), level + 1));
			} else if (value.isEnum()) {
				ClientEnumValue cnmvalue = value.asEnum();
				b.append(entry.getName()).append(": ");
				b.append(cnmvalue.getValue()).append("\n");
			} else if (value.isPrimitive()) {
				b.append(entry.getName()).append(": ");
				b.append(entry.getValue()).append("\n");
			}
		}
		return b.toString();
	}

	public static void intend(StringBuilder builder, int intendLevel) {
		for (int i = 0; i < intendLevel; i++) {
			builder.append("  ");
		}
	}

	public static String prettyPrint(Map<String, Object> properties, int level) {
		StringBuilder b = new StringBuilder();
		Set<Entry<String, Object>> entries = properties.entrySet();

		for (Entry<String, Object> entry : entries) {
			intend(b, level);
			b.append(entry.getKey()).append(": ");
			Object value = entry.getValue();
			if (value instanceof Map) {
				value = prettyPrint((Map<String, Object>) value, level + 1);
			} else if (value instanceof Calendar) {
				Calendar cal = (Calendar) value;
				value = SimpleDateFormat.getInstance().format(cal.getTime());
			}
			b.append(value).append("\n");
		}
		// remove last line break
		b.deleteCharAt(b.length() - 1);
		return b.toString();
	}

}
