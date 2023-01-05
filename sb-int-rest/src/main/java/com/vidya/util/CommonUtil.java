package com.vidya.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.messaging.MessageHeaders;

public class CommonUtil {

	public static Map<String, Object> extractAllHeaders(MessageHeaders headers) {
		if (null != headers && !headers.isEmpty()) {
			return headers.entrySet().parallelStream().collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		}
		return Map.of();
	}
}