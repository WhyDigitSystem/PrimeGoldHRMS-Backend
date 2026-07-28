package com.efit.hrms.entity;

public enum OverTime {
 NO,YES;

	  public static OverTime fromString(String value) {
	        if (value == null) return null;
	        switch (value.trim().toUpperCase()) {
	            case "YES":
	            case "Y":
	                return YES;
	            case "NO":
	            case "N":
	                return NO;
	            default:
	                throw new IllegalArgumentException("Invalid OT Flag: " + value);
	        }
	    }
	  
	  
}
