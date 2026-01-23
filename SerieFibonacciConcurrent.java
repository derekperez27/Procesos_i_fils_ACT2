package procesos;

import java.util.Scanner;

/*
 SERIE DE FIBONACCI CONCURRENT
 - Rebre per consola el nombre de fils a crear.
 - Cada fil calcula Fibonacci(index) segons el seu índex (fil 0 -> F(0)).
 - El main imprimeix els resultats en ordre creixent d'índex.
 Implementació mínima: lectura per Scanner a l'inici; fils escriuen en array compartit.
*/
public class SerieFibonacciConcurrent {

    private static long fib(int n) {
        if (n <= 1) return n;
        long a = 0, b = 1;
        for (int i = 2; i <= n; i++) {
            long c = a + b;
            a = b;
            b = c;
        }
        return b;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Numero de fils: ");
        int nThreads;
        try {
            nThreads = sc.nextInt();
            if (nThreads <= 0) throw new NumberFormatException();
        } catch (Exception e) {
            System.out.println("nThreads ha de ser un enter positiu.");
            return;
        }

        if (nThreads - 1 > 92) {
            System.out.println("Limit: indices Fibonacci <= 92 per cabre en long.");
            return;
        }

        final long[] resultats = new long[nThreads];
        Thread[] fils = new Thread[nThreads];

        for (int i = 0; i < nThreads; i++) {
            final int idx = i;
            fils[i] = new Thread(() -> resultats[idx] = fib(idx));
            fils[i].start();
        }

        for (int i = 0; i < nThreads; i++) {
            try { fils[i].join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }

        System.out.println("Fibonacci (0.." + (nThreads-1) + "):");
        for (int i = 0; i < nThreads; i++) System.out.println("F(" + i + ") = " + resultats[i]);
    }
}
