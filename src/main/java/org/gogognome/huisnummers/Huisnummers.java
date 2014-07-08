package org.gogognome.huisnummers;

public class Huisnummers {

	public static void main(String[] args) {
		long m = 1;
		while (true) {
			long nSquared = ((m * m) + m) / 2;
			long n = (long) (Math.sqrt(nSquared) + 0.5);
			if (n * n == nSquared) {
				// assertCorrect(n, m);
				System.out.println(String.format("%d %d", n, m));
			}
			m++;
		}
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
