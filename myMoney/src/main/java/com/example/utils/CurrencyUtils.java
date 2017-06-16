package com.example.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public final class CurrencyUtils {

	private CurrencyUtils(){
		
	}
	
	public static String format(BigDecimal value, String symbol) {
		if(value == null){
			return "";
		}
		NumberFormat currencyInstance = NumberFormat.getCurrencyInstance();
		DecimalFormatSymbols symbols = ((DecimalFormat)currencyInstance).getDecimalFormatSymbols();
		symbols.setCurrencySymbol(symbol);
		((DecimalFormat)currencyInstance).setDecimalFormatSymbols(symbols);
		return currencyInstance.format(value);
	}

}
