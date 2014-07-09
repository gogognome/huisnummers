package org.gogognome.huisnummers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

// Oplossing gevonden op http://math.stackexchange.com/questions/855300/distribution-of-integer-solution-pairs-x-y-for-2x2-y2-y
public class EquationTest {
	public static void main(String[] args) {
		BigInteger SIX = BigInteger.valueOf(6);
		BigInteger x0 = BigInteger.valueOf(1);
		BigInteger x1 = BigInteger.valueOf(6);
		for (int i = 0; i < 30; i++) {
			BigInteger x = x1.multiply(SIX);
			x = x.subtract(x0);
			BigInteger xx = x.multiply(x);
			BigInteger t = xx.add(xx);
			BigDecimal z = new BigDecimal(t.toString(10));
			BigDecimal minusPHalf = BigDecimal.valueOf(-0.5);
			BigDecimal pHalfPow2 = BigDecimal.valueOf(0.25);
			BigDecimal disc = pHalfPow2.add(z);
			BigDecimal r = minusPHalf.add(bigSqrt(disc));
			String s = r.toString();
			BigInteger y = new BigInteger(s.substring(0, s.indexOf(".")));
			if (verify(x, y)) {
				System.out.println(x + ", " + y);
			} else {
				System.out.println("Incorrect answer: " + x + ", " + y);
			}
			x0 = x1;
			x1 = x;
		}
	}

	private static boolean verify(BigInteger x, BigInteger y) {
		BigInteger a = BigInteger.valueOf(2).multiply(x).multiply(x);
		BigInteger b = y.multiply(y).add(y);
		return a.equals(b);
	}

	// From http://stackoverflow.com/questions/13649703/square-root-of-bigdecimal-in-java
	private static final BigDecimal SQRT_DIG = new BigDecimal(150);
	private static final BigDecimal SQRT_PRE = new BigDecimal(10).pow(SQRT_DIG.intValue());

	/**
	 * Private utility method used to compute the square root of a BigDecimal.
	 *
	 * @author Luciano Culacciatti
	 * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-BigDecimal
	 */
	private static BigDecimal sqrtNewtonRaphson(BigDecimal c, BigDecimal xn, BigDecimal precision) {
		BigDecimal fx = xn.pow(2).add(c.negate());
		BigDecimal fpx = xn.multiply(new BigDecimal(2));
		BigDecimal xn1 = fx.divide(fpx, 2 * SQRT_DIG.intValue(), RoundingMode.HALF_DOWN);
		xn1 = xn.add(xn1.negate());
		BigDecimal currentSquare = xn1.pow(2);
		BigDecimal currentPrecision = currentSquare.subtract(c);
		currentPrecision = currentPrecision.abs();
		if (currentPrecision.compareTo(precision) <= -1) {
			return xn1;
		}
		return sqrtNewtonRaphson(c, xn1, precision);
	}

	/**
	 * Uses Newton Raphson to compute the square root of a BigDecimal.
	 *
	 * @author Luciano Culacciatti
	 * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-BigDecimal
	 */
	public static BigDecimal bigSqrt(BigDecimal c) {
		return sqrtNewtonRaphson(c, new BigDecimal(1), new BigDecimal(1).divide(SQRT_PRE));
	}
}