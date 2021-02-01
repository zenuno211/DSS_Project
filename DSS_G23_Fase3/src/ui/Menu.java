
package ui;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;



/**
 * Esta classe implementa um menu em modo texto.
 *
 * @author Josá Creissac Campos
 * @version v2.2 (20201208)
 */
public class Menu {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public interface MenuHandler {
        void execute();
    }

    public interface MenuPreCondition {
        boolean validate();
    }



    // Varíavel de classe para suportar leitura
    private static Scanner is = new Scanner(System.in);
    
    // variáveis de instância
    private List<String> opcoes;
    private List<MenuPreCondition> disponivel;  // Lista de pré-condições
    private List<MenuHandler> handlers;         // Lista de handlers

    private int op;

    /**
     * Constructor for objects of class Menu
     */
    public Menu(String[] opcoes) {
        this.opcoes = Arrays.asList(opcoes);
        this.disponivel = new ArrayList<>();
        this.handlers = new ArrayList<>();
        this.opcoes.forEach(s-> {
            this.disponivel.add(()->true);
            this.handlers.add(()->System.out.println("\nATENÇÃO: Opção não implementada!"));
        });

        this.op = 0;
    }

    /**
     * Método para apresentar o menu e ler uma opção.
     */
    public void executa(int k) {
        do {
            showMenu(k);
            this.op = lerOpcao();
            if (op>0 && !this.disponivel.get(op-1).validate()) {
                System.out.println("Opção indisponível! Tente novamente.");
            } else if (op>0) {
                // executar handler
                this.handlers.get(op-1).execute();
            }
        } while (this.op != 0);
    }

