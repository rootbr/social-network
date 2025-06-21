#!/bin/bash

# Load Testing Script for User Search Endpoint
# This script runs JMeter tests with different load levels

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Configuration
TEST_PLAN="user-search-load-test.jmx"
RESULTS_DIR="results"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")

echo -e "${GREEN}=== User Search Load Testing Script ===${NC}"
echo "Timestamp: $TIMESTAMP"

# Create results directory
mkdir -p "$RESULTS_DIR"

# Function to run a specific test
run_test() {
    local test_name=$1
    local thread_group=$2
    local description=$3
    
    echo -e "\n${YELLOW}Running $description...${NC}"
    
    # Create a temporary JMX file with only the specified thread group enabled
    local temp_jmx="temp_${test_name}_${TIMESTAMP}.jmx"
    cp "$TEST_PLAN" "$temp_jmx"
    
    # Enable only the specified thread group and disable others
    case $thread_group in
        "1")
            sed -i '' 's/testname="1 User Load Test" enabled="false"/testname="1 User Load Test" enabled="true"/g' "$temp_jmx"
            sed -i '' 's/testname="10 Users Load Test" enabled="true"/testname="10 Users Load Test" enabled="false"/g' "$temp_jmx"
            sed -i '' 's/testname="100 Users Load Test" enabled="true"/testname="100 Users Load Test" enabled="false"/g' "$temp_jmx"
            sed -i '' 's/testname="1000 Users Load Test" enabled="true"/testname="1000 Users Load Test" enabled="false"/g' "$temp_jmx"
            ;;
        "10")
            sed -i '' 's/testname="1 User Load Test" enabled="true"/testname="1 User Load Test" enabled="false"/g' "$temp_jmx"
            sed -i '' 's/testname="10 Users Load Test" enabled="false"/testname="10 Users Load Test" enabled="true"/g' "$temp_jmx"
            sed -i '' 's/testname="100 Users Load Test" enabled="true"/testname="100 Users Load Test" enabled="false"/g' "$temp_jmx"
            sed -i '' 's/testname="1000 Users Load Test" enabled="true"/testname="1000 Users Load Test" enabled="false"/g' "$temp_jmx"
            ;;
        "100")
            sed -i '' 's/testname="1 User Load Test" enabled="true"/testname="1 User Load Test" enabled="false"/g' "$temp_jmx"
            sed -i '' 's/testname="10 Users Load Test" enabled="true"/testname="10 Users Load Test" enabled="false"/g' "$temp_jmx"
            sed -i '' 's/testname="100 Users Load Test" enabled="false"/testname="100 Users Load Test" enabled="true"/g' "$temp_jmx"
            sed -i '' 's/testname="1000 Users Load Test" enabled="true"/testname="1000 Users Load Test" enabled="false"/g' "$temp_jmx"
            ;;
        "1000")
            sed -i '' 's/testname="1 User Load Test" enabled="true"/testname="1 User Load Test" enabled="false"/g' "$temp_jmx"
            sed -i '' 's/testname="10 Users Load Test" enabled="true"/testname="10 Users Load Test" enabled="false"/g' "$temp_jmx"
            sed -i '' 's/testname="100 Users Load Test" enabled="true"/testname="100 Users Load Test" enabled="false"/g' "$temp_jmx"
            sed -i '' 's/testname="1000 Users Load Test" enabled="false"/testname="1000 Users Load Test" enabled="true"/g' "$temp_jmx"
            ;;
    esac
    
    # Run JMeter test
    local result_file="$RESULTS_DIR/${test_name}_${TIMESTAMP}.jtl"
    local log_file="$RESULTS_DIR/${test_name}_${TIMESTAMP}.log"
    
    if command -v jmeter >/dev/null 2>&1; then
        jmeter -n -t "$temp_jmx" -l "$result_file" -j "$log_file" -e -o "$RESULTS_DIR/${test_name}_${TIMESTAMP}_html"
        echo -e "${GREEN}✓ Test completed. Results saved to: $result_file${NC}"
        echo -e "${GREEN}✓ HTML report generated: $RESULTS_DIR/${test_name}_${TIMESTAMP}_html/index.html${NC}"
    else
        echo -e "${RED}ERROR: JMeter not found. Please install JMeter or add it to PATH${NC}"
        echo "Installation instructions:"
        echo "  Ubuntu/Debian: sudo apt install jmeter"
        echo "  macOS: brew install jmeter"
        echo "  Manual: Download from https://jmeter.apache.org/download_jmeter.cgi"
        rm "$temp_jmx"
        exit 1
    fi
    
    # Clean up temporary file
    rm "$temp_jmx"
    
    # Show basic statistics
    if [ -f "$result_file" ]; then
        echo -e "\n${YELLOW}Basic Statistics for $description:${NC}"
        echo "Total requests: $(tail -n +2 "$result_file" | wc -l)"
        echo "Success rate: $(awk -F',' 'NR>1 && $8=="true" {success++} NR>1 {total++} END {printf "%.2f%%", (success/total)*100}' "$result_file")"
        echo "Average response time: $(awk -F',' 'NR>1 {sum+=$2; count++} END {printf "%.0f ms", sum/count}' "$result_file")"
        echo "Min response time: $(awk -F',' 'NR>1 {if(min=="" || $2<min) min=$2} END {printf "%.0f ms", min}' "$result_file")"
        echo "Max response time: $(awk -F',' 'NR>1 {if($2>max) max=$2} END {printf "%.0f ms", max}' "$result_file")"
    fi
}

# Check if application is running
echo -e "\n${YELLOW}Checking if application is running...${NC}"
if curl -s "http://localhost:8080/user/search?first_name=test&last_name=test" >/dev/null 2>&1; then
    echo -e "${GREEN}✓ Application is running on localhost:8080${NC}"
else
    echo -e "${RED}ERROR: Application is not running on localhost:8080${NC}"
    echo "Please start the application first:"
    echo "  mvn clean package && java -jar target/social-network-1.0.0.jar"
    echo "  OR"
    echo "  docker-compose up"
    exit 1
fi

# Parse command line arguments
case "${1:-all}" in
    "1")
        run_test "baseline_1_user" "1" "Baseline Test (1 concurrent user)"
        ;;
    "10")
        run_test "load_10_users" "10" "Load Test (10 concurrent users)"
        ;;
    "100")
        run_test "load_100_users" "100" "Load Test (100 concurrent users)"
        ;;
    "1000")
        run_test "stress_1000_users" "1000" "Stress Test (1000 concurrent users)"
        ;;
    "all")
        echo -e "\n${YELLOW}Running all load tests...${NC}"
        run_test "baseline_1_user" "1" "Baseline Test (1 concurrent user)"
        sleep 5
        run_test "load_10_users" "10" "Load Test (10 concurrent users)"
        sleep 5
        run_test "load_100_users" "100" "Load Test (100 concurrent users)"
        sleep 10
        run_test "stress_1000_users" "1000" "Stress Test (1000 concurrent users)"
        ;;
    *)
        echo "Usage: $0 [1|10|100|1000|all]"
        echo "  1    - Run 1 user test only"
        echo "  10   - Run 10 users test only"
        echo "  100  - Run 100 users test only"
        echo "  1000 - Run 1000 users test only"
        echo "  all  - Run all tests (default)"
        exit 1
        ;;
esac

echo -e "\n${GREEN}=== Load testing completed! ===${NC}"
echo "Results are saved in: $RESULTS_DIR/"
echo "To analyze results:"
echo "  1. Open HTML reports in your browser"
echo "  2. Use JTL files for custom analysis"
echo "  3. Compare results before and after index optimization"