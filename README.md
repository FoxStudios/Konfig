# Konfig <img src="https://img.shields.io/github/v/release/FoxStudios/Konfig?logo=github&style=for-the-badge">
A simple and easy to use configuration system for Java.

## Installation
**Gradle:**
```gradle
repositories {
    maven { url 'https://maven.foxes4life.net' }
}

dependencies {
    implementation 'net.foxes4life:konfig:1.4.0'
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
    <version>1.4.0</version>
</dependency>
```

## Usage


### Creating a config manager
```java
import net.foxes4life.konfig.Konfig;

public class ExampleConfig extends Konfig<ExampleSettings> {
    public ExampleConfig() {
        super("example");
    }

    public void initializeDefaults() {
        setDefault(ExampleSettings.SETTING, "Hello, world!");
        setDefault(ExampleSettings.ANOTHER_SETTING, 616);
    }
}

// In another file
public enum ExampleSettings {
    SETTING,
    ANOTHER_SETTING
}
```

### Getting and setting values
```java
ExampleConfig config = new ExampleConfig();

String setting = config.get(ExampleSettings.SETTING, String.class);
int anotherSetting = config.get(ExampleSettings.ANOTHER_SETTING, int.class);

config.set(ExampleSettings.SETTING, "Hello, Konfig!");
config.save();
```

###### Copyright (C) 2022 FoxStudios