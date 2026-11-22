#!/bin/bash
# ==============================================================================
# Skillora Run Script (Akanshi's Contribution)
# Executes the Skillora Console Application
# ==============================================================================

# Locate Java runtime
if [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/java" ]; then
    JAVA_CMD="$JAVA_HOME/bin/java"
elif [ -x "/Library/Java/JavaVirtualMachines/jdk-26.jdk/Contents/Home/bin/java" ]; then
    JAVA_CMD="/Library/Java/JavaVirtualMachines/jdk-26.jdk/Contents/Home/bin/java"
else
    JAVA_CMD="java"
fi

$JAVA_CMD -cp "lib/mysql-connector-j-8.0.33.jar:bin:resources:." com.skillora.main.SkilloraApp
