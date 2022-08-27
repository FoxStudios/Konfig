# Konfig <img src="https://img.shields.io/github/v/release/FoxStudios/Konfig?logo=github&style=for-the-badge">
A simple and easy to use configuration system for Java.

## Installation
**Gradle:**
```gradle
repositories {
    maven { url 'https://maven.foxes4life.net' }
}

dependencies {
    implementation 'net.foxes4life:konfig:1.2.0'
}
```
**Maven:**
```xml
<repositories>
    <repository>
        <id>foxes4life</id>
        <name>FoxStudios Maven</name>
        <url>https://maven.foxes4life.net</url>
    </repository>
</repositories>

<dependency>
    <groupId>net.foxes4life</groupId>
    <artifactId>konfig</artifactId>
    <version>1.2.0</version>
</dependency>
```

## Usage

```java
// Defining a configuration
Konfig konfig = new Konfig("example"); // creates a new instance and tries to load the config file

KonfigCategory category = new KonfigCategory("category");
category.add("key", "default value"); // default value can be a String, Number, Boolean or JsonElement

konfig.add(category);

// Getting values
konfig.get("category", "key");

// Setting values
konfig.set("category", "key", "value");
konfig.save(); // saves the configuration to the file
```

###### Copyright (C) 2022 FoxStudios