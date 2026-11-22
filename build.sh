#!/bin/bash
# ==============================================================================
# Skillora Build Script (Akanshi's Contribution)
# Compiles all Java classes into the bin/ directory
# ==============================================================================

# Locate Java compiler
if [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/javac" ]; then
    JAVAC_CMD="$JAVA_HOME/bin/javac"
elif [ -x "/Library/Java/JavaVirtualMachines/jdk-26.jdk/Contents/Home/bin/javac" ]; then
    JAVAC_CMD="/Library/Java/JavaVirtualMachines/jdk-26.jdk/Contents/Home/bin/javac"
else
    JAVAC_CMD="javac"
fi

echo "Using Java Compiler: $JAVAC_CMD"
mkdir -p bin

# Compile all source files
$JAVAC_CMD -cp "lib/mysql-connector-j-8.0.33.jar:." -d bin $(find src -name "*.java")

if [ $? -eq 0 ]; then
    echo "=================================================="
    echo "✓ Build successful! Classes compiled into bin/"
    echo "=================================================="
else
    echo "✗ Build failed. Please check compiler errors."
    exit 1
fi
