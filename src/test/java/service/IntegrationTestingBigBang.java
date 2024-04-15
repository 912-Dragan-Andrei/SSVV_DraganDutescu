package service;

import domain.Nota;
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
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class IntegrationTestingBigBang {
    private Service service;
    public IntegrationTestingBigBang()
    {
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
    private String generateUniqueID() {
        return UUID.randomUUID().toString();
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
    void testAddNota() throws ValidationException {

        String idTema = generateUniqueID();

        Tema tema = new Tema(idTema, "descriere", 1, 2);
        service.addTema(tema);

        String idStud = generateUniqueID();
        Student student = new Student(idStud, "Andrei", 1, "email.com");
        service.addStudent(student);

        String id = generateUniqueID();
        String data = "2024-01-01";
        String[] date = data.split("-");


        LocalDate dataPredare = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        Nota notaCatalog = new Nota(id, idStud, idTema, 10, dataPredare);

        assertDoesNotThrow(() -> {
            double result = service.addNota(notaCatalog,"feedback");
            assertEquals(notaCatalog.getNota(), result);
        });
    }

    @Test
    void testBigBangIntegration(){
        addStudentWithValidData();
        testAddTema();
        testAddNota();
    }

}
