#!/bin/bash

# Define variables
PROJECT_DIR="$(pwd)"
TARGET_DIR="$PROJECT_DIR/target"
CLASSPATH_DIR="$HOME/.jobber/classpath"
BIN_DIR="$HOME/bin"
LAUNCHER_SCRIPT="$BIN_DIR/jobber"

# Clean and build the project using Maven
echo "Cleaning and building the project..."
mvn clean package || { echo "Maven build failed"; exit 1; }

# Clean up the old installation
echo "Cleaning up old installation..."

# Safety check to prevent accidental removal of the entire home directory
if [[ "$CLASSPATH_DIR" == "$HOME" || "$CLASSPATH_DIR" == "$HOME/" ]]; then
    echo "Error: Attempting to remove the entire home directory. Aborting."
    exit 1
fi

rm -rf "$CLASSPATH_DIR" "$LAUNCHER_SCRIPT"

# Create the classpath directory if it doesn't exist
echo "Setting up classpath directory at $CLASSPATH_DIR..."
mkdir -p "$CLASSPATH_DIR"

# Copy the compiled JAR file to the classpath directory
echo "Copying the compiled JAR file to $CLASSPATH_DIR..."
cp "$TARGET_DIR"/*.jar "$CLASSPATH_DIR" || { echo "Failed to copy JAR file"; exit 1; }

# Copy the project dependencies to the classpath directory
echo "Copying project dependencies to $CLASSPATH_DIR..."
cp "$TARGET_DIR/lib/"* "$CLASSPATH_DIR" || { echo "Failed to copy dependencies"; exit 1; }

# Create the bin directory if it doesn't exist
echo "Setting up bin directory at $BIN_DIR..."
mkdir -p "$BIN_DIR"

# Create the launcher script
echo "Creating the launcher script at $LAUNCHER_SCRIPT..."
cat > "$LAUNCHER_SCRIPT" <<EOF
#!/bin/bash
java -classpath "$CLASSPATH_DIR/*" com.patricktwohig.jobber.cli.Main "\$@"
EOF

# Make the launcher script executable
chmod +x "$LAUNCHER_SCRIPT"

echo "Setup complete. You can now run the application using the 'jobber' command."
