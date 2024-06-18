package pb.ajneb97.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Random;

public class UtilidadesOtros {

    public static String getTiempo(int tiempo) {
        int minutos = tiempo / 60;
        int segundos = tiempo - (minutos * 60);
        String segundosMsg = "";
        String minutosMsg = "";
        if (segundos >= 0 && segundos <= 9) {
            segundosMsg = "0" + segundos;
        } else {
            segundosMsg = segundos + "";
        }

        if (minutos >= 0 && minutos <= 9) {
            minutosMsg = "0" + minutos;
        } else {
            minutosMsg = minutos + "";
        }

        return minutosMsg + ":" + segundosMsg;
    }

    public static int coinsGanados(Player jugador, FileConfiguration config) {
        String coinsString = config.getString("coins_per_kill");
        if (coinsString.contains("-")) {
            String[] separados = coinsString.split("-");
            int num1 = Integer.valueOf(separados[0]);
            int num2 = Integer.valueOf(separados[1]);
            return getNumeroAleatorio(num1, num2);
        } else {
            return Integer.valueOf(coinsString);
        }
    }

    public static int getNumeroAleatorio(int min, int max) {
        Random r = new Random();
        int numero = r.nextInt((max - min) + 1) + min;
        return numero;
    }

    public static void generarParticula(String particle, Location loc, float xOffset, float yOffset, float zOffset, float speed, int count) {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();

        // TODO
//        public void generarParticula(String particle, Location loc, float xOffset, float yOffset, float zOffset, float speed, int count) {
//            loc.getWorld().spawnParticle(Particle.valueOf(particle), loc, count, xOffset, yOffset, zOffset, speed);
//        }

    }

    public static boolean pasaConfigInventario(Player jugador, FileConfiguration config) {
        if (config.getString("empty_inventory_to_join").equals("true")) {
            PlayerInventory inv = jugador.getInventory();
            for (ItemStack item : inv.getContents()) {
                if (item != null && !item.getType().equals(Material.AIR)) {
                    return false;
                }
            }
            for (ItemStack item : inv.getArmorContents()) {
                if (item != null && !item.getType().equals(Material.AIR)) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}
