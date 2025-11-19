package uptc.edu.co;

import uptc.edu.co.model.Producto;
import uptc.edu.co.service.ProductoService;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        ProductoService servicio = new ProductoService();
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n*** CRUD de Productos ***");
            System.out.println("1. Agregar producto");
            System.out.println("2. Listar productos");
            System.out.println("3. Actualizar producto");
            System.out.println("4. Eliminar producto");
            System.out.println("0. Salir");
            System.out.print("Selecciona una opción: ");
            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    System.out.print("ID: ");
                    int id = sc.nextInt();
                    sc.nextLine(); // Limpiar buffer
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Precio: ");
                    double precio = sc.nextDouble();
                    Producto nuevo = new Producto(id, nombre, precio);
                    servicio.agregarProducto(nuevo);
                    System.out.println("Producto agregado.");
                    break;
                case 2:
                    System.out.println("Lista de productos:");
                    for (Producto p : servicio.listarProductos()) {
                        System.out.println(p);
                    }
                    break;
                case 3:
                    System.out.print("ID de producto a actualizar: ");
                    int idUpdate = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Nuevo nombre: ");
                    String nombreUpdate = sc.nextLine();
                    System.out.print("Nuevo precio: ");
                    double precioUpdate = sc.nextDouble();
                    if (servicio.actualizarProducto(idUpdate, nombreUpdate, precioUpdate)) {
                        System.out.println("Producto actualizado.");
                    } else {
                        System.out.println("Producto no encontrado.");
                    }
                    break;
                case 4:
                    System.out.print("ID del producto a eliminar: ");
                    int idDelete = sc.nextInt();
                    if (servicio.eliminarProducto(idDelete)) {
                        System.out.println("Producto eliminado.");
                    } else {
                        System.out.println("Producto no encontrado.");
                    }
                    break;
                case 0:
                    System.out.println("¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
        sc.close();
    }
}
