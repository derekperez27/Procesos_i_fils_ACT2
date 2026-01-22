package procesos;

import java.util.Scanner;

/*
 CÀLCUL CONCURRENT DE SUMES PARCIALS
 - Rebre un únic argument per línia de comandes: nombre de fils a crear.
 - Cada fil sol·licitarà a l'usuari un enter N i calcularà la suma de 1..N.
 - El main esperarà tots els fils amb join() i mostrarà els resultats en l'ordre de creació.
 Implementació mínima i senzilla: lectura sincronitzada de System.in per evitar races.
*/
public class CalculConcurrentDeSumesParcials {
    private static final Scanner scanner = new Scanner(System.in);

    // lectura sincronitzada per evitar accessos simultanis a System.in
    private static synchronized long llegirEnterNoNegatiu(int threadId) {
        while (true) {
            System.out.print("Hilo " + threadId + ", introdueix N (>=0): ");
            String line = scanner.nextLine();
            try {
                long n = Long.parseLong(line.trim());
                if (n >= 0) return n;
            } catch (NumberFormatException e) {
                // seguir demanant
            }
            System.out.println("Entrada invàlida. Introdueix un enter no negatiu.");
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Ús: java CalculConcurrentDeSumesParcials <nThreads>");
            return;
        }
        int nThreads;
        try {
            nThreads = Integer.parseInt(args[0]);
            if (nThreads <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("nThreads ha de ser un enter positiu.");
            return;
        }

        final long[] resultats = new long[nThreads];
        Thread[] fils = new Thread[nThreads];

        for (int i = 0; i < nThreads; i++) {
            final int idx = i;
            fils[i] = new Thread(() -> {
                long N = llegirEnterNoNegatiu(idx);
                // usar fórmula per eficiència i per no fer bucles llargs
                long suma = N * (N + 1) / 2;
                resultats[idx] = suma;
            });
            fils[i].start();
        }

        // esperar tots els fils en ordre de creació
        for (int i = 0; i < nThreads; i++) {
            try { fils[i].join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }

        System.out.println("Resultats (ordre de creació):");
        for (int i = 0; i < nThreads; i++) {
            System.out.println("Hilo " + i + ": " + resultats[i]);
        }
    }
}
