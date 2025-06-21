#!/bin/bash

# Скрипт для установки JMeter плагинов для графиков

echo "🔧 Установка JMeter плагинов для графиков..."

# Find JMeter installation
JMETER_HOME=""
if command -v jmeter >/dev/null 2>&1; then
    JMETER_BIN=$(which jmeter)
    echo "✓ JMeter найден: $JMETER_BIN"
    
    # For Homebrew installation, JMeter is usually in /opt/homebrew/bin/jmeter
    # The actual JMeter home is in libexec
    if [[ "$JMETER_BIN" == *"/opt/homebrew/"* ]]; then
        JMETER_HOME="/opt/homebrew/libexec"
        echo "✓ Homebrew JMeter installation detected"
    else
        JMETER_HOME=$(dirname "$(dirname "$JMETER_BIN")")
    fi
    
    echo "✓ JMeter Home: $JMETER_HOME"
else
    echo "❌ JMeter не найден в PATH"
    echo "Установите JMeter:"
    echo "  brew install jmeter"
    exit 1
fi

# Check for different possible lib/ext locations
LIB_EXT_DIR=""
POSSIBLE_DIRS=(
    "$JMETER_HOME/lib/ext"
    "/opt/homebrew/libexec/lib/ext"
    "/opt/homebrew/Cellar/jmeter/*/libexec/lib/ext"
    "/usr/local/Cellar/jmeter/*/libexec/lib/ext"
)

for dir in "${POSSIBLE_DIRS[@]}"; do
    # Handle glob patterns
    for expanded_dir in $dir; do
        if [ -d "$expanded_dir" ]; then
            LIB_EXT_DIR="$expanded_dir"
            echo "✓ Найдена директория lib/ext: $LIB_EXT_DIR"
            break 2
        fi
    done
done

if [ -z "$LIB_EXT_DIR" ]; then
    echo "❌ Директория lib/ext не найдена"
    echo "Проверьте установку JMeter:"
    echo "  ls -la /opt/homebrew/libexec/lib/"
    echo "  ls -la /opt/homebrew/Cellar/jmeter/"
    exit 1
fi

echo "📦 Скачивание JMeter Plugins Manager..."

# Download Plugins Manager
PLUGINS_MANAGER_URL="https://jmeter-plugins.org/get/"
PLUGINS_MANAGER_JAR="$LIB_EXT_DIR/jmeter-plugins-manager.jar"

if curl -L "$PLUGINS_MANAGER_URL" -o "$PLUGINS_MANAGER_JAR"; then
    echo "✅ JMeter Plugins Manager установлен"
else
    echo "❌ Ошибка при скачивании Plugins Manager"
    exit 1
fi

echo ""
echo "🎯 Следующие шаги:"
echo "1. Перезапустите JMeter"
echo "2. Откройте: Options → Plugins Manager"
echo "3. Установите следующие плагины:"
echo "   - jp@gc - Response Times Over Time"
echo "   - jp@gc - Transactions per Second"
echo "   - jp@gc - Response Times Percentiles"
echo "   - jp@gc - Active Threads Over Time"
echo "   - jp@gc - Bytes Throughput Over Time"
echo ""
echo "📊 Альтернатива - HTML Dashboard (уже включен):"
echo "   jmeter -n -t test.jmx -l results.jtl -e -o dashboard/"
echo ""
echo "✅ Установка завершена!"