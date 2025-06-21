#!/usr/bin/env zsh

# Комплексный анализ производительности индексов
# Автоматически тестирует все варианты индексов и генерирует полный отчет

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Configuration
RESULTS_DIR="comprehensive_analysis"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
REPORT_FILE="$RESULTS_DIR/comprehensive_report_${TIMESTAMP}.md"
TEST_PLAN="user-search-load-test.jmx"

# Database configuration
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
DB_NAME="${DB_NAME:-social_network}"
DB_USER="${DB_USER:-social_network}"
DB_PASSWORD="${DB_PASSWORD:-social_network}"

# Test configurations (name:description pairs)
INDEX_CONFIGS=(
    "no_index:Без индексов (baseline)"
    "composite_index:Композитный (first_name, last_name)"
    "reverse_composite:Обратный композитный (last_name, first_name)"
    "separate_indexes:Отдельные B-tree индексы"
    "first_name_only:Только first_name"
    "last_name_only:Только last_name"
    "gin_indexes:GIN индексы (pg_trgm)"
    "combined_indexes:Комбинированные B-tree + GIN"
)

# Function to get config description
get_config_description() {
    local config_name="$1"
    for config in "${INDEX_CONFIGS[@]}"; do
        if [[ "$config" == "$config_name:"* ]]; then
            echo "${config#*:}"
            return
        fi
    done
    echo "Unknown configuration"
}

# Function to get config name from index
get_config_name() {
    local index="$1"
    local config="${INDEX_CONFIGS[$index]}"
    echo "${config%%:*}"
}

# Test load levels
LOAD_LEVELS=(1 10 100 1000)

echo -e "${GREEN}=========================================${NC}"
echo -e "${GREEN}  КОМПЛЕКСНЫЙ АНАЛИЗ ИНДЕКСОВ${NC}"
echo -e "${GREEN}=========================================${NC}"
echo "Timestamp: $TIMESTAMP"
echo "Results directory: $RESULTS_DIR"

# Create results directory
mkdir -p "$RESULTS_DIR"

# Function to check prerequisites
check_prerequisites() {
    echo -e "\n${YELLOW}Проверка prerequisites...${NC}"
    
    # Check PostgreSQL client
    if ! command -v psql >/dev/null 2>&1; then
        echo -e "${RED}ERROR: PostgreSQL client not found${NC}"
        echo "export PATH=\"/opt/homebrew/opt/postgresql@14/bin:\$PATH\""
        exit 1
    fi
    
    # Check JMeter
    if ! command -v jmeter >/dev/null 2>&1; then
        echo -e "${RED}ERROR: JMeter not found${NC}"
        exit 1
    fi
    
    # Check test plan
    if [ ! -f "$TEST_PLAN" ]; then
        echo -e "${RED}ERROR: Test plan not found: $TEST_PLAN${NC}"
        exit 1
    fi
    
    # Check database connection
    if ! PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c "SELECT 1;" >/dev/null 2>&1; then
        echo -e "${RED}ERROR: Cannot connect to database${NC}"
        exit 1
    fi
    
    # Check application
    if ! curl -s "http://localhost:8080/user/search?first_name=test&last_name=test" >/dev/null 2>&1; then
        echo -e "${RED}ERROR: Application not running on localhost:8080${NC}"
        exit 1
    fi
    
    echo -e "${GREEN}✓ All prerequisites OK${NC}"
}

# Function to execute SQL command
execute_sql() {
    local sql_command="$1"
    local description="$2"
    
    echo -e "${BLUE}SQL: $description${NC}"
    PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c "$sql_command" 2>/dev/null
}

# Function to drop all indexes
drop_all_indexes() {
    execute_sql "
        DO \$\$
        DECLARE
            index_name text;
        BEGIN
            FOR index_name IN 
                SELECT indexname 
                FROM pg_indexes 
                WHERE tablename = 'users' 
                AND indexname != 'users_pkey'
            LOOP
                EXECUTE 'DROP INDEX IF EXISTS ' || index_name;
            END LOOP;
        END \$\$;
    " "Dropping all user indexes"
}

