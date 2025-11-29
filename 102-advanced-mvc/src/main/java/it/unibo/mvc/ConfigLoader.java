package it.unibo.mvc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


class ConfigData{
    int minimum;
    int maximum;
    int attempts;
}

public final class ConfigLoader {
    public static Configuration load(String fileName)
    {
        
        try (InputStream inputStream = ConfigLoader.class.getResourceAsStream(fileName)) {
            
            if (inputStream == null) {
                throw new IllegalArgumentException("Config file not found: " + fileName);
            }
            
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))){
                ConfigData config = new ConfigData();
                bufferedReader.lines()
                .filter(line -> !line.isBlank())
                .forEach(
                    line -> {
                        String[] parts = line.split(": ");

                        switch (parts[0].toLowerCase()) {
                            case "minimum" -> config.minimum = Integer.parseInt(parts[1]);
                            case "maximum" -> config.maximum = Integer.parseInt(parts[1]);
                            case "attempts" -> config.attempts = Integer.parseInt(parts[1]);
                        }
                    }
                );
                return new Configuration(config.maximum, config.minimum, config.attempts);
            } catch (Exception e) {
                System.err.println(e.toString());
            }


        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return null;
    }
}
