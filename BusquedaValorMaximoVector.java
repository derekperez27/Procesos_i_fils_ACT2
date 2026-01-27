package procesos;

import java.util.Random;
import java.util.Scanner;

/*
 Búsqueda concurrente del valor máximo en un vector.
 Entrada por teclado: tamaño del vector y número de hilos.
 Cada hilo procesa un bloque consecutivo de tamaño floor.
 Si quedan posiciones (resto), las procesa el programa principal.
 Al final se muestra el valor máximo y qué hilo (o el main) lo encontró.
*/
public class BusquedaValorMaximoVector {
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
        int[] maximaPorHilo = new int[nThreads];
        Thread[] hilos = new Thread[nThreads];

        for (int i = 0; i < nThreads; i++) {
            final int idx = i;
            final int start = i * blockSize;
            final int end = start + blockSize; // exclusive
            maximaPorHilo[i] = Integer.MIN_VALUE;
            hilos[i] = new Thread(() -> {
                int localMax = Integer.MIN_VALUE;
                for (int j = start; j < end; j++) {
                    if (vec[j] > localMax) localMax = vec[j];
                }
                maximaPorHilo[idx] = localMax;
            });
            hilos[i].start();
        }

        // main procesa las posiciones sobrantes
        int remainderStart = blockSize * nThreads;
        int mainMax = Integer.MIN_VALUE;
        for (int j = remainderStart; j < size; j++) {
            if (vec[j] > mainMax) mainMax = vec[j];
        }

        // esperar a hilos
        for (int i = 0; i < nThreads; i++) {
            try { hilos[i].join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }

        // determinar máximo global y quién lo encontró
        int globalMax = mainMax;
        String foundBy = "Programa principal";
        for (int i = 0; i < nThreads; i++) {
            if (maximaPorHilo[i] > globalMax) {
                globalMax = maximaPorHilo[i];
                foundBy = "Hilo " + i;
            }
        }

        System.out.println("Vector (primeros 20 valors mostrats si el tamany es gran):");
        for (int i = 0; i < Math.min(size,20); i++) System.out.print(vec[i]+" ");
        System.out.println("\nMaxim global: " + globalMax + " trobat per: " + foundBy);
    }
}

