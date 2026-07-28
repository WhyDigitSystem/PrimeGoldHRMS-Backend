package com.efit.hrms.entity;

public enum Category {

	 NO,YES;

	  public static Category fromString(String value) {
	        if (value == null) return null;
	        switch (value.trim().toUpperCase()) {
	            case "YES":
	            case "Y":
	                return YES;
	            case "NO":
	            case "N":
	                return NO;
	            default:
	                throw new IllegalArgumentException("Invalid Category: " + value);
	        }
	    }
}
