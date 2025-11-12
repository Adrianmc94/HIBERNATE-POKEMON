package org.example.services;

import org.example.model.Pokemon;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import java.util.List;

public class PokemonService {
    private final SessionFactory sessionFactory;

    public PokemonService() {
        try {
            this.sessionFactory = new Configuration().configure("properties.xml").buildSessionFactory();
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Error al inicializar SessionFactory: " + e);
        }
    }

    // --- CRUD con Objetos ---
    public void insertar(Pokemon p) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(p);
            tx.commit();
        }
    }

    public Pokemon leerPorId(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Pokemon.class, id);
        }
    }

    public void actualizar(Pokemon p) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(p);
            tx.commit();
        }
    }

    // --- CRUD con Queries (HQL) ---
    public List<Pokemon> listarTodos() {
        try (Session session = sessionFactory.openSession()) {
            // Usamos JOIN FETCH para cargar las relaciones de golpe y evitar N+1
            return session.createQuery("SELECT p FROM Pokemon p JOIN FETCH p.adestrador JOIN FETCH p.pokedexEntry", Pokemon.class).list();
        }
    }

    public void actualizarConQuery(Integer id, String nuevoNombre) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("UPDATE Pokemon SET nome = :nuevoNombre WHERE id = :id")
                    .setParameter("nuevoNombre", nuevoNombre)
                    .setParameter("id", id)
                    .executeUpdate();
            tx.commit();
        }
    }

    public void eliminarTodosConQuery() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("DELETE FROM Pokemon").executeUpdate();
            tx.commit();
        }
    }

    public void cerrar() { sessionFactory.close(); }
}
