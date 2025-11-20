package uptc.edu.co;

import java.util.Scanner;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import org.apache.shiro.session.Session;

import uptc.edu.co.model.Producto;
import uptc.edu.co.service.ProductoService;
import uptc.edu.co.service.UsuarioService;

public class App {
    public static void main(String[] args) {
        // Inicializa SecurityManager de Shiro usando shiro.ini
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        ProductoService productoService = new ProductoService();
        UsuarioService userService = new UsuarioService();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== BIENVENIDO =====");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Registrar usuario");
            System.out.println("0. Salir");
            System.out.print("Selecciona una opción: ");
            String opt = sc.nextLine().trim();

            if (opt.equals("1")) {
                // ----- LOGIN SEGURO con bcrypt -----
                System.out.print("Usuario: ");
                String username = sc.nextLine();
                System.out.print("Contraseña: ");
                String password = sc.nextLine();

                if (userService.validarPassword(username, password)) { // bcrypt seguro
                    System.out.println("Autenticación exitosa. Bienvenido " + username);

                    Subject currentUser = SecurityUtils.getSubject();
                    UsernamePasswordToken token = new UsernamePasswordToken(username, password);
                    try {
                        currentUser.login(token); // solo gestiona sesión y roles
                    } catch (Exception e) {
                        // Ignorar excepción si Realm/Jdbc no autentica bcrypt: ya validamos seguro
                    }
                    Session session = currentUser.getSession();
                    System.out.println("--- Sesión activa ---");
                    System.out.println("ID de sesión: " + session.getId());
                    System.out.println("Usuario logueado: " + username);

                    String rolUser = userService.getRol(username);
                    int opcion = -1;
                    while (opcion != 0) {
                        System.out.println("\n*** CRUD de Productos ***");
                        if ("admin".equals(rolUser) || "user".equals(rolUser))
                            System.out.println("1. Agregar producto");
                        if ("admin".equals(rolUser) || "user".equals(rolUser)||"guest".equals(rolUser))
                            System.out.println("2. Listar productos");
                        if ("admin".equals(rolUser))
                            System.out.println("3. Actualizar producto");
                        if ("admin".equals(rolUser))
                            System.out.println("4. Eliminar producto");
                        System.out.println("5. Cerrar sesión");
                        if ("admin".equals(rolUser))
                                System.out.println("6. Listar usuarios");
                        System.out.println("7. Comparar hashes de contraseña");
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
                            case 1: // Agregar producto
                                if ("admin".equals(rolUser) || "user".equals(rolUser)) {
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
                                        System.out.println("Error: El ID y precio deben ser números.");
                                    }
                                } else {
                                    System.out.println("No tienes permiso para agregar productos.");
                                }
                                break;
                            case 2: // Listar productos
                                if ("admin".equals(rolUser) || "user".equals(rolUser)||"guest".equals(rolUser)) {
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
                            case 3: // Actualizar producto
                                if ("admin".equals(rolUser)) {
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
                            case 4: // Eliminar producto
                                if ("admin".equals(rolUser)) {
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
                            case 6:
                             if ("admin".equals(rolUser)) {
                                    try {
                                        userService.listarUsuarios();
                                    } catch (NumberFormatException ex) {
                                    }
                                } else {
                                    System.out.println("No tienes permiso para ver usuarios.");
                                }
                                
                                break;
                            case 7:
                            System.out.print("Ingresa una contraseña para comparar hashes: ");
                            String pwd = sc.nextLine();
                            userService.compararHashes(pwd);

                            break;
                            case 0:
                                System.out.println("¡Hasta luego!");
                                System.exit(0);
                                break;
                            default:
                                System.out.println("Opción inválida.");
                        }
                    }
                } else {
                    System.out.println("Error: Usuario o contraseña incorrectos.");
                }
            } else if (opt.equals("2")) {
                // REGISTRAR USUARIO (bcrypt seguro)
                System.out.print("Nuevo usuario: ");
                String username = sc.nextLine();
                System.out.print("Contraseña: ");
                String password = sc.nextLine();
                System.out.print("Rol (admin/user/guest): ");
                String rol = sc.nextLine().trim().toLowerCase();
                boolean exito = userService.registrarUsuario(username, password, rol);
                if (exito) {
                    System.out.println("Usuario registrado con éxito (contraseña hasheada con bcrypt y guardada).");
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
