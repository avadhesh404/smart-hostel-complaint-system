#!/bin/bash

# Smart Hostel Complaint Management System - Setup Script
# This script helps set up the project on macOS

set -e

echo "=========================================="
echo "Smart Hostel Complaint Management System"
echo "Setup Script for macOS"
echo "=========================================="
echo ""

# Check if Homebrew is installed
if ! command -v brew &> /dev/null; then
    echo "❌ Homebrew is not installed."
    echo "Please install Homebrew first:"
    echo "  /bin/bash -c \"\$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)\""
    exit 1
fi

echo "✅ Homebrew is installed"
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "⚠️  Java is not installed. Installing Java 17..."
    brew install openjdk@17
    echo "✅ Java 17 installed"
else
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -lt 17 ]; then
        echo "⚠️  Java version is less than 17. Installing Java 17..."
        brew install openjdk@17
    else
        echo "✅ Java $JAVA_VERSION is installed"
    fi
fi
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "⚠️  Maven is not installed. Installing Maven..."
    brew install maven
    echo "✅ Maven installed"
else
    echo "✅ Maven is installed"
fi
echo ""

# Check if MySQL is installed
if ! command -v mysql &> /dev/null; then
    echo "⚠️  MySQL is not installed. Installing MySQL 8.0..."
    brew install mysql@8.0
    echo "✅ MySQL 8.0 installed"
else
    echo "✅ MySQL is installed"
fi
echo ""

# Start MySQL service
echo "🔧 Starting MySQL service..."
brew services start mysql@8.0
echo "✅ MySQL service started"
echo ""

# Wait for MySQL to be ready
echo "⏳ Waiting for MySQL to be ready..."
sleep 5

# Check if MySQL is running
if mysqladmin ping &> /dev/null; then
    echo "✅ MySQL is running"
else
    echo "❌ MySQL failed to start. Please check manually:"
    echo "  brew services list"
    echo "  mysql -u root -p"
    exit 1
fi
echo ""

# Create database
echo "🔧 Creating database..."
mysql -u root -p <<EOF
CREATE DATABASE IF NOT EXISTS hostel_complaint_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
EOF
echo "✅ Database created"
echo ""

# Run schema
echo "🔧 Running database schema..."
mysql -u root -p hostel_complaint_db < src/main/resources/schema.sql
echo "✅ Database schema created"
echo ""

# Ask about sample data
read -p "Do you want to load sample data? (y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "🔧 Loading sample data..."
    mysql -u root -p hostel_complaint_db < src/main/resources/sample_data.sql
    echo "✅ Sample data loaded"
fi
echo ""

# Configure database
echo "🔧 Configuring database connection..."
read -p "Enter MySQL username (default: root): " db_user
db_user=${db_user:-root}

read -sp "Enter MySQL password: " db_password
echo

# Update config.properties
sed -i.bak "s/db.username=.*/db.username=$db_user/" src/main/resources/config.properties
sed -i.bak "s/db.password=.*/db.password=$db_password/" src/main/resources/config.properties
echo "✅ Database configuration updated"
echo ""

# Build project
echo "🔧 Building project..."
mvn clean compile
echo "✅ Project built successfully"
echo ""

echo "=========================================="
echo "✅ Setup Complete!"
echo "=========================================="
echo ""
echo "To run the application:"
echo "  mvn exec:java -Dexec.mainClass=\"com.hostel.complaint.view.MainApplication\""
echo ""
echo "Default credentials:"
echo "  Admin: admin / admin123"
echo "  Student: samplestudent1 / student123"
echo "  Staff: samplestaff1 / staff123"
echo ""
echo "For more information, see HOW_TO_RUN.md"
echo ""
