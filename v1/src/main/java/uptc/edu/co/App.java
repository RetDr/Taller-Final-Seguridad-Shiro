package uptc.edu.co;

import uptc.edu.co.model.Producto;
import uptc.edu.co.service.ProductoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        // Configuración de Apache Shiro
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        Scanner sc = new Scanner(System.in);
        ProductoService servicio = new ProductoService();

        while (true) {
            // Autenticación usuario
            System.out.println("\n===== AUTENTICACIÓN =====");
            System.out.print("Usuario: ");
            String username = sc.nextLine();
            System.out.print("Contraseña: ");
            String password = sc.nextLine();

            Subject currentUser = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);

            try {
                currentUser.login(token);
                System.out.println("Autenticación exitosa. Bienvenido " + currentUser.getPrincipal());
            } catch (AuthenticationException ae) {
                System.out.println("Error: Usuario o contraseña incorrectos.\n¿Intentar de nuevo? [S/N]: ");
                String retry = sc.nextLine().trim().toUpperCase();
                if (!retry.equals("S")) break;
                continue;
            }

            // Menú CRUD
            int opcion = -1;
            while (opcion != 0) {
                System.out.println("\n*** CRUD de Productos ***");
                System.out.println("1. Agregar producto");
                System.out.println("2. Listar productos");
                System.out.println("3. Actualizar producto");
                System.out.println("4. Eliminar producto");
                System.out.println("5. Cerrar sesión");
                System.out.println("0. Salir");
                System.out.print("Selecciona una opción: ");
                try {
                    opcion = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException ex) {
                    System.out.println("¡Debe ingresar un número!");
                    opcion = -1;
                    continue;
                }

                switch (opcion) {
                    case 1:
                        if (currentUser.hasRole("admin") || currentUser.isPermitted("producto:add")) {
                            try {
                                System.out.print("ID: ");
                                int id = Integer.parseInt(sc.nextLine().trim());
                                System.out.print("Nombre: ");
                                String nombre = sc.nextLine();
                                System.out.print("Precio: ");
                                double precio = Double.parseDouble(sc.nextLine().trim());
                                Producto nuevo = new Producto(id, nombre, precio);
                                servicio.agregarProducto(nuevo);
                                System.out.println("Producto agregado.");
                            } catch (NumberFormatException ex) {
                                System.out.println("Error: El ID y precio deben ser números.");
                            }
                        } else {
                            System.out.println("No tienes permiso para agregar productos.");
                        }
                        break;
                    case 2:
                        if (currentUser.hasRole("admin") || currentUser.isPermitted("producto:view")) {
                            System.out.println("Lista de productos:");
                            if (servicio.listarProductos().isEmpty()) {
                                System.out.println("No hay productos registrados.");
                            } else {
                                for (Producto p : servicio.listarProductos()) {
                                    System.out.println(p);
                                }
                            }
                        } else {
                            System.out.println("No tienes permiso para ver los productos.");
                        }
                        break;
                    case 3:
                        if (currentUser.hasRole("admin")) {
                            try {
                                System.out.print("ID de producto a actualizar: ");
                                int idUpdate = Integer.parseInt(sc.nextLine().trim());
                                System.out.print("Nuevo nombre: ");
                                String nombreUpdate = sc.nextLine();
                                System.out.print("Nuevo precio: ");
                                double precioUpdate = Double.parseDouble(sc.nextLine().trim());
                                if (servicio.actualizarProducto(idUpdate, nombreUpdate, precioUpdate)) {
                                    System.out.println("Producto actualizado.");
                                } else {
                                    System.out.println("Producto no encontrado.");
                                }
                            } catch (NumberFormatException ex) {
                                System.out.println("Error: El ID y el precio deben ser números.");
                            }
                        } else {
                            System.out.println("No tienes permiso para actualizar productos.");
                        }
                        break;
                    case 4:
                        if (currentUser.hasRole("admin")) {
                            try {
                                System.out.print("ID del producto a eliminar: ");
                                int idDelete = Integer.parseInt(sc.nextLine().trim());
                                if (servicio.eliminarProducto(idDelete)) {
                                    System.out.println("Producto eliminado.");
                                } else {
                                    System.out.println("Producto no encontrado.");
                                }
                            } catch (NumberFormatException ex) {
                                System.out.println("Error: El ID debe ser un número.");
                            }
                        } else {
                            System.out.println("No tienes permiso para eliminar productos.");
                        }
                        break;
                    case 5:
                        System.out.println("Cerrando sesión...");
                        currentUser.logout();
                        opcion = 0; // sale al ciclo de login
                        break;
                    case 0:
                        System.out.println("¡Hasta luego!");
                        currentUser.logout();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            }
        }
        sc.close();
    }
}
