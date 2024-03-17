import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class InvoiceSystem {
    private Connection connection;


    public InvoiceSystem() {
        try {
            // Establish connection to the database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/karloss", "root", "Karlosgwapo123!");
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database.");
            e.printStackTrace();
            // Rethrow the exception or handle it as appropriate
            throw new RuntimeException("Failed to connect to the database.", e);
        }
    }
       
    // Client Management
    public void addClient(String name, String email) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Clients (name, email) VALUES (?, ?)");
            statement.setString(1, name);
            statement.setString(2, email);
            statement.executeUpdate();
            System.out.println("Client added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   
    public void updateClient(int clientId, String newName, String newEmail) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE Clients SET name = ?, email = ? WHERE client_id = ?");
            statement.setString(1, newName);
            statement.setString(2, newEmail);
            statement.setInt(3, clientId);
            statement.executeUpdate();
            System.out.println("Client updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteClient(int clientId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Clients WHERE client_id = ?");
            statement.setInt(1, clientId);
            statement.executeUpdate();
            System.out.println("Client deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Service Management
    public void addService(String name, double rate) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Services (name, rate) VALUES (?, ?)");
            statement.setString(1, name);
            statement.setDouble(2, rate);
            statement.executeUpdate();
            System.out.println("Service added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateService(int serviceId, String newName, double newRate) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE Services SET name = ?, rate = ? WHERE service_id = ?");
            statement.setString(1, newName);
            statement.setDouble(2, newRate);
            statement.setInt(3, serviceId);
            statement.executeUpdate();
            System.out.println("Service updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteService(int serviceId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Services WHERE service_id = ?");
            statement.setInt(1, serviceId);
            statement.executeUpdate();
            System.out.println("Service deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Invoice Management
    public void createInvoice(int clientId, List<Integer> serviceIds) {
        try {
            double totalAmount = 0.0;
            for (int serviceId : serviceIds) {
                PreparedStatement statement = connection.prepareStatement("SELECT rate FROM Services WHERE service_id = ?");
                statement.setInt(1, serviceId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    totalAmount += resultSet.getDouble("rate");
                }
            }
            PreparedStatement insertInvoice = connection.prepareStatement("INSERT INTO Invoices (client_id, total_amount) VALUES (?, ?)");
            insertInvoice.setInt(1, clientId);
            insertInvoice.setDouble(2, totalAmount);
            insertInvoice.executeUpdate();
            System.out.println("Invoice created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateInvoice(int invoiceId, List<Integer> serviceIds) {
        try {
            double totalAmount = 0.0;
            for (int serviceId : serviceIds) {
                PreparedStatement statement = connection.prepareStatement("SELECT rate FROM Services WHERE service_id = ?");
                statement.setInt(1, serviceId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    totalAmount += resultSet.getDouble("rate");
                }
            }
            PreparedStatement updateInvoice = connection.prepareStatement("UPDATE Invoices SET total_amount = ? WHERE invoice_id = ?");
            updateInvoice.setDouble(1, totalAmount);
            updateInvoice.setInt(2, invoiceId);
            updateInvoice.executeUpdate();
            System.out.println("Invoice updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteInvoice(int invoiceId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Invoices WHERE invoice_id = ?");
            statement.setInt(1, invoiceId);
            statement.executeUpdate();
            System.out.println("Invoice deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void viewAllClients() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Clients");
            while (resultSet.next()) {
                int clientId = resultSet.getInt("client_id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                System.out.println("Client ID: " + clientId + ", Name: " + name + ", Email: " + email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void viewTotalBilledAmountForEachClient() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT client_id, SUM(total_amount) AS total_amount FROM Invoices GROUP BY client_id");
            while (resultSet.next()) {
                int clientId = resultSet.getInt("client_id");
                double totalAmount = resultSet.getDouble("total_amount");
                System.out.println("Client ID: " + clientId + ", Total Billed Amount: $" + totalAmount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void viewAllServices() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Services");
            while (resultSet.next()) {
                int serviceId = resultSet.getInt("service_id");
                String name = resultSet.getString("name");
                double rate = resultSet.getDouble("rate");
                System.out.println("Service ID: " + serviceId + ", Name: " + name + ", Rate: $" + rate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void viewTotalHoursBilledForEachService() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT service_id, SUM(hours) AS total_hours FROM Invoice_Line_Items GROUP BY service_id");
            while (resultSet.next()) {
                int serviceId = resultSet.getInt("service_id");
                double totalHours = resultSet.getDouble("total_hours");
                System.out.println("Service ID: " + serviceId + ", Total Hours Billed: " + totalHours);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void viewAllInvoicesForClient(int clientId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Invoices WHERE client_id = ?");
            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int invoiceId = resultSet.getInt("invoice_id");
                double totalAmount = resultSet.getDouble("total_amount");
                System.out.println("Invoice ID: " + invoiceId + ", Total Amount: $" + totalAmount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void viewTotalAmountForEachInvoice() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Invoices");
            while (resultSet.next()) {
                int invoiceId = resultSet.getInt("invoice_id");
                double totalAmount = resultSet.getDouble("total_amount");
                System.out.println("Invoice ID: " + invoiceId + ", Total Amount: $" + totalAmount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public double getTotalIncome() {
        double totalIncome = 0.0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT SUM(total_amount) AS total_income FROM Invoices");
            if (resultSet.next()) {
                totalIncome = resultSet.getDouble("total_income");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalIncome;
    }


    public String getMostPopularService() {
        String mostPopularService = "";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT service_id, COUNT(*) AS service_count FROM Invoice_Line_Items GROUP BY service_id ORDER BY service_count DESC LIMIT 1");
            if (resultSet.next()) {
                int serviceId = resultSet.getInt("service_id");
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM Services WHERE service_id = ?");
                preparedStatement.setInt(1, serviceId);
                ResultSet serviceResultSet = preparedStatement.executeQuery();
                if (serviceResultSet.next()) {
                    mostPopularService = serviceResultSet.getString("name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mostPopularService;
    }


    public String getTopClient() {
        String topClient = "";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT client_id, SUM(total_amount) AS total_amount FROM Invoices GROUP BY client_id ORDER BY total_amount DESC LIMIT 1");
            if (resultSet.next()) {
                int clientId = resultSet.getInt("client_id");
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM Clients WHERE client_id = ?");
                preparedStatement.setInt(1, clientId);
                ResultSet clientResultSet = preparedStatement.executeQuery();
                if (clientResultSet.next()) {
                    topClient = clientResultSet.getString("name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topClient;
    }


    public void addServiceToInvoice(int invoiceId, int serviceId, double hours) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Invoice_Line_Items (invoice_id, service_id, hours) VALUES (?, ?, ?)");
            statement.setInt(1, invoiceId);
            statement.setInt(2, serviceId);
            statement.setDouble(3, hours);
            statement.executeUpdate();
            System.out.println("Service added to invoice successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateServiceHoursInInvoice(int invoiceId, int serviceId, double hours) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE Invoice_Line_Items SET hours = ? WHERE invoice_id = ? AND service_id = ?");
            statement.setDouble(1, hours);
            statement.setInt(2, invoiceId);
            statement.setInt(3, serviceId);
            statement.executeUpdate();
            System.out.println("Service hours in invoice updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void viewAllServicesForInvoice(int invoiceId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Invoice_Line_Items WHERE invoice_id = ?");
            statement.setInt(1, invoiceId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int serviceId = resultSet.getInt("service_id");
                double hours = resultSet.getDouble("hours");
                System.out.println("Service ID: " + serviceId + ", Hours: " + hours);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void displayMenu() {
        System.out.println("--- Invoice System Menu ---");
        System.out.println("Client Management:");
        System.out.println("1. Add Client");
        System.out.println("2. Update Client");
        System.out.println("3. Delete Client");
        System.out.println("\nService Management:");
        System.out.println("4. Add Service");
        System.out.println("5. Update Service");
        System.out.println("6. Delete Service");
        System.out.println("\nInvoice Management:");
        System.out.println("7. Create Invoice");
        System.out.println("8. Update Invoice");
        System.out.println("9. Delete Invoice");
        System.out.println("\nView Information:");
        System.out.println("10. View All Clients");
        System.out.println("11. View Total Billed Amount for Each Client");
        System.out.println("12. View All Services");
        System.out.println("13. View Total Hours Billed for Each Service");
        System.out.println("14. View All Invoices for Client");
        System.out.println("15. View Total Amount for Each Invoice");
        System.out.println("\nFinancial Analysis:");
        System.out.println("16. Get Total Income");
        System.out.println("17. Get Most Popular Service");
        System.out.println("18. Get Top Client");
        System.out.println("\nOther Options:");
        System.out.println("19. Exit");
        System.out.println("\nInvoice Services Management:");
        System.out.println("20. Add Service to Invoice");
        System.out.println("21. Update Service Hours in Invoice");
        System.out.println("22. View All Services for Invoice");
    }


    // Main method for testing
    public static void main(String[] args) {
        InvoiceSystem invoiceSystem = new InvoiceSystem();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;


        while (!exit) {
            invoiceSystem.displayMenu();
            System.out.print("\nEnter your choice: ");
            int choice = scanner.nextInt();


            switch (choice) {
                case 1:
                    System.out.print("Enter client name: ");
                    String clientName = scanner.next();
                    System.out.print("Enter client email: ");
                    String clientEmail = scanner.next();
                    invoiceSystem.addClient(clientName, clientEmail);
                    break;
                case 2:
                    System.out.print("Enter client ID to update: ");
                    int clientIdToUpdate = scanner.nextInt();
                    System.out.print("Enter new client name: ");
                    String newClientName = scanner.next();
                    System.out.print("Enter new client email: ");
                    String newClientEmail = scanner.next();
                    invoiceSystem.updateClient(clientIdToUpdate, newClientName, newClientEmail);
                    break;
                case 3:
                    System.out.print("Enter client ID to delete: ");
                    int clientIdToDelete = scanner.nextInt();
                    invoiceSystem.deleteClient(clientIdToDelete);
                    break;
                case 4:
                    System.out.print("Enter service name: ");
                    String serviceName = scanner.next();
                    System.out.print("Enter service rate: ");
                    double serviceRate = scanner.nextDouble();
                    invoiceSystem.addService(serviceName, serviceRate);
                    break;
                case 5:
                    System.out.print("Enter service ID to update: ");
                    int serviceIdToUpdate = scanner.nextInt();
                    System.out.print("Enter new service name: ");
                    String newServiceName = scanner.next();
                    System.out.print("Enter new service rate: ");
                    double newServiceRate = scanner.nextDouble();
                    invoiceSystem.updateService(serviceIdToUpdate, newServiceName, newServiceRate);
                    break;
                case 6:
                    System.out.print("Enter service ID to delete: ");
                    int serviceIdToDelete = scanner.nextInt();
                    invoiceSystem.deleteService(serviceIdToDelete);
                    break;
                case 7:
                    System.out.print("Enter client ID for the invoice: ");
                    int clientIdForInvoice = scanner.nextInt();
                    List<Integer> servicesForInvoice = new ArrayList<>();
                    boolean addMoreServices = true;
                    while (addMoreServices) {
                        System.out.print("Enter service ID to add to invoice (0 to stop adding): ");
                        int serviceIdToAdd = scanner.nextInt();
                        if (serviceIdToAdd == 0) {
                            addMoreServices = false;
                        } else {
                            servicesForInvoice.add(serviceIdToAdd);
                        }
                    }
                    invoiceSystem.createInvoice(clientIdForInvoice, servicesForInvoice);
                    break;
                case 8:
                    System.out.print("Enter invoice ID to update: ");
                    int invoiceIdToUpdate = scanner.nextInt();
                    List<Integer> updatedServicesForInvoice = new ArrayList<>();
                    boolean updateMoreServices = true;
                    while (updateMoreServices) {
                        System.out.print("Enter service ID to update in invoice (0 to stop updating): ");
                        int serviceIdToUpdateInInvoice = scanner.nextInt();
                        if (serviceIdToUpdateInInvoice == 0) {
                            updateMoreServices = false;
                        } else {
                            updatedServicesForInvoice.add(serviceIdToUpdateInInvoice);
                        }
                    }
                    invoiceSystem.updateInvoice(invoiceIdToUpdate, updatedServicesForInvoice);
                    break;
                case 9:
                    System.out.print("Enter invoice ID to delete: ");
                    int invoiceIdToDelete = scanner.nextInt();
                    invoiceSystem.deleteInvoice(invoiceIdToDelete);
                    break;
                case 10:
                    System.out.println("\nAll Clients:");
                    invoiceSystem.viewAllClients();
                    break;
                case 11:
                    System.out.println("\nTotal Billed Amount for Each Client:");
                    invoiceSystem.viewTotalBilledAmountForEachClient();
                    break;
                case 12:
                    System.out.println("\nAll Services:");
                    invoiceSystem.viewAllServices();
                    break;
                case 13:
                    System.out.println("\nTotal Hours Billed for Each Service:");
                    invoiceSystem.viewTotalHoursBilledForEachService();
                    break;
                case 14:
                    System.out.print("Enter client ID to view invoices: ");
                    int clientIdToViewInvoices = scanner.nextInt();
                    System.out.println("\nAll Invoices for Client:");
                    invoiceSystem.viewAllInvoicesForClient(clientIdToViewInvoices);
                    break;
                case 15:
                    System.out.println("\nTotal Amount for Each Invoice:");
                    invoiceSystem.viewTotalAmountForEachInvoice();
                    break;
                case 16:
                    System.out.println("\nTotal Income: $" + invoiceSystem.getTotalIncome());
                    break;
                case 17:
                    System.out.println("\nMost Popular Service: " + invoiceSystem.getMostPopularService());
                    break;
                case 18:
                    System.out.println("\nTop Client: " + invoiceSystem.getTopClient());
                    break;
                case 19:
                    exit = true;
                    System.out.println("Exiting...");
                    break;
                case 20:
                    System.out.print("Enter invoice ID to add service: ");
                    int invoiceIdToAddService = scanner.nextInt();
                    System.out.print("Enter service ID to add to invoice: ");
                    int serviceIdToAddToInvoice = scanner.nextInt();
                    System.out.print("Enter hours for the service: ");
                    double hoursToAddToInvoice = scanner.nextDouble();
                    invoiceSystem.addServiceToInvoice(invoiceIdToAddService, serviceIdToAddToInvoice, hoursToAddToInvoice);
                    break;
                case 21:
                    System.out.print("Enter invoice ID to update service hours: ");
                    int invoiceIdToUpdateHours = scanner.nextInt();
                    System.out.print("Enter service ID to update hours: ");
                    int serviceIdToUpdateHours = scanner.nextInt();
                    System.out.print("Enter updated hours for the service: ");
                    double updatedHours = scanner.nextDouble();
                    invoiceSystem.updateServiceHoursInInvoice(invoiceIdToUpdateHours, serviceIdToUpdateHours, updatedHours);
                    break;
                case 22:
                    System.out.print("Enter invoice ID to view services: ");
                    int invoiceIdToViewServices = scanner.nextInt();
                    System.out.println("\nAll Services for Invoice:");
                    invoiceSystem.viewAllServicesForInvoice(invoiceIdToViewServices);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }


        scanner.close();
    }
}


   