# Function to create indexes based on configuration
create_indexes() {
    local config_name="$1"
    
    case "$config_name" in
        "no_index")
            # No indexes to create
            ;;
        "composite_index")
            execute_sql "CREATE INDEX idx_composite ON users (first_name, last_name);" "Creating composite index"
            ;;
        "reverse_composite")
            execute_sql "CREATE INDEX idx_reverse_composite ON users (last_name, first_name);" "Creating reverse composite index"
            ;;
        "separate_indexes")
            execute_sql "CREATE INDEX idx_first_name ON users (first_name);" "Creating first_name index"
            execute_sql "CREATE INDEX idx_last_name ON users (last_name);" "Creating last_name index"
            ;;
        "first_name_only")
            execute_sql "CREATE INDEX idx_first_name ON users (first_name);" "Creating first_name index"
            ;;
        "last_name_only")
            execute_sql "CREATE INDEX idx_last_name ON users (last_name);" "Creating last_name index"
            ;;
        "gin_indexes")
            execute_sql "CREATE EXTENSION IF NOT EXISTS pg_trgm;" "Creating pg_trgm extension"
            execute_sql "CREATE INDEX idx_first_name_gin ON users USING gin (first_name gin_trgm_ops);" "Creating GIN index on first_name"
            execute_sql "CREATE INDEX idx_last_name_gin ON users USING gin (last_name gin_trgm_ops);" "Creating GIN index on last_name"
            ;;
        "combined_indexes")
            execute_sql "CREATE EXTENSION IF NOT EXISTS pg_trgm;" "Creating pg_trgm extension"
            execute_sql "CREATE INDEX idx_first_name_btree ON users (first_name);" "Creating B-tree index on first_name"
            execute_sql "CREATE INDEX idx_last_name_btree ON users (last_name);" "Creating B-tree index on last_name"
            execute_sql "CREATE INDEX idx_first_name_gin ON users USING gin (first_name gin_trgm_ops);" "Creating GIN index on first_name"
            execute_sql "CREATE INDEX idx_last_name_gin ON users USING gin (last_name gin_trgm_ops);" "Creating GIN index on last_name"
            ;;
    esac
    
    execute_sql "ANALYZE users;" "Updating table statistics"
}

# Function to get SQL analysis
get_sql_analysis() {
    local config_name="$1"
    local output_file="$RESULTS_DIR/${config_name}_sql_analysis.txt"
    
    echo -e "${BLUE}Getting SQL analysis for $config_name${NC}"
    
    PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" > "$output_file" << 'EOF'
\echo 'Index information:'
SELECT 
    indexname,
    pg_size_pretty(pg_relation_size(indexname::regclass)) as size
FROM pg_indexes 
WHERE tablename = 'users' 
AND indexname != 'users_pkey'
ORDER BY pg_relation_size(indexname::regclass) DESC;

\echo '\nTotal indexes size:'
SELECT pg_size_pretty(pg_indexes_size('users'::regclass)) as total_indexes_size;

\echo '\nTable size:'
SELECT pg_size_pretty(pg_total_relation_size('users'::regclass)) as total_table_size;

\echo '\nEXPLAIN ANALYZE for LIKE search:'
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT) 
SELECT * FROM users 
WHERE first_name LIKE 'John%' AND last_name LIKE 'Doe%';

\echo '\nEXPLAIN ANALYZE for exact search:'
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT) 
SELECT * FROM users 
WHERE first_name = 'John' AND last_name = 'Doe';
EOF
}

