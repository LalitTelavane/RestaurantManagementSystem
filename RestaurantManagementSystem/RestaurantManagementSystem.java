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
    private JTextField searchField; // Search Bar
    private JTextArea orderTextArea; //Order TextArea
    private JLabel totalEarningsLabel; // Total earnings label
    private double totalEarnings; // Total earnings
    private JLabel totalCustomersLabel; // Total number of customers label
    private int totalCustomers; // Total number of customers
    private JPanel productPanel; // Product panel to display products

    public RestaurantManagementSystem() {
        super("Restaurant Management System");
        products = new ArrayList<>();
        orderedProducts = new ArrayList<>();
        totalEarnings = 0.0;
        totalCustomers = 0;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1860, 1020);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        productPanel = new JPanel(new GridLayout(0, 3)); // Initialize the product panel

        JScrollPane productScrollPane = new JScrollPane(productPanel);

        // Modify font size for the title or header
        JLabel titleLabel = new JLabel("BVP Snacks Corner:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Change font size to 24
        productScrollPane.setColumnHeaderView(titleLabel);

        searchField = new JTextField(20); // Search bar with a width of 15 columns
        searchField.setPreferredSize(new Dimension(90, 30)); // Set search bar size
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterProducts(searchField.getText());
                }
            });

        JButton addProductButton = new JButton("Add Product");
        addProductButton.setPreferredSize(new Dimension(110, 30)); // Set preferred size for buttons
        addProductButton.setFocusable(false);
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
                }
            });

        JButton deleteProductButton = new JButton("Delete Product");
        deleteProductButton.setPreferredSize(new Dimension(130, 30)); 
        deleteProductButton.setFocusable(false);// Set preferred size for buttons
        deleteProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProduct();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 400, 0, 0));
        buttonPanel.add(searchField); // Add Search Bar To The Button Panel
        buttonPanel.add(addProductButton); //Adds Add Product Button
        buttonPanel.add(deleteProductButton); //Adds Delete Button

        // Order Panel
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderTextArea = new JTextArea(30, 45); // Increase rows and columns
        orderTextArea.setEditable(false);
        JScrollPane orderScrollPane = new JScrollPane(orderTextArea);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        totalCustomersLabel = new JLabel("Total No. of Customers: " + totalCustomers + "               ");
        topPanel.add(totalCustomersLabel);

        totalEarningsLabel = new JLabel("Total Earnings: Rs." + totalEarnings);
        topPanel.add(totalEarningsLabel);
        
        orderPanel.add(topPanel, BorderLayout.NORTH);
        orderPanel.add(new JLabel("Orders: "), BorderLayout.WEST);
        orderPanel.add(orderScrollPane, BorderLayout.CENTER);

        JButton totalButton = new JButton("Total");
        totalButton.setFocusable(false);
        totalButton.setPreferredSize(new Dimension(110, 30));
        totalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateTotal();
            }
        });

        JButton printReceiptButton = new JButton("Print Receipt");
        printReceiptButton.setFocusable(false);
        printReceiptButton.setPreferredSize(new Dimension(110, 30));
        printReceiptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printReceipt();
                }
            });

        JButton resetButton = new JButton("Reset");
        resetButton.setFocusable(false);
        resetButton.setPreferredSize(new Dimension(110, 30));
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderTextArea.setText(""); // Clear The Order Text Area
                clearQuantityFields(); // Clear The Quantity Fields
                orderedProducts.clear(); // Clear The Ordered Products List
                }
            });

        JPanel orderButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        orderButtonPanel.add(totalButton);
        orderButtonPanel.add(printReceiptButton);
        orderButtonPanel.add(resetButton);
        orderPanel.add(orderButtonPanel, BorderLayout.SOUTH);

        // Create a container panel for product and order panels
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(productScrollPane, BorderLayout.CENTER);
        mainPanel.add(orderPanel, BorderLayout.EAST);

        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        addPredefinedItems();

        setVisible(true);
    }

    // Method to add predefined items to the product panel
    private void addPredefinedItems() {
        // Add your predefined items here
        products.add(new Product("Masala Dosa", 50, "images/masaladosa.jfif"));
        products.add(new Product("Idli", 40, "images/Idli.jfif"));
        products.add(new Product("Samosa", 12, "images/samosa.jfif"));
        products.add(new Product("Vada Pav", 15, "images/vadapav.jfif"));
        products.add(new Product(" Aloo Tikki Burger", 99, "images/AlooTikkiBurger.jpg"));
        products.add(new Product(" Dominator Pizza", 99, "images/dominatorpizza.jfif"));
        products.add(new Product(" Panner Tikka Salad", 60, "images/PaneerTikkaSalad.jpg"));
        products.add(new Product(" Tosted Sandwich", 50, "images/Tostedsandwich.jfif"));
        products.add(new Product(" Creamy Tamato Pasta", 99, "images/creamytomatopasta.jpg"));
        products.add(new Product("Coco Cola ", 20, "images/cococola.jfif"));
        products.add(new Product("Soft Drinks ", 40, "images/softdrinks1.jfif"));
        // Update the product list
        updateProductList();
    }

    private void addProduct() {
        String name = JOptionPane.showInputDialog("Enter product name:");
        if (name == null || name.isEmpty()) {
            return; // Cancel or empty name, do nothing
        }
        double price = 0.0;
        try {
            price = Double.parseDouble(JOptionPane.showInputDialog("Enter product price:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid price! Please enter a valid number.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Product Image");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg", "webp","jfif");
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
            JLabel productLabel = new JLabel("<html><center>" + product.getName() + "<br>Rs." + product.getPrice() + "</center></html>", JLabel.CENTER);
            JLabel imageLabel = new JLabel(new ImageIcon(image), JLabel.CENTER); // Center the image
            JLabel quantityLabel = new JLabel("Quantity:");
            JTextField quantityField = new JTextField(5);
            // Create a panel to hold the purchase button
            JButton purchaseButton = new JButton("Purchase");
            purchaseButton.setFocusable(false);
            JPanel productPanelItem = new JPanel(new BorderLayout());

            JPanel productInfoPanel = new JPanel();
            productInfoPanel.setLayout(new BoxLayout(productInfoPanel, BoxLayout.Y_AXIS)); // Set BoxLayout for vertical arrangement
    
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Center the button panel
    
            buttonPanel.add(quantityLabel);
            buttonPanel.add(quantityField);
            buttonPanel.add(purchaseButton);

            // Center align components horizontally within productInfoPanel
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            productLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
            productInfoPanel.add(imageLabel); // Add the image label
            productInfoPanel.add(productLabel);
            productInfoPanel.add(buttonPanel); // Add the button panel with quantity components
    
            productPanelItem.add(productInfoPanel, BorderLayout.CENTER); // Center the product info panel within the bordered frame
       //     productPanelItem.add(purchaseButton, BorderLayout.SOUTH); // Add the purchase button below the product info panel
    
            purchaseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        int quantity = Integer.parseInt(quantityField.getText());
                        for (int i = 0; i < quantity; i++) {
                            orderedProducts.add(product);
                        }
                        updateOrderTextArea();
                    }
                     catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid quantity! Please enter a valid number.");
                    }
                }
            });
             // Decrease the width of the purchase button
           purchaseButton.setPreferredSize(new Dimension(100, 30));
            // Add a border to the product panel item without any space inside
            productPanelItem.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Zero thickness line border
            productPanel.add(productPanelItem); // Add the product panel item to the product panel
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
                sb.append("Item: ").append(product.getName()).append(", Qty: ").append(quantity).append(", Price: ").append(product.getPrice()).append(", Total Price: ").append(totalPrice).append("\n");
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
        orderTextArea.append("\nTotal Amount: Rs." + totalAmount);
        // Update total earnings
        totalEarnings += totalAmount;
        totalEarningsLabel.setText("Total Earnings: Rs." + totalEarnings);
        // Update total number of customers
        totalCustomers++;
        totalCustomersLabel.setText("Total No. of Customers: " + totalCustomers);
        }

    private void printReceipt() {
        String customerId = JOptionPane.showInputDialog("Enter Customer ID:");
        String customerName = JOptionPane.showInputDialog("Enter Customer Name:");
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());

        String receiptContent = "BVP Snacks Corner\n" + "\n\nDate: " + date + "     Time: " + time + "\n\nCustomer ID: " + customerId + "\nCustomer Name: " + customerName +  "\n\n\nOrder:\n" + orderTextArea.getText() + "\n\n\n Thanks For Coming" + "\n Visit Again";

        JFileChooser fileChooser = new JFileChooser();
        int saveOption = fileChooser.showSaveDialog(this);
        if (saveOption == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(receiptContent);
                JOptionPane.showMessageDialog(this, "Receipt saved successfully!");
                } 
            catch (IOException e) {
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