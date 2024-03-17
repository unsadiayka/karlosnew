

CREATE TABLE IF NOT EXISTS Clients (
    client_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Services (
    service_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    rate DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS Invoices (
    invoice_id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (client_id) REFERENCES Clients(client_id)
);

CREATE TABLE IF NOT EXISTS Invoice_Line_Items (
    line_item_id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT NOT NULL,
    service_id INT NOT NULL,
    hours DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (invoice_id) REFERENCES Invoices(invoice_id),
    FOREIGN KEY (service_id) REFERENCES Services(service_id)
);