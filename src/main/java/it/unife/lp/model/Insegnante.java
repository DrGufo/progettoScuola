package it.unife.lp.model;

import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Insegnante {
    private int id;
    private String nome;
    private String cognome;
    private String materia;
    
    @JsonIgnore
    private List<Corso> corsiInsegnati;
    
    @JsonIgnore
    private List<Integer> corsiInsegnatiIds; // Temporary storage for JSON IDs
    
    @JsonProperty("corsiInsegnati")
    private List<Integer> getCorsiInsegnatiIds() {
        if (corsiInsegnati == null) {
            corsiInsegnati = javafx.collections.FXCollections.observableArrayList();
        }
        return corsiInsegnati.stream().map(Corso::getId).collect(Collectors.toList());
    }
    
    @JsonProperty("corsiInsegnati")
    private void setCorsiInsegnatiIds(List<Integer> ids) {
        this.corsiInsegnatiIds = ids;
    }
    
    @JsonIgnore
    public List<Integer> getTempCorsiInsegnatiIds() {
        return corsiInsegnatiIds;
    }

    public Insegnante() {
        // Default constructor for Jackson
        this.corsiInsegnati = javafx.collections.FXCollections.observableArrayList();
    }

    public Insegnante(int id, String nome, String cognome, String materia, List<Corso> corsiInsegnati) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.materia = materia;
        this.corsiInsegnati = corsiInsegnati;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public StringProperty nomeProperty() {
        return new SimpleStringProperty(nome);
    }
    public String getCognome() {
        return cognome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    public StringProperty cognomeProperty() {
        return new SimpleStringProperty(cognome);
    }
    public String getMateria() {
        return materia;
    }
    public void setMateria(String materia) {
        this.materia = materia;
    }
    public List<Corso> getCorsiInsegnati() {
        return corsiInsegnati;
    }
    public void setCorsiInsegnati(List<Corso> corsiInsegnati) {
        this.corsiInsegnati = corsiInsegnati;
    }
    public String toString() {
        return nome + " " + cognome + " Materia: " + materia;
    }
    public void aggiungiCorso(Corso corso) {
        this.corsiInsegnati.add(corso);
    }
    public void rimuoviCorso(Corso corso) {
        this.corsiInsegnati.remove(corso);
    }
}
