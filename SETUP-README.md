# Installation Guide for Gradle, Java 21, and Kotlin using Homebrew on macOS

## Prerequisites

Make sure you have Homebrew installed on your system. If not, you can install it by running the following command in your terminal:

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

After installing Homebrew, run the following command to ensure it is up to date:

```bash
brew update
```

## Step 1: Install Java 21

1. **Add the AdoptOpenJDK tap**: AdoptOpenJDK is a popular place to get the latest versions of Java.
   ```bash
   brew tap homebrew/cask-versions
   ```

2. **Install Java 21**: Use Homebrew to install Java 21.
   ```bash
   brew install openjdk@21
   ```

3. **Set JAVA_HOME**: You need to set the `JAVA_HOME` environment variable to point to the Java installation.

    - First, find the installation path:
      ```bash
      /usr/libexec/java_home -v 21
      ```

    - Add the following line to your `~/.zshrc` or `~/.bash_profile` file to set `JAVA_HOME` automatically:
      ```bash
      export JAVA_HOME=$(/usr/libexec/java_home -v 21)
      ```

    - Refresh your terminal configuration:
      ```bash
      source ~/.zshrc
      ```
      or
      ```bash
      source ~/.bash_profile
      ```

## Step 2: Install Gradle

1. **Install Gradle**: Use Homebrew to install the latest version of Gradle.
   ```bash
   brew install gradle
   ```

2. **Verify the Installation**: Check that Gradle is installed correctly by running:
   ```bash
   gradle -v
   ```

## Step 3: Install Kotlin

1. **Install Kotlin**: Use Homebrew to install Kotlin.
   ```bash
   brew install kotlin
   ```

2. **Verify the Installation**: Check that Kotlin is installed correctly by running:
   ```bash
   kotlinc -version
   ```

## Summary

You have successfully installed Java 21, Gradle, and Kotlin using Homebrew on macOS. You can now start using these tools for your development projects.

## Troubleshooting

- If you encounter any issues with the `JAVA_HOME` setting, make sure that your shell profile file (`~/.zshrc` or `~/.bash_profile`) is configured correctly and sourced.
- Ensure Homebrew is updated to the latest version if you encounter any installation issues.

This setup provides you with a powerful combination for building applications using Java, Gradle, and Kotlin. Let me know if you need help with anything else!


