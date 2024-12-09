package com.fastcart.service.interf;

import java.util.List;

import com.fastcart.dto.CartItemDto;

public interface CartService {

	/**
	 * Obtiene una lista de los productos (representados como `CartItemDto`) que
	 * están en el carrito de un usuario específico.
	 *
	 * @param userName El nombre del usuario cuyo carrito de compras se va a
	 *                 consultar.
	 * @return Una lista de objetos `CartItemDto` que representan los productos en
	 *         el carrito.
	 */
	public List<CartItemDto> getProductsCart(String userName);

	/**
	 * Añade un producto al carrito de compras de un usuario específico. Si el
	 * producto ya está en el carrito, se actualiza la cantidad.
	 *
	 * @param userName  El nombre del usuario que está añadiendo un producto al
	 *                  carrito.
	 * @param idProduct El identificador del producto que se desea añadir al
	 *                  carrito.
	 * @param num       La cantidad del producto que se va a añadir.
	 *
	 * @return Un valor booleano que indica si la operación fue exitosa.
	 */
	public boolean addItemCart(String userName, Long idProduct, int num);

	/**
	 * Actualiza la cantidad de un producto en el carrito de compras de un usuario.
	 * Si la cantidad es mayor que 0, actualiza el producto; si es 0, elimina el
	 * producto del carrito.
	 *
	 * @param userName  El nombre del usuario cuyo carrito se va a actualizar.
	 * @param idProduct El identificador del producto que se va a actualizar o
	 *                  eliminar.
	 * @param num       La nueva cantidad del producto. Si es 0, se eliminará del
	 *                  carrito.
	 * @return Un valor booleano que indica si la operación fue exitosa.
	 */
	public boolean updateOrDeleteProductCart(String userName, Long idProduct, int num);

	/**
	 * Genera un archivo PDF con los productos del carrito de compras (factura) de
	 * un usuario específico.
	 *
	 * @param userName El nombre del usuario cuyo carrito se va a utilizar para
	 *                 generar el PDF.
	 * @return Un array de bytes que representa el contenido del PDF generado.
	 */
	public byte[] generateCartPdf(String userName);

}
