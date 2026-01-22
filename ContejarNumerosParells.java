package procesos;

import java.util.Random;
import java.util.Scanner;

/*
 Contar números pars en un vector.
 Entrada per teclat: tamaño del vector y número de hilos.
 Cada fil conta pars en su rango; el main sume els resultats.
*/
public class ContejarNumerosParells {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Tamany del vector: ");
        int size = sc.nextInt();
        System.out.print("Numero de fils: ");
        int nThreads = sc.nextInt();
        if (size <= 0 || nThreads <= 0) {
            System.out.println("Entrades han de ser positives.");
            return;
        }

        int[] vec = new int[size];
        Random rnd = new Random();
        for (int i = 0; i < size; i++) vec[i] = rnd.nextInt(1000);

        int blockSize = size / nThreads;
        int[] cuentas = new int[nThreads];
        Thread[] hilos = new Thread[nThreads];

        for (int i = 0; i < nThreads; i++) {
            final int idx = i;
            final int start = i * blockSize;
            final int end = start + blockSize; 
            cuentas[i] = 0;
            hilos[i] = new Thread(() -> {
                int cnt = 0;
                for (int j = start; j < end; j++) if ((vec[j] & 1) == 0) cnt++;
                cuentas[idx] = cnt;
            });
            hilos[i].start();
        }

        // main processa sobrants
        int remainderStart = blockSize * nThreads;
        int mainCnt = 0;
        for (int j = remainderStart; j < size; j++) if ((vec[j] & 1) == 0) mainCnt++;

        // esperar fils i sumar
        int total = mainCnt;
        for (int i = 0; i < nThreads; i++) {
            try { hilos[i].join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            total += cuentas[i];
        }

        System.out.println("Total numeros parells: " + total);
    }
}

