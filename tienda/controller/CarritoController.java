package com.example.tienda.controller;

// Permite trabajar con listas de objetos, como productos, pedidos o carritos.
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.tienda.model.Carrito;
import com.example.tienda.model.DetallePedido;
import com.example.tienda.model.Pago;
import com.example.tienda.model.Pedido;
import com.example.tienda.model.Producto;
import com.example.tienda.model.Usuario;
import com.example.tienda.repository.CarritoRepository;
import com.example.tienda.repository.DetallePedidoRepository;
import com.example.tienda.repository.PagoRepository;
import com.example.tienda.repository.PedidoRepository;
import com.example.tienda.repository.ProductoRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class CarritoController {

    // Repositorio para gestionar los productos del carrito
    @Autowired
    private CarritoRepository carritoRepository;

    // Repositorio para gestionar los productos
    @Autowired
    private ProductoRepository productoRepository;

    // Repositorio para gestionar los pedidos
    @Autowired
    private PedidoRepository pedidoRepository;

    // Repositorio para gestionar los detalles de cada pedido
    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    // Repositorio para gestionar los pagos
    @Autowired
    private PagoRepository pagoRepository;

    // ================= AGREGAR AL CARRITO =================

    // Agrega un producto al carrito del usuario
    @PostMapping("/agregar/{id}")
    public String agregarAlCarrito(
            @PathVariable Long id,
            HttpSession session
    ) {

        // Obtener usuario desde la sesión
        Usuario usuario =
                (Usuario) session.getAttribute("usuario");

        // Si no existe sesión, redirigir al login
        if (usuario == null) {

            return "redirect:/login";
        }

        // Buscar producto por su ID
        Producto producto =
                productoRepository.findById(id).orElse(null);

        if (producto != null) {

            // Verificar si el producto ya existe en el carrito
            Carrito carritoExistente =
                    carritoRepository.findByUsuario_IdAndProducto_Id(
                            usuario.getId(),
                            producto.getId()
                    );

            if (carritoExistente != null) {

                // Incrementar cantidad si ya existe
                carritoExistente.setCantidad(
                        carritoExistente.getCantidad() + 1
                );

                carritoRepository.save(carritoExistente);

            } else {

                // Crear nuevo registro en carrito
                Carrito nuevoCarrito = new Carrito();

                nuevoCarrito.setUsuario(usuario);

                nuevoCarrito.setProducto(producto);

                nuevoCarrito.setCantidad(1);

                carritoRepository.save(nuevoCarrito);
            }
        }

        return "redirect:/carrito";
    }

    // ================= VER CARRITO =================

    // Muestra los productos agregados al carrito
    @GetMapping("/carrito")
    public String verCarrito(
            Model model,
            HttpSession session
    ) {

        // Obtener usuario de la sesión
        Usuario usuario =
                (Usuario) session.getAttribute("usuario");

        // Validar inicio de sesión
        if (usuario == null) {

            return "redirect:/login";
        }

        // Obtener productos del carrito del usuario
        List<Carrito> carrito =
                carritoRepository.findByUsuarioId(
                        usuario.getId()
                );

        double total = 0;

        // Calcular total aplicando descuentos
        for (Carrito item : carrito) {

            double precio = item.getProducto().getPrecio();

            double descuento = item.getProducto().getDescuento();

            double precioFinal =
                    precio - (precio * descuento / 100);

            total += precioFinal * item.getCantidad();
        }

        // Enviar datos a la vista
        model.addAttribute("carrito", carrito);

        model.addAttribute("total", total);

        return "carrito";
    }

    // ================= ELIMINAR PRODUCTO =================

    // Elimina un producto específico del carrito
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(
            @PathVariable Long id
    ) {

        carritoRepository.deleteById(id);

        return "redirect:/carrito";
    }

    // ================= VACIAR CARRITO =================

    // Elimina todos los productos del carrito
    @PostMapping("/vaciar-carrito")
    public String vaciarCarrito(
            HttpSession session
    ) {

        // Obtener usuario de la sesión
        Usuario usuario =
                (Usuario) session.getAttribute("usuario");

        if (usuario == null) {

            return "redirect:/login";
        }

        // Obtener productos del carrito
        List<Carrito> carrito =
                carritoRepository.findByUsuarioId(
                        usuario.getId()
                );

        // Eliminar todos los registros
        carritoRepository.deleteAll(carrito);

        return "redirect:/carrito";
    }

    // ================= FINALIZAR COMPRA =================

    // Genera el pedido, detalles y pago de la compra
    @PostMapping("/finalizar-compra")
    public String finalizarCompra(
            @RequestParam String metodoPago,
            HttpSession session
    ) {

        // Obtener usuario autenticado
        Usuario usuario =
                (Usuario) session.getAttribute("usuario");

        if (usuario == null) {

            return "redirect:/login";
        }

        // Obtener carrito del usuario
        List<Carrito> carrito =
                carritoRepository.findByUsuarioId(
                        usuario.getId()
                );

        // Verificar que existan productos
        if (carrito.isEmpty()) {

            return "redirect:/carrito";
        }

        double total = 0;

        // Crear pedido
        Pedido pedido = new Pedido();

        pedido.setUsuario(usuario);

        // Calcular total de la compra
        for (Carrito item : carrito) {

            double precio = item.getProducto().getPrecio();

            double descuento =
                    item.getProducto().getDescuento();

            double precioFinal =
                    precio - (precio * descuento / 100);

            total += precioFinal * item.getCantidad();
        }

        pedido.setTotal(total);

        pedidoRepository.save(pedido);

        // Guardar detalles del pedido
        for (Carrito item : carrito) {

            double precio = item.getProducto().getPrecio();

            double descuento =
                    item.getProducto().getDescuento();

            double precioFinal =
                    precio - (precio * descuento / 100);

            DetallePedido detalle =
                    new DetallePedido();

            detalle.setPedido(pedido);

            detalle.setProducto(item.getProducto());

            detalle.setCantidad(item.getCantidad());

            detalle.setSubtotal(
                    precioFinal * item.getCantidad()
            );

            detallePedidoRepository.save(detalle);
        }

        // Registrar pago realizado
        Pago pago = new Pago();

        pago.setPedido(pedido);

        pago.setMetodoPago(metodoPago);

        pago.setMonto(total);

        pago.setEstado("PAGADO");

        pagoRepository.save(pago);

        pagoRepository.save(pago);

        // Vaciar carrito luego de finalizar compra
        carritoRepository.deleteAll(carrito);

        return "redirect:/compra-exitosa";
    }

    // ================= COMPRA EXITOSA =================

    // Muestra mensaje de compra realizada correctamente
    @GetMapping("/compra-exitosa")
    public String compraExitosa(
            HttpSession session,
            Model model
    ) {

        Usuario usuario =
                (Usuario) session.getAttribute("usuario");

        model.addAttribute(
                "nombreUsuario",
                usuario.getNombre()
        );

        return "compra-exitosa";
    }

    // ================= HISTORIAL DE COMPRAS =================

    // Muestra todos los pedidos realizados por el usuario
    @GetMapping("/historial-compras")
    public String historialCompras(
            HttpSession session,
            Model model
    ) {

        Usuario usuario =
                (Usuario) session.getAttribute("usuario");

        if (usuario == null) {

            return "redirect:/login";
        }

        List<Pedido> pedidos =
                pedidoRepository.findByUsuarioId(
                        usuario.getId()
                );

        model.addAttribute(
                "pedidos",
                pedidos
        );

        return "historial-compras";
    }

    // ================= FACTURA =================

    // Muestra la factura de un pedido específico
    @GetMapping("/factura/{id}")
    public String verFactura(
            @PathVariable Long id,
            Model model
    ) {

        Pedido pedido =
                pedidoRepository.findById(id)
                        .orElse(null);

        if (pedido == null) {

            return "redirect:/historial-compras";
        }

        // Obtener detalles del pedido
        List<DetallePedido> detalles =
                detallePedidoRepository
                        .findByPedidoId(id);

        model.addAttribute(
                "pedido",
                pedido
        );

        model.addAttribute(
                "detalles",
                detalles
        );

        return "factura";
    }

    // ================= DETALLE DEL PEDIDO =================

    // Muestra el detalle completo de un pedido
    @GetMapping("/detalle-pedido/{id}")
    public String detallePedido(
            @PathVariable Long id,
            Model model
    ) {

        Pedido pedido =
                pedidoRepository.findById(id).orElse(null);

        if (pedido == null) {

            return "redirect:/historial-compras";
        }

        List<DetallePedido> detalles =
                detallePedidoRepository.findByPedidoId(id);

        model.addAttribute("pedido", pedido);

        model.addAttribute("detalles", detalles);

        return "detalle-pedido";
    }
}