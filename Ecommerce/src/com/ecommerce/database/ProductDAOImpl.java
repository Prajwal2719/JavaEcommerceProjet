package com.ecommerce.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.ecommerce.interfaces.ProductDAOInterface;
import com.ecommerce.model.Product;

public class ProductDAOImpl implements ProductDAOInterface {

    @Override
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String query = "SELECT * FROM Products ORDER BY name";
        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setQuantity(rs.getInt("quantity"));
                productList.add(product);
            }
        } catch (Exception e) {
            System.out.println("Error fetching products.");
            e.printStackTrace();
        }
        return productList;
    }

	@Override
	public void addProduct(Product product) {
		
	}
}
