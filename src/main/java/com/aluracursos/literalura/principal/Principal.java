package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.IAutorRepository;
import com.aluracursos.literalura.repository.ILibroRepository;
import com.aluracursos.literalura.service.API;
import com.aluracursos.literalura.service.ConvierteDatos;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private static final String URL_BASE = "https://gutendex.com/books/?search=";
    private API api = new API();
    private ConvierteDatos convertir = new ConvierteDatos();
    private ILibroRepository libroRepository;
    private IAutorRepository autorRepository;
    private List<Autor> autores;
    private List<Libro> libros;

    public Principal(ILibroRepository libroRepository, IAutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }


    public void mostrarMenu() {
        var opcion = -1;
        var menu = """
                    
                    ✦✦✦ LITERALURA ✦✦✦
                    1- Buscar Libro por titulo
                    2- Listar libros registrados
                    3- Listar autores registrados
                    4- Listar autores vivos en un determinado año
                    5- Listar libros por idioma
                    6- Buscar autor en la BD
                    0- Salir
                    """;
        while (opcion != 0) {
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();
                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        mostrarLibrosRegistrados();
                        break;
                    case 3:
                        mostrarAutoresRegistrados();
                        break;
                    case 4:
                        mostrarAutoresDeterminadoAnio();
                        break;
                    case 5:
                        mostrarLibrosPorIdioma();
                        break;
                    case 6:
                        buscarPorAutor();
                        break;
                    case 0:
                        System.out.println("Cerrando aplicacion");
                        break;
                    default:
                        System.out.println("Opcion invalida");
                        break;
                }

        }
    }

    private Datos getDatosLibros() {
            System.out.println("Escribe el nombre del libro que quieres buscar:");
            var libroABuscar = "";
            libroABuscar = teclado.nextLine();
            var json = api.obtenerDatos(URL_BASE + libroABuscar.replace(" ", "%20"));
            var datos = convertir.obtenerDatos(json, Datos.class);
            return datos;
    }

    private void buscarLibroPorTitulo() {
        DatosLibro libroRecibido = getDatosLibros().resultados().get(0);
        DatosAutor autorRecibido = libroRecibido.autor().get(0);
        if (!libroRecibido.titulo().isEmpty()) {
            String respuesta = """
                    
                    ✦✦✦✦✦✦✦LIBRO BUSCADO✦✦✦✦✦✦
                    Titulo: '%s'
                    Autor: '%s'
                    Idiomas: '%s'
                    Numero de Descargas: %d
                    ✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦
                                 ✦✦✦
                    
                    """.formatted(libroRecibido.titulo(), autorRecibido.nombre(), libroRecibido.idiomas().get(0), libroRecibido.numeroDeDescargas());
            System.out.println(respuesta);

            //VERIFICA SI EL LIBRO SE ENCUENTRA EN LA BASE DE DATOS
            Optional<Libro> libroEnRepo = libroRepository.findByTitulo(libroRecibido.titulo());
            if (libroEnRepo.isPresent()) {
                System.out.println("►►► EL LIBRO QUE BUSCA YA SE ENCUENTRA REGISTRADO EN LA BASE DE DATOS");
            } else {

                Autor autor = new Autor(autorRecibido);
                Libro libro = new Libro(libroRecibido, autor);

                //VERIFICA SI EL AUTOR SE ENCUENTRA EN LA BASE DE DATOS
                Optional<Autor> autorEnRepo = autorRepository.findByNombre(autorRecibido.nombre());
                if (autorEnRepo.isPresent()) {
                    libroRepository.save(libro);
                } else {
                    Autor autorNuevo = autorRepository.save(autor);
                    libro.setAutor(autorNuevo);
                    libroRepository.save(libro);
                }
                System.out.println("►## LIBRO REGISTRADO! ##◄");
            }
        } else {
            System.out.println("Libro no encontrado");
        }


    }

    private void buscarPorAutor() {
        System.out.println("Escribe el nombre del autor que quieres buscar en la BD:");
        var nombre = teclado.nextLine();
        Optional<Autor> autorBuscado = autorRepository.findByNombreContainsIgnoreCase(nombre);
        if (autorBuscado.isPresent()) {
            autorBuscado.stream().forEach(System.out::println);
        } else {
            System.out.println("Autor no encontrado");
        }
    }

    private void mostrarLibrosRegistrados() {
        libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros para mostrar");
        } else {
            libros.stream().forEach(System.out::println);
        }
    }

    private void mostrarAutoresRegistrados() {
        autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores para mostrar");
        } else {
            autores.stream().forEach(System.out::println);
        }
    }

    private void mostrarAutoresDeterminadoAnio() {
        System.out.println("Ingresa el año:");
        var anio = teclado.nextInt();
        autores = autorRepository.listaAutoresVivosPorAnio(anio);

        if (autores.isEmpty()) {
            System.out.println("► SIN DATOS PARA MOSTRAR");
            System.out.println(" ");
        } else {
            autores.stream().forEach(System.out::println);
        }
    }

    private void mostrarLibrosPorIdioma() {

        System.out.println("Selecciona el lenguaje que desea buscar:");
        var option = -1;
        while(option != 9) {
            var menu = """
                    1 - Ingles
                    2 - Español
                    3 - Frances
                    4 - Portugues
                    9 - Volver al menu anterior
                    """;
            System.out.println(menu);
            option = teclado.nextInt();

            switch (option) {
                case 1:
                    List<Libro> enIngles = libroRepository.findByIdiomas("{en}");
                    if (!enIngles.isEmpty()) {
                        enIngles.stream().forEach(System.out::println);
                    } else {
                        System.out.println("No se encontraron resultados");
                    }
                    break;
                case 2:
                    List<Libro> enEspanol = libroRepository.findByIdiomas("es");
                    if (!enEspanol.isEmpty()) {
                        enEspanol.stream().forEach(System.out::println);
                    } else {
                        System.out.println("No se encontraron resultados");
                    }
                    break;
                case 3:
                    List<Libro> enFrances = libroRepository.findByIdiomas("fr");
                    if (!enFrances.isEmpty()) {
                        enFrances.stream().forEach(System.out::println);
                    } else {
                        System.out.println("No se encontraron resultados");
                    }
                    break;
                case 4:
                    List<Libro> enPortugues = libroRepository.findByIdiomas("pt");
                    if (!enPortugues.isEmpty()) {
                        enPortugues.stream().forEach(System.out::println);
                    } else {
                        System.out.println("No se encontraron resultados");
                    }
                    break;
                case 9:
                    break;
                default:
                    System.out.println("Ningún idioma seleccionado");
            }
        }

    }


}
