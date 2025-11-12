package org.example.app;


import org.example.model.Adestrador;
import org.example.model.Pokedex;
import org.example.model.Pokemon;
import org.example.services.AdestradorService;
import org.example.services.PokedexService;
import org.example.services.PokemonService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        PokedexService ps = new PokedexService();
        AdestradorService as = new AdestradorService();
        PokemonService pks = new PokemonService();

        // 1. Definición de datos originales para inserción y reversión
        List<Pokedex> pokedexOriginal = crearPokemonsPokedexOriginal();
        List<Adestrador> adestradoresOriginal = crearAdestradoresOriginal();

        // 2. Ejecutar la secuencia completa dos veces

        System.out.println("Secuencia ejecutada con OBJETOS y métodos Hibernate (session.get(), session.update()):");
        ejecutarSecuencia(ps, as, pks, pokedexOriginal, adestradoresOriginal, true);

        System.out.println("Secuencia ejecutada con QUERIES específicas (session.createQuery().executeUpdate()):");
        ejecutarSecuencia(ps, as, pks, pokedexOriginal, adestradoresOriginal, false);

        ps.cerrar();
        as.cerrar();
        pks.cerrar();
    }

    private static List<Pokedex> crearPokemonsPokedexOriginal() {
        return List.of(
                new Pokedex("P3_Poke1", new BigDecimal("30.1"), "Info3-1"),
                new Pokedex("P3_Poke2", new BigDecimal("30.2"), "Info3-2"),
                new Pokedex("P3_Poke3", new BigDecimal("30.3"), "Info3-3"),
                new Pokedex("P3_Poke4", new BigDecimal("30.4"), "Info3-4"),
                new Pokedex("P3_Poke5", new BigDecimal("30.5"), "Info3-5"),
                new Pokedex("P3_Poke6", new BigDecimal("30.6"), "Info3-6"),
                new Pokedex("P3_Poke7", new BigDecimal("30.7"), "Info3-7"),
                new Pokedex("P3_Poke8", new BigDecimal("30.8"), "Info3-8"),
                new Pokedex("P3_Poke9", new BigDecimal("30.9"), "Info3-9"),
                new Pokedex("P3_Poke10", new BigDecimal("30.10"), "Info3-10")
        );
    }

    private static List<Adestrador> crearAdestradoresOriginal() {
        return List.of(
                new Adestrador("Ash P3", new Date(90, 4, 22)),
                new Adestrador("Misty P3", new Date(88, 7, 10))
        );
    }

    private static void ejecutarSecuencia(PokedexService ps, AdestradorService as, PokemonService pks,
                                          List<Pokedex> pokedexOriginal, List<Adestrador> adestradoresOriginal,
                                          boolean useObjects) {

        String tipoEjecucion = useObjects ? "OBJETOS" : "QUERIES";

        // 0. Limpiar tablas
        pks.eliminarTodosConQuery();
        ps.eliminarTodosConQuery();
        as.eliminarTodosConQuery();
        System.out.println("Tablas limpiadas.");

        // 1. Inserir 10 pokemons en pokedex
        System.out.println("Insertando 10 Pokemons en Pokedex.");
        pokedexOriginal.forEach(ps::insertar);

        // 2. Inserir 2 adestradores
        System.out.println("Insertando 2 Adestradores.");
        adestradoresOriginal.forEach(as::insertar);

        // 3. Inserir 12 pokemons na táboa de pokemon (6 para cada adestrador)
        System.out.println("Insertando 12 Pokemons relacionados (6 para cada Adestrador).");
        Adestrador a1 = as.leerPorId(1);
        Adestrador a2 = as.leerPorId(2);
        Pokedex p1 = ps.leerPorId(1);
        Pokedex p2 = ps.leerPorId(2);

        if (a1 != null && a2 != null && p1 != null && p2 != null) {
            for (int i = 1; i <= 6; i++) {
                pks.insertar(new Pokemon("PK_A1_" + i, new Date(125, 0, i), p1, a1));
                pks.insertar(new Pokemon("PK_A2_" + i, new Date(125, 1, i), p2, a2));
            }
        }

        // Listar
        System.out.println("Listado Pokedex:");
        ps.listarTodos().forEach(System.out::println);
        System.out.println("Listado Adestradores:");
        as.listarTodos().forEach(System.out::println);
        System.out.println("Listado Pokemon (Tabla Pokemon):");
        pks.listarTodos().forEach(System.out::println);


        /*
        List<Pokedex> pToExport = List.of(ps.leerPorId(1), ps.leerPorId(2));
        List<Adestrador> aToExport = List.of(as.leerPorId(1), as.leerPorId(2));

        System.out.println("7.Exportando 2 Pokedex a binario (pokedex_export.ser).");
        ps.serializar(pToExport, "pokedex_export.ser");
        System.out.println("Exportando 2 Adestradores a XML (adestradores_export.xml).");
        as.toXML(aToExport, "adestradores_export.xml"); // <-- CORREGIDO: PUNTO Y COMA AÑADIDO
        */



        // 9. Modificar 2 Pokedex
        System.out.println("Modificando 2 Pokedex con " + tipoEjecucion);
        if (useObjects) {
            Pokedex mod1 = ps.leerPorId(1); mod1.setNome("MOD_POKE_1_OBJ"); ps.actualizar(mod1);
            Pokedex mod2 = ps.leerPorId(2); mod2.setNome("MOD_POKE_2_OBJ"); ps.actualizar(mod2);
        } else {
            ps.actualizarConQuery(1, "MOD_POKE_1_QUERY");
            ps.actualizarConQuery(2, "MOD_POKE_2_QUERY");
        }

        // 10. Modificar 2 Adestradores
        System.out.println("Modificando 2 Adestradores con " + tipoEjecucion);
        if (useObjects) {
            Adestrador modA1 = as.leerPorId(1); modA1.setNome("MOD_ADEST_1_OBJ"); as.actualizar(modA1);
            Adestrador modA2 = as.leerPorId(2); modA2.setNome("MOD_ADEST_2_OBJ"); as.actualizar(modA2);
        } else {
            as.actualizarConQuery(1, "MOD_ADEST_1_QUERY");
            as.actualizarConQuery(2, "MOD_ADEST_2_QUERY");
        }

        // 11. Modificar 4 Pokemons
        System.out.println("Modificando 4 Pokemons con " + tipoEjecucion);
        for (int i = 1; i <= 4; i++) {
            if (useObjects) {
                Pokemon pk = pks.leerPorId(i);
                if (pk != null) {
                    pk.setNome("MOD_PK_" + i + "_OBJ");
                    pks.actualizar(pk);
                }
            } else {
                pks.actualizarConQuery(i, "MOD_PK_" + i + "_QUERY");
            }
        }

        // 12-14. Listar
        System.out.println("--- Listados Post-Modificación ---");
        System.out.println("Listado Pokedex (Modificado):");
        ps.listarTodos().forEach(System.out::println);
        System.out.println("Listado Adestradores (Modificado):");
        as.listarTodos().forEach(System.out::println);
        System.out.println("Listado Pokemon (Modificado):");
        pks.listarTodos().forEach(System.out::println);



        /*
        System.out.println("Importando datos Pokedex de binario.");
        List<Pokedex> pImported = ps.deserializar("pokedex_export.ser");

        if (pImported != null && pImported.size() >= 2) {
            System.out.println("Revirtiendo Pokedex 1 y 2 a estado inicial.");
            pImported.get(0).setId(1);
            pImported.get(1).setId(2);
            ps.actualizar(pImported.get(0));
            ps.actualizar(pImported.get(1));
        }

        System.out.println("Importando datos Adestrador de XML.");
        List<Adestrador> aImported = as.fromXML("adestradores_export.xml");

        if (aImported != null && aImported.size() >= 2) {
            System.out.println("Revirtiendo Adestradores 1 y 2 a estado inicial.");
            aImported.get(0).setId(1);
            aImported.get(1).setId(2);
            as.actualizar(aImported.get(0));
            as.actualizar(aImported.get(1));
        }
        */

        // 19. Lista-los datos das táboas
        System.out.println("Listados Finales");
        ps.listarTodos().forEach(System.out::println);
        as.listarTodos().forEach(System.out::println);
        pks.listarTodos().forEach(System.out::println);

        // 20. Eliminar tódo-los datos das táboas
        System.out.println("Eliminando todos los datos.");
        pks.eliminarTodosConQuery();
        ps.eliminarTodosConQuery();
        as.eliminarTodosConQuery();
    }
}