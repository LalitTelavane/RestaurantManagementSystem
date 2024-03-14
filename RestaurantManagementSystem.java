import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RestaurantManagementSystem extends JFrame {
    private List<Product> products;
    private List<Product> orderedProducts;
    private JTextField searchField; // Search bar
    private JTextArea orderTextArea;
    private JLabel totalEarningsLabel; // Total earnings label
    private double totalEarnings; // Total earnings
    private JPanel productPanel; // Product panel to display products

    public RestaurantManagementSystem() {
        super("Restaurant Management System");
        products = new ArrayList<>();
        orderedProducts = new ArrayList<>();
        totalEarnings = 0.0;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        productPanel = new JPanel(new GridLayout(0, 3)); // Initialize the product panel
        setTitle("Menu Items");

        JScrollPane productScrollPane = new JScrollPane(productPanel);

        productScrollPane.setColumnHeaderView(new JLabel("Menu Items:"));

        searchField = new JTextField(20); // Search bar with a width of 20 columns
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterProducts(searchField.getText());
            }
        });

        JButton addProductButton = new JButton("Add Product");
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        JButton deleteProductButton = new JButton("Delete Product");
        deleteProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProduct();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(searchField); // Add search bar to the button panel
        buttonPanel.add(addProductButton);
        buttonPanel.add(deleteProductButton);

        // Order Panel
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderTextArea = new JTextArea(30, 50); // Increase rows and columns
        orderTextArea.setEditable(false);
        JScrollPane orderScrollPane = new JScrollPane(orderTextArea);
        orderPanel.add(new JLabel("Orders: "), BorderLayout.NORTH);
        orderPanel.add(orderScrollPane, BorderLayout.CENTER);

        // Add buttons for total and print receipt
        JButton totalButton = new JButton("Total");
        totalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateTotal();
            }
        });

        JButton printReceiptButton = new JButton("Print Receipt");
        printReceiptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printReceipt();
            }
        });
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderTextArea.setText(""); // Clear the order text area
                clearQuantityFields(); // Clear the quantity fields
                orderedProducts.clear(); // Clear the ordered products list
            }
        });
        

        JPanel orderButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        orderButtonPanel.add(totalButton);
        orderButtonPanel.add(printReceiptButton);
        orderButtonPanel.add(resetButton);
        orderPanel.add(orderButtonPanel, BorderLayout.SOUTH);

        // Total earnings label
        totalEarningsLabel = new JLabel("Total Earnings: $" + totalEarnings);
        totalEarningsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        orderPanel.add(totalEarningsLabel, BorderLayout.NORTH);

        getContentPane().add(productScrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(orderPanel, BorderLayout.EAST);

        // Add predefined items
        addPredefinedItems();

        setVisible(true);
    }

    // Method to add predefined items to the product panel
    private void addPredefinedItems() {
        // Add your predefined items here
        products.add(new Product("Burger", 5.99, "1.png"));
        products.add(new Product("Pizza", 8.99, "1.png"));
        products.add(new Product("Salad", 4.99, "1.png"));
        products.add(new Product("Pasta", 7.99, "1.png"));
        products.add(new Product("Sandwich", 6.99, "1.png"));
        products.add(new Product("Fries", 2.99, "1.png"));
        products.add(new Product("Soda", 1.99, "1.png"));

        // Update the product list
        updateProductList();
    }

    private void addProduct() {
        String name = JOptionPane.showInputDialog("Enter product name:");
        if (name == null || name.isEmpty()) return; // Cancel or empty name, do nothing
        double price = 0.0;
        try {
            price = Double.parseDouble(JOptionPane.showInputDialog("Enter product price:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid price! Please enter a valid number.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Product Image");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String imagePath = selectedFile.getAbsolutePath();

            Product product = new Product(name, price, imagePath);
            products.add(product);

            updateProductList();
        }
    }

    private void deleteProduct() {
        String[] options = products.stream().map(Product::getName).toArray(String[]::new);
        String selectedProduct = (String) JOptionPane.showInputDialog(null,
                "Select a product to delete:", "Delete Product",
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (selectedProduct != null) {
            products.removeIf(product -> product.getName().equals(selectedProduct));
            updateProductList();
        }
    }

    private void filterProducts(String query) {
        List<Product> filteredProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredProducts.add(product);
            }
        }
        updateProductList(filteredProducts);
    }

    private void updateProductList() {
        updateProductList(products);
    }

    private void updateProductList(List<Product> productList) {
        productPanel.removeAll(); // Clear the product panel

        int columns = 4; // Number of columns in the grid layout
        int rows = (int) Math.ceil((double) productList.size() / columns); // Calculate number of rows needed

        productPanel.setLayout(new GridLayout(rows, columns)); // Set the layout to a grid with calculated rows and columns

        for (Product product : productList) {
            ImageIcon imageIcon = new ImageIcon(product.getImagePath());
            Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            JLabel productLabel = new JLabel(product.getName() + " - $" + product.getPrice(), new ImageIcon(image),
                    JLabel.CENTER);
            JLabel quantityLabel = new JLabel("Quantity:");
            JTextField quantityField = new JTextField(3);
            JButton purchaseButton = new JButton("Purchase");

            JPanel productPanelItem = new JPanel(new BorderLayout());
            JPanel productInfoPanel = new JPanel(new GridLayout(2, 1));
            JPanel buttonPanel = new JPanel();

            buttonPanel.add(quantityLabel);
            buttonPanel.add(quantityField);
            buttonPanel.add(purchaseButton);

            productInfoPanel.add(productLabel);
            productInfoPanel.add(buttonPanel);

            productPanelItem.add(productInfoPanel, BorderLayout.NORTH);

            purchaseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        int quantity = Integer.parseInt(quantityField.getText());
                        for (int i = 0; i < quantity; i++) {
                            orderedProducts.add(product);
                        }
                        updateOrderTextArea();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid quantity! Please enter a valid number.");
                    }
                }
            });

            productPanel.add(productPanelItem); // Add the product label to the product panel
        }

        revalidate(); // Revalidate the layout
        repaint(); // Repaint the UI
    }

    private void updateOrderTextArea() {
        StringBuilder sb = new StringBuilder();
        // Create a temporary list to keep track of processed products
        List<Product> processedProducts = new ArrayList<>();
        for (Product product : orderedProducts) {
            // Check if the product has been processed already
            if (!processedProducts.contains(product)) {
                int quantity = 0;
                double totalPrice = 0.0;
                // Calculate the total quantity and total price for the product
                for (Product orderedProduct : orderedProducts) {
                    if (orderedProduct.equals(product)) {
                        quantity++;
                        totalPrice += orderedProduct.getPrice();
                    }
                }
                // Append the product details to the StringBuilder
                sb.append("Product Name: ").append(product.getName()).append(", Quantity: ").append(quantity).append(", Price: ").append(product.getPrice()).append(", Total Price: ").append(totalPrice).append("\n");
                // Add the processed product to the list
                processedProducts.add(product);
            }
        }
        // Set the text of the orderTextArea
        orderTextArea.setText(sb.toString());
    }

    private void calculateTotal() {
        double totalAmount = 0.0;
        for (Product product : orderedProducts) {
            totalAmount += product.getPrice();
        }
        // Append the total amount to the orderTextArea
        orderTextArea.append("\nTotal Amount: $" + totalAmount);
        // Update total earnings
        totalEarnings += totalAmount;
        totalEarningsLabel.setText("Total Earnings: $" + totalEarnings);
    }

    private void printReceipt() {
        String customerId = JOptionPane.showInputDialog("Enter Customer ID:");
        String customerName = JOptionPane.showInputDialog("Enter Customer Name:");
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());

        String receiptContent = "Customer ID: " + customerId + "\nCustomer Name: " + customerName + "\nDate: " + date + "\nTime: " + time + "\n\nOrder:\n" + orderTextArea.getText();

        JFileChooser fileChooser = new JFileChooser();
        int saveOption = fileChooser.showSaveDialog(this);
        if (saveOption == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(receiptContent);
                JOptionPane.showMessageDialog(this, "Receipt saved successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error occurred while saving the receipt.");
            }
        }
    }

    private void clearQuantityFields() {
        Component[] components = productPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel productPanelItem = (JPanel) component;
                for (Component innerComponent : productPanelItem.getComponents()) {
                    if (innerComponent instanceof JPanel) {
                        JPanel productInfoPanel = (JPanel) innerComponent;
                        for (Component buttonPanelComponent : productInfoPanel.getComponents()) {
                            if (buttonPanelComponent instanceof JPanel) {
                                JPanel buttonPanel = (JPanel) buttonPanelComponent;
                                for (Component field : buttonPanel.getComponents()) {
                                    if (field instanceof JTextField) {
                                        JTextField quantityField = (JTextField) field;
                                        quantityField.setText(""); // Clear the quantity field
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RestaurantManagementSystem();
            }
        });
    }
}

class Product {
    private String name;
    private double price;
    private String imagePath;

    public Product(String name, double price, String imagePath) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }
}
