/**
 * 
 */
package com.nihilent.util;

/**
 * @author hrishikesh.madur
 *
 */
public class MathUtil {
	
	public static int gcd(int firstNumber, int secondNumber) {
	    if (secondNumber == 0) {
	      return firstNumber;
	    }
	    return gcd(secondNumber, firstNumber % secondNumber);
	  }

}
