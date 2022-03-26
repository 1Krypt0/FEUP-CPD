package MatrixProduct;

import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.Math.min;

public class MatrixProduct {

    static int getDimensions(Scanner scanner){
        System.out.println("Dimensions: lins=cols ? ");
        int retVal = scanner.nextInt();
        scanner.nextLine();
        return retVal;
    }
    static void onMult(int m_ar, int m_br) {

        long time1, time2;
        double temp;
        int i, j, k;

        double[] pha, phb, phc;

        pha = new double[m_ar * m_ar];
        phb = new double[m_ar * m_ar];
        phc = new double[m_ar * m_ar];

        for (i = 0; i < m_ar; i++) {
            for (j = 0; j < m_ar; j++) {
                pha[i * m_ar + j] = 1.0;
            }
        }

        for (i = 0; i < m_br; i++) {
            for (j = 0; j < m_br; j++) {
                phb[i * m_br + j] = (double) i+1;
            }
        }

        time1 = System.currentTimeMillis();

        for (i = 0; i < m_ar; i++) {
            for (j = 0; j < m_br; j++) {
                temp = 0;
                for (k = 0; k < m_ar; k++) {
                    temp += pha[i * m_ar + k] * phb[k * m_br + j];
                }
                phc[i * m_ar + j] = temp;
            }
        }

        time2 = System.currentTimeMillis();

        System.out.println("Time: " + (time2 - time1) + " milliseconds");

        System.out.println("Result Matrix:");
        for (i = 0; i < min(10, m_br); i++) {
            System.out.print(phc[i] + " ");
        }
        System.out.print("\n");
    }

    static void onMultLine(int m_ar, int m_br) {
        long time1, time2;
        int i, j, k;
        double temp;

        double[] pha = new double[m_ar * m_ar];
        double[] phb = new double[m_ar * m_ar];
        double[] phc = new double[m_ar * m_ar];

        for (i = 0; i < m_ar; i++) {
            for (j = 0; j < m_ar; j++) {
                pha[i * m_ar + j] = (double) 1.0;
            }
        }

        for (i = 0; i < m_br; i++) {
            for (j = 0; j < m_br; j++) {
               phb[i * m_br + j] = (double) (i + 1);
            }
        }

        for (i = 0; i < m_ar; i++) {
            for (j = 0; j < m_ar; j++) {
                phc[i * m_ar + j] = (double) 0;
            }
        }

        time1 = System.currentTimeMillis();

        for (i = 0; i < m_ar; i++) {
            for (j = 0; j < m_br; j++) {
                for (k = 0; k < m_ar; k++) {
                    phc[i * m_ar + k] += pha[i * m_ar + j] * phb[j * m_ar + k];
                }
            }
        }

        time2 = System.currentTimeMillis();

        System.out.println("Time: " + (time2 - time1) + " milliseconds");

        System.out.println("Result Matrix:");
        for (i = 0; i < min(10, m_br); i++) {
            System.out.print(phc[i] + " ");
        }
        System.out.print("\n");
    }

    static void onMultBlock(int m_ar, int m_br, int bkSize) {
        long time1, time2;
        int i, j, k;

        double[] pha = new double[m_ar * m_ar];
        double[] phb = new double[m_ar * m_ar];
        double[] phc = new double[m_ar * m_ar];

        for (i = 0; i < m_ar; i++) {
            for (j = 0; j < m_br; j++) {
                phc[i * m_ar + j] = 0;
            }
        }

        for (i = 0; i < m_ar; i++) {
            for (j = 0; j < m_br; j++) {
                pha[i * m_ar + j] = (double) 1.0;
            }
        }

        for (i = 0; i < m_br; i++) {
            for (j = 0; j < m_br; j++) {
                phb[i * m_br + j] = (double) (i + 1);
            }
        }

        time1 = System.currentTimeMillis();

        for (int jj = 0; jj < m_ar; jj += bkSize) {
            for (int kk = 0; kk < m_ar; kk += bkSize) {
                for (i = 0; i < m_ar; i++) {
                    for (j = jj; j < (Math.min((jj + bkSize), m_ar)); j++) {
                        for (k = kk; k < (Math.min((kk + bkSize), m_ar)); k++) {
                            phc[i * m_ar + k] += pha[i * m_ar + j] * phb[j * m_br + k];
                        }
                    }
                }
            }
        }

        time2 = System.currentTimeMillis();

        System.out.println("Time: " + (time2 - time1) + " milliseconds");

        System.out.println("Result Matrix:");
        for (i = 0; i < min(10, m_br); i++) {
            System.out.print(phc[i] + " ");
        }
        System.out.print("\n");

    }



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int op = -1;
        do {
            try {
                System.out.println("1. Multiplication");
                System.out.println("2. Line Multiplication");
                System.out.println("3. Block Multiplication");
                System.out.print("Selection?: ");
                op = scanner.nextInt();
                scanner.nextLine();
                switch (op) {
                    case 1:
                        op = getDimensions(scanner);
                        onMult(op, op);
                        break;
                    case 2:
                        op = getDimensions(scanner);
                        System.out.println("missing implementation");
                        break;
                    case 3:
                        //IMPLEMENT
                        System.out.println("missing implementation");
                        break;
                    default:
                        return;
                }
                onMult(600, 600);
            } catch (InputMismatchException e) {
                System.out.println("You must input a valid integer.");
                scanner.next();
            }
        } while (op != 0);
        scanner.close();
        return;
    }
}
