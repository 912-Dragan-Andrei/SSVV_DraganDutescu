package service;

import domain.Student;
import domain.Tema;
import org.junit.jupiter.api.Test;
import repository.NotaXMLRepo;
import repository.StudentFileRepository;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class ServiceTest {
    private Service service;

    public ServiceTest() {

        // createTempFiles();

        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        String filenameStudent = "fisiere/Studenti.xml";
        String filenameTema = "fisiere/Teme.xml";
        String filenameNota = "fisiere/Note.xml";

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        this.service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    @Test
    void addStudentWithValidData() {
        String id = generateUniqueID();
        Student student = new Student(id, "Andrei", 1, "email.com");


        assertDoesNotThrow(() -> {
            Student result = service.addStudent(student);
            assertEquals(student, result);
        });
    }

    @Test
    void addStudentWithEmptyName() {
        String id = generateUniqueID();
        Student student = new Student(id, "", 1, "email.com");


        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Student result = service.addStudent(student);
        });
        assertEquals("Nume incorect!", exception.getMessage());
    }

    @Test
    void addStudentWithEmptyID() {

        Student student = new Student("", "Andrei", 1, "email.com");


        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Student result = service.addStudent(student);
        });
        assertEquals("Id incorect!", exception.getMessage());
    }

    @Test
    void addStudentWithExistingID() {
        String id = generateUniqueID();
        Student student = new Student(id, "Andrei", 1, "email.com");
        service.addStudent(student);

        assertNull(service.addStudent(student));
    }

    @Test
    void addStudentWithEmptyEmail() {
        String id = generateUniqueID();
        Student student = new Student(id, "Andrei", 1, "");


        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Student result = service.addStudent(student);
        });
        assertEquals("Email incorect!", exception.getMessage());
    }

    @Test
    void addStudentWithNegativeGroup() {
        String id = generateUniqueID();
        Student student = new Student(id, "Andrei", -1, "email.com");


        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Student result = service.addStudent(student);
        });
        assertEquals("Grupa incorecta!", exception.getMessage());
    }



    @Test
    void addStudentWithMaxIntGroupPlusOne() {
        String id = generateUniqueID();
        long maxIntPlusOneGroup = (long) Integer.MAX_VALUE + 1;
        int invalidGroup = (int) maxIntPlusOneGroup;
        Student student = new Student(id, "Andrei", invalidGroup, "email.com");


        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Student result = service.addStudent(student);
        });
        assertEquals("Grupa incorecta!", exception.getMessage());
    }

    @Test
    void addStudentWithGroupZero() {
        String id = generateUniqueID();
        Student student = new Student(id, "Andrei", 0, "email.com");


        assertDoesNotThrow(() -> {
            Student result = service.addStudent(student);
            assertEquals(student, result);
        });
    }

    @Test
    void addStudentWithMaxIntGroup() {
        String id = generateUniqueID();
        Student student = new Student(id, "Andrei", Integer.MAX_VALUE, "email.com");


        assertDoesNotThrow(() -> {
            Student result = service.addStudent(student);
            assertEquals(student, result);
        });
    }

    @Test
    void addStudentWithMaxIntMinusOneGroup() {
        String id = generateUniqueID();
        Student student = new Student(id, "Andrei", Integer.MAX_VALUE - 1, "email.com");


        assertDoesNotThrow(() -> {
            Student result = service.addStudent(student);
            assertEquals(student, result);
        });
    }

    @Test
    void testAddTemaWithInvalidNrTema() {
        Tema tema = new Tema("", "", 0, 0);
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            service.addTema(tema);
        });
        assertEquals("Numar tema invalid!", exception.getMessage());
    }

    @Test
    void testAddTemaWithInvalidDescriere() {
        String id = generateUniqueID();
        Tema tema = new Tema(id, "", 0, 0);
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            service.addTema(tema);
        });
        assertEquals("Descriere invalida!", exception.getMessage());
    }

    @Test
    void testAddTemaWithInvalidDeadline() {
        String id = generateUniqueID();
        Tema tema = new Tema(id, "descriere", 0, 0);
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            service.addTema(tema);
        });
        assertEquals("Deadlineul trebuie sa fie intre 1-14.", exception.getMessage());
    }

    @Test
    void testAddTemaWithInvalidPrimire() {
        String id = generateUniqueID();
        Tema tema = new Tema(id, "descriere", 3, 0);
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            service.addTema(tema);
        });
        assertEquals("Saptamana primirii trebuie sa fie intre 1-14.", exception.getMessage());
    }

    @Test
    void testAddTema() throws ValidationException {
        String id = generateUniqueID();
        Tema tema = new Tema(id, "descriere", 3, 2);

        assertDoesNotThrow(() -> {
            Tema result = service.addTema(tema);
            assertEquals(tema, result);
        });
    }









    private String generateUniqueID() {
        return UUID.randomUUID().toString();
    }


}