# Function to run JMeter test for specific load
run_jmeter_test() {
    local config_name="$1"
    local load_level="$2"
    local test_name="${config_name}_${load_level}_users"
    local result_file="$RESULTS_DIR/${test_name}.jtl"
    local dashboard_dir="$RESULTS_DIR/${test_name}_dashboard"
    
    echo -e "${CYAN}Running JMeter test: $load_level users${NC}"
    
    # Create temporary JMX with correct thread group enabled
    local temp_jmx="temp_${test_name}_${TIMESTAMP}.jmx"
    cp "$TEST_PLAN" "$temp_jmx"
    
    # Enable appropriate thread group based on load level
    case $load_level in
        1)
            sed -i '' 's/testname="1 User Load Test" enabled="false"/testname="1 User Load Test" enabled="true"/g' "$temp_jmx"
            sed -i '' 's/testname="10 Users Load Test" enabled="true"/testname="10 Users Load Test" enabled="false"/g' "$temp_jmx"
            sed -i '' 's/testname="100 Users Load Test" enabled="true"/testname="100 Users Load Test" enabled="false"/g' "$temp_jmx"
            sed -i '' 's/testname="1000 Users Load Test" enabled="true"/testname="1000 Users Load Test" enabled="false"/g' "$temp_jmx"
            ;;
        10)
            sed -i '' 's/testname="1 User Load Test" enabled="true"/testname="1 User Load Test" enabled="false"/g' "$temp_jmx"
            sed -i '' 's/testname="10 Users Load Test" enabled="false"/testname="10 Users Load Test" enabled="true"/g' "$temp_jmx"
            sed -i '' 's/testname="100 Users Load Test" enabled="true"/testname="100 Users Load Test" enabled="false"/g' "$temp_jmx"
            sed -i '' 's/testname="1000 Users Load Test" enabled="true"/testname="1000 Users Load Test" enabled="false"/g' "$temp_jmx"
            ;;
        100)
            sed -i '' 's/testname="1 User Load Test" enabled="true"/testname="1 User Load Test" enabled="false"/g' "$temp_jmx"
            sed -i '' 's/testname="10 Users Load Test" enabled="true"/testname="10 Users Load Test" enabled="false"/g' "$temp_jmx"
            sed -i '' 's/testname="100 Users Load Test" enabled="false"/testname="100 Users Load Test" enabled="true"/g' "$temp_jmx"
            sed -i '' 's/testname="1000 Users Load Test" enabled="true"/testname="1000 Users Load Test" enabled="false"/g' "$temp_jmx"
            ;;
        1000)
            sed -i '' 's/testname="1 User Load Test" enabled="true"/testname="1 User Load Test" enabled="false"/g' "$temp_jmx"
            sed -i '' 's/testname="10 Users Load Test" enabled="true"/testname="10 Users Load Test" enabled="false"/g' "$temp_jmx"
            sed -i '' 's/testname="100 Users Load Test" enabled="true"/testname="100 Users Load Test" enabled="false"/g' "$temp_jmx"
            sed -i '' 's/testname="1000 Users Load Test" enabled="false"/testname="1000 Users Load Test" enabled="true"/g' "$temp_jmx"
            ;;
    esac
    
    # Run JMeter with HTML dashboard generation
    jmeter -n -t "$temp_jmx" -l "$result_file" -e -o "$dashboard_dir" >/dev/null 2>&1
    
    # Clean up
    rm "$temp_jmx"
    
    echo -e "${GREEN}✓ Test completed: $test_name${NC}"
}

# Function to extract metrics from JTL file
extract_metrics() {
    local jtl_file="$1"
    
    if [ ! -f "$jtl_file" ]; then
        echo "N/A,N/A,N/A,N/A,N/A"
        return
    fi
    
    local total_requests=$(tail -n +2 "$jtl_file" | wc -l)
    local avg_time=$(awk -F',' 'NR>1 {sum+=$2; count++} END {if(count>0) printf "%.0f", sum/count; else print "0"}' "$jtl_file")
    local min_time=$(awk -F',' 'NR>1 {if(min=="" || $2<min) min=$2} END {if(min!="") printf "%.0f", min; else print "0"}' "$jtl_file")
    local max_time=$(awk -F',' 'NR>1 {if($2>max) max=$2} END {if(max!="") printf "%.0f", max; else print "0"}' "$jtl_file")
    local success_rate=$(awk -F',' 'NR>1 && $8=="true" {success++} NR>1 {total++} END {if(total>0) printf "%.1f", (success/total)*100; else print "0.0"}' "$jtl_file")
    local throughput=$(awk -F',' 'NR>1 {sum+=$2; count++} END {if(count>0 && sum>0) printf "%.1f", (count*1000)/(sum/count); else print "0.0"}' "$jtl_file")
    
    echo "$avg_time,$min_time,$max_time,$success_rate,$throughput"
}

