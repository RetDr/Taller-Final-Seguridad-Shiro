package uptc.edu.co.service;

import uptc.edu.co.model.Producto;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {
    private List<Producto> productos = new ArrayList<>();

    // CREATE
    public void agregarProducto(Producto p) {
        productos.add(p);
    }

    // READ
    public List<Producto> listarProductos() {
        return productos;
    }

    // UPDATE
    public boolean actualizarProducto(int id, String nuevoNombre, double nuevoPrecio) {
        for (Producto p : productos) {
            if (p.getId() == id) {
                p.setNombre(nuevoNombre);
                p.setPrecio(nuevoPrecio);
                return true;
            }
        }
        return false;
    }

    // DELETE
    public boolean eliminarProducto(int id) {
        return productos.removeIf(p -> p.getId() == id);
    }
}
