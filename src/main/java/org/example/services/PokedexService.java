package org.example.services;

import org.example.model.Pokedex;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import java.io.*;
import java.util.List;

public class PokedexService {
    private final SessionFactory sessionFactory;

    public PokedexService() {
        try {
            this.sessionFactory = new Configuration().configure("properties.xml").buildSessionFactory();
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Error al inicializar SessionFactory: " + e);
        }
    }

    // --- CRUD con Objetos ---
    public void insertar(Pokedex p) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(p);
            tx.commit();
        }
    }

    public Pokedex leerPorId(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Pokedex.class, id);
        }
    }

    public void actualizar(Pokedex p) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(p);
            tx.commit();
        }
    }

    // --- CRUD con Queries (HQL) ---
    public List<Pokedex> listarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Pokedex", Pokedex.class).list();
        }
    }

    public void actualizarConQuery(Integer id, String nuevoNombre) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("UPDATE Pokedex SET nome = :nuevoNombre WHERE id = :id")
                    .setParameter("nuevoNombre", nuevoNombre)
                    .setParameter("id", id)
                    .executeUpdate();
            tx.commit();
        }
    }

    public void eliminarTodosConQuery() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("DELETE FROM Pokedex").executeUpdate();
            tx.commit();
        }
    }

    // NO ENTRA EN EXAMEN
    /*
    public void serializar(List<Pokedex> lista, String archivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(lista);
        } catch (IOException e) {
            System.err.println("Error al serializar Pokedex: " + e.getMessage());
        }
    }

    public List<Pokedex> deserializar(String archivo) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Pokedex>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al deserializar Pokedex: " + e.getMessage());
            return null;
        }
    }
    */


    public void cerrar() { sessionFactory.close(); }
}