# Function to analyze configuration
analyze_configuration() {
    local config_name="$1"
    local config_description=$(get_config_description "$config_name")
    
    echo -e "\n${GREEN}=========================================${NC}"
    echo -e "${GREEN}Анализ: $config_description${NC}"
    echo -e "${GREEN}=========================================${NC}"
    
    # 1. Setup indexes
    drop_all_indexes
    create_indexes "$config_name"
    
    # 2. Get SQL analysis
    get_sql_analysis "$config_name"
    
    # 3. Run JMeter tests for all load levels
    for load_level in "${LOAD_LEVELS[@]}"; do
        run_jmeter_test "$config_name" "$load_level"
        sleep 5  # Brief pause between tests
    done
    
    echo -e "${GREEN}✓ Configuration $config_name completed${NC}"
}

# Function to initialize report
initialize_report() {
    cat > "$REPORT_FILE" << EOF
# Комплексный анализ производительности индексов

## Обзор тестирования
- **Дата**: $(date)
- **База данных**: PostgreSQL
- **Инструмент**: Apache JMeter
- **Приложение**: Social Network User Search API

## Конфигурации индексов

EOF

    for config in "${INDEX_CONFIGS[@]}"; do
        local name="${config%%:*}"
        local description="${config#*:}"
        echo "- **$name**: $description" >> "$REPORT_FILE"
    done

    cat >> "$REPORT_FILE" << 'EOF'

## Методология тестирования

### Нагрузочные сценарии
- **1 пользователь**: 100 запросов (baseline)
- **10 пользователей**: 1000 запросов (умеренная нагрузка)
- **100 пользователей**: 10000 запросов (высокая нагрузка)
- **1000 пользователей**: 10000 запросов (стресс-тест)

### Тестовые запросы
- **LIKE поиск**: `WHERE first_name LIKE 'John%' AND last_name LIKE 'Doe%'`
- **Точный поиск**: `WHERE first_name = 'John' AND last_name = 'Doe'`

---

EOF
}

# Function to add configuration results to report
add_configuration_to_report() {
    local config_name="$1"
    local config_description=$(get_config_description "$config_name")
    
    cat >> "$REPORT_FILE" << EOF
## $config_description

### SQL Анализ

#### Информация об индексах
\`\`\`
EOF

    # Add SQL analysis
    if [ -f "$RESULTS_DIR/${config_name}_sql_analysis.txt" ]; then
        cat "$RESULTS_DIR/${config_name}_sql_analysis.txt" >> "$REPORT_FILE"
    fi
    
    cat >> "$REPORT_FILE" << 'EOF'
```

### Результаты нагрузочного тестирования

| Нагрузка | Avg (ms) | Min (ms) | Max (ms) | Success (%) | Throughput (req/s) |
|----------|----------|----------|----------|-------------|-------------------|
EOF

    # Add performance metrics
    for load_level in "${LOAD_LEVELS[@]}"; do
        local jtl_file="$RESULTS_DIR/${config_name}_${load_level}_users.jtl"
        local metrics=$(extract_metrics "$jtl_file")
        IFS=',' read -r avg min max success throughput <<< "$metrics"
        echo "| $load_level users | $avg | $min | $max | $success | $throughput |" >> "$REPORT_FILE"
    done

    cat >> "$REPORT_FILE" << EOF

### HTML Отчеты
EOF

    # Add links to HTML dashboards
    for load_level in "${LOAD_LEVELS[@]}"; do
        echo "- [$load_level users](./${config_name}_${load_level}_users_dashboard/index.html)" >> "$REPORT_FILE"
    done

    echo -e "\n---\n" >> "$REPORT_FILE"
}

