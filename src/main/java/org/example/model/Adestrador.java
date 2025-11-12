package org.example.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Adestrador")
public class Adestrador implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    @Temporal(TemporalType.DATE)
    private Date nacemento;

    public Adestrador() {}
    public Adestrador(String nome, Date nacemento) {
        this.nome = nome;
        this.nacemento = nacemento;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Date getNacemento() { return nacemento; }
    public void setNacemento(Date nacemento) { this.nacemento = nacemento; }

    @Override
    public String toString() {
        return "Adestrador{id=" + id + ", nome='" + nome + "', nacemento=" + nacemento + '}';
    }
}