    /** Apresenta Título */
    private void showTitle(int i){
        switch (i) {
            case 1:
                System.out.println(ANSI_PURPLE + " __  __                    "  + ANSI_RESET + ANSI_YELLOW + "   _                                           "       + ANSI_RESET);
                System.out.println(ANSI_PURPLE + "|  \\/  | ___ _ __  _   _"    + ANSI_RESET + ANSI_YELLOW + "     / \\   _ __ _ __ ___   __ _ _______ _ __ ___  "   + ANSI_RESET);
                System.out.println(ANSI_PURPLE + "| |\\/| |/ _ \\ '_ \\| | | |" + ANSI_RESET + ANSI_YELLOW + "   / _ \\ | '__| '_ ` _ \\ / _` |_  / _ \\ '_ ` _ \\ " + ANSI_RESET);
                System.out.println(ANSI_PURPLE + "| |  | |  __/ | | | |_| |  "  + ANSI_RESET + ANSI_YELLOW + "/ ___ \\| |  | | | | | | (_| |/ /  __/ | | | | |"      + ANSI_RESET);
                System.out.println(ANSI_PURPLE + "|_|  |_|\\___|_| |_|\\__,_|"  + ANSI_RESET + ANSI_YELLOW + " /_/   \\_\\_|  |_| |_| |_|\\__,_/___\\___|_| |_| |_|" + ANSI_RESET);    
                break;
            case 2:
                System.out.println(ANSI_PURPLE + " __  __                    "  + ANSI_RESET + ANSI_CYAN + " ____           _             "     + ANSI_RESET);
                System.out.println(ANSI_PURPLE + "|  \\/  | ___ _ __  _   _"    + ANSI_RESET + ANSI_CYAN + "   / ___| ___  ___| |_ ___  _ __ "  + ANSI_RESET);
                System.out.println(ANSI_PURPLE + "| |\\/| |/ _ \\ '_ \\| | | |" + ANSI_RESET + ANSI_CYAN + " | |  _ / _ \\/ __| __/ _ \\| '__|" + ANSI_RESET);
                System.out.println(ANSI_PURPLE + "| |  | |  __/ | | | |_| | "   + ANSI_RESET + ANSI_CYAN + "| |_| |  __/\\__ \\ || (_) | |   "  + ANSI_RESET);
                System.out.println(ANSI_PURPLE + "|_|  |_|\\___|_| |_|\\__,_|"  + ANSI_RESET + ANSI_CYAN + "  \\____|\\___||___/\\__\\___/|_| " + ANSI_RESET);
                break;
            case 3:
                System.out.println(ANSI_PURPLE + " __  __                    "  + ANSI_RESET + ANSI_RED + "____       _           _   "        + ANSI_RESET);
                System.out.println(ANSI_PURPLE + "|  \\/  | ___ _ __  _   _"    + ANSI_RESET + ANSI_RED + "  |  _ \\ ___ | |__   ___ | |_ "    + ANSI_RESET);
                System.out.println(ANSI_PURPLE + "| |\\/| |/ _ \\ '_ \\| | | |" + ANSI_RESET + ANSI_RED + " | |_) / _ \\| '_ \\ / _ \\| __|"   + ANSI_RESET);
                System.out.println(ANSI_PURPLE + "| |  | |  __/ | | | |_| | "   + ANSI_RESET + ANSI_RED + "|  _ < (_) | |_) | (_) | |_ "       + ANSI_RESET);
                System.out.println(ANSI_PURPLE + "|_|  |_|\\___|_| |_|\\__,_|"  + ANSI_RESET + ANSI_RED + " |_| \\_\\___/|_.__/ \\___/ \\__|"  + ANSI_RESET);
                break;
            case 4:
                System.out.println(ANSI_PURPLE + " __  __                    "  + ANSI_RESET + ANSI_BLUE + "_         _ _             "      + ANSI_RESET);
                System.out.println(ANSI_PURPLE + "|  \\/  | ___ _ __  _   _"    + ANSI_RESET + ANSI_BLUE + "  | |    ___(_) |_ ___  _ __ "   + ANSI_RESET);
                System.out.println(ANSI_PURPLE + "| |\\/| |/ _ \\ '_ \\| | | |" + ANSI_RESET + ANSI_BLUE + " | |   / _ \\ | __/ _ \\| '__|"  + ANSI_RESET);
                System.out.println(ANSI_PURPLE + "| |  | |  __/ | | | |_| | "   + ANSI_RESET + ANSI_BLUE + "| |__|  __/ | || (_) | |   "     + ANSI_RESET);
                System.out.println(ANSI_PURPLE + "|_|  |_|\\___|_| |_|\\__,_|"  + ANSI_RESET + ANSI_BLUE + " |_____\\___|_|\\__\\___/|_|   " + ANSI_RESET);
                break;
            default:
                break;
        }
    }

    /** Apresentar o menu */
    private void showMenu(int k) {
        showTitle(k);
        for (int i=0; i<this.opcoes.size(); i++) {
            System.out.print(i+1);
            System.out.print(" - ");
            System.out.println(this.opcoes.get(i));
        }
        System.out.println("0 - Sair.");
    }

    /** Ler uma opção válida */
    private int lerOpcao() {
        int op;
        //Scanner is = new Scanner(System.in);

        System.out.print("Opção: ");
        op = is.nextInt();
        while ( op < 0 || op > this.opcoes.size()) {
            System.out.println("Opção Inválida!!!");
            op = lerOpcao();
        }
        return op;
    }

    /**
     * Método para obter a última opção lida
     */
    public int getOpcao() {
        return this.op;
    }

    /**
     * Método que regista uma uma pré-condição numa opção do menu.
     *
     * @param i índice da opção (começa em 1)
     * @param b pré-condição a registar
     */
    public void setPreCondition(int i, MenuPreCondition b) {
        this.disponivel.set(i-1,b);
    }

    /**
     * Método para registar um handler numa opção do menu.
     *
     * @param i indice da opção  (começa em 1)
     * @param h handlers a registar
     */
    public void setHandler(int i, MenuHandler h) {
        this.handlers.set(i-1, h);
    }
}
