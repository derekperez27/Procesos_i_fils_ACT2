package procesos;

/*
 COMPROBACIÓ DE NÚMEROS PRIMERS
 - Rebre per línia de comandes diversos enters; la entrada finalitza amb -1.
 - Crear un fil per cada número (fins a -1) que comprovarà si és primer.
 - El programa principal mostrarà els resultats en el mateix ordre d'entrada.
*/
public class ComprobacioNumerosPrimos {

    private static boolean isPrime(long n) {
        if (n < 2) return false;
        if (n % 2 == 0) return n == 2;
        long r = (long) Math.sqrt(n);
        for (long i = 3; i <= r; i += 2) if (n % i == 0) return false;
        return true;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Ús: java ComprobacioNumerosPrimos <n1> <n2> ... (-1 per acabar)");
            return;
        }

        // comptar nombres fins a -1
        int count = 0;
        for (String s : args) {
            try {
                long v = Long.parseLong(s);
                if (v == -1) break;
                count++;
            } catch (NumberFormatException e) {
                // ignorar entrades no numèriques
            }
        }
        if (count == 0) {
            System.out.println("No hi ha nombres vàlids abans de -1.");
            return;
        }

        final long[] nums = new long[count];
        int idx = 0;
        for (String s : args) {
            try {
                long v = Long.parseLong(s);
                if (v == -1) break;
                nums[idx++] = v;
            } catch (NumberFormatException e) { /* ignorar */ }
        }

        final boolean[] esPrimo = new boolean[count];
        Thread[] fils = new Thread[count];
        for (int i = 0; i < count; i++) {
            final int j = i;
            fils[i] = new Thread(() -> esPrimo[j] = isPrime(nums[j]));
            fils[i].start();
        }

        for (int i = 0; i < count; i++) {
            try { fils[i].join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }

        for (int i = 0; i < count; i++) {
            System.out.println(nums[i] + (esPrimo[i] ? " es primer" : " no es primer"));
        }
    }
}
