package com.example.calculators;

import java.math.BigDecimal;

public final class TargetCalculator {

	private TargetCalculator() {
		
	}
	
	public static BigDecimal calculate(String startegy, BigDecimal sum, BigDecimal target) {
		if(sum == null || target == null){
			return null;
		}
		ITargetCalculatorStrategy strategy = getCurrentStrategy(startegy);
		BigDecimal calculate = strategy.calculate(sum, target);
		return calculate;
	}

	public static ITargetCalculatorStrategy getCurrentStrategy(String startegy) {
		ITargetCalculatorStrategy strategy = TargetCalculatorStrategies.getStrategy(startegy);
		return strategy;
	}

	public interface ITargetCalculatorStrategy{
		BigDecimal calculate(BigDecimal sum,BigDecimal target);
	}
	
	public enum TargetCalculatorStrategies implements ITargetCalculatorStrategy{
		JUST_TARGET("justTarget"){
			@Override
			public BigDecimal calculate(BigDecimal sum,BigDecimal target) {
				return target;
			}
		},
		TARGET_MINUS_CURR("target_minus_current"){
			@Override
			public BigDecimal calculate(BigDecimal sum,BigDecimal target) {
				return target.subtract(sum);
			}
		},
		EMPTY(""){
			@Override
			public BigDecimal calculate(BigDecimal sum,BigDecimal target) {
				return null;
			}
		};

		private String name;

		TargetCalculatorStrategies(String name) {
			this.name = name;
		}
		
		public static ITargetCalculatorStrategy getStrategy(String startegy) {
			for(TargetCalculatorStrategies st : values()){
				if(st.name.equals(startegy)){
					return st;
				}
			}
			return EMPTY;
		}
		
	}
}
