/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productbill.database;

import productbill.tab_bill.BillingDetail;
import productbill.customer.Customer;
import productbill.product.Product;
import productbill.category.Category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import productbill.hsn.HSN;
import productbill.order.BillOrder;
import productbill.stock.Stock;
import productbill.tab_bill.OrderDetail;
import productbill.tab_bill.TotalBill;
import productbill.utils.Utils;

/**
 *
 * @author iCT-3
 */
public class SQLHelper {

	private final Connection conn;

	public SQLHelper() {
		conn = SQLConnection.connection();
	}

	public void createTables() {
		String userTable = "CREATE TABLE IF NOT EXISTS USER (id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , name TEXT, email TEXT, passward TEXT, mobile TEXT)";
		String vendorTable = "CREATE TABLE IF NOT EXISTS VENDOR (id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , company_name TEXT, contact_person TEXT, contact_number TEXT, address TEXT, reg_number TEXT)";
		String orderDetailTable = "CREATE TABLE IF NOT EXISTS ORDER_DETAIL (id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , product_id INTEGER, bill_id INTEGER, quantity INTEGER, created_on DATETIME DEFAULT CURRENT_TIMESTAMP, modify_on DATETIME DEFAULT CURRENT_TIMESTAMP)";
		String customerTable = "CREATE TABLE IF NOT EXISTS CUSTOMER (id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , name TEXT, mobile TEXT, address TEXT, gender TEXT)";
		String categoryTable = "CREATE TABLE IF NOT EXISTS CATEGORY (id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , name TEXT, parent_category_id TEXT)";
		String productStockTable = "CREATE TABLE IF NOT EXISTS PRODUCT_STOCK (id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , product_id INTEGER, quantity INTEGER)";
		String productTable = "CREATE TABLE IF NOT EXISTS PRODUCT (id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , title TEXT, desc TEXT, category_id TEXT, code TEXT, measure_type TEXT, purchased_price TEXT, purchased_from TEXT, hsn_id INTEGER, selling_price TEXT, mrp TEXT, color TEXT, tags TEXT, vendor_id TEXT, active TEXT)";
		String billTable = "CREATE TABLE IF NOT EXISTS BILL_DETAIL (id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , productName TEXT, categoryName TEXT, price TEXT, quantity TEXT, total TEXT, customer_id INTEGER NOT NULL, product_id INTEGER NOT NULL)";
		String totalBill = "CREATE TABLE IF NOT EXISTS BILL (id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , total_amount TEXT, customer_id TEXT, exchange_count INTEGER DEFAULT 0, created_on TEXT DEFAULT CURRENT_TIMESTAMP, modify_on TEXT DEFAULT CURRENT_TIMESTAMP)";
		String hsnTable = "CREATE TABLE IF NOT EXISTS HSN (id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , hsn_code TEXT, description TEXT,type INTEGER, gst FLOAT)";

		try {
			PreparedStatement userTableStr = conn.prepareStatement(userTable);
			PreparedStatement vendorTableStr = conn.prepareStatement(vendorTable);
			PreparedStatement orderDetailTableStr = conn.prepareStatement(orderDetailTable);
			PreparedStatement customerTableStr = conn.prepareStatement(customerTable);
			PreparedStatement categoryTableStr = conn.prepareStatement(categoryTable);
			PreparedStatement productStockTableStr = conn.prepareStatement(productStockTable);
			PreparedStatement productTableStr = conn.prepareStatement(productTable);
			PreparedStatement billTableStr = conn.prepareStatement(billTable);
			PreparedStatement totalBillStr = conn.prepareStatement(totalBill);
			PreparedStatement hsnStr = conn.prepareStatement(hsnTable);

			userTableStr.execute();
			vendorTableStr.execute();
			orderDetailTableStr.execute();
			customerTableStr.execute();
			categoryTableStr.execute();
			productStockTableStr.execute();
			productTableStr.execute();
			billTableStr.execute();
			totalBillStr.execute();
			hsnStr.execute();
		} catch (SQLException ex) {
			System.out.println(ex);
		}
	}

