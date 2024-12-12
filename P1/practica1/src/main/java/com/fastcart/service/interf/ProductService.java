package com.fastcart.service.interf;

import java.util.List;
import java.util.Optional;

import com.fastcart.model.Product;

public interface ProductService {

	/**
	 * Obtiene una lista de todos los productos disponibles.
	 *
	 * @return Una lista de objetos `Product` que representan todos los productos.
	 */
	public List<Product> getAllProducts();

	/**
	 * Busca un producto por su ID.
	 *
	 * @param id El identificador del producto que se va a buscar.
	 * @return Un `Optional<Product>` que contiene el producto si se encuentra, o
	 *         vacío si no existe.
	 */
	public Optional<Product> getProductById(Long id);

	/**
	 * Verifica si un producto existe en el sistema por su ID.
	 *
	 * @param id El identificador del producto que se va a verificar.
	 * @return `true` si el producto existe, `false` en caso contrario.
	 */
	public boolean doesProductExist(Long id);

	/**
	 * Guarda un nuevo producto en el sistema.
	 *
	 * @param product Un objeto `ProductDto` que contiene los datos del producto a
	 *                guardar.
	 * @return El objeto `Product` guardado.
	 */
	public Product saveProduct(Product product);

	/**
	 * Edita un producto existente.
	 *
	 * @param product Un objeto `ProductDto` que contiene los nuevos datos del
	 *                producto.
	 * @return El objeto `Product` editado.
	 */
	public Product editProduct(Product product);

	/**
	 * Elimina un producto por su ID.
	 *
	 * @param id El identificador del producto que se va a eliminar.
	 * @return `true` si el producto fue eliminado con éxito, `false` si no se pudo
	 *         eliminar.
	 */
	public boolean deleteProduct(Long id);

	/**
	 * Busca productos por su nombre y filtra por rango de precio.
	 *
	 * @param name     El nombre (o parte del nombre) del producto a buscar.
	 * @param minPrice El precio mínimo en el filtro.
	 * @param maxPrice El precio máximo en el filtro.
	 * @return Una lista de productos que coinciden con los criterios de búsqueda y
	 *         filtro.
	 */
	public List<Product> searchAndFilterProducts(String name, Double minPrice, Double maxPrice);

	/**
	 * Exporta todos los productos a un script SQL
	 *
	 * @return Un array de bytes que representa el contenido del archivo exportado.
	 */
	public byte[] exportProducts();

}
