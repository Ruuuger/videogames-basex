package videogames.ui;

import videogames.dao.BaseXVideojocDAOImpl;
import videogames.dao.VideojocDAO;
import videogames.db.BaseXConnection;
import videogames.model.Videojoc;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MainApp {

    public static void main(String[] args) {
        BaseXConnection connection = null;
        try (Scanner scanner = new Scanner(System.in)) {
            connection = new BaseXConnection();
            VideojocDAO dao = new BaseXVideojocDAOImpl(connection.getContext());

            boolean sortir = false;
            while (!sortir) {
                mostrarMenu();
                String opcio = scanner.nextLine().trim();
                try {
                    switch (opcio) {
                        case "1":
                            llistarTot(dao);
                            break;
                        case "2":
                            cercarPerId(dao, scanner);
                            break;
                        case "3":
                            afegirVideojoc(dao, scanner);
                            break;
                        case "4":
                            afegirPlataforma(dao, scanner);
                            break;
                        case "5":
                            modificarPreu(dao, scanner);
                            break;
                        case "6":
                            modificarEstat(dao, scanner);
                            break;
                        case "7":
                            eliminarVideojoc(dao, scanner);
                            break;
                        case "0":
                            sortir = true;
                            break;
                        default:
                            System.out.println("Opció no vàlida.");
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("No s'ha pogut iniciar l'aplicació: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.close();
            }
            System.out.println("Aplicació finalitzada.");
        }
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("=== GESTOR DE CATÀLEG DE VIDEOJOCS ===");
        System.out.println("1. Llistar tots els videojocs");
        System.out.println("2. Cercar videojoc per ID");
        System.out.println("3. Afegir nou videojoc");
        System.out.println("4. Afegir plataforma a un videojoc");
        System.out.println("5. Modificar preu d'un videojoc");
        System.out.println("6. Modificar estat d'un videojoc");
        System.out.println("7. Eliminar un videojoc");
        System.out.println("0. Sortir");
        System.out.print("Selecciona una opció: ");
    }

    private static void llistarTot(VideojocDAO dao) throws Exception {
        List<Videojoc> videojocs = dao.llistarTot();
        if (videojocs.isEmpty()) {
            System.out.println("No hi ha videojocs al catàleg.");
            return;
        }
        for (Videojoc videojoc : videojocs) {
            System.out.println(videojoc);
            System.out.println("-----------------------------");
        }
    }

    private static void cercarPerId(VideojocDAO dao, Scanner scanner) throws Exception {
        System.out.print("Introdueix l'ID: ");
        String id = scanner.nextLine().trim();
        Videojoc videojoc = dao.cercarPerId(id);
        if (videojoc == null) {
            System.out.println("No trobat");
        } else {
            System.out.println(videojoc);
        }
    }

    private static void afegirVideojoc(VideojocDAO dao, Scanner scanner) throws Exception {
        System.out.print("ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Estat: ");
        String estat = scanner.nextLine().trim();
        System.out.print("Títol: ");
        String titol = scanner.nextLine().trim();
        System.out.print("Desenvolupador: ");
        String desenvolupador = scanner.nextLine().trim();
        System.out.print("Preu: ");
        double preu = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Plataformes (separades per coma): ");
        List<String> plataformes = Arrays.stream(scanner.nextLine().split(","))
                .map(String::trim)
                .filter(p -> !p.isEmpty())
                .collect(Collectors.toList());
        System.out.print("Any de llançament: ");
        int anyLlancament = Integer.parseInt(scanner.nextLine().trim());

        dao.afegirVideojoc(new Videojoc(id, estat, titol, desenvolupador, preu, plataformes, anyLlancament));
        System.out.println("Videojoc afegit correctament.");
    }

    private static void afegirPlataforma(VideojocDAO dao, Scanner scanner) throws Exception {
        System.out.print("ID del videojoc: ");
        String id = scanner.nextLine().trim();
        System.out.print("Nova plataforma: ");
        String plataforma = scanner.nextLine().trim();
        dao.afegirPlataforma(id, plataforma);
        System.out.println("Plataforma afegida correctament.");
    }

    private static void modificarPreu(VideojocDAO dao, Scanner scanner) throws Exception {
        System.out.print("ID del videojoc: ");
        String id = scanner.nextLine().trim();
        System.out.print("Nou preu: ");
        double preu = Double.parseDouble(scanner.nextLine().trim());
        dao.modificarPreu(id, preu);
        System.out.println("Preu modificat correctament.");
    }

    private static void modificarEstat(VideojocDAO dao, Scanner scanner) throws Exception {
        System.out.print("ID del videojoc: ");
        String id = scanner.nextLine().trim();
        System.out.print("Nou estat: ");
        String estat = scanner.nextLine().trim();
        dao.modificarEstat(id, estat);
        System.out.println("Estat modificat correctament.");
    }

    private static void eliminarVideojoc(VideojocDAO dao, Scanner scanner) throws Exception {
        System.out.print("ID del videojoc a eliminar: ");
        String id = scanner.nextLine().trim();
        System.out.print("Segur que vols eliminar-lo? (s/n): ");
        String confirmacio = scanner.nextLine().trim();
        if ("s".equalsIgnoreCase(confirmacio)) {
            dao.eliminarVideojoc(id);
            System.out.println("Videojoc eliminat correctament.");
        } else {
            System.out.println("Operació cancel·lada.");
        }
    }
}