	public boolean deleteAllTable() {
		String query = "DROP TABLE HSN";// , customer_details, product_bill, vendor, bill_details";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			int count = stmt.executeUpdate();
			boolean isSuccess = count == 1;
			stmt.close();
			return isSuccess;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	public boolean insertCategory(Category user) {
		String query = "INSERT INTO CATEGORY (id, name, parent_category_id) VALUES (?,?,?)";
		try {
			boolean isSuccess;
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, user.getId());
			stmt.setString(2, user.getName());
			stmt.setString(3, user.getParentCategory());

			int count = stmt.executeUpdate();
			isSuccess = count == 1;
			stmt.close();

			return isSuccess;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	public int insertCategoryReturnID(Category user) {
		String query = "INSERT INTO CATEGORY (id, name, parent_category_id) VALUES (?,?,?)";
		try {
			PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, user.getId());
			stmt.setString(2, user.getName());
			stmt.setString(3, user.getParentCategory());

			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			int key = 0;
			if (rs.next()) {
				// Retrieve the auto generated key(s).
				key = rs.getInt(1);
			}
			stmt.close();
			return key;
		} catch (SQLException e) {
			System.out.println(e);
			return 0;
		}
	}

	public ObservableList<Category> getAllCategory() {
		ObservableList<Category> data = FXCollections.observableArrayList();
		String query = "SELECT CATEGORIES1.id AS category_id,CATEGORIES1.name AS category_name,CATEGORIES1.parent_category_id AS category_parent_id,CATEGORIES2.name AS parent_name FROM CATEGORY AS CATEGORIES1 LEFT JOIN CATEGORY AS CATEGORIES2 ON CATEGORIES1.parent_category_id = CATEGORIES2.id ";

		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				data.add(new Category(rs.getString("category_id"), rs.getString("category_name"),
						rs.getString("category_parent_id"), rs.getString("parent_name")));
			}
			stmt.close();
			return data;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
	}

	public boolean deleteCategory(String id) {
		String query = "DELETE FROM CATEGORY WHERE id = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, id);
			int count = stmt.executeUpdate();
			boolean isSuccess = count == 1;
			stmt.close();
			return isSuccess;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	public ObservableList<Category> searchCategory(String id) {
		ObservableList<Category> data = FXCollections.observableArrayList();
		String query = "SELECT CATEGORIES1.id AS category_id,CATEGORIES1.name AS category_name,CATEGORIES1.parent_category_id AS category_parent_id,CATEGORIES2.name AS parent_name FROM CATEGORY AS CATEGORIES1 LEFT JOIN CATEGORY AS CATEGORIES2 ON CATEGORIES1.parent_category_id = CATEGORIES2.id WHERE CATEGORIES1.id = ?";

		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				data.add(new Category(rs.getString("category_id"), rs.getString("category_name"),
						rs.getString("category_parent_id"), rs.getString("parent_name")));
			}
			stmt.close();
			return data;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
	}

	public boolean updateCategory(Category category) {
		String query = "UPDATE CATEGORY SET name = ?, id = ?, parent_category_id = ? WHERE id = ?";
		try {
			boolean isSuccess;
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, category.getName());
			stmt.setString(2, category.getId());
			stmt.setString(3, category.getParentCategory());
			stmt.setString(4, category.getId());

			int count = stmt.executeUpdate();
			isSuccess = count == 1;
			stmt.close();

			return isSuccess;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	public ObservableList<Category> getAllSubCategory() {
		ObservableList<Category> data = FXCollections.observableArrayList();
		String query = "SELECT CATEGORIES1.id AS category_id,CATEGORIES1.name AS category_name,CATEGORIES1.parent_category_id AS category_parent_id,CATEGORIES2.name AS parent_name FROM CATEGORY AS CATEGORIES1 LEFT JOIN CATEGORY AS CATEGORIES2 ON CATEGORIES1.parent_category_id = CATEGORIES2.id WHERE parent_category_id != 0";

		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				data.add(new Category(rs.getString("category_id"), rs.getString("category_name"),
						rs.getString("category_parent_id"), rs.getString("parent_name")));
			}
			stmt.close();
			return data;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
	}

