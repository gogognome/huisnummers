package org.gogognome.huisnummers;

import java.math.BigInteger;

/**
 * Dit algoritme is gebaseerd op de volgende gelijkheid:
 *
 * 1 + ... + (n-1) = (n+1) + ... + m
 *
 * Pas Gauss toe.
 *
 * 1/2 * n * (n-1) = 1/2 * (n+1+m) * (m-n)
 *
 * Herschrijven resulteert in:
 *
 * n*n = 1/2 * (m*m) + 1/2 * m
 *
 * Voor oplopende m wordt n*n uitgerekend. Als n dan een geheel getal blijkt te zijn hebben we een oplossing gevonden: huisnummer n in een straat met nummers 1
 * tot en met m.
 *
 * @author Sander Kooijmans
 */
public class Huisnummers {

	public static void main(String[] args) {
		int nrThreads = Runtime.getRuntime().availableProcessors() / 2;
		nrThreads = 1;
		for (int i = 0; i < nrThreads; i++) {
			new HuisnummerZoekThread((i + 1), nrThreads).start();
		}
	}

	private static class HuisnummerZoekThread extends Thread {

		private final int start;
		private final int delta;

		public HuisnummerZoekThread(int start, int delta) {
			this.start = start;
			this.delta = delta;
		}

		@Override
		public void run() {
			long m = start;
			// Above a limit an overflow occurs in the calculations. After this limit has been reached, switch to the slower BigIntegers.
			long limitForLongs = (long) Math.sqrt(Long.MAX_VALUE);
			limitForLongs = 1;
			while (m < limitForLongs) {
				long nSquared = ((m * m) + m) / 2;
				long n = (long) (Math.sqrt(nSquared) + 0.5);
				if (n * n == nSquared) {
					assertCorrect(n, m);
					System.out.println(String.format("%d %d", n, m));
				}
				m += delta;
			}

			System.out.println("Switching to BigInteger");

			// findNumbers2(BigInteger.valueOf((long) Math.sqrt(m)), BigInteger.valueOf(m), BigInteger.valueOf(delta));
			findNumbers2(new BigInteger("7134842596"), new BigInteger("10090191164"), BigInteger.valueOf(1));
			findNumbers(new BigInteger("7134842596"), new BigInteger("10090191164"), BigInteger.valueOf(1));
		}

		private void findNumbers(BigInteger bigN, BigInteger bigM, BigInteger bigDelta) {
			long nextLoggingTimestamp = System.currentTimeMillis() + 60000;
			while (true) {
				BigInteger nSquared = ((bigM.multiply(bigM)).add(bigM)).shiftRight(1);
				while (bigN.multiply(bigN).compareTo(nSquared) < 0) {
					bigN = bigN.add(BigInteger.ONE);
				}
				if (bigN.multiply(bigN).compareTo(nSquared) == 0) {
					System.out.println(String.format("OPLOSSING GEVONDEN: %s %s", bigN.toString(), bigM.toString()));
				}
				bigM = bigM.add(bigDelta);

				long now = System.currentTimeMillis();
				if (now >= nextLoggingTimestamp) {
					System.out.println("bezig met n: " + bigN + ", m: " + bigM);
					nextLoggingTimestamp = now + 60000;
				}
			}
		}

		private void findNumbers2(BigInteger bigN, BigInteger bigM, BigInteger bigDelta) {
			long nextLoggingTimestamp = System.currentTimeMillis() + 60000;
			while (true) {
				BigInteger nSquaredTimesTwo = bigN.multiply(bigN).shiftLeft(1);
				while (bigM.multiply(bigM.add(BigInteger.ONE)).compareTo(nSquaredTimesTwo) < 0) {
					bigM = bigM.add(BigInteger.ONE);
				}
				if (bigM.multiply(bigM.add(BigInteger.ONE)).compareTo(nSquaredTimesTwo) == 0) {
					System.out.println(String.format("OPLOSSING GEVONDEN: %s %s", bigN.toString(), bigM.toString()));
				}
				bigN = bigN.add(bigDelta);

				long now = System.currentTimeMillis();
				if (now >= nextLoggingTimestamp) {
					System.out.println("bezig met n: " + bigN + ", m: " + bigM);
					nextLoggingTimestamp = now + 60000;
				}
			}
		}

	}

	private final static BigInteger sqrt(BigInteger n) {
		BigInteger a = BigInteger.ONE;
		BigInteger b = n.shiftRight(5).add(BigInteger.valueOf(8));
		while (b.compareTo(a) >= 0) {
			BigInteger mid = a.add(b).shiftRight(1);
			if (mid.multiply(mid).compareTo(n) > 0)
				b = mid.subtract(BigInteger.ONE);
			else
				a = mid.add(BigInteger.ONE);
		}
		return a.subtract(BigInteger.ONE);
	}

	private static void assertCorrect(long n, long m) {
		long total1 = 0;
		long total2 = 0;
		for (long i = 1; i < n; i++) {
			total1 += i;
		}
		for (long i = n + 1; i <= m; i++) {
			total2 += i;
		}

		if (total1 != total2) {
			System.out.println(String.format("Solution %d %d is incorrect: %d != %d", n, m, total1, total2));
		}
	}
}
