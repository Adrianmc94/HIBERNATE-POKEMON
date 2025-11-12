package org.example.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Pokemon")
public class Pokemon implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    @Temporal(TemporalType.DATE)
    private Date nacemento;

    // Relación ManyToOne: Varios Pokemon a una entrada de Pokedex
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PokedexEntry")
    private Pokedex pokedexEntry;

    // Relación ManyToOne: Varios Pokemon a un Adestrador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Adestrador")
    private Adestrador adestrador;

    public Pokemon() {}
    public Pokemon(String nome, Date nacemento, Pokedex pokedexEntry, Adestrador adestrador) {
        this.nome = nome;
        this.nacemento = nacemento;
        this.pokedexEntry = pokedexEntry;
        this.adestrador = adestrador;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Date getNacemento() { return nacemento; }
    public Pokedex getPokedexEntry() { return pokedexEntry; }
    public void setPokedexEntry(Pokedex pokedexEntry) { this.pokedexEntry = pokedexEntry; }
    public Adestrador getAdestrador() { return adestrador; }
    public void setAdestrador(Adestrador adestrador) { this.adestrador = adestrador; }

    @Override
    public String toString() {
        return "Pokemon{id=" + id + ", nome='" + nome +
                ", PokedexEntry=" + (pokedexEntry != null ? pokedexEntry.getId() : "null") +
                ", Adestrador=" + (adestrador != null ? adestrador.getId() : "null") + '}';
    }
}