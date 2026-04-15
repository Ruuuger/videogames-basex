package videogames.model;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Videojoc {
    private String id;
    private String estat;
    private String titol;
    private String desenvolupador;
    private double preu;
    private List<String> plataformes;
    private int anyLlancament;

    public Videojoc(String id, String estat, String titol, String desenvolupador, double preu,
                    List<String> plataformes, int anyLlancament) {
        this.id = id;
        this.estat = estat;
        this.titol = titol;
        this.desenvolupador = desenvolupador;
        this.preu = preu;
        this.plataformes = plataformes == null ? new ArrayList<>() : new ArrayList<>(plataformes);
        this.anyLlancament = anyLlancament;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEstat() {
        return estat;
    }

    public void setEstat(String estat) {
        this.estat = estat;
    }

    public String getTitol() {
        return titol;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }

    public String getDesenvolupador() {
        return desenvolupador;
    }

    public void setDesenvolupador(String desenvolupador) {
        this.desenvolupador = desenvolupador;
    }

    public double getPreu() {
        return preu;
    }

    public void setPreu(double preu) {
        this.preu = preu;
    }

    public List<String> getPlataformes() {
        return new ArrayList<>(plataformes);
    }

    public void setPlataformes(List<String> plataformes) {
        this.plataformes = plataformes == null ? new ArrayList<>() : new ArrayList<>(plataformes);
    }

    public int getAnyLlancament() {
        return anyLlancament;
    }

    public void setAnyLlancament(int anyLlancament) {
        this.anyLlancament = anyLlancament;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ");
        for (String plataforma : plataformes) {
            joiner.add(plataforma);
        }
        return "ID: " + id + System.lineSeparator()
                + "Estat: " + estat + System.lineSeparator()
                + "Títol: " + titol + System.lineSeparator()
                + "Desenvolupador: " + desenvolupador + System.lineSeparator()
                + "Preu: " + String.format("%.2f", preu) + " €" + System.lineSeparator()
                + "Plataformes: " + joiner + System.lineSeparator()
                + "Any de llançament: " + anyLlancament;
    }
}
