package com.kaodev.util;

import com.kaodev.domain.Persona;

import java.util.ArrayList;
import java.util.List;

public class MockData {

    private static Persona persona1 = new Persona();
    private static Persona persona2 = new Persona();
    private static Persona persona3 = new Persona();

    private static void fillData() {
        persona1.setNombre("Mock");
        persona1.setApellido("Mcmocker");
        persona1.setEmail("mocker@mail.com");
        persona1.setTelefono("123456789");
        persona1.setSaldo(100.10);

        persona2.setNombre("Other");
        persona2.setApellido("Mock");
        persona2.setEmail("omock@mail.com");
        persona2.setTelefono("987654321");
        persona2.setSaldo(321.98);

        persona3.setNombre("Sir Mock");
        persona3.setApellido("Mcmocken");
        persona3.setEmail("sirmock@mail.com");
        persona3.setTelefono("192837465");
        persona3.setSaldo(98.98);
    }

    public static List<Persona> getMockData() {
        fillData();
        List<Persona> list = new ArrayList<>();
        list.add(persona1);
        list.add(persona2);
        list.add(persona3);
        return list;
    }
}