# Function to generate summary
generate_summary() {
    cat >> "$REPORT_FILE" << 'EOF'
## Сводный анализ

### Сравнение среднего времени отклика (100 пользователей)

| Конфигурация | Avg Response Time (ms) |
|--------------|------------------------|
EOF

    # Generate summary table
    for config in "${INDEX_CONFIGS[@]}"; do
        local config_name="${config%%:*}"
        local config_description="${config#*:}"
        local jtl_file="$RESULTS_DIR/${config_name}_100_users.jtl"
        local metrics=$(extract_metrics "$jtl_file")
        IFS=',' read -r avg min max success throughput <<< "$metrics"
        echo "| $config_description | $avg |" >> "$REPORT_FILE"
    done

    cat >> "$REPORT_FILE" << 'EOF'

### Сравнение пропускной способности (100 пользователей)

| Конфигурация | Throughput (req/s) |
|--------------|-------------------|
EOF

    # Generate throughput table
    for config in "${INDEX_CONFIGS[@]}"; do
        local config_name="${config%%:*}"
        local config_description="${config#*:}"
        local jtl_file="$RESULTS_DIR/${config_name}_100_users.jtl"
        local metrics=$(extract_metrics "$jtl_file")
        IFS=',' read -r avg min max success throughput <<< "$metrics"
        echo "| $config_description | $throughput |" >> "$REPORT_FILE"
    done

    cat >> "$REPORT_FILE" << 'EOF'

## Рекомендации

### Лучшие результаты по категориям

1. **Лучшее время отклика**: [Будет определено после анализа]
2. **Лучшая пропускная способность**: [Будет определено после анализа]
3. **Лучший баланс**: [Будет определено после анализа]
4. **Наименьший размер индексов**: [Будет определено после анализа]

### Выбор индекса в зависимости от нагрузки

- **Низкая нагрузка (1-10 пользователей)**: Любой индекс показывает хорошие результаты
- **Средняя нагрузка (10-100 пользователей)**: Композитные индексы предпочтительнее
- **Высокая нагрузка (100+ пользователей)**: Требуется баланс между производительностью и размером

### Типы запросов

- **Для LIKE поиска с префиксами**: B-tree композитные индексы
- **Для частичного LIKE поиска**: GIN индексы с pg_trgm
- **Для точного поиска**: B-tree композитные индексы
- **Для смешанных запросов**: Комбинированные индексы

## Заключение

Данный анализ показывает влияние различных типов индексов на производительность API поиска пользователей. Выбор оптимального индекса зависит от паттернов использования, нагрузки и требований к производительности.

---

**Сгенерировано**: $(date)  
**Общее время тестирования**: [Будет рассчитано]
EOF
}

# Main execution
main() {
    local start_time=$(date +%s)
    
    # Check prerequisites
    check_prerequisites
    
    # Initialize report
    initialize_report
    
    # Analyze each configuration
    for config in "${INDEX_CONFIGS[@]}"; do
        local config_name="${config%%:*}"
        analyze_configuration "$config_name"
        add_configuration_to_report "$config_name"
    done
    
    # Generate summary
    generate_summary
    
    local end_time=$(date +%s)
    local duration=$((end_time - start_time))
    
    echo -e "\n${GREEN}=========================================${NC}"
    echo -e "${GREEN}  АНАЛИЗ ЗАВЕРШЕН!${NC}"
    echo -e "${GREEN}=========================================${NC}"
    echo "Время выполнения: $((duration / 60)) минут $((duration % 60)) секунд"
    echo "Отчет сохранен: $REPORT_FILE"
    echo ""
    echo "Для просмотра результатов:"
    echo "  cat $REPORT_FILE"
    echo "  open $RESULTS_DIR/"
}

# Run analysis
main "$@"