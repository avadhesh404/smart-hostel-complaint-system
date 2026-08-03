#!/bin/bash

# Smart Hostel Complaint Management System - Run Script
# This script runs the application

echo "=========================================="
echo "Smart Hostel Complaint Management System"
echo "Starting Application..."
echo "=========================================="
echo ""

# Check if MySQL is running
if ! mysqladmin ping &> /dev/null; then
    echo "❌ MySQL is not running. Please start MySQL first:"
    echo "  brew services start mysql@8.0"
    echo "  or"
    echo "  sudo /usr/local/mysql/support-files/mysql.server start"
    exit 1
fi

echo "✅ MySQL is running"
echo ""

# Check if project is built
if [ ! -d "target" ]; then
    echo "🔧 Project not built. Building now..."
    mvn clean compile
    echo "✅ Project built"
fi

# Run the application
echo "🚀 Starting application..."
echo ""
mvn exec:java -Dexec.mainClass="com.hostel.complaint.view.MainApplication"
