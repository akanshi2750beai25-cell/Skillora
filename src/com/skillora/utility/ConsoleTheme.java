package com.skillora.utility;

/**
 * Standardized ANSI terminal formatting, theme colors, banners, and borders.
 */
public class ConsoleTheme {

    // ANSI Escape Codes
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";

    // Palette Colors
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String WHITE = "\u001B[37m";

    private static final String LINE = "================================================================================";
    private static final String SUB_LINE = "--------------------------------------------------------------------------------";

    public static void printBanner() {
        System.out.println(BLUE + BOLD + LINE);
        System.out.println("  ███████╗██╗  ██╗██╗██╗     ██╗      ██████╗ ██████╗  █████╗ ");
        System.out.println("  ██╔════╝██║ ██╔╝██║██║     ██║     ██╔═══██╗██╔══██╗██╔══██╗");
        System.out.println("  ███████╗█████╔╝ ██║██║     ██║     ██║   ██║██████╔╝███████║");
        System.out.println("  ╚════██║██╔═██╗ ██║██║     ██║     ██║   ██║██╔══██╗██╔══██║");
        System.out.println("  ███████║██║  ██╗██║███████╗███████╗╚██████╔╝██║  ██║██║  ██║");
        System.out.println("  ╚══════╝╚═╝  ╚═╝╚═╝╚══════╝╚══════╝ ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝");
        System.out.println(CYAN + "     Skillora - Student Registration, Authentication & Profile System" + WHITE);
        System.out.println(BLUE + BOLD + LINE + RESET);
    }

    public static void printHeader(String title) {
        System.out.println("\n" + BLUE + BOLD + LINE);
        System.out.println("  " + title.toUpperCase());
        System.out.println(LINE + RESET);
    }

    public static void printSubHeader(String title) {
        System.out.println("\n" + CYAN + BOLD + ">> " + title + RESET);
        System.out.println(CYAN + SUB_LINE + RESET);
    }

    public static void printSuccess(String message) {
        System.out.println(GREEN + BOLD + "[SUCCESS] " + RESET + GREEN + message + RESET);
    }

    public static void printWarning(String message) {
        System.out.println(YELLOW + BOLD + "[WARNING] " + RESET + YELLOW + message + RESET);
    }

    public static void printError(String message) {
        System.out.println(RED + BOLD + "[ERROR] " + RESET + RED + message + RESET);
    }

    public static void printInfo(String message) {
        System.out.println(CYAN + "[INFO] " + RESET + WHITE + message + RESET);
    }

    public static void printDivider() {
        System.out.println(BLUE + SUB_LINE + RESET);
    }
}
