package it.unife.lp.model;

import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Studente {
    private int id;
    private String nome;
    private String cognome;
    private String dataNascita;
    private String classe;
    
    @JsonIgnore
    private List<Corso> corsi;
    
    @JsonIgnore
    private List<Integer> corsiIds; // Temporary storage for JSON IDs
    
    @JsonProperty("corsi")
    private List<Integer> getCorsiIds() {
        if (corsi == null) {
            corsi = javafx.collections.FXCollections.observableArrayList();
        }
        return corsi.stream().map(Corso::getId).collect(Collectors.toList());
    }
    
    @JsonProperty("corsi")
    private void setCorsiIds(List<Integer> ids) {
        this.corsiIds = ids;
    }
    
    @JsonIgnore
    public List<Integer> getTempCorsiIds() {
        return corsiIds;
    }

    public Studente() {
        // Default constructor for Jackson
        this.corsi = javafx.collections.FXCollections.observableArrayList();
    }

    public Studente(int id, String nome, String cognome, String dataNascita, String classe, List<Corso> corsi) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.classe = classe;
        this.corsi = corsi;
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
    public String getDataNascita() {
        return dataNascita;
    }
    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }
    public String getClasse() {
        return classe;
    }
    public void setClasse(String classe) {
        this.classe = classe;
    }
    public List<Corso> getCorsi() {
        return corsi;
    }
    public void setCorsi(List<Corso> corsi) {
        this.corsi = corsi;
    }

    public String toString() {
        return nome + " " + cognome + " Nato il: " + dataNascita + " Classe: " + classe;
    }

    public void aggiungiCorso(Corso corso) {
        this.corsi.add(corso);
    }

    public void rimuoviCorso(Corso corso) {
        this.corsi.remove(corso);
    }
}
