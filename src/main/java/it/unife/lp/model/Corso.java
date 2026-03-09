package it.unife.lp.model;

import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Corso {
    private int id;
    private String nome;
    private String descrizione;
    
    @JsonIgnore
    private Insegnante insegnante;
    
    @JsonProperty("insegnante")
    private InsegnanteRef getInsegnanteForJson() {
        return insegnante != null ? new InsegnanteRef(insegnante.getId()) : null;
    }
    
    @JsonProperty("insegnante")
    private void setInsegnanteFromJson(InsegnanteRef ref) {
        // Relationship will be rebuilt by rebuildRelationships()
    }
    
    @JsonIgnore
    private List<Studente> studentiIscritti;
    
    @JsonIgnore
    private List<Integer> studentiIscrittiIds; // Temporary storage for JSON IDs
    
    @JsonProperty("studentiIscritti")
    private List<Integer> getStudentiIscrittiIds() {
        if (studentiIscritti == null) {
            studentiIscritti = javafx.collections.FXCollections.observableArrayList();
        }
        return studentiIscritti.stream().map(Studente::getId).collect(Collectors.toList());
    }
    
    @JsonProperty("studentiIscritti")
    private void setStudentiIscrittiIds(List<Integer> ids) {
        this.studentiIscrittiIds = ids;
    }
    
    @JsonIgnore
    public List<Integer> getTempStudentiIscrittiIds() {
        return studentiIscrittiIds;
    }
    
    public static class InsegnanteRef {
        private int id;
        
        public InsegnanteRef() {}
        
        public InsegnanteRef(int id) { 
            this.id = id; 
        }
        
        public int getId() {
            return id;
        }
        
        public void setId(int id) {
            this.id = id;
        }
    }

    public Corso() {
        // Default constructor for Jackson
        this.studentiIscritti = javafx.collections.FXCollections.observableArrayList();
    }

    public Corso(int id, String nome, String descrizione, Insegnante insegnante, List<Studente> studentiIscritti) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.insegnante = insegnante;
        this.studentiIscritti = studentiIscritti;
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
    public String getDescrizione() {
        return descrizione;
    }
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    public Insegnante getInsegnante() {
        return insegnante;
    }
    public void setInsegnante(Insegnante insegnante) {
        this.insegnante = insegnante;
    }
    public List<Studente> getStudentiIscritti() {
        return studentiIscritti;
    }
    public void setStudentiIscritti(List<Studente> studentiIscritti) {
        this.studentiIscritti = studentiIscritti;
    }

    public String toString() {
        return nome + ": " + descrizione + " Insegnante: " + insegnante.getNome() + " " + insegnante.getCognome();
    }

    public void aggiungiStudente(Studente studente) {
        this.studentiIscritti.add(studente);
    }
    public void rimuoviStudente(Studente studente) {
        this.studentiIscritti.remove(studente);
    }
}