	public ObservableList<Product> getAllProduct() {
		ObservableList<Product> data = FXCollections.observableArrayList();
		String query = "SELECT * FROM PRODUCT LEFT JOIN HSN ON HSN.id=PRODUCT.hsn_id LEFT JOIN PRODUCT_STOCK ON PRODUCT_STOCK.product_id=PRODUCT.id";

		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Product product = new Product(rs.getString("id"), rs.getString("title"), rs.getString("desc"),
						rs.getString("category_id"), rs.getString("code"), rs.getString("measure_type"),
						rs.getString("purchased_price"), rs.getString("purchased_from"), rs.getString("hsn_id"),
						rs.getString("selling_price"), rs.getString("mrp"), rs.getString("color"), rs.getString("tags"),
						rs.getString("vendor_id"), rs.getString("active"), rs.getString("gst"));
				product.setQuantity(rs.getInt("quantity"));
				data.add(product);
			}
			stmt.close();
			return data;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
	}

	public boolean insertProduct(Product product) {
		String query = "INSERT INTO PRODUCT (title , desc , category_id , code , measure_type , purchased_price , purchased_from , hsn_id , selling_price , mrp , color , tags , vendor_id , active) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			boolean isSuccess;
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, product.getTitle());
			stmt.setString(2, product.getDesc());
			stmt.setString(3, product.getCategoryId());
			stmt.setString(4, product.getCode());
			stmt.setString(5, product.getMeasureType());
			stmt.setString(6, product.getPurchasedPrice());
			stmt.setString(7, product.getPurchasedFrom());
			stmt.setString(8, product.getHsnCode());
			stmt.setString(9, product.getSellingPrice());
			stmt.setString(10, product.getMrp());
			stmt.setString(11, product.getColor());
			stmt.setString(12, product.getTags());
			stmt.setString(13, product.getVendorId());
			stmt.setString(14, product.getActive());

			int count = stmt.executeUpdate();
			isSuccess = count == 1;
			stmt.close();

			return isSuccess;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	public boolean updateProduct(Product product) {
		String query = "UPDATE PRODUCT SET id = ?, title = ?, desc = ?, category_id = ?, code = ?, measure_type = ?, purchased_price = ?, purchased_from = ?, hsn_id = ?, selling_price = ?, mrp = ?, color = ?, tags = ?, vendor_id = ?, active = ? WHERE id = ?";
		try {
			boolean isSuccess;
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, product.getId());
			stmt.setString(2, product.getTitle());
			stmt.setString(3, product.getDesc());
			stmt.setString(4, product.getCategoryId());
			stmt.setString(5, product.getCode());
			stmt.setString(6, product.getMeasureType());
			stmt.setString(7, product.getPurchasedPrice());
			stmt.setString(8, product.getPurchasedFrom());
			stmt.setString(9, product.getHsnCode());
			stmt.setString(10, product.getSellingPrice());
			stmt.setString(11, product.getMrp());
			stmt.setString(12, product.getColor());
			stmt.setString(13, product.getTags());
			stmt.setString(14, product.getVendorId());
			stmt.setString(15, product.getActive());
			stmt.setString(16, product.getId());

			int count = stmt.executeUpdate();
			isSuccess = count == 1;
			stmt.close();

			return isSuccess;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	public boolean updateProductStockWithID(String id, String quantity) {
		String query = "UPDATE PRODUCT_STOCK SET quantity = ? WHERE product_id = ?";
		try {
			boolean isSuccess;
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, quantity);
			stmt.setString(2, id);

			int count = stmt.executeUpdate();
			isSuccess = count == 1;
			stmt.close();

			return isSuccess;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	public boolean deleteProduct(String id) {
		String query = "DELETE FROM PRODUCT WHERE id = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, id);
			int count = stmt.executeUpdate();
			boolean isSuccess = count == 1;
			stmt.close();
			return isSuccess;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	public boolean insertCustomer(Customer customer) {
		String query = "INSERT INTO CUSTOMER (name, mobile, address, gender) VALUES (?,?,?,?)";
		try {
			boolean isSuccess;
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, customer.getName());
			stmt.setString(2, customer.getMobile());
			stmt.setString(3, customer.getAddress());
			stmt.setString(4, customer.getGender());

			int count = stmt.executeUpdate();
			isSuccess = count == 1;
			stmt.close();

			return isSuccess;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	public ObservableList<Customer> getAllCustomer() {
		ObservableList<Customer> data = FXCollections.observableArrayList();
		String query = "SELECT * FROM CUSTOMER";

		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				data.add(new Customer(rs.getString("id"), rs.getString("name"), rs.getString("mobile"),
						rs.getString("address"), rs.getString("gender")));
			}
			stmt.close();
			return data;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
	}

	public boolean deleteCustomer(String id) {
		String query = "DELETE FROM CUSTOMER WHERE id = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, id);
			int count = stmt.executeUpdate();
			boolean isSuccess = count == 1;
			stmt.close();
			return isSuccess;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	public ObservableList<Customer> searchCustomer(String id) {
		ObservableList<Customer> data = FXCollections.observableArrayList();
		String query = "SELECT * FROM CUSTOMER WHERE id = ?";

		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				data.add(new Customer(rs.getString("id"), rs.getString("name"), rs.getString("mobile"),
						rs.getString("address"), rs.getString("gender")));
			}
			stmt.close();

			stmt.close();
			return data;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
	}

	public boolean updateCustomer(Customer customer) {
		String query = "UPDATE CUSTOMER SET id = ?, name = ?, mobile = ?, address = ?, gender = ? WHERE id = ?";
		try {
			boolean isSuccess;
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, customer.getId());
			stmt.setString(2, customer.getName());
			stmt.setString(3, customer.getMobile());
			stmt.setString(4, customer.getAddress());
			stmt.setString(5, customer.getGender());
			stmt.setString(6, customer.getId());

			int count = stmt.executeUpdate();
			isSuccess = count == 1;
			stmt.close();

			return isSuccess;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	public boolean insertBill(BillingDetail bill) {
		String query = "INSERT INTO BILL_DETAIL (productName, categoryName, price, quantity, total, customer_id, product_id) VALUES (?,?,?,?,?,?,?)";
		try {
			boolean isSuccess;
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, bill.getProductName());
			stmt.setString(2, bill.getCategoryName());
			stmt.setString(3, bill.getPrice());
			stmt.setString(4, bill.getQuantity());
			stmt.setString(5, bill.getTotal());
			stmt.setString(6, bill.getCustomerId());
			stmt.setString(7, bill.getProductId());

			int count = stmt.executeUpdate();
			isSuccess = count == 1;
			stmt.close();

			return isSuccess;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	public ObservableList<BillingDetail> getAllBill() {
		ObservableList<BillingDetail> data = FXCollections.observableArrayList();
		String query = "SELECT * FROM BILL_DETAIL";

		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				data.add(new BillingDetail(rs.getString("id"), rs.getString("product_id"), rs.getString("productName"),
						rs.getString("email"), rs.getString("discount"), rs.getString("balance"),
						rs.getString("available"), rs.getString("id")));
			}
			stmt.close();

			stmt.close();
			return data;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
	}

	public boolean deleteBillDetails() {
		String query = "DELETE FROM BILL_DETAIL";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			int count = stmt.executeUpdate();
			boolean isSuccess = count == 1;
			stmt.close();
			return isSuccess;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public int insertTotalBill(TotalBill bill) {
		String query = "INSERT INTO BILL (total_amount, customer_id) VALUES (?,?)";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, bill.getTotalAmount());
			stmt.setString(2, bill.getCustomerId());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			int key = 0;
			if (rs.next()) {
				// Retrieve the auto generated key(s).
				key = rs.getInt(1);
			}
			stmt.close();
			return key;
		} catch (SQLException e) {
			System.out.println(e);
			return 0;
		}
	}

	public ObservableList<TotalBill> getAllTotalBill() {
		ObservableList<TotalBill> data = FXCollections.observableArrayList();
		String query = "SELECT * FROM BILL LEFT JOIN CUSTOMER ON BILL.customer_id = CUSTOMER.id";

		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				if (rs.getString("name") == "" || rs.getString("name") == null) {
					data.add(new TotalBill(rs.getString("id"), rs.getString("total_amount"),
							rs.getString("customer_id"), rs.getString("customer_id"), rs.getString("created_on"),
							rs.getString("modify_on")));
				} else {
					data.add(
							new TotalBill(rs.getString("id"), rs.getString("total_amount"), rs.getString("customer_id"),
									rs.getString("name"), rs.getString("created_on"), rs.getString("modify_on")));
				}
			}
			stmt.close();
			return data;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
	}

	public ObservableList<TotalBill> getAllSelectedDateTotalBill(String fromDate, String toDate, String searchBy) {
		ObservableList<TotalBill> data = FXCollections.observableArrayList();
		String query = "SELECT * FROM BILL LEFT JOIN CUSTOMER ON BILL.customer_id = CUSTOMER.id WHERE (BILL.created_on BETWEEN ? AND ?) AND ((BILL.customer_id LIKE ?) OR (BILL.id LIKE ?) OR (CUSTOMER.name LIKE ?)) ;";

		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, fromDate);
			stmt.setString(2, toDate);
			if (searchBy.trim().equals("")) {
				stmt.setString(3, "%");
				stmt.setString(4, "%");
				stmt.setString(5, "%");
			} else {
				stmt.setString(3, "%" + searchBy + "%");
				stmt.setString(4, "%" + searchBy + "%");
				stmt.setString(5, "%" + searchBy + "%");
			}

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				if (rs.getString("name") == "" || rs.getString("name") == null) {
					data.add(new TotalBill(rs.getString("id"), rs.getString("total_amount"),
							rs.getString("customer_id"), rs.getString("customer_id"), rs.getString("created_on"),
							rs.getString("modify_on")));
				} else {
					data.add(
							new TotalBill(rs.getString("id"), rs.getString("total_amount"), rs.getString("customer_id"),
									rs.getString("name"), rs.getString("created_on"), rs.getString("modify_on")));
				}
			}
			stmt.close();
			return data;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
	}

	public boolean insertStock(Stock stock) {
		String query = "INSERT INTO PRODUCT_STOCK (product_id, quantity) VALUES (?,?)";
		try {
			boolean isSuccess;
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, stock.getProductId());
			stmt.setString(2, stock.getQuantity());

			int count = stmt.executeUpdate();
			isSuccess = count == 1;
			stmt.close();

			return isSuccess;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	public ObservableList<HSN> getAllHSN() {
		ObservableList<HSN> data = FXCollections.observableArrayList();
		String query = "SELECT * FROM HSN";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				data.add(new HSN(rs.getString("id"), rs.getString("hsn_code"), rs.getString("gst"),
						rs.getString("description"), rs.getString("type"),
						((rs.getString("type") != null) && (rs.getString("type").equals("0")) ? "Service" : "Goods")));
			}
			stmt.close();
			return data;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
	}

	public boolean checkHSN(String hsncode) {
		String query = "SELECT * FROM HSN WHERE hsn_code = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, hsncode);
			ResultSet rs = stmt.executeQuery();
			stmt.close();
			if (rs.getRow() > 0) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	public boolean insertHSN(HSN hsn) {
		String query = "INSERT INTO HSN (hsn_code, gst ,description,type) VALUES (?,?,?,?)";
		try {
			boolean isSuccess;
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, hsn.getHSN());
			stmt.setString(2, hsn.getGST());
			stmt.setString(3, hsn.getDescription());
			stmt.setString(4, hsn.getType());

			int count = stmt.executeUpdate();
			isSuccess = count == 1;
			stmt.close();

			return isSuccess;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	public boolean updateHSN(HSN hsn) {
		String query = "UPDATE HSN SET gst = ?,hsn_code = ?,description = ?,type = ? WHERE id = ?";
		try {
			boolean isSuccess;
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, hsn.getGST());
			stmt.setString(2, hsn.getHSN());
			stmt.setString(3, hsn.getDescription());
			stmt.setString(4, hsn.getType());
			stmt.setString(5, hsn.getId());

			int count = stmt.executeUpdate();
			isSuccess = count == 1;
			stmt.close();

			return isSuccess;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	public boolean updateHSNWithCode(HSN hsn) {
		String query = "UPDATE HSN SET gst = ?,description = ?,type = ? WHERE hsn_code = ?";
		try {
			boolean isSuccess;
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, hsn.getGST());
			stmt.setString(2, hsn.getHSN());
			stmt.setString(3, hsn.getType());

			int count = stmt.executeUpdate();
			isSuccess = count == 1;
			stmt.close();

			return isSuccess;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	public boolean deleteHSN(HSN hsn) {
		String query = "DELETE FROM HSN WHERE id = ?";
		try {
			boolean isSuccess;
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, hsn.getId());

			int count = stmt.executeUpdate();
			isSuccess = count == 1;
			stmt.close();

			return isSuccess;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	public HSN getHSN(String id) {
		String query = "SELECT * FROM HSN WHERE id = ?";
		HSN hsn = null;
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				hsn = new HSN(rs.getString("id"), rs.getString("hsn_code"), rs.getString("gst"),
						rs.getString("description"), rs.getString("type"),
						(rs.getString("type").equals("0") ? "Service" : "Goods"));
			}

			stmt.close();
			return hsn;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
	}

	public boolean updateProductStock(Stock stock) {
		String query = "UPDATE PRODUCT_STOCK SET product_id = ?, quantity = ? WHERE product_id = ?";
		try {
			boolean isSuccess;
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, stock.getProductId());
			stmt.setString(2, stock.getQuantity());
			stmt.setString(3, stock.getProductId());

			int count = stmt.executeUpdate();
			isSuccess = count == 1;
			stmt.close();

			return isSuccess;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	public ObservableList<Stock> getAllProductStock() {
		ObservableList<Stock> data = FXCollections.observableArrayList();
		String query = "SELECT * FROM PRODUCT_STOCK LEFT JOIN PRODUCT ON PRODUCT_STOCK.product_id = PRODUCT.id";

		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				data.add(new Stock(rs.getString("id"), rs.getString("product_id"), rs.getString("title"),
						rs.getString("quantity")));
			}
			stmt.close();
			return data;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
	}

	public ObservableList<Stock> getAllProductStockDetails() {
		ObservableList<Stock> data = FXCollections.observableArrayList();
		String query = "SELECT * FROM PRODUCT LEFT JOIN PRODUCT_STOCK ON PRODUCT.id = PRODUCT_STOCK.product_id";

		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				data.add(new Stock(rs.getString("id"), rs.getString("product_id"), rs.getString("title"),
						rs.getString("quantity")));
			}
			stmt.close();
			return data;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
	}

	public Stock getProductStock(String id) {
		String query = "SELECT * FROM PRODUCT_STOCK WHERE product_id = ?";
		Stock stock = null;
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				stock = new Stock(rs.getString("id"), rs.getString("product_id"), rs.getString("product_id"),
						rs.getString("quantity"));
			}
			stmt.close();
			return stock;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
	}

	public Category getCategory(String name) {
		String query = "SELECT CATEGORIES1.id AS category_id,CATEGORIES1.name AS category_name,CATEGORIES1.parent_category_id AS category_parent_id,CATEGORIES2.name AS parent_name FROM CATEGORY AS CATEGORIES1 LEFT JOIN CATEGORY AS CATEGORIES2 ON CATEGORIES1.parent_category_id = CATEGORIES2.id WHERE name = ?";
		Category category = null;
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				category = new Category(rs.getString("category_id"), rs.getString("category_name"),
						rs.getString("category_parent_id"), rs.getString("parent_name"));
			}
			stmt.close();
			return category;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
	}

	public Product getProduct(String code) {
		String query = "SELECT * FROM PRODUCT LEFT JOIN HSN ON HSN.hsn_code=PRODUCT.hsn_id LEFT JOIN PRODUCT_STOCK ON PRODUCT_STOCK.product_id=PRODUCT.id WHERE code = ?";
		Product product = null;
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, code);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				product = new Product(rs.getString("id"), rs.getString("title"), rs.getString("desc"),
						rs.getString("category_id"), rs.getString("code"), rs.getString("measure_type"),
						rs.getString("purchased_price"), rs.getString("purchased_from"), rs.getString("hsn_code"),
						rs.getString("selling_price"), rs.getString("mrp"), rs.getString("color"), rs.getString("tags"),
						rs.getString("vendor_id"), rs.getString("active"), rs.getString("gst"));
				product.setQuantity(rs.getInt("quantity"));
			}
			stmt.close();
			return product;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
	}

	public ObservableList<Stock> getSelectedInventoryStock(String endRange, String startRange, String searchBy) {
		ObservableList<Stock> data = FXCollections.observableArrayList();
		String query = "SELECT * FROM PRODUCT_STOCK LEFT JOIN PRODUCT ON PRODUCT_STOCK.product_id = PRODUCT.id WHERE (PRODUCT_STOCK.quantity<= ? AND PRODUCT_STOCK.quantity>= ? ) AND ((PRODUCT_STOCK.product_id LIKE ?) OR (PRODUCT_STOCK.id LIKE ?) OR (PRODUCT.title LIKE ?)) ;";

		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, startRange);
			stmt.setString(2, endRange);
			if (searchBy.trim().equals("")) {
				stmt.setString(3, "%");
				stmt.setString(4, "%");
				stmt.setString(5, "%");
			} else {
				stmt.setString(3, "%" + searchBy + "%");
				stmt.setString(4, "%" + searchBy + "%");
				stmt.setString(5, "%" + searchBy + "%");
			}
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				data.add(new Stock(rs.getString("id"), rs.getString("product_id"), rs.getString("title"),
						rs.getString("quantity")));
			}
			stmt.close();
			return data;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
	}

	public boolean insertOrderDetail(OrderDetail orderDetail) {
		String query = "INSERT INTO ORDER_DETAIL (product_id, bill_id, quantity) VALUES (?,?,?)";
		String updateQuery = "UPDATE PRODUCT_STOCK SET quantity = quantity - ? WHERE product_id = ?";
		try {
			boolean isSuccess;
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, orderDetail.getProductId());
			stmt.setInt(2, orderDetail.getBillId());
			stmt.setInt(3, orderDetail.getQuantity());

			PreparedStatement stmt2 = conn.prepareStatement(updateQuery);
			stmt2.setInt(1, orderDetail.getQuantity());
			stmt2.setInt(2, orderDetail.getProductId());

			stmt2.executeUpdate();
			int count = stmt.executeUpdate();
			isSuccess = count == 1;
			stmt.close();

			return isSuccess;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	public BillOrder getBillOrder(String billID) {
		String billQuery = "SELECT *,BILL.id AS bill_id FROM BILL LEFT JOIN CUSTOMER ON BILL.customer_id = CUSTOMER.id  WHERE BILL.id = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(billQuery);
			stmt.setString(1, billID);

			ResultSet rs = stmt.executeQuery();
			BillOrder billOrder = null;
			if (rs.next()) {
				billOrder = new BillOrder();
				billOrder.setCustomerId(rs.getString("customer_id"));
				if (rs.getString("name") == null) {
					billOrder.setCustomerName(rs.getString("customer_id"));
				} else {
					billOrder.setCustomerName(rs.getString("name"));
				}
				billOrder.setTotalAmount(rs.getString("total_amount"));
				billOrder.setBillId(rs.getString("bill_id"));
				billOrder.setExchangeCount(rs.getInt("exchange_count"));
			}
			stmt.close();
			return billOrder;
		} catch (Exception ex) {
			return null;
		}
	}

	public ObservableList<BillingDetail> getAllProductByBillID(String billId, String customerId) {
		String query = "SELECT *,ORDER_DETAIL.id AS order_product_id from ORDER_DETAIL LEFT JOIN PRODUCT ON PRODUCT .id= ORDER_DETAIL.product_id LEFT JOIN CATEGORY ON CATEGORY.id= PRODUCT.category_id LEFT JOIN HSN ON HSN.hsn_code= PRODUCT.hsn_id WHERE ORDER_DETAIL.bill_id = ?";
		ObservableList<BillingDetail> data = FXCollections.observableArrayList();
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, billId);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				double total = Utils.defaultDouble(rs.getString("quantity"))
						* Utils.defaultDouble(rs.getString("selling_price"));
				double gstAmount = (total * Utils.defaultDouble(rs.getString("gst"))) / 100;
				String totalStr = Utils.defaultString((total + gstAmount));
				BillingDetail detail = new BillingDetail(rs.getString("order_product_id"), rs.getString("product_id"),
						rs.getString("title"), customerId, rs.getString("name"), rs.getString("quantity"),
						rs.getString("selling_price"), totalStr);
				detail.setProductCode(rs.getString("code"));
				detail.setGst(rs.getString("gst"));
				data.add(detail);
			}
			stmt.close();
			return data;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}

	}

	public boolean updateOrderDetil(OrderDetail orderDetail) {
		String query = "UPDATE ORDER_DETAIL SET quantity = ? WHERE id = ?";
		try {
			boolean isSuccess;
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, orderDetail.getQuantity());
			stmt.setInt(2, orderDetail.getId());

			int count = stmt.executeUpdate();
			isSuccess = count == 1;
			stmt.close();

			return isSuccess;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	public boolean deleteOrderDetail(String id) {
		String query = "DELETE FROM ORDER_DETAIL WHERE id = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, id);
			int count = stmt.executeUpdate();
			boolean isSuccess = count == 1;
			stmt.close();
			return isSuccess;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	public boolean updateBill(TotalBill totalBill) {
		String query = "UPDATE BILL SET total_amount = ?, exchange_count = ?, modify_on = ? WHERE id = ?";
		try {
			boolean isSuccess;
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, totalBill.getTotalAmount());
			stmt.setInt(2, totalBill.getExchangeCount() + 1);
			stmt.setString(3, "" + new Timestamp(System.currentTimeMillis()));
			stmt.setString(4, totalBill.getId());

			int count = stmt.executeUpdate();
			isSuccess = count == 1;
			stmt.close();

			return isSuccess;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}
}
