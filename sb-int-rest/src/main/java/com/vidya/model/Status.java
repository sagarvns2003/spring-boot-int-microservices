package com.vidya.model;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {
	ACTIVE,

	IN_ACTIVE;

	@JsonCreator
	public static Status parse(final String statusString) {
		return Status.valueOf(statusString.toUpperCase(Locale.getDefault()));
	}
}
