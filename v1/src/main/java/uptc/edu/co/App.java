package uptc.edu.co;

import java.util.Scanner;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import uptc.edu.co.model.Producto;
import uptc.edu.co.service.ProductoService;
import uptc.edu.co.service.UsuarioService;

public class App {
    public static void main(String[] args) {
        // Configuración de Apache Shiro
        IniRealm iniRealm = new IniRealm("classpath:shiro.ini");
        SecurityManager securityManager = new DefaultSecurityManager(iniRealm);
        SecurityUtils.setSecurityManager(securityManager);

        UsuarioService userService = new UsuarioService();
        ProductoService productoService = new ProductoService();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== BIENVENIDO =====");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Registrar usuario");
            System.out.println("0. Salir");
            System.out.print("Selecciona una opción: ");
            String opt = sc.nextLine().trim();

            if (opt.equals("1")) {
                // Iniciar sesión
                System.out.print("Usuario: ");
                String username = sc.nextLine();
                System.out.print("Contraseña: ");
                String password = sc.nextLine();

                Subject currentUser = SecurityUtils.getSubject();
                UsernamePasswordToken token = new UsernamePasswordToken(username, password);

                try {
                    currentUser.login(token); // Autenticación vía Shiro
                    System.out.println("Autenticación exitosa. Bienvenido " + currentUser.getPrincipal());

                    // Información de sesión
                    System.out.println("----- Información de sesión -----");
                    System.out.println("ID sesión: " + currentUser.getSession().getId());
                    System.out.println("Usuario logueado: " + currentUser.getPrincipal());
                    System.out.println("Inicio sesión: " + currentUser.getSession().getStartTimestamp());
                    System.out.println("Timeout (ms): " + currentUser.getSession().getTimeout());

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
                                if (currentUser.isPermitted("producto:add")) {
                                    try {
                                        System.out.print("ID: ");
                                        int id = Integer.parseInt(sc.nextLine().trim());
                                        System.out.print("Nombre: ");
                                        String nombre = sc.nextLine();
                                        System.out.print("Precio: ");
                                        double precio = Double.parseDouble(sc.nextLine().trim());
                                        Producto nuevo = new Producto(id, nombre, precio);
                                        productoService.agregarProducto(nuevo);
                                        System.out.println("Producto agregado.");
                                    } catch (NumberFormatException ex) {
                                        System.out.println("Error: El ID y el precio deben ser números.");
                                    }
                                } else {
                                    System.out.println("No tienes permiso para agregar productos.");
                                }
                                break;
                            case 2:
                                if (currentUser.isPermitted("producto:view")) {
                                    System.out.println("Lista de productos:");
                                    if (productoService.listarProductos().isEmpty()) {
                                        System.out.println("No hay productos registrados.");
                                    } else {
                                        for (Producto p : productoService.listarProductos()) {
                                            System.out.println(p);
                                        }
                                    }
                                } else {
                                    System.out.println("No tienes permiso para ver los productos.");
                                }
                                break;
                            case 3:
                                if (currentUser.isPermitted("producto:update")) {
                                    try {
                                        System.out.print("ID de producto a actualizar: ");
                                        int idUpdate = Integer.parseInt(sc.nextLine().trim());
                                        System.out.print("Nuevo nombre: ");
                                        String nombreUpdate = sc.nextLine();
                                        System.out.print("Nuevo precio: ");
                                        double precioUpdate = Double.parseDouble(sc.nextLine().trim());
                                        if (productoService.actualizarProducto(idUpdate, nombreUpdate, precioUpdate)) {
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
                                if (currentUser.isPermitted("producto:delete")) {
                                    try {
                                        System.out.print("ID del producto a eliminar: ");
                                        int idDelete = Integer.parseInt(sc.nextLine().trim());
                                        if (productoService.eliminarProducto(idDelete)) {
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
                                opcion = 0;
                                break;
                            case 0:
                                System.out.println("¡Hasta luego!");
                                System.exit(0);
                                break;
                            default:
                                System.out.println("Opción inválida.");
                        }
                    }
                } catch (AuthenticationException ae) {
                    System.out.println("Error: Usuario o contraseña incorrectos.");
                }
            } else if (opt.equals("2")) {
                // Registro de usuario
                System.out.print("Nuevo usuario: ");
                String username = sc.nextLine();
                System.out.print("Contraseña: ");
                String password = sc.nextLine();
                System.out.print("Rol (user/guest): ");
                String rol = sc.nextLine().trim().toLowerCase();
                boolean exito = userService.registrarUsuario(username, password, rol);
                if (exito) {
                    System.out.println("Usuario registrado con éxito (contraseña hasheada y guardada).");

                    // Comparativo de hash débil vs fuerte
                    userService.compararHashes(password);
                } else {
                    System.out.println("Ya existe ese usuario.");
                }
            } else if (opt.equals("0")) {
                System.out.println("¡Hasta luego!");
                break;
            } else {
                System.out.println("Opción inválida.");
            }
        }
        sc.close();
    }
}
