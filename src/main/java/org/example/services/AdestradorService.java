package org.example.services;

import org.example.model.Adestrador;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
// import com.thoughtworks.xstream.XStream;
// import com.thoughtworks.xstream.security.AnyTypePermission;
import java.io.*;
import java.util.List;

public class AdestradorService {
    private final SessionFactory sessionFactory;

    public AdestradorService() {
        try {
            this.sessionFactory = new Configuration().configure("properties.xml").buildSessionFactory();
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Error al inicializar SessionFactory: " + e);
        }
    }

    // --- CRUD con Objetos ---
    public void insertar(Adestrador a) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(a);
            tx.commit();
        }
    }

    public Adestrador leerPorId(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Adestrador.class, id);
        }
    }

    public void actualizar(Adestrador a) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(a);
            tx.commit();
        }
    }

    // --- CRUD con Queries (HQL) ---
    public List<Adestrador> listarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Adestrador", Adestrador.class).list();
        }
    }

    public void actualizarConQuery(Integer id, String nuevoNombre) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("UPDATE Adestrador SET nome = :nuevoNombre WHERE id = :id")
                    .setParameter("nuevoNombre", nuevoNombre)
                    .setParameter("id", id)
                    .executeUpdate();
            tx.commit();
        }
    }

    public void eliminarTodosConQuery() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("DELETE FROM Adestrador").executeUpdate();
            tx.commit();
        }
    }

    // NO ENTRA EN EXAMEN
    /*
    private XStream configurarXStream() {
        XStream xstream = new XStream();
        xstream.addPermission(AnyTypePermission.ANY);
        xstream.alias("adestrador", Adestrador.class);
        xstream.alias("lista", List.class);
        return xstream;
    }

    public void toXML(List<Adestrador> lista, String archivo) {
        XStream xstream = configurarXStream();
        try (FileWriter writer = new FileWriter(archivo)) {
            writer.write(xstream.toXML(lista));
        } catch (IOException e) {
            System.err.println("Error al exportar Adestrador a XML: " + e.getMessage());
        }
    }

    public List<Adestrador> fromXML(String archivo) {
        XStream xstream = configurarXStream();
        try (FileReader reader = new FileReader(archivo)) {
            // XStream deserializa el objeto raíz, que es la lista.
            return (List<Adestrador>) xstream.fromXML(reader);
        } catch (IOException e) {
            System.err.println("Error al importar Adestrador desde XML: " + e.getMessage());
            return null;
        }
    }
        */
    public void cerrar() { sessionFactory.close(); }
}
