import com.google.gson.JsonArray;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) throws IOException {
        JsonArray listaRegistroLocal = new JsonArray();
        ArrayList<String> listaRegistroGlobal = new ArrayList<>();

        Estructura relleno = new Estructura();
        Scanner lectura = new Scanner(System.in);

        while (true) {
            try {
                relleno.menu();
                System.out.println("Ingresa tu Eleccion: ");
                int eleccion01 = lectura.nextInt();
                lectura.nextLine();

                if (eleccion01 == 8) {
                    break;
                }

                String inicial = "";
                String cambio = "";

                if (eleccion01 == 7) {
                    relleno.escribirMonedas();
                    System.out.println("Ingrese su moneda 1 o Nativa: ");
                    inicial = lectura.nextLine().toUpperCase();
                    System.out.println("Ingrese su moneda 2 a Conversion: ");
                    cambio = lectura.nextLine().toUpperCase();

                } else {
                    switch (eleccion01) {
                        case 1:
                            inicial = "USD";
                            cambio = "ARS";
                            break;
                        case 2:
                            inicial = "ARS";
                            cambio = "USD";
                            break;
                        case 3:
                            inicial = "USD";
                            cambio = "BOB";
                            break;
                        case 4:
                            inicial = "BOB";
                            cambio = "USD";
                            break;
                        case 5:
                            inicial = "USD";
                            cambio = "BRL";
                            break;
                        case 6:
                            inicial = "USD";
                            cambio = "COP";
                            break;
                        default:
                            System.out.println("Elección inválida.");
                            continue;
                    }
                }

                System.out.println("Ingrese la cantidad a Convertir");
                Double cantidad = lectura.nextDouble();
                lectura.nextLine();

                String moneda1 = inicial.toUpperCase();
                String moneda2 = cambio.toUpperCase();

                ConsultaApi consulta = new ConsultaApi();
                Rate representation = consulta.tasaApi(moneda1, moneda2, cantidad);
                Double tasaDeCambio = representation.conversion_rate();
                Double totalConversion = representation.conversion_result();

                LocalDateTime ahora = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String tiempoRegistro = ahora.format(formatter);

                String registro = String.format("[%s] Se convirtieron %f %s a %s %f", tiempoRegistro, cantidad, moneda1, moneda2, totalConversion);
                String registroGlobal = String.format("[%s]", tiempoRegistro);

                var jsonObject = Json.getJsonObject(moneda1, moneda2, tasaDeCambio, cantidad, totalConversion, registroGlobal);
                System.out.println(cantidad + " " + moneda1 + " x " + tasaDeCambio + " -> " + moneda2 + " = " + totalConversion);


                listaRegistroGlobal.add(registro);
                listaRegistroLocal.add(jsonObject);

                GeneradorArchivos archivoJson = new GeneradorArchivos();
                archivoJson.saveJson(listaRegistroLocal);
            } catch (InputMismatchException e) {
                System.out.println("Error: La entrada no es un número.");
                lectura.nextLine();
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException("Error al leer/escribir el archivo: " + e.getMessage());
            }
        }

        GeneradorArchivos archivoRegistro = new GeneradorArchivos();
        archivoRegistro.AgregarDatosJson(listaRegistroGlobal);

        lectura.close();

    }
}