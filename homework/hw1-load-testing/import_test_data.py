#!/usr/bin/env python3
"""
Script to import test data into PostgreSQL database for load testing.
Generates 1,000,000 user records from the provided CSV file.
"""

import csv
import psycopg2
import uuid
import random
from datetime import datetime, date
import bcrypt
import sys
import os

# Database configuration
DB_CONFIG = {
    'host': 'localhost',
    'port': 5432,
    'database': 'social_network',
    'user': 'social_network',
    'password': 'social_network'
}

def generate_password_hash(password="test123"):
    """Generate BCrypt hash for password with cost factor 7 (as per config)"""
    return bcrypt.hashpw(password.encode('utf-8'), bcrypt.gensalt(rounds=7)).decode('utf-8')

def parse_csv_line(line):
    """Parse CSV line in format: LastName FirstName,YYYY-MM-DD,City"""
    parts = line.strip().split(',')
    if len(parts) != 3:
        return None
    
    full_name = parts[0].strip()
    birth_date = parts[1].strip()
    city = parts[2].strip()
    
    # Split full name into first and last name
    name_parts = full_name.split(' ', 1)
    if len(name_parts) != 2:
        return None
    
    last_name = name_parts[0]
    first_name = name_parts[1]
    
    return {
        'first_name': first_name,
        'last_name': last_name,
        'birth_date': birth_date,
        'city': city
    }

def generate_biography():
    """Generate random biography text"""
    hobbies = [
        "Увлекаюсь программированием",
        "Люблю читать книги",
        "Занимаюсь спортом", 
        "Путешествую по миру",
        "Изучаю иностранные языки",
        "Слушаю музыку",
        "Готовлю вкусную еду",
        "Фотографирую природу",
        "Играю на музыкальных инструментах",
        "Рисую в свободное время"
    ]
    return random.choice(hobbies)

def import_test_data(csv_file_path, target_count=1000000):
    """Import test data from CSV file"""
    
    print(f"Connecting to database...")
    try:
        conn = psycopg2.connect(**DB_CONFIG)
        cursor = conn.cursor()
        
        # Check current record count
        cursor.execute('SELECT COUNT(*) FROM "users"')
        current_count = cursor.fetchone()[0]
        print(f"Current users count: {current_count}")
        
        if current_count >= target_count:
            print(f"Already have {current_count} records, target is {target_count}. Nothing to do.")
            return
        
        print(f"Reading CSV file: {csv_file_path}")
        
        # Read all CSV data with error handling for incomplete UTF-8
        csv_data = []
        with open(csv_file_path, 'r', encoding='utf-8', errors='ignore') as file:
            for line_num, line in enumerate(file, 1):
                if line_num % 100000 == 0:
                    print(f"Reading line {line_num}...")
                
                parsed = parse_csv_line(line)
                if parsed:
                    csv_data.append(parsed)
        
        print(f"Loaded {len(csv_data)} records from CSV")
        
        # Generate password hash once (same for all test users)
        password_hash = generate_password_hash()
        print("Generated password hash")
        
        # Calculate how many records to insert
        records_to_insert = target_count - current_count
        print(f"Will insert {records_to_insert} new records")
        
        # Prepare batch insert
        batch_size = 1000
        users_batch = []
        principals_batch = []
        
        print("Generating and inserting records...")
        
        for i in range(records_to_insert):
            if i % 50000 == 0:
                print(f"Progress: {i}/{records_to_insert} ({i/records_to_insert*100:.1f}%)")
            
            # Pick random record from CSV data
            csv_record = random.choice(csv_data)
            
            # Generate UUID
            user_id = str(uuid.uuid4())
            
            # Create user record
            user_record = (
                user_id,
                csv_record['first_name'],
                csv_record['last_name'],
                csv_record['birth_date'],
                generate_biography(),
                csv_record['city']
            )
            users_batch.append(user_record)
            
            # Create principal record
            principal_record = (user_id, password_hash)
            principals_batch.append(principal_record)
            
            # Insert batch when it reaches batch_size
            if len(users_batch) >= batch_size:
                # Insert users
                cursor.executemany(
                    'INSERT INTO "users" ("id", "first_name", "last_name", "birth_date", "biography", "city") VALUES (%s, %s, %s, %s, %s, %s)',
                    users_batch
                )
                
                # Insert principals
                cursor.executemany(
                    'INSERT INTO "principals" ("id", "encoded_password") VALUES (%s, %s)',
                    principals_batch
                )
                
                conn.commit()
                users_batch.clear()
                principals_batch.clear()
        
        # Insert remaining records
        if users_batch:
            cursor.executemany(
                'INSERT INTO "users" ("id", "first_name", "last_name", "birth_date", "biography", "city") VALUES (%s, %s, %s, %s, %s, %s)',
                users_batch
            )
            
            cursor.executemany(
                'INSERT INTO "principals" ("id", "encoded_password") VALUES (%s, %s)',
                principals_batch
            )
            
            conn.commit()
        
        # Final count
        cursor.execute('SELECT COUNT(*) FROM "users"')
        final_count = cursor.fetchone()[0]
        print(f"Import completed! Total users: {final_count}")
        
        cursor.close()
        conn.close()
        
    except Exception as e:
        print(f"Error: {e}")
        sys.exit(1)

if __name__ == "__main__":
    csv_file = os.path.join(os.path.dirname(__file__), "people.v2.csv")
    
    if not os.path.exists(csv_file):
        print(f"CSV file not found: {csv_file}")
        sys.exit(1)
    
    print("=== Social Network Test Data Import ===")
    print(f"Target: 1,000,000 user records")
    print(f"CSV file: {csv_file}")
    print()
    
    import_test_data(csv